package me.machadolucas.timesheet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import me.machadolucas.timesheet.bean.JSFSpringBean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class TimesheetApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimesheetApplication.class, args);
    }

    @Bean
    public JSFSpringBean jsfSpringBean() {
        return new JSFSpringBean();
    }

    @Configuration
    @Profile("dev")
    static class ConfigureJSFContextParameters implements ServletContextInitializer {

        @Override
        public void onStartup(ServletContext servletContext) throws ServletException {

            servletContext.setInitParameter("javax.faces.DEFAULT_SUFFIX", ".xhtml");
            servletContext.setInitParameter("javax.faces.PARTIAL_STATE_SAVING_METHOD", "true");
            servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
            servletContext.setInitParameter("facelets.DEVELOPMENT", "true");
            servletContext.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD", "1");

        }
    }
}
