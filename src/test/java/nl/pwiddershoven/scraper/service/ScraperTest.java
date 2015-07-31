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
    public void scrape_generatesJson() {
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

        ScrapeConfiguration scrapeConfiguration = new ScrapeConfiguration("http://example.org", script, "application/json");
        System.out.println(scraper.scrape(scrapeConfiguration));
    }

    @Test
    public void scrape_generatesFeed() {
        String script = "function(page) {\n" +
                        "    var feed = newFeed()\n" +
                        "      .setTitle(\"My feed\")\n" +
                        "      .setDescription(\"My feed\")\n" +
                        "      .setLink(\"http://google.com\");\n" +
                        "\n" +
                        "    var entry = feed.newEntry()\n" +
                        "      .setTitle(\"Item 1\")\n" +
                        "      .setLink(\"http://google.com\")\n" +
                        "      .setPublishedDate(new java.util.Date())\n" +
                        "      .setDescription(\"ohai!\");\n" +
                        "\n" +
                        "    feed.addEntry(entry);\n" +
                        "    return feed;\n" +
                        "}";

        ScrapeConfiguration scrapeConfiguration = new ScrapeConfiguration("http://example.org", script, "text/xml");
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