package nl.pwiddershoven.script.repository;

import java.util.HashMap;
import java.util.Map;

import nl.pwiddershoven.script.service.ScriptConfiguration;

import org.springframework.stereotype.Component;

@Component
public class InMemoryScriptConfigurationRepository implements ScriptConfigurationRepository {
    private final Map<String, ScriptConfiguration> storage = new HashMap<>();
    private long nextId = 1;

    @Override
    public ScriptConfiguration find(String id) {
        return storage.get(id);
    }

    @Override
    public String save(ScriptConfiguration scriptConfiguration) {
        String id = "" + nextId();
        storage.put(id, scriptConfiguration);

        return id;
    }

    @Override
    public void update(String id, ScriptConfiguration scriptConfiguration) {
        storage.put(id, scriptConfiguration);
    }

    private synchronized long nextId() {
        return nextId++;
    }
}
