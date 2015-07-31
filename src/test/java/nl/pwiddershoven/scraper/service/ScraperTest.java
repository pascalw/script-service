package nl.pwiddershoven.scraper.service;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StreamUtils;

public class ScraperTest {
    private Scraper scraper = new Scraper();
    private PageFetcher mockPageFetcher = mock(PageFetcher.class);

    @Before
    public void setUp() {
        scraper.setPageFetcher(mockPageFetcher);

        when(mockPageFetcher.fetch(anyString())).thenReturn(resourceContent("index.html"));
    }

    @Test
    public void scrapes() {
        String script = "function(page) { return page.title(); }";
        ScrapeConfiguration scrapeConfiguration = new ScrapeConfiguration("http://example.org", script);

        System.out.println(scraper.scrape(scrapeConfiguration));
    }

    @Test
    public void scrapes2() {
        String script = "function(page) {\n" +
                        "  var result = { speakers: [] };\n" +
                        "  \n" +
                        "  page.select(\"#speakers ul li\").stream().forEach(function(li) {\n" +
                        "    var name = li.select(\"h3\").text();\n" +
                        "    var bio = li.select(\"h4\").text();\n" +
                        "    result.speakers.push({ name: name, bio: bio });\n" +
                        "  });\n" +
                        "  \n" +
                        "  return result;\n" +
                        "}";

        ScrapeConfiguration scrapeConfiguration = new ScrapeConfiguration("http://example.org", script);
        System.out.println(scraper.scrape(scrapeConfiguration));
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