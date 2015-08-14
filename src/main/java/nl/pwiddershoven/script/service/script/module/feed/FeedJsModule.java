package nl.pwiddershoven.script.service.script.module.feed;

import java.io.StringReader;

import nl.pwiddershoven.script.service.PageFetcher;
import nl.pwiddershoven.script.service.script.module.JsModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;

@Component
public class FeedJsModule implements JsModule {

    private PageFetcher pageFetcher;

    @Autowired
    public FeedJsModule(PageFetcher pageFetcher) {
        this.pageFetcher = pageFetcher;
    }

    @Override
    public String name() {
        return "feed";
    }

    public FeedBuilder newFeed() {
        return new FeedBuilder();
    }

    public FeedBuilder fetchFeed(String url) throws FeedException {
        SyndFeedInput syndFeedInput = new SyndFeedInput();

        String pageSource = pageFetcher.fetch(url);
        SyndFeed feed = syndFeedInput.build(new StringReader(pageSource));

        return new FeedBuilder(feed);
    }
}
