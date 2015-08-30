package nl.pwiddershoven.scriptor.repository;

import java.util.List;
import java.util.stream.Collectors;

import nl.pwiddershoven.scriptor.service.EndpointConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Primary
@Component
public class MongoEndpointConfigurationRepository implements EndpointConfigurationRepository {
    private static final String COLLECTION_NAME = "endpointConfigurations";

    @Autowired
    private MongoTemplate mongoTemplate;

    private static class StorageObject {
        @Id
        public String id;
        public String script;
        public String contentType;
        public String accessToken;
    }

    @Override
    public List<EndpointConfiguration> findAll(int offset, int perPage) {
        Query query = new Query();
        query.skip(offset);
        query.limit(perPage);

        return convert(mongoTemplate.find(query, StorageObject.class, COLLECTION_NAME));
    }

    @Override
    public EndpointConfiguration find(String id) {
        return convert(mongoTemplate.findById(id, StorageObject.class, COLLECTION_NAME));
    }

    @Override
    public String save(EndpointConfiguration endpointConfiguration) {
        StorageObject storageObject = new StorageObject();
        storageObject.script = endpointConfiguration.processingScript;
        storageObject.contentType = endpointConfiguration.contentType;
        storageObject.accessToken = endpointConfiguration.accessToken;

        mongoTemplate.save(storageObject, COLLECTION_NAME);
        return storageObject.id;
    }

    @Override
    public void update(String id, EndpointConfiguration endpointConfiguration) {
        mongoTemplate.upsert(queryForId(id), createUpdate(id, endpointConfiguration), COLLECTION_NAME);
    }

    @Override
    public void remove(String id) {
        mongoTemplate.remove(queryForId(id), COLLECTION_NAME);
    }

    private Query queryForId(String id) {
        return new Query(Criteria.where("_id").is(id));
    }

    private List<EndpointConfiguration> convert(List<StorageObject> storageObjects) {
        return storageObjects.stream().map(this::convert).collect(Collectors.toList());
    }

    private EndpointConfiguration convert(StorageObject storageObject) {
        if (storageObject == null)
            return null;

        return new EndpointConfiguration(storageObject.id, storageObject.script, storageObject.contentType, storageObject.accessToken);
    }

    private Update createUpdate(String id, EndpointConfiguration endpointConfiguration) {
        StorageObject storageObject = new StorageObject();
        storageObject.id = id;
        storageObject.script = endpointConfiguration.processingScript;
        storageObject.contentType = endpointConfiguration.contentType;
        storageObject.accessToken = endpointConfiguration.accessToken;

        DBObject dbDoc = new BasicDBObject();
        mongoTemplate.getConverter().write(storageObject, dbDoc);
        return Update.fromDBObject(dbDoc);
    }

}
