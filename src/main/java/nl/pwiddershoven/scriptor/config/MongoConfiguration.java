package nl.pwiddershoven.scriptor.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
public class MongoConfiguration {
    @Autowired
    private MappingMongoConverter mappingMongoConverter;

    @PostConstruct
    public void configureMappingMongoConverter() throws Exception {
        // prevents the java class getting included in Mongo Documents,
        // we don't necessarily want to tie our data to a specific class.
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }
}
