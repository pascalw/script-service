package nl.pwiddershoven.script.repository;

import java.util.List;

import nl.pwiddershoven.script.service.ScriptConfiguration;

public interface ScriptConfigurationRepository {

    List<ScriptConfiguration> findAll(int offset, int perPage);

    ScriptConfiguration find(String id);

    String save(ScriptConfiguration scriptConfiguration);

    void update(String id, ScriptConfiguration scriptConfiguration);

    void remove(String id);
}
