package nl.pwiddershoven.script.service;

import java.util.Date;

import org.junit.Test;

public class FeedBuilderTest {
    @Test
    public void buildFeed() {
        FeedBuilder.FeedEntry entry = new FeedBuilder.FeedEntry();
        entry.setTitle("Item 1");
        entry.setLink("http://google.com");
        entry.setPublishedDate(new Date());
        entry.setDescription("ohai!");

        String xml = new FeedBuilder()
                .setTitle("My feed")
                .setDescription("My feed")
                .setLink("http://google.com")
                .addEntry(entry)
                .build("atom_1.0");

        System.out.println(xml);
    }

}