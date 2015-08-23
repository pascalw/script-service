package nl.pwiddershoven.scriptor;

import nl.pwiddershoven.scriptor.config.CloudProfileInitializer;

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
