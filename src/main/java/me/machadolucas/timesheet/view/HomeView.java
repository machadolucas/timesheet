package me.machadolucas.timesheet.view;

import java.util.List;

import javax.annotation.PostConstruct;

import me.machadolucas.timesheet.bean.ParserBean;
import me.machadolucas.timesheet.entity.Day;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI(path = "/")
public class HomeView extends UI {

    private TextArea textArea = new TextArea();
    private Table results = new Table();

    @Autowired
    private ParserBean parserBean;

    @Override
    protected void init(final VaadinRequest vaadinRequest) {
        Page.getCurrent().setTitle("Timesheet");

    }

    @PostConstruct
    private void buildPage() {

        final VerticalLayout layout = new VerticalLayout();

        final HorizontalLayout title = new HorizontalLayout();
        final Label titleLabel = new Label("Calculadora de horas");
        title.setMargin(true);
        title.setWidth("100%");
        title.addComponent(titleLabel);
        title.setComponentAlignment(titleLabel, Alignment.MIDDLE_CENTER);

        this.textArea.setWidth("100%");
        this.textArea.addStyleName(ValoTheme.TEXTAREA_SMALL);
        this.textArea.setRows(15);
        final HorizontalLayout marginLayoutInput = new HorizontalLayout();
        marginLayoutInput.setMargin(true);
        marginLayoutInput.setWidth("100%");
        marginLayoutInput.addComponent(this.textArea);
        marginLayoutInput.setComponentAlignment(this.textArea, Alignment.MIDDLE_CENTER);

        final HorizontalLayout marginLayoutButton = new HorizontalLayout();
        final Button calculate = new Button("Calcular horas", this::calculate);
        calculate.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        marginLayoutButton.setWidth("100%");
        marginLayoutButton.addComponent(calculate);
        marginLayoutButton.setComponentAlignment(calculate, Alignment.MIDDLE_CENTER);

        layout.addComponents(title, marginLayoutInput, marginLayoutButton, this.results);
        setContent(layout);
    }

    private void calculate(final Button.ClickEvent event) {
        final List<Day> days = this.parserBean.processInputUolTable(this.textArea.getValue());
    }

}
