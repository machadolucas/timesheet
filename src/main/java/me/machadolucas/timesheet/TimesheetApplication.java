package me.machadolucas.timesheet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.eclipse.jetty.server.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class TimesheetApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimesheetApplication.class, args);
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
            servletContext.setInitParameter("primefaces.THEME", "bootstrap");

        }
    }

    @Bean
    public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory(
            @Value("${server.port:8080}") final String port,
            @Value("${jetty.threadPool.maxThreads:200}") final String maxThreads,
            @Value("${jetty.threadPool.minThreads:8}") final String minThreads,
            @Value("${jetty.threadPool.idleTimeout:60000}") final String idleTimeout) {
        final JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory(
                Integer.valueOf(port));
        factory.addServerCustomizers(new JettyServerCustomizer() {
            @Override
            public void customize(final Server server) {
                server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", "600000");
            }
        });
        return factory;
    }
}
