package nl.pwiddershoven.scriptor.service.script.module.cache;

import nl.pwiddershoven.scriptor.repository.CacheRepository;
import nl.pwiddershoven.scriptor.service.script.JsContext;
import nl.pwiddershoven.scriptor.service.script.module.JsModule;
import nl.pwiddershoven.scriptor.service.script.module.JsModuleProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Usage:
 * 
 * var cache = require('cache');
 * cache.set('something');
 * 
 * var cached = cache.get();
 * cache.set({ set: 'anything' });
 */
@Component
public class CacheModuleProvider implements JsModuleProvider {
    private final CacheRepository cacheRepository;

    @Autowired
    public CacheModuleProvider(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    @Override
    public String name() {
        return "cache";
    }

    @Override
    public CacheModule module(JsContext jsContext) {
        return new CacheModule(cacheRepository, jsContext.getAttribute("id", String.class));
    }

    public static class CacheModule implements JsModule {
        private final CacheRepository cacheRepository;
        private final String cacheId;

        public CacheModule(CacheRepository cacheRepository, String cacheId) {
            this.cacheRepository = cacheRepository;
            this.cacheId = cacheId;
        }

        public Object get() {
            return cacheRepository.get(cacheId);
        }

        public void set(Object data) {
            cacheRepository.set(cacheId, data);
        }
    }
}
