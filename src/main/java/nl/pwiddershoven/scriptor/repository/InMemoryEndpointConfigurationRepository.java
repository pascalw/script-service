package nl.pwiddershoven.scriptor.repository;

import java.util.*;
import java.util.stream.Collectors;

import nl.pwiddershoven.scriptor.service.EndpointConfiguration;

import org.springframework.stereotype.Component;

@Component
public class InMemoryEndpointConfigurationRepository implements EndpointConfigurationRepository {
    private final Map<String, EndpointConfiguration> storage = new HashMap<>();
    private long nextId = 1;

    @Override
    public List<EndpointConfiguration> findAll(int offset, int perPage) {
        return storage.values().stream()
                .skip(offset)
                .limit(perPage)
                .collect(Collectors.toList());
    }

    @Override
    public EndpointConfiguration find(String id) {
        return storage.get(id);
    }

    @Override
    public String save(EndpointConfiguration endpointConfiguration) {
        String id = "" + nextId();
        storage.put(id, endpointConfiguration);

        return id;
    }

    @Override
    public void update(String id, EndpointConfiguration endpointConfiguration) {
        storage.put(id, endpointConfiguration);
    }

    @Override
    public void remove(String id) {
        storage.remove(id);
    }

    private synchronized long nextId() {
        return nextId++;
    }
}
