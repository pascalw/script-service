package nl.pwiddershoven.scraper.repository;

import java.util.HashMap;
import java.util.Map;

import nl.pwiddershoven.scraper.service.ScrapeConfiguration;

import org.springframework.stereotype.Component;

@Component
public class InMemoryScrapeConfigurationRepository implements ScrapeConfigurationRepository {
    private final Map<String, ScrapeConfiguration> storage = new HashMap<>();
    private long nextId = 1;

    @Override
    public ScrapeConfiguration find(String id) {
        return storage.get(id);
    }

    @Override
    public String save(ScrapeConfiguration scrapeConfiguration) {
        String id = "" + nextId();
        storage.put(id, scrapeConfiguration);

        return id;
    }

    private synchronized long nextId() {
        return nextId++;
    }
}
