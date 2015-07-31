package nl.pwiddershoven.scraper.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nl.pwiddershoven.scraper.repository.ScrapeConfigurationRepository;
import nl.pwiddershoven.scraper.service.ScrapeConfiguration;
import nl.pwiddershoven.scraper.service.Scraper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class ScrapeController {
    private final Logger logger = Logger.getLogger(ScrapeController.class);

    static class ScrapeRequest {
        @NotNull
        public String pageUrl;
        @NotNull
        public String script;
        @NotNull
        public String contentType;
    }

    @Autowired
    private Scraper scraper;

    @Autowired
    private ScrapeConfigurationRepository scrapeConfigurationRepository;

    @POST
    @Path("/doScrape")
    public Response scrape(ScrapeRequest scrapeRequest) {
        ScrapeConfiguration scrapeConfiguration = buildScrapeConfiguration(scrapeRequest);
        return doScrape(scrapeConfiguration);
    }

    @POST
    @Path("/scrape")
    public Response createConfiguration(@Valid ScrapeRequest scrapeRequest) {
        String id = scrapeConfigurationRepository.save(buildScrapeConfiguration(scrapeRequest));
        return Response.created(getLocation(id)).build();
    }

    @PUT
    @Path("/scrape/{id}")
    public void updateConfiguration(@PathParam("id") String id, ScrapeRequest scrapeRequest) {
        scrapeConfigurationRepository.update(id, buildScrapeConfiguration(scrapeRequest));
    }

    private ScrapeConfiguration buildScrapeConfiguration(ScrapeRequest scrapeRequest) {
        return new ScrapeConfiguration(scrapeRequest.pageUrl, scrapeRequest.script, scrapeRequest.contentType);
    }

    @GET
    @Path("/scrape/{id}")
    public Object getConfiguration(@PathParam("id") String id) {
        ScrapeConfiguration scrapeConfiguration = scrapeConfigurationRepository.find(id);
        return doScrape(scrapeConfiguration);
    }

    private Response doScrape(ScrapeConfiguration scrapeConfiguration) {
        long start = System.currentTimeMillis();
        Object result = scraper.scrape(scrapeConfiguration);
        long end = System.currentTimeMillis();

        logger.info("Processing took " + (end - start));

        return Response.ok()
                .type(scrapeConfiguration.contentType)
                .entity(result)
                .build();
    }

    private URI getLocation(String id) {
        try {
            return new URI("/scrape/" + id);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
