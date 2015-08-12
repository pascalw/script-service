package nl.pwiddershoven.script.service;

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

    private LoadingCache<String, String> pageCache;

    @PostConstruct
    private void createCache() {
        this.pageCache = CacheBuilder.newBuilder()
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .build(CacheLoader.from(pageFetcher::fetch));
    }

    @Override
    public String fetch(String urlString) {
        try {
            return pageCache.get(urlString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
