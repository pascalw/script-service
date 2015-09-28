package nl.pwiddershoven.scriptor.service.script.module.http;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.api.client.http.HttpResponse;

public class HttpModuleProviderTest {
    private HttpModuleProvider.HttpModule httpModule = new HttpModuleProvider().module(null);

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(0);

    @Test
    public void get() throws IOException {
        stubFor(WireMock.get(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html")
                        .withBody("<html><p>Hello</p></html>")));

        HttpResponse response = httpModule.get(mockServerAddress() + "/");
        assertEquals("<html><p>Hello</p></html>", response.parseAsString());
    }

    @Test
    public void post() throws IOException {
        stubFor(WireMock.post(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withStatus(200)));

        HttpResponse response = httpModule.post(mockServerAddress() + "/", "application/json", "{\"message\": \"hello, world!\"}");
        assertEquals(200, response.getStatusCode());

        verify(postRequestedFor(urlEqualTo("/"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(equalTo("{\"message\": \"hello, world!\"}")));
    }

    @Test
    public void put() throws IOException {
        stubFor(WireMock.put(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withStatus(200)));

        HttpResponse response = httpModule.put(mockServerAddress() + "/", "application/json", "{\"message\": \"hello, world!\"}");
        assertEquals(200, response.getStatusCode());

        verify(putRequestedFor(urlEqualTo("/"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(equalTo("{\"message\": \"hello, world!\"}")));
    }

    @Test
    public void patch() throws IOException {
        stubFor(WireMock.patch(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withStatus(200)));

        HttpResponse response = httpModule.patch(mockServerAddress() + "/", "application/json", "{\"message\": \"hello, world!\"}");
        assertEquals(200, response.getStatusCode());

        verify(patchRequestedFor(urlEqualTo("/"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(equalTo("{\"message\": \"hello, world!\"}")));
    }

    @Test
    public void delete() throws IOException {
        stubFor(WireMock.delete(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withStatus(200)));

        HttpResponse response = httpModule.delete(mockServerAddress() + "/");
        assertEquals(200, response.getStatusCode());

        verify(deleteRequestedFor(urlEqualTo("/")));
    }

    @Test
    public void requestFactory() throws IOException {
        stubFor(WireMock.get(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html")
                        .withBody("<html><p>Hello</p></html>")));

        HttpModuleProvider.HttpModule.HttpRequestFactoryBridge factory = httpModule.requestFactory();
        HttpModuleProvider.HttpModule.HttpRequestBridge request = factory.buildGetRequest(mockServerAddress() + "/");

        HttpResponse response = request.execute();
        assertEquals("<html><p>Hello</p></html>", response.parseAsString());
    }

    private String mockServerAddress() {
        return "http://localhost:" + wireMockRule.port();
    }

}