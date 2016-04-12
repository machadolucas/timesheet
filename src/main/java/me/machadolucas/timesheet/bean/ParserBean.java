package me.machadolucas.timesheet.bean;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import me.machadolucas.timesheet.entity.Day;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Data
@Component
public class ParserBean {

    private Document doc;

    public List<Day> processInputUolTable(final String inputUolTable) {

        final List<Day> parsedDays = new LinkedList<>();

        this.doc = Jsoup.parse(inputUolTable);

        final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Duration totalWorkDuration = Duration.ZERO;

        final Elements dateLines = this.doc.getElementsByAttributeValue("lv", "1");
        for (final Element dateLine : dateLines) {
            final String dateString = dateLine.select("table td").get(1).text().substring(1, 11);
            final LocalDate dayDate = LocalDate.parse(dateString, dateFormat);

            final String workDurationString = dateLine.nextElementSibling().child(0).text();

            final Day day = new Day();
            day.setDay(dayDate);
            day.setWorkDuration(parse(workDurationString));

            parsedDays.add(day);
        }

        for (final Day day : parsedDays) {
            totalWorkDuration = totalWorkDuration.plus(day.getWorkDuration());
        }
        return parsedDays;
    }

    private static Duration parse(final String input) {
        final int colonIndex = input.indexOf(':');
        String hh = input.substring(0, colonIndex);
        final String mm = input.substring(colonIndex + 1);
        final boolean isNegative = hh.charAt(0) == '-';
        if (isNegative) {
            hh = hh.substring(1);
        }
        final StringBuffer parseString = new StringBuffer();
        if (isNegative) {
            parseString.append("-");
        }
        parseString.append("PT");
        parseString.append(hh).append("H");
        parseString.append(mm).append("M");
        return Duration.parse(parseString.toString());
    }

}