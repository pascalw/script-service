package nl.pwiddershoven.script.config;

import java.io.IOException;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.google.common.net.HttpHeaders;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter extends OncePerRequestFilter {
    private final Set<String> allowedOrigins = parseList(readFromEnv("CORS_ALLOWED_ORIGINS", "*"));
    private final Set<String> allowedMethods = parseList(readFromEnv("CORS_ALLOWED_METHODS", "GET, POST, PUT, DELETE"));
    private final Set<String> allowedHeaders = parseList(readFromEnv("CORS_ALLOWED_HEADERS", "Authorization, Content-Type"));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (originIsWhiteListed(request)) {
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HttpHeaders.ORIGIN));

            if (isCORSPreflightRequest(request)) {
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, Joiner.on(",").join(allowedMethods));
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, Joiner.on(",").join(allowedHeaders));
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isCORSPreflightRequest(HttpServletRequest request) {
        return request.getMethod().equals(RequestMethod.OPTIONS.toString())
               && request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD) != null
               && request.getHeader(HttpHeaders.ORIGIN) != null;
    }

    private boolean originIsWhiteListed(HttpServletRequest request) {
        return allowsAnyOrigin() || allowedOrigins.contains(request.getHeader(HttpHeaders.ORIGIN));
    }

    private boolean allowsAnyOrigin() {
        return allowedOrigins.contains("*");
    }

    private String readFromEnv(String key, String defaultValue) {
        String envValue = System.getenv(key);

        if (envValue == null)
            envValue = defaultValue;

        return envValue;
    }

    private Set<String> parseList(String commaSeparatedString) {
        return Sets.newHashSet(Splitter.on(",").split(commaSeparatedString));
    }
}
