package nl.pwiddershoven.scraper.service;

import com.google.common.base.MoreObjects;

public class ScrapeConfiguration {
    public final String pageUrl;
    public final String processingScript;
    public final String contentType;

    public ScrapeConfiguration(String pageUrl, String processingScript, String contentType) {
        this.pageUrl = pageUrl;
        this.processingScript = processingScript;
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("pageUrl", pageUrl)
                .add("processingScript", processingScript)
                .add("contentType", contentType)
                .toString();
    }
}
