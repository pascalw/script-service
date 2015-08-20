package nl.pwiddershoven.script.controller;

import javax.validation.constraints.NotNull;

import nl.pwiddershoven.script.service.ScriptConfiguration;

import com.google.common.base.Strings;

public class ScriptConfigurationDTO {
    public String id;

    @NotNull
    public String script;

    @NotNull
    public String contentType;

    public String accessToken;

    ScriptConfiguration toScriptConfiguration() {
        String accessToken = Strings.isNullOrEmpty(this.accessToken) ? null : this.accessToken;
        return new ScriptConfiguration(script, contentType, accessToken);
    }
}
