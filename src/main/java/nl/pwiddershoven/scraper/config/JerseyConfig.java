package nl.pwiddershoven.scraper.config;

import nl.pwiddershoven.scraper.controller.ScrapeController;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(JacksonFeature.class);
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        register(ScrapeController.class);
    }
}
