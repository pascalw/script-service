package nl.pwiddershoven.scriptor.service;

import java.util.Map;

public interface PageFetcher {
    String fetch(String urlString);

    String fetch(String urlString, Map<String, Object> headers);
}