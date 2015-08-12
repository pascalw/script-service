package nl.pwiddershoven.script;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@ComponentScan
@Configuration
public class Application extends SpringBootServletInitializer {

    public static void main(String args[]) {
        Class<?>[] configurations = { Application.class };
        SpringApplication.run(configurations, args);
    }
}
