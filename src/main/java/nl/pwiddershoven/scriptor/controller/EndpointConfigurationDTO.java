package nl.pwiddershoven.scriptor.controller;

import javax.validation.constraints.NotNull;

import nl.pwiddershoven.scriptor.service.EndpointConfiguration;

import com.google.common.base.Strings;

public class EndpointConfigurationDTO {
    public String id;

    @NotNull
    public String script;

    @NotNull
    public String contentType;

    public String accessToken;

    EndpointConfiguration toEndpointConfiguration() {
        String accessToken = Strings.isNullOrEmpty(this.accessToken) ? null : this.accessToken;
        return new EndpointConfiguration(script, contentType, accessToken);
    }
}
