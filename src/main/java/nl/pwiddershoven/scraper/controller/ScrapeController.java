package nl.pwiddershoven.scraper.controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import nl.pwiddershoven.scraper.service.ScrapeConfiguration;
import nl.pwiddershoven.scraper.service.Scraper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class ScrapeController {
    private final Logger logger = Logger.getLogger(ScrapeController.class);

    static class ScrapeRequest {
        public String pageUrl;
        public String script;
    }

    @Autowired
    private Scraper scraper;

    @POST
    @Path("/scrape")
    public Object scrape(ScrapeRequest scrapeRequest) {
        ScrapeConfiguration scrapeConfiguration = new ScrapeConfiguration(scrapeRequest.pageUrl, scrapeRequest.script);

        long start = System.currentTimeMillis();
        Object result = scraper.scrape(scrapeConfiguration);
        long end = System.currentTimeMillis();

        logger.info("Processing took " + (end - start));

        return result;
    }
}
