package nl.pwiddershoven.scriptor.service.script.module.request;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.container.ContainerRequestContext;

import nl.pwiddershoven.scriptor.service.script.JsContext;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

public class RequestModuleProviderTest {
    private RequestModuleProvider requestModuleProvider = new RequestModuleProvider(new ObjectMapper());
    private JsContext jsContext = mock(JsContext.class);

    @Test
    public void delegates_to_request_context() {
        ContainerRequestContext mockContainerRequest = mock(ContainerRequestContext.class);
        when(jsContext.getAttribute("request", ContainerRequestContext.class)).thenReturn(mockContainerRequest);

        RequestModuleProvider.RequestModule module = requestModuleProvider.module(jsContext);

        module.getUriInfo();

        verify(mockContainerRequest).getUriInfo();
        verifyNoMoreInteractions(mockContainerRequest);
    }

    @Test
    public void provides_json_parsed_request_body() throws IOException {
        ContainerRequestContext mockContainerRequest = mock(ContainerRequestContext.class);
        when(mockContainerRequest.getEntityStream()).thenReturn(new ByteArrayInputStream("{\"hello\": \"world!\"}".getBytes(StandardCharsets.UTF_8)));

        when(jsContext.getAttribute("request", ContainerRequestContext.class)).thenReturn(mockContainerRequest);

        RequestModuleProvider.RequestModule module = requestModuleProvider.module(jsContext);
        assertEquals(ImmutableMap.of("hello", "world!"), module.getEntityJSON());
    }
}
