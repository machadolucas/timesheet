package me.machadolucas.timesheet.view;

import java.time.Duration;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import me.machadolucas.timesheet.bean.ParserBean;
import me.machadolucas.timesheet.entity.Day;

@SpringUI(path = "/")
public class HomeView extends UI {

    private final TextArea textArea = new TextArea("Cole o HTML da table do sistema de ponto:");
    private final Grid<Day> results = new Grid<>();

    private final Label totalWorkDurationLabel = new Label();

    private Duration totalWorkDuration = Duration.ZERO;

    @Autowired
    private ParserBean parserBean;

    @Override
    protected void init(final VaadinRequest vaadinRequest) {
        Page.getCurrent().setTitle("Timesheet");
    }

    @PostConstruct
    private void buildPage() {

        final VerticalLayout layout = new VerticalLayout();

        final Label titleLabel = new Label("Calculadora de horas");
        titleLabel.setStyleName(ValoTheme.LABEL_H2);
        final HorizontalLayout title = new HorizontalLayout();
        title.setMargin(false);
        title.setWidth("100%");
        title.addComponent(titleLabel);
        title.setComponentAlignment(titleLabel, Alignment.MIDDLE_CENTER);

        this.textArea.setWidth("100%");
        this.textArea.addStyleName(ValoTheme.TEXTAREA_SMALL);
        this.textArea.setRows(10);
        final HorizontalLayout marginLayoutInput = new HorizontalLayout();
        marginLayoutInput.setMargin(false);
        marginLayoutInput.setWidth("100%");
        marginLayoutInput.addComponent(this.textArea);
        marginLayoutInput.setComponentAlignment(this.textArea, Alignment.MIDDLE_CENTER);

        final Button calculate = new Button("Calcular horas", this::calculate);
        calculate.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        final HorizontalLayout marginLayoutButton = new HorizontalLayout();
        marginLayoutButton.setWidth("100%");
        marginLayoutButton.addComponent(calculate);
        marginLayoutButton.setComponentAlignment(calculate, Alignment.MIDDLE_CENTER);

        results.addColumn(Day::getDay).setCaption("Data");
        results.addColumn(day -> formatDuration(day.getWorkDuration())).setCaption("Saldo");

        totalWorkDurationLabel.setStyleName(ValoTheme.LABEL_BOLD);

        layout.addComponents(title, marginLayoutInput, marginLayoutButton, this.results, this.totalWorkDurationLabel);
        setContent(layout);
    }

    private void calculate(final Button.ClickEvent event) {

        final List<Day> days = this.parserBean.processInputUolTable(this.textArea.getValue());
        this.results.setItems(days);

        this.totalWorkDuration = days.stream().map(Day::getWorkDuration).reduce(Duration.ZERO, Duration::plus);

        this.textArea.setValue("");

        this.totalWorkDurationLabel.setValue("Saldo mensal: " + formatDuration(this.totalWorkDuration));
    }

    private String formatDuration(Duration duration) {
        final long hours = duration.toHours();
        final long minutes = duration.minusHours(hours).toMinutes();

        if (hours <= 0l && minutes < 0l) {
            return String.format("-%d:%02d", Math.abs(hours), Math.abs(minutes));
        }
        return String.format("%d:%02d", hours, minutes);
    }

}
