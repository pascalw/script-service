package nl.pwiddershoven.scraper.repository;

import nl.pwiddershoven.scraper.service.ScrapeConfiguration;

public interface ScrapeConfigurationRepository {

    public ScrapeConfiguration find(String id);

    String save(ScrapeConfiguration scrapeConfiguration);

    void update(String id, ScrapeConfiguration scrapeConfiguration);
}
