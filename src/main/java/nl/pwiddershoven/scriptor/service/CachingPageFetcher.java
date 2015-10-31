package nl.pwiddershoven.scriptor.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.google.common.cache.*;

@Primary
@Component
public class CachingPageFetcher implements PageFetcher {
    private final PageFetcher pageFetcher;
    private final LoadingCache<String, String> pageCache;

    @Autowired
    public CachingPageFetcher(PageFetcher pageFetcher) {
        this.pageFetcher = pageFetcher;
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
