package nl.pwiddershoven.script.service.script.module.scraper;

import nl.pwiddershoven.script.service.PageFetcher;
import nl.pwiddershoven.script.service.script.module.JsModuleProvider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScraperModuleProvider implements JsModuleProvider {

    public static class ScraperModule implements JsModule {
        private final PageFetcher pageFetcher;

        public ScraperModule(PageFetcher pageFetcher) {
            this.pageFetcher = pageFetcher;
        }

        public Document scrape(String url) {
            String pageSource = pageFetcher.fetch(url);
            return Jsoup.parse(pageSource, url);
        }
    }

    private ScraperModule module;

    @Autowired
    public ScraperModuleProvider(PageFetcher pageFetcher) {
        module = new ScraperModule(pageFetcher);
    }

    @Override
    public String name() {
        return "scraper";
    }

    @Override
    public ScraperModule module() {
        return module;
    }

}
