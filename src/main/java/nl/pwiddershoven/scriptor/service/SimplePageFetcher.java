package nl.pwiddershoven.scriptor.service;

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.IOUtils;
import com.google.common.base.Charsets;

@Component
public class SimplePageFetcher implements PageFetcher {
    private final Logger logger = Logger.getLogger(PageFetcher.class);
    private final HttpRequestFactory httpRequestFactory = new NetHttpTransport().createRequestFactory();

    @Override
    public String fetch(String urlString) {
        return fetch(urlString, Collections.emptyMap());
    }

    @Override
    public String fetch(String urlString, Map<String, Object> headers) {
        long start = System.currentTimeMillis();
        try {
            HttpRequest request = httpRequestFactory.buildGetRequest(toUrl(urlString));

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.putAll(headers);
            request.setHeaders(httpHeaders);

            return parseAsString(request.execute());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            long end = System.currentTimeMillis();
            logger.info("Fetching took " + (end - start));
        }
    }

    /**
     * Patched @{link HttpResponse#parseAsString} with default UTF-8 instead of ISO_8859_1
     * which IMO makes more sense.
     */
    private String parseAsString(HttpResponse response) throws IOException {
        InputStream content = response.getContent();
        if (content == null) {
            return "";
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(content, out);

        HttpMediaType mediaType = response.getMediaType();

        Charset charset = mediaType == null || mediaType.getCharsetParameter() == null ? Charsets.UTF_8 : mediaType.getCharsetParameter();
        return out.toString(charset.name());
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
