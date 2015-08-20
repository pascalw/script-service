package nl.pwiddershoven.script.service.script.module.request;

import static org.mockito.Mockito.*;

import javax.ws.rs.container.ContainerRequestContext;

import nl.pwiddershoven.script.service.script.JsContext;

import org.junit.Test;

public class RequestModuleProviderTest {
    private RequestModuleProvider requestModuleProvider = new RequestModuleProvider();
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
}