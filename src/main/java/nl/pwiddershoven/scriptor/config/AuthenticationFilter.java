package nl.pwiddershoven.scriptor.config;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

public class AuthenticationFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(AuthenticationFilter.class);
    private static final String VALID_ACCESS_TOKEN = getConfiguredToken();
    private static final Response DENIED_RESPONSE = Response.status(401).build();

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
        String configuredToken = System.getenv("AUTHENTICATION_TOKEN");

        if (configuredToken == null) {
            String randomToken = new BigInteger(80, new SecureRandom()).toString(32);
            LOGGER.warn(String.format("No AUTHENTICATION_TOKEN supplied, using generated token '%s'", randomToken));
            return randomToken;
        }

        return configuredToken;
    }
}
