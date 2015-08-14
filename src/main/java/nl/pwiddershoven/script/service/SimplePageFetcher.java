package nl.pwiddershoven.script.service;

import java.net.URI;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;

@Component
public class SimplePageFetcher implements PageFetcher {
    private final Logger logger = Logger.getLogger(PageFetcher.class);
    private final HttpRequestFactory httpRequestFactory = new NetHttpTransport().createRequestFactory();

    @Override
    public String fetch(String urlString) {
        long start = System.currentTimeMillis();
        try {
            HttpRequest request = httpRequestFactory.buildGetRequest(toUrl(urlString));
            return request.execute().parseAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            long end = System.currentTimeMillis();
            logger.info("Fetching took " + (end - start));
        }
    }

    private GenericUrl toUrl(String urlString) {
        URI encodedUri = UriComponentsBuilder
                .fromHttpUrl(urlString)
                .build()
                .encode()
                .toUri();

        return new GenericUrl(encodedUri);
    }

}
