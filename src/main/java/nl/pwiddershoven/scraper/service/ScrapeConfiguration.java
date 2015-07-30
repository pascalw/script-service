package nl.pwiddershoven.scraper.service;

import com.google.common.base.MoreObjects;

public class ScrapeConfiguration {
    public final String pageUrl;
    public final String processingScript;

    public ScrapeConfiguration(String pageUrl, String processingScript) {
        this.pageUrl = pageUrl;
        this.processingScript = processingScript;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("pageUrl", pageUrl)
                .add("processingScript", processingScript)
                .toString();
    }
}
