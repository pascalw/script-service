package nl.pwiddershoven.script.service.script.module.feed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import nl.pwiddershoven.script.service.PageFetcher;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.util.StreamUtils;

import com.rometools.rome.io.FeedException;

public class FeedModuleTest {
    private PageFetcher mockPageFetcher = mock(PageFetcher.class);
    private FeedModule feedModule = new FeedModule(mockPageFetcher);

    @Test
    public void newFeed_returnsEmptyFeedBuilder() {
        FeedBuilder feedBuilder = feedModule.newFeed();
        assertTrue(feedBuilder != null);
    }

    @Test
    public void fetchFeed_fetches_and_parses_feed() throws FeedException {
        when(mockPageFetcher.fetch("http://example.org")).thenReturn(resourceContent("feed.xml"));

        FeedBuilder feedBuilder = feedModule.fetch("http://example.org");
        assertEquals(3, StringUtils.countMatches(feedBuilder.build(), "<item>"));
    }

    private String resourceContent(String resource) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(resource);
        try {
            return StreamUtils.copyToString(stream, Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}