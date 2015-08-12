package nl.pwiddershoven.script.config;

import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.cloud.*;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.ConfigurableEnvironment;

public class CloudProfileInitializer implements ApplicationContextInitializer<AnnotationConfigEmbeddedWebApplicationContext> {
    @Override
    public void initialize(AnnotationConfigEmbeddedWebApplicationContext applicationContext) {
        Cloud cloud = getCloud();

        if (cloud != null) {
            ConfigurableEnvironment appEnvironment = applicationContext.getEnvironment();
            appEnvironment.addActiveProfile("cloud");
        }
    }

    private Cloud getCloud() {
        try {
            CloudFactory cloudFactory = new CloudFactory();
            return cloudFactory.getCloud();
        } catch (CloudException ce) {
            return null;
        }
    }
}
