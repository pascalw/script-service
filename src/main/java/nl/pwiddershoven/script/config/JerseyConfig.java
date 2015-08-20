package nl.pwiddershoven.script.config;

import nl.pwiddershoven.script.controller.ScriptConfigurationController;
import nl.pwiddershoven.script.controller.ScriptExecutionController;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(JacksonFeature.class);
        register(AuthenticationFilter.class);

        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        property(ServletProperties.FILTER_FORWARD_ON_404, true);

        register(ScriptExecutionController.class);
        register(ScriptConfigurationController.class);
    }
}
