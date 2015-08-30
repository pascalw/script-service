package nl.pwiddershoven.scriptor.service;

import com.google.common.base.MoreObjects;

public class EndpointConfiguration {
    public final String id;
    public final String processingScript;
    public final String contentType;
    public final String accessToken;

    public EndpointConfiguration(String processingScript, String contentType, String accessToken) {
        this(null, processingScript, contentType, accessToken);
    }

    public EndpointConfiguration(String id, String processingScript, String contentType, String authenticationToken) {
        this.id = id;
        this.processingScript = processingScript;
        this.contentType = contentType;
        this.accessToken = authenticationToken;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("processingScript", processingScript)
                .add("contentType", contentType)
                .toString();
    }
}
