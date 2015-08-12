package nl.pwiddershoven.script;

import nl.pwiddershoven.script.config.CloudProfileInitializer;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@ComponentScan
@Configuration
public class Application extends SpringBootServletInitializer {

    public static void main(String args[]) {
        new SpringApplicationBuilder(Application.class)
                .initializers(new CloudProfileInitializer())
                .run(args);
    }
}
