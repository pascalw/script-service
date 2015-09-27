package nl.pwiddershoven.scriptor.service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.google.common.cache.*;

@Primary
@Component
public class CachingPageFetcher implements PageFetcher {
    @Autowired
    private PageFetcher pageFetcher;

    private LoadingCache<CacheKey, String> pageCache;

    @PostConstruct
    private void createCache() {
        this.pageCache = CacheBuilder.newBuilder()
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .build(CacheLoader.from(key -> pageFetcher.fetch(key.url, key.headers)));
    }

    @Override
    public String fetch(String urlString) {
        return fetch(urlString, Collections.emptyMap());
    }

    @Override
    public String fetch(String urlString, Map<String, Object> headers) {
        try {
            return pageCache.get(new CacheKey(urlString, headers));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private class CacheKey {
        public final String url;
        public final Map<String, Object> headers;

        public CacheKey(String url, Map<String, Object> headers) {
            this.url = url;
            this.headers = headers;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            CacheKey cacheKey = (CacheKey) o;
            return Objects.equals(url, cacheKey.url) &&
                   Objects.equals(headers, cacheKey.headers);
        }

        @Override
        public int hashCode() {
            return Objects.hash(url, headers);
        }
    }

}
