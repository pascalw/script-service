package nl.pwiddershoven.scriptor.service.script.module.cache;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import nl.pwiddershoven.scriptor.repository.CacheRepository;
import nl.pwiddershoven.scriptor.service.script.JsContext;
import nl.pwiddershoven.scriptor.service.script.module.JsModule;

import org.junit.Test;

public class CacheModuleProviderTest {
    private static final String CACHE_ID = "cacheId";

    private final CacheRepository cacheRepository = mock(CacheRepository.class);
    private final CacheModuleProvider cacheModuleProvider = new CacheModuleProvider(cacheRepository);

    @Test
    public void saves_data_by_id() {
        MockJsContext jsContext = new MockJsContext("id", CACHE_ID);
        CacheModuleProvider.CacheModule module = cacheModuleProvider.module(jsContext);

        module.set("MY DATA");
        verify(cacheRepository).set(CACHE_ID, "MY DATA");
    }

    @Test
    public void retrieves_data_by_id() {
        when(cacheRepository.get(CACHE_ID)).thenReturn("MY DATA");

        MockJsContext jsContext = new MockJsContext("id", CACHE_ID);
        CacheModuleProvider.CacheModule module = cacheModuleProvider.module(jsContext);

        assertEquals("MY DATA", module.get());
    }

    private class MockJsContext implements JsContext {
        private Map<String, Object> attributes = new HashMap<>();

        public MockJsContext(String attributeName, Object attributeValue) {
            setAttribute(attributeName, attributeValue);
        }

        @Override
        public JsModule require(String moduleName) {
            return null;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T getAttribute(String attributeName, Class<T> attributeClass) {
            return (T) attributes.get(attributeName);
        }

        public void setAttribute(String name, Object value) {
            attributes.put(name, value);
        }
    }

}
