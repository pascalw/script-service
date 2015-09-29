package nl.pwiddershoven.scriptor.service.script.module.scraper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import nl.pwiddershoven.scriptor.service.PageFetcher;

import org.jsoup.nodes.Document;
import org.junit.Test;

public class ScraperModuleProviderTest {
    private PageFetcher mockPageFetcher = mock(PageFetcher.class);
    private ScraperModuleProvider.ScraperModule scraperModule = new ScraperModuleProvider(mockPageFetcher).module(null);

    @Test
    public void returns_jsoup_document_for_requested_url() {
        when(mockPageFetcher.fetch("http://example.org", Collections.emptyMap())).thenReturn("<html>\n" +
                                                                                             "  <head>\n" +
                                                                                             "    <title>Hello, world!</title>\n" +
                                                                                             "  </head>\n" +
                                                                                             "</html>");

        Document document = scraperModule.scrape("http://example.org");
        assertEquals("Hello, world!", document.title());
    }

    @Test
    public void returns_jsoup_document_for_html_string() {
        Document document = scraperModule.parseHtml("<html><title>Hello, world!</title></html>", "http://example.org");
        assertEquals("Hello, world!", document.title());
    }

    @Test
    public void returns_jsoup_document_for_html_string_passes_base_uri() {
        Document document = scraperModule.parseHtml("<html><a href=\"/relative\">Link</a></html>", "http://example.org");
        assertEquals("http://example.org/relative", document.select("a").attr("abs:href"));
    }
}