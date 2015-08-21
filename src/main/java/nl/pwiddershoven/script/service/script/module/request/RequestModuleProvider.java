package nl.pwiddershoven.script.service.script.module.request;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;

import nl.pwiddershoven.script.service.script.JsContext;
import nl.pwiddershoven.script.service.script.module.JsModule;
import nl.pwiddershoven.script.service.script.module.JsModuleProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RequestModuleProvider implements JsModuleProvider {
    private ObjectMapper objectMapper;

    @Override
    public String name() {
        return "request";
    }

    @Override
    public RequestModule module(JsContext jsContext) {
        ContainerRequestContext request = jsContext.getAttribute("request", ContainerRequestContext.class);
        return new RequestModule(request, objectMapper);
    }

    public static class RequestModule implements JsModule {
        private final ContainerRequestContext request;
        private final ObjectMapper objectMapper;

        public RequestModule(ContainerRequestContext request, ObjectMapper objectMapper) {
            this.request = request;
            this.objectMapper = objectMapper;
        }

        public UriInfo getUriInfo() {
            return request.getUriInfo();
        }

        public String getMethod() {
            return request.getMethod();
        }

        public MultivaluedMap<String, String> getHeaders() {
            return request.getHeaders();
        }

        public String getHeaderString(String name) {
            return request.getHeaderString(name);
        }

        public Locale getLanguage() {
            return request.getLanguage();
        }

        public int getLength() {
            return request.getLength();
        }

        public MediaType getMediaType() {
            return request.getMediaType();
        }

        public Map<String, Cookie> getCookies() {
            return request.getCookies();
        }

        public InputStream getEntityStream() {
            return request.getEntityStream();
        }

        public Map<String, Object> getEntityJSON() throws IOException {
            return objectMapper.readValue(request.getEntityStream(), new TypeReference<HashMap<String, Object>>() {});
        }
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
