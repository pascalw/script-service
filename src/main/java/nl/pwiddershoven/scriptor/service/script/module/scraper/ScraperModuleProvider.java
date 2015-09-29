package nl.pwiddershoven.scriptor.service.script.module.scraper;

import java.util.Collections;
import java.util.Map;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import nl.pwiddershoven.scriptor.service.PageFetcher;
import nl.pwiddershoven.scriptor.service.script.JsContext;
import nl.pwiddershoven.scriptor.service.script.MarshalingHelper;
import nl.pwiddershoven.scriptor.service.script.module.JsModule;
import nl.pwiddershoven.scriptor.service.script.module.JsModuleProvider;

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
            return scrape(url, Collections.emptyMap());
        }

        public Document scrape(String url, Map<String, Object> headers) {
            String pageSource = pageFetcher.fetch(url, unwrap(headers));
            return Jsoup.parse(pageSource, url);
        }

        public Document parseHtml(String html, String baseUri) {
            return Jsoup.parse(html, baseUri);
        }

        @SuppressWarnings("unchecked")
        private Map<String, Object> unwrap(Map<String, Object> headers) {
            if (headers instanceof ScriptObjectMirror)
                headers = (Map<String, Object>) MarshalingHelper.unwrap((ScriptObjectMirror) headers);

            return headers;
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
    public ScraperModule module(JsContext jsContext) {
        return module;
    }

}
