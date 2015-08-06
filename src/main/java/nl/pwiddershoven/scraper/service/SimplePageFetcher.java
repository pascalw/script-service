package nl.pwiddershoven.scraper.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class SimplePageFetcher implements PageFetcher {
    private final Logger logger = Logger.getLogger(PageFetcher.class);

    @Override
    public String fetch(String urlString) {
        long start = System.currentTimeMillis();
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String inputLine;
                StringBuilder sb = new StringBuilder();

                while ((inputLine = in.readLine()) != null)
                    sb.append(inputLine);

                in.close();
                return sb.toString();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            long end = System.currentTimeMillis();
            logger.info("Fetching took " + (end - start));
        }
    }
}
