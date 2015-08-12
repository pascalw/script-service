package nl.pwiddershoven.script.service;

import com.google.common.base.MoreObjects;

public class ScriptConfiguration {
    public final String id;
    public final String processingScript;
    public final String contentType;

    public ScriptConfiguration(String processingScript, String contentType) {
        this(null, processingScript, contentType);
    }

    public ScriptConfiguration(String id, String processingScript, String contentType) {
        this.id = id;
        this.processingScript = processingScript;
        this.contentType = contentType;
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
