package nl.pwiddershoven.scraper.service;

import javax.script.*;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Scraper {
    private static ScriptEngine jsEngine = new ScriptEngineManager().getEngineByName("nashorn");
    private static final String SCRIPT_WRAPPER = "process = ";

    private PageFetcher pageFetcher;

    public Scraper() {
        try {
            jsEngine.eval("newFeed = function() { return Java.type('nl.pwiddershoven.scraper.service.Scraper.JsContext').newFeed(); };");
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public Object scrape(ScrapeConfiguration scrapeConfiguration) {
        Document document = fetchDocument(scrapeConfiguration);
        return process(document, scrapeConfiguration);
    }

    private Document fetchDocument(ScrapeConfiguration scrapeConfiguration) {
        String pageSource = pageFetcher.fetch(scrapeConfiguration.pageUrl);
        return Jsoup.parse(pageSource, scrapeConfiguration.pageUrl);
    }

    private Object process(Document document, ScrapeConfiguration scrapeConfiguration) {
        try {
            ScriptContext ctx = new SimpleScriptContext();

            Bindings bindings = jsEngine.createBindings();
            bindings.put("__doc", document);

            ctx.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
            ctx.setBindings(jsEngine.getBindings(ScriptContext.ENGINE_SCOPE), ScriptContext.ENGINE_SCOPE);

            jsEngine.eval(SCRIPT_WRAPPER + scrapeConfiguration.processingScript, ctx);
            Object result = jsEngine.eval("process(__doc);", ctx);

            if (result instanceof ScriptObjectMirror)
                result = MarshalingHelper.unwrap((ScriptObjectMirror) result);

            if(result instanceof FeedBuilder)
                result = ((FeedBuilder)result).build();

            return result;
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public static class JsContext {
        public static FeedBuilder newFeed() {
            return new FeedBuilder();
        }
    }

    @Autowired
    public void setPageFetcher(PageFetcher pageFetcher) {
        this.pageFetcher = pageFetcher;
    }
}
