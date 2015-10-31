package nl.pwiddershoven.scriptor.controller;

import javax.validation.constraints.NotNull;

import nl.pwiddershoven.scriptor.service.EndpointConfiguration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;

public class EndpointConfigurationDTO {
    public final String id;

    @NotNull
    public final String script;

    @NotNull
    public final String contentType;

    public final String accessToken;

    @JsonCreator
    public EndpointConfigurationDTO(
            @JsonProperty("id") String id,
            @JsonProperty("script") String script,
            @JsonProperty("contentType") String contentType,
            @JsonProperty("accessToken") String accessToken) {
        this.id = id;
        this.script = script;
        this.contentType = contentType;
        this.accessToken = accessToken;
    }

    public EndpointConfiguration toEndpointConfiguration() {
        String accessToken = Strings.isNullOrEmpty(this.accessToken) ? null : this.accessToken;
        return new EndpointConfiguration(script, contentType, accessToken);
    }
}
