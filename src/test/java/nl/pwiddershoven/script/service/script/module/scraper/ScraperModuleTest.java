package nl.pwiddershoven.script.service.script.module.scraper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nl.pwiddershoven.script.service.PageFetcher;

import org.jsoup.nodes.Document;
import org.junit.Test;

public class ScraperModuleTest {
    private PageFetcher mockPageFetcher = mock(PageFetcher.class);
    private ScraperModuleProvider.ScraperModule scraperModule = new ScraperModuleProvider(mockPageFetcher).module(null);

    @Test
    public void returns_jsoup_document_for_requested_url() {
        when(mockPageFetcher.fetch("http://example.org")).thenReturn("<html>\n" +
                                                                     "  <head>\n" +
                                                                     "    <title>Hello, world!</title>\n" +
                                                                     "  </head>\n" +
                                                                     "</html>");

        Document document = scraperModule.scrape("http://example.org");
        assertEquals("Hello, world!", document.title());
    }
}