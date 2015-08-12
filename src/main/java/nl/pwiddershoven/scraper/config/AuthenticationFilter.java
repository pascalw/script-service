package nl.pwiddershoven.scraper.config;

import java.io.IOException;

import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String VALID_ACCESS_TOKEN = getConfiguredToken();
    private static final Response DENIED_RESPONSE = Response.status(401).build();
    private static final String DEFAULT_TOKEN = "A:wS'7U-x3Gt";

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        if (isAuthenticationRequired()) {
            String requestToken = extractToken(containerRequestContext);

            if (!VALID_ACCESS_TOKEN.equals(requestToken))
                containerRequestContext.abortWith(DENIED_RESPONSE);
        }
    }

    private String extractToken(ContainerRequestContext requestContext) {
        String authorizationHeaderValue = requestContext.getHeaderString("Authorization");

        if (authorizationHeaderValue == null)
            return null;

        String[] parts = authorizationHeaderValue.split("token ");
        if (parts.length != 2)
            return null;

        return parts[1];
    }

    private boolean isAuthenticationRequired() {
        return resourceInfo.getResourceMethod().getAnnotation(AuthenticationNotRequired.class) == null;
    }

    private static String getConfiguredToken() {
        String configuredToken = System.getenv("ACCESS_TOKEN");

        if (configuredToken == null)
            return DEFAULT_TOKEN;

        return configuredToken;
    }
}
