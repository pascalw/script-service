package nl.pwiddershoven.script.service.script.module.request;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;

import nl.pwiddershoven.script.service.script.JsContext;
import nl.pwiddershoven.script.service.script.module.JsModule;
import nl.pwiddershoven.script.service.script.module.JsModuleProvider;

import org.springframework.stereotype.Component;

@Component
public class RequestModuleProvider implements JsModuleProvider {
    @Override
    public String name() {
        return "request";
    }

    @Override
    public JsModule module(JsContext jsContext) {
        ContainerRequestContext request = jsContext.getAttribute("request", ContainerRequestContext.class);
        return new RequestModule(request);
    }

    public static class RequestModule implements JsModule {
        private final ContainerRequestContext request;

        public RequestModule(ContainerRequestContext request) {
            this.request = request;
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
    }
}
