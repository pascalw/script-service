package nl.pwiddershoven.script.service;

import com.google.common.base.MoreObjects;

public class ScriptConfiguration {
    public final String processingScript;
    public final String contentType;

    public ScriptConfiguration(String processingScript, String contentType) {
        this.processingScript = processingScript;
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("processingScript", processingScript)
                .add("contentType", contentType)
                .toString();
    }
}
