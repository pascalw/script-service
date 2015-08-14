package nl.pwiddershoven.script.service.script.context.feed;

import java.io.StringReader;

import nl.pwiddershoven.script.service.PageFetcher;
import nl.pwiddershoven.script.service.script.context.JsContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;

@Component
public class FeedJsContext implements JsContext {

    private PageFetcher pageFetcher;

    @Autowired
    public FeedJsContext(PageFetcher pageFetcher) {
        this.pageFetcher = pageFetcher;
    }

    @Override
    public String moduleName() {
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
