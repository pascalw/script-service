package nl.pwiddershoven.scraper.controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import nl.pwiddershoven.scraper.service.ScrapeConfiguration;
import nl.pwiddershoven.scraper.service.Scraper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class ScrapeController {

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
        return scraper.scrape(scrapeConfiguration);
    }
}
