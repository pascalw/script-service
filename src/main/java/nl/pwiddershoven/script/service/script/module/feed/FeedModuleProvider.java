package nl.pwiddershoven.script.service.script.module.feed;

import java.io.StringReader;

import nl.pwiddershoven.script.service.PageFetcher;
import nl.pwiddershoven.script.service.script.module.JsModuleProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;

@Component
public class FeedModuleProvider implements JsModuleProvider {

    private FeedModule module;

    public static class FeedModule implements JsModule {
        private PageFetcher pageFetcher;

        public FeedModule(PageFetcher pageFetcher) {
            this.pageFetcher = pageFetcher;
        }

        public FeedBuilder newFeed() {
            return new FeedBuilder();
        }

        public FeedBuilder fetch(String url) throws FeedException {
            SyndFeedInput syndFeedInput = new SyndFeedInput();

            String pageSource = pageFetcher.fetch(url);
            SyndFeed feed = syndFeedInput.build(new StringReader(pageSource));

            return new FeedBuilder(feed);
        }
    }

    @Autowired
    public FeedModuleProvider(PageFetcher pageFetcher) {
        module = new FeedModule(pageFetcher);
    }

    @Override
    public String name() {
        return "feed";
    }

    @Override
    public FeedModule module() {
        return module;
    }

}
