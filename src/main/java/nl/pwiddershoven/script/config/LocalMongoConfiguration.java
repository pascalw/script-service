package nl.pwiddershoven.script.config;

import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
@Profile("!cloud")
public class LocalMongoConfiguration extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return System.getProperty("MONGO_DATABASE", "script-service");
    }

    @Bean
    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient();
    }
}
