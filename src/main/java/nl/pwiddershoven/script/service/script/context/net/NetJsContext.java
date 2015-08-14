package nl.pwiddershoven.script.service.script.context.net;

import nl.pwiddershoven.script.service.PageFetcher;
import nl.pwiddershoven.script.service.script.context.JsContext;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NetJsContext implements JsContext {

    private PageFetcher pageFetcher;

    @Autowired
    public NetJsContext(PageFetcher pageFetcher) {
        this.pageFetcher = pageFetcher;
    }

    @Override
    public String moduleName() {
        return "net";
    }

    public Document fetchDocument(String url) {
        String pageSource = pageFetcher.fetch(url);
        return Jsoup.parse(pageSource, url);
    }
}
