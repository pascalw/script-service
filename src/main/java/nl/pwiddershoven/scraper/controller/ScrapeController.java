package nl.pwiddershoven.scraper.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nl.pwiddershoven.scraper.config.AuthenticationNotRequired;
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

    static class ScrapeConfigurationDTO {
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
    public Response scrape(ScrapeConfigurationDTO scrapeConfigurationDTO) {
        ScrapeConfiguration scrapeConfiguration = buildScrapeConfiguration(scrapeConfigurationDTO);
        return doScrape(scrapeConfiguration);
    }

    @POST
    @Path("/config")
    public Response createConfiguration(@Valid ScrapeConfigurationDTO scrapeConfigurationDTO) {
        String id = scrapeConfigurationRepository.save(buildScrapeConfiguration(scrapeConfigurationDTO));
        return Response.created(getLocation(id)).build();
    }

    @PUT
    @Path("/config/{id}")
    public void updateConfiguration(@PathParam("id") String id, ScrapeConfigurationDTO scrapeConfigurationDTO) {
        scrapeConfigurationRepository.update(id, buildScrapeConfiguration(scrapeConfigurationDTO));
    }

    private ScrapeConfiguration buildScrapeConfiguration(ScrapeConfigurationDTO scrapeConfigurationDTO) {
        return new ScrapeConfiguration(scrapeConfigurationDTO.pageUrl, scrapeConfigurationDTO.script, scrapeConfigurationDTO.contentType);
    }

    @GET
    @Path("/config/{id}")
    public ScrapeConfigurationDTO getScrapeConfig(@PathParam("id") String id) {
        ScrapeConfiguration scrapeConfiguration = scrapeConfigurationRepository.find(id);

        ScrapeConfigurationDTO scrapeConfigurationDTO = new ScrapeConfigurationDTO();
        scrapeConfigurationDTO.script = scrapeConfiguration.processingScript;
        scrapeConfigurationDTO.contentType = scrapeConfiguration.contentType;
        scrapeConfigurationDTO.pageUrl = scrapeConfiguration.pageUrl;

        return scrapeConfigurationDTO;
    }

    @GET
    @AuthenticationNotRequired
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
            return new URI("/config/" + id);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
