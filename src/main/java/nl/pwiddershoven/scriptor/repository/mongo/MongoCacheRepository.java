package nl.pwiddershoven.scriptor.repository.mongo;

import nl.pwiddershoven.scriptor.repository.CacheRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoCacheRepository implements CacheRepository {
    private static final String COLLECTION_NAME = "script.cache";

    private static class StorageObject {
        @Id
        public String id;
        public Object data;

        public StorageObject(String id, Object data) {
            this.id = id;
            this.data = data;
        }
    }

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoCacheRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Object get(String id) {
        StorageObject storageObject = mongoTemplate.findById(id, StorageObject.class, COLLECTION_NAME);
        return storageObject == null ? null : storageObject.data;
    }

    @Override
    public void set(String id, Object data) {
        mongoTemplate.save(new StorageObject(id, data), COLLECTION_NAME);
    }
}
