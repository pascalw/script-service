package nl.pwiddershoven.script.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Map;

import nl.pwiddershoven.script.service.script.ScriptExecutor;
import nl.pwiddershoven.script.service.script.context.JsContext;
import nl.pwiddershoven.script.service.script.context.feed.FeedJsContext;
import nl.pwiddershoven.script.service.script.context.net.NetJsContext;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StreamUtils;

import com.google.common.collect.*;

public class ScriptExecutorTest {
    private ScriptExecutor scriptExecutor = new ScriptExecutor();
    private PageFetcher mockPageFetcher = mock(PageFetcher.class);

    @Before
    public void setUp() {
        HashSet<JsContext> jsContexts = Sets.newHashSet(new FeedJsContext(mockPageFetcher), new NetJsContext(mockPageFetcher));
        scriptExecutor.setJsContexts(jsContexts);
    }

    @Test
    public void script_generatesJson() {
        when(mockPageFetcher.fetch("http://example.org")).thenReturn(resourceContent("index.html"));

        String script = "var page = net.fetchDocument('http://example.org');\n" +
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
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> execute(ScriptConfiguration scriptConfiguration) {
        return (Map<String, Object>) scriptExecutor.execute(scriptConfiguration);
    }

    @Test
    public void script_generatesFeed() {
        String script = "var f = feed.newFeed()\n" +
                        "  .setTitle(\"My feed\")\n" +
                        "  .setDescription(\"My feed\")\n" +
                        "  .setLink(\"http://google.com\");\n" +
                        "\n" +
                        "var entry = f.newEntry()\n" +
                        "  .setTitle(\"Item 1\")\n" +
                        "  .setLink(\"http://google.com\")\n" +
                        "  .setPublishedDate(new java.util.Date())\n" +
                        "  .setDescription(\"ohai!\");\n" +
                        "\n" +
                        "f.addEntry(entry);\n" +
                        "return f;";

        ScriptConfiguration scriptConfiguration = new ScriptConfiguration(script, "text/xml");
        System.out.println(scriptExecutor.execute(scriptConfiguration));
    }

    @Test
    public void script_modifiesFeed() throws Exception {
        when(mockPageFetcher.fetch(anyString())).thenReturn(resourceContent("feed.xml"));

        String script = "var f = feed.fetchFeed('http://example.org');\n" +
                        "f.filterEntries(function(e) { return e.getTitle().contains(\"1.546\") });\n" +
                        "\n" +
                        "return f.build();";

        ScriptConfiguration scriptConfiguration = new ScriptConfiguration(script, "text/xml");
        String feedXml = (String) scriptExecutor.execute(scriptConfiguration);

        assertTrue(feedXml.contains("1.546"));
        assertEquals(1, StringUtils.countMatches(feedXml, "<item>"));
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