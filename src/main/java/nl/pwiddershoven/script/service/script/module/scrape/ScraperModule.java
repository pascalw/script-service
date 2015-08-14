package nl.pwiddershoven.script.service.script.module.scrape;

import nl.pwiddershoven.script.service.PageFetcher;
import nl.pwiddershoven.script.service.script.module.JsModule;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScraperModule implements JsModule {

    private PageFetcher pageFetcher;

    @Autowired
    public ScraperModule(PageFetcher pageFetcher) {
        this.pageFetcher = pageFetcher;
    }

    @Override
    public String name() {
        return "scraper";
    }

    public Document scrape(String url) {
        String pageSource = pageFetcher.fetch(url);
        return Jsoup.parse(pageSource, url);
    }
}
