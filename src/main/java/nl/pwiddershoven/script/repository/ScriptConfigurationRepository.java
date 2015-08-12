package nl.pwiddershoven.script.repository;

import nl.pwiddershoven.script.service.ScriptConfiguration;

public interface ScriptConfigurationRepository {

    ScriptConfiguration find(String id);

    String save(ScriptConfiguration scriptConfiguration);

    void update(String id, ScriptConfiguration scriptConfiguration);
}
