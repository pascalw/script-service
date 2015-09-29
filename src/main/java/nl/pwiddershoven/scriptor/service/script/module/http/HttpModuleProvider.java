package nl.pwiddershoven.scriptor.service.script.module.http;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import nl.pwiddershoven.scriptor.service.script.JsContext;
import nl.pwiddershoven.scriptor.service.script.MarshalingHelper;
import nl.pwiddershoven.scriptor.service.script.module.JsModule;
import nl.pwiddershoven.scriptor.service.script.module.JsModuleProvider;

import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.api.client.http.*;
import com.google.api.client.http.apache.ApacheHttpTransport;

@Component
public class HttpModuleProvider implements JsModuleProvider {
    private HttpModule httpModule = new HttpModule(buildHttpTransport());

    private ApacheHttpTransport buildHttpTransport() {
        // Google-http-client is not fully compatible with apache http client 4.3.
        // The minimal client however works. Does not support cookie handling, etc, but
        // should be fine in most cases.
        return new ApacheHttpTransport(HttpClients.createMinimal());
    }

    public static class HttpModule implements JsModule {
        private final HttpRequestFactory httpRequestFactory;

        HttpModule(HttpTransport httpTransport) {
            this.httpRequestFactory = httpTransport.createRequestFactory();
        }

        public HttpResponse get(String url) throws IOException {
            HttpRequest request = httpRequestFactory.buildGetRequest(toUrl(url));
            return request.execute();
        }

        public HttpResponse post(String url, String contentType, String content) throws IOException {
            HttpContent httpContent = toHttpContent(contentType, content);
            HttpRequest request = httpRequestFactory.buildPostRequest(toUrl(url), httpContent);

            return request.execute();
        }

        public HttpResponse put(String url, String contentType, String content) throws IOException {
            HttpContent httpContent = toHttpContent(contentType, content);
            HttpRequest request = httpRequestFactory.buildPutRequest(toUrl(url), httpContent);

            return request.execute();
        }

        public HttpResponse patch(String url, String contentType, String content) throws IOException {
            HttpContent httpContent = toHttpContent(contentType, content);
            HttpRequest request = httpRequestFactory.buildPatchRequest(toUrl(url), httpContent);

            return request.execute();
        }

        public HttpResponse delete(String url) throws IOException {
            HttpRequest request = httpRequestFactory.buildDeleteRequest(toUrl(url));
            return request.execute();
        }

        public HttpRequestFactoryBridge requestFactory() {
            return new HttpRequestFactoryBridge();
        }

        private HttpContent toHttpContent(String contentType, String content) {
            return ByteArrayContent.fromString(contentType, content);
        }

        private GenericUrl toUrl(String urlString) {
            URI encodedUri = UriComponentsBuilder
                    .fromHttpUrl(urlString)
                    .build()
                    .encode()
                    .toUri();

            return new GenericUrl(encodedUri);
        }

        public class HttpRequestFactoryBridge {
            public HttpRequestBridge buildRequest(String requestMethod, String urlString, String contentType, String content) throws IOException {
                return wrap(httpRequestFactory.buildRequest(requestMethod, toUrl(urlString), toHttpContent(contentType, content)));
            }

            public HttpRequestBridge buildDeleteRequest(String urlString) throws IOException {
                return wrap(httpRequestFactory.buildDeleteRequest(toUrl(urlString)));
            }

            public HttpRequestBridge buildGetRequest(String urlString) throws IOException {
                return wrap(httpRequestFactory.buildGetRequest(toUrl(urlString)));
            }

            public HttpRequestBridge buildPostRequest(String urlString, String contentType, String content) throws IOException {
                return wrap(httpRequestFactory.buildPostRequest(toUrl(urlString), toHttpContent(contentType, content)));
            }

            public HttpRequestBridge buildPutRequest(String urlString, String contentType, String content) throws IOException {
                return wrap(httpRequestFactory.buildPutRequest(toUrl(urlString), toHttpContent(contentType, content)));
            }

            public HttpRequestBridge buildPatchRequest(String urlString, String contentType, String content) throws IOException {
                return wrap(httpRequestFactory.buildPatchRequest(toUrl(urlString), toHttpContent(contentType, content)));
            }

            public HttpRequestBridge buildHeadRequest(String urlString) throws IOException {
                return wrap(httpRequestFactory.buildHeadRequest(toUrl(urlString)));
            }

            private HttpRequestBridge wrap(HttpRequest request) {
                return new HttpRequestBridge(request);
            }
        }

        public class HttpRequestBridge {
            private final HttpRequest httpRequest;

            public HttpRequestBridge(HttpRequest httpRequest) {
                this.httpRequest = httpRequest;
            }

            public HttpResponse execute() throws IOException {
                return httpRequest.execute();
            }

            public HttpHeaders getHeaders() {
                return httpRequest.getHeaders();
            }

            public void setHeaders(Map<String, Object> headers) {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(unwrap(headers));

                httpRequest.setHeaders(httpHeaders);
            }

            @SuppressWarnings("unchecked")
            private Map<String, Object> unwrap(Map<String, Object> input) {
                return (Map<String, Object>) MarshalingHelper.unwrap(input);
            }
        }
    }

    @Override
    public String name() {
        return "http";
    }

    @Override
    public HttpModule module(JsContext jsContext) {
        return httpModule;
    }
}
