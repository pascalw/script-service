package nl.pwiddershoven.script.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;

public class FeedBuilder {
    private SyndFeed feed;
    private List<SyndEntry> entries;

    public FeedBuilder() {
        feed = new SyndFeedImpl();
        entries = new ArrayList<>();
    }

    public FeedBuilder(SyndFeed feed) {
        this.feed = feed;
        this.entries = feed.getEntries();
    }

    public FeedBuilder setTitle(String title) {
        feed.setTitle(title);
        return this;
    }

    public FeedBuilder setLink(String link) {
        feed.setLink(link);
        return this;
    }

    public FeedBuilder setDescription(String description) {
        feed.setDescription(description);
        return this;
    }

    public FeedBuilder addEntry(SyndEntry entry) {
        entries.add(entry);
        return this;
    }

    public FeedBuilder addEntry(FeedEntry entry) {
        return addEntry(entry.entry);
    }

    public FeedEntry newEntry() {
        return new FeedEntry();
    }

    public void filterEntries(Predicate<FeedEntry> predicate) {
        entries = entries.stream()
                .map(FeedEntry::new)
                .filter(predicate)
                .map(e -> e.entry)
                .collect(Collectors.toList());
    }

    public String build(String feedType) {
        feed.setFeedType(feedType);
        feed.setEntries(entries);

        StringWriter stringWriter = new StringWriter();
        SyndFeedOutput output = new SyndFeedOutput();

        try {
            output.output(feed, stringWriter);
        } catch (IOException | FeedException e) {
            throw new RuntimeException(e);
        }

        return stringWriter.toString();
    }

    public String build() {
        return build("rss_2.0");
    }

    public static class FeedEntry {
        private SyndEntry entry;

        public FeedEntry() {
            this(new SyndEntryImpl());
        }

        public FeedEntry(SyndEntry entry) {
            this.entry = entry;
        }

        public String getTitle() {
            return entry.getTitle();
        }

        public FeedEntry setTitle(String title) {
            entry.setTitle(title);
            return this;
        }

        public String getLink() {
            return entry.getLink();
        }

        public FeedEntry setLink(String link) {
            entry.setLink(link);
            return this;
        }

        public String getDescription() {
            SyndContent syndContent = entry.getDescription();
            return syndContent.getValue();
        }

        public FeedEntry setDescription(String description) {
            SyndContent syndContent = new SyndContentImpl();
            syndContent.setType("text/html");
            syndContent.setValue(description);

            entry.setDescription(syndContent);
            return this;
        }

        public Date getPublishedDate() {
            return entry.getPublishedDate();
        }

        public FeedEntry setPublishedDate(Date publishedDate) {
            entry.setPublishedDate(publishedDate);
            return this;
        }

        public String getAuthor() {
            return entry.getAuthor();
        }

        public FeedEntry setAuthor(String author) {
            SyndPerson authorPerson = new SyndPersonImpl();
            authorPerson.setName(author);

            List<SyndPerson> authors = ImmutableList.of(authorPerson);
            entry.setAuthors(authors);

            return this;
        }

        public List<String> getCategories() {
            return entry.getCategories().stream()
                    .map(SyndCategory::getName)
                    .collect(Collectors.toList());
        }
    }
}
