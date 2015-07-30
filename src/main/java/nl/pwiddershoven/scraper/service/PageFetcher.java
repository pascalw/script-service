package nl.pwiddershoven.scraper.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.stereotype.Component;

@Component
public class PageFetcher {

    public String fetch(String urlString) {
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
        }
    }
}
