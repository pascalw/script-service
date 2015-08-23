package nl.pwiddershoven.scriptor.repository;

import java.util.*;
import java.util.stream.Collectors;

import nl.pwiddershoven.scriptor.service.ScriptConfiguration;

import org.springframework.stereotype.Component;

@Component
public class InMemoryScriptConfigurationRepository implements ScriptConfigurationRepository {
    private final Map<String, ScriptConfiguration> storage = new HashMap<>();
    private long nextId = 1;

    @Override
    public List<ScriptConfiguration> findAll(int offset, int perPage) {
        return storage.values().stream()
                .skip(offset)
                .limit(perPage)
                .collect(Collectors.toList());
    }

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

    @Override
    public void remove(String id) {
        storage.remove(id);
    }

    private synchronized long nextId() {
        return nextId++;
    }
}
