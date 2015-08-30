package nl.pwiddershoven.scriptor.repository;

import java.util.List;

import nl.pwiddershoven.scriptor.service.EndpointConfiguration;

public interface EndpointConfigurationRepository {

    List<EndpointConfiguration> findAll(int offset, int perPage);

    EndpointConfiguration find(String id);

    String save(EndpointConfiguration endpointConfiguration);

    void update(String id, EndpointConfiguration endpointConfiguration);

    void remove(String id);
}
