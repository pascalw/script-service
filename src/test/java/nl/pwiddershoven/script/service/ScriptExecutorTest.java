package nl.pwiddershoven.script.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StreamUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class ScriptExecutorTest {
    private ScriptExecutor scriptExecutor = new ScriptExecutor();
    private PageFetcher mockPageFetcher = mock(PageFetcher.class);

    @Before
    public void setUp() {
        scriptExecutor.setPageFetcher(mockPageFetcher);

        when(mockPageFetcher.fetch(anyString())).thenReturn(resourceContent("index.html"));
    }

    @Test
    public void script_generatesJson() {
        String script = "var page = fetchDocument('http://example.org');\n" +
                        "var result = { speakers: [] };\n" +
                        "  \n" +
                        "page.select(\"#speakers ul li\").stream().forEach(function(li) {\n" +
                        "  var name = li.select(\"h3\").text();\n" +
                        "  var bio = li.select(\"h4\").text();\n" +
                        "  result.speakers.push({ name: name, bio: bio });\n" +
                        "});\n" +
                        "  \n" +
                        "return result;";

        ScriptConfiguration scriptConfiguration = new ScriptConfiguration(script, "application/json");
        Map<String, Object> result = execute(scriptConfiguration);

        assertEquals(ImmutableMap.of("speakers", (Object) ImmutableList.of(
                ImmutableMap.of("name", "Dr. André Kuipers", "bio", "Astronaut & Ambassador of Earth"),
                ImmutableMap.of("name", "Corey Haines", "bio", "Software Journeyman"),
                ImmutableMap.of("name", "Kevlin Henney", "bio", "Consultant, speaker, writer and trainer"),
                ImmutableMap.of("name", "Francesc Campoy", "bio", "Go Developer Programs Engineer"),
                ImmutableMap.of("name", "Jessie Frazelle", "bio", "Core Docker maintainer"),
                ImmutableMap.of("name", "Mark Bates", "bio", "Software Developer, Author, Father, Entrepreneur")
                )), result);

        verify(mockPageFetcher).fetch("http://example.org");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> execute(ScriptConfiguration scriptConfiguration) {
        return (Map<String, Object>) scriptExecutor.execute(scriptConfiguration);
    }

    @Test
    public void script_generatesFeed() {
        String script = "var feed = newFeed()\n" +
                        "  .setTitle(\"My feed\")\n" +
                        "  .setDescription(\"My feed\")\n" +
                        "  .setLink(\"http://google.com\");\n" +
                        "\n" +
                        "var entry = feed.newEntry()\n" +
                        "  .setTitle(\"Item 1\")\n" +
                        "  .setLink(\"http://google.com\")\n" +
                        "  .setPublishedDate(new java.util.Date())\n" +
                        "  .setDescription(\"ohai!\");\n" +
                        "\n" +
                        "feed.addEntry(entry);\n" +
                        "return feed;";

        ScriptConfiguration scriptConfiguration = new ScriptConfiguration(script, "text/xml");
        System.out.println(scriptExecutor.execute(scriptConfiguration));
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