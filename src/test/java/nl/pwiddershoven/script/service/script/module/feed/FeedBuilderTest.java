package nl.pwiddershoven.script.service.script.module.feed;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import nl.pwiddershoven.script.service.script.module.feed.FeedBuilder;

import org.junit.Test;

public class FeedBuilderTest {
    @Test
    public void buildFeed() {
        FeedBuilder.FeedEntry entry = new FeedBuilder.FeedEntry();
        entry.setTitle("Item 1");
        entry.setLink("http://google.com");
        entry.setPublishedDate(new Date(1439586116384l));
        entry.setDescription("ohai!");

        String xml = new FeedBuilder()
                .setTitle("My feed")
                .setDescription("My feed")
                .setLink("http://google.com")
                .addEntry(entry)
                .build("atom_1.0");

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                     "<feed xmlns=\"http://www.w3.org/2005/Atom\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\r\n" +
                     "  <title>My feed</title>\r\n" +
                     "  <link rel=\"alternate\" href=\"http://google.com\" />\r\n" +
                     "  <subtitle>My feed</subtitle>\r\n" +
                     "  <entry>\r\n" +
                     "    <title>Item 1</title>\r\n" +
                     "    <link rel=\"alternate\" href=\"http://google.com\" />\r\n" +
                     "    <author>\r\n" +
                     "      <name />\r\n" +
                     "    </author>\r\n" +
                     "    <updated>2015-08-14T21:01:56Z</updated>\r\n" +
                     "    <published>2015-08-14T21:01:56Z</published>\r\n" +
                     "    <summary type=\"html\">ohai!</summary>\r\n" +
                     "    <dc:date>2015-08-14T21:01:56Z</dc:date>\r\n" +
                     "  </entry>\r\n" +
                     "</feed>\r\n", xml);
    }

}