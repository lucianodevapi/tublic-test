package com.marketinginapp.startup.ms.user.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.io.IOException;

@Slf4j

@Configuration
public class CustomCorsFilter implements Filter {

    private final static String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private final static String ACCESS_CONTROL_ALLOW_METHOD = "Access-Control-Allow-Methods";
    private final static String ACCESS_CONTROL_ALLOW_HEADER = "Access-Control-Allow-Headers";
    private final static String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        log.info("ServletRequest {}", request);
        log.info("ServletResponse {}", request);
        final HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        httpServletResponse.setHeader(ACCESS_CONTROL_ALLOW_METHOD, "*");
        httpServletResponse.setHeader(ACCESS_CONTROL_ALLOW_HEADER, "Authorization, Content-Type");
        httpServletResponse.setHeader(ACCESS_CONTROL_MAX_AGE, "3600");

        if(HttpMethod.OPTIONS.name().equalsIgnoreCase(((HttpServletRequest) request).getMethod())) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        }
        chain.doFilter(request, response);
    }
}