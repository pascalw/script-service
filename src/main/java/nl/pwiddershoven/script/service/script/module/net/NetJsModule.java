package nl.pwiddershoven.script.service.script.module.net;

import nl.pwiddershoven.script.service.PageFetcher;
import nl.pwiddershoven.script.service.script.module.JsModule;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NetJsModule implements JsModule {

    private PageFetcher pageFetcher;

    @Autowired
    public NetJsModule(PageFetcher pageFetcher) {
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
