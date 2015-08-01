package nl.pwiddershoven.scraper.config;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.net.HttpHeaders;

@Component
public class CorsFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (originIsWhiteListed(request)) {
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HttpHeaders.ORIGIN));

            if (isCORSPreflightRequest(request)) {
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, Joiner.on(",").join(getAllowedMethods()));
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, Joiner.on(",").join(getAllowedHeaders()));
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
        return true;
    }

    private List<String> getAllowedMethods() {
        return ImmutableList.of("GET", "POST", "PUT", "DELETE");
    }

    private List<String> getAllowedHeaders() {
        return ImmutableList.of("Authorization", "Content-Type");
    }
}
