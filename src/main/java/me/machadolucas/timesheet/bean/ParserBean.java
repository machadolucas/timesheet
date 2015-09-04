package me.machadolucas.timesheet.bean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;

import lombok.Data;
import me.machadolucas.timesheet.entity.Day;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Data
@ManagedBean
@SessionScoped
public class ParserBean {

    private String inputUolTable;

    private Document doc;

    private List<Day> parsedDays = new LinkedList<>();
    private Duration totalWorkDuration = Duration.ZERO;

    public void processInputUolTable() {

        doc = Jsoup.parse(inputUolTable);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        totalWorkDuration = Duration.ZERO;

        try {
            Elements dateLines = doc.getElementsByAttributeValue("lv", "1");
            for (Element dateLine : dateLines) {
                String dateString = dateLine.select("table td").get(1).text().substring(1, 11);
                Date dayDate = dateFormat.parse(dateString);

                String workDurationString = dateLine.nextElementSibling().child(0).text();

                Day day = new Day();
                day.setDay(dayDate);
                day.setWorkDuration(parse(workDurationString));

                parsedDays.add(day);
            }

            for (Day day : parsedDays) {
                totalWorkDuration = totalWorkDuration.plus(day.getWorkDuration());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static Duration parse(String input) {
        int colonIndex = input.indexOf(':');
        String hh = input.substring(0, colonIndex);
        String mm = input.substring(colonIndex + 1);
        boolean isNegative = hh.charAt(0) == '-';
        if (isNegative) {
            hh = hh.substring(1);
        }
        StringBuffer parseString = new StringBuffer();
        if (isNegative) {
            parseString.append("-");
        }
        parseString.append("PT");
        parseString.append(hh).append("H");
        parseString.append(mm).append("M");
        return Duration.parse(parseString.toString());
    }

}