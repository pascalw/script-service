package nl.pwiddershoven.script.repository;

import java.util.List;
import java.util.stream.Collectors;

import nl.pwiddershoven.script.service.ScriptConfiguration;

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
public class MongoScriptConfigurationRepository implements ScriptConfigurationRepository {
    private static final String COLLECTION_NAME = "scriptConfigs";

    @Autowired
    private MongoTemplate mongoTemplate;

    private static class StorageObject {
        @Id
        public String id;
        public String script;
        public String contentType;
    }

    @Override
    public List<ScriptConfiguration> findAll(int offset, int perPage) {
        Query query = new Query();
        query.skip(offset);
        query.limit(perPage);

        return convert(mongoTemplate.find(query, StorageObject.class, COLLECTION_NAME));
    }

    @Override
    public ScriptConfiguration find(String id) {
        return convert(mongoTemplate.findById(id, StorageObject.class, COLLECTION_NAME));
    }

    @Override
    public String save(ScriptConfiguration scriptConfiguration) {
        StorageObject storageObject = new StorageObject();
        storageObject.script = scriptConfiguration.processingScript;
        storageObject.contentType = scriptConfiguration.contentType;

        mongoTemplate.save(storageObject, COLLECTION_NAME);
        return storageObject.id;
    }

    @Override
    public void update(String id, ScriptConfiguration scriptConfiguration) {
        mongoTemplate.upsert(new Query(Criteria.where("_id").is(id)), createUpdate(id, scriptConfiguration), COLLECTION_NAME);
    }

    private List<ScriptConfiguration> convert(List<StorageObject> storageObjects) {
        return storageObjects.stream().map(this::convert).collect(Collectors.toList());
    }

    private ScriptConfiguration convert(StorageObject storageObject) {
        return new ScriptConfiguration(storageObject.script, storageObject.contentType);
    }

    private Update createUpdate(String id, ScriptConfiguration scriptConfiguration) {
        StorageObject storageObject = new StorageObject();
        storageObject.id = id;
        storageObject.script = scriptConfiguration.processingScript;
        storageObject.contentType = scriptConfiguration.contentType;

        DBObject dbDoc = new BasicDBObject();
        mongoTemplate.getConverter().write(storageObject, dbDoc);
        return Update.fromDBObject(dbDoc);
    }

}
