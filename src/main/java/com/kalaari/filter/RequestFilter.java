package com.kalaari.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RequestFilter implements Filter {

    private Boolean checkExternalRequestAuthentication(HttpServletRequest httpServletRequest, ServletResponse response)
            throws IOException {
        String forwardedHost = httpServletRequest.getHeader("Host");
        log.info("Forwarded Host:" + forwardedHost);
        return true;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            MDC.put("requestId", UUID.randomUUID().toString());
            chain.doFilter(request, response);
        } finally {
            // Cleans up the ThreadLocal data again
            MDC.clear();
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }
}