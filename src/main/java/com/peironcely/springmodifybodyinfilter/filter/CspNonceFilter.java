package com.peironcely.springmodifybodyinfilter.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@Slf4j
public class CspNonceFilter implements Filter {

    private final static SecureRandom SECURE_RANDOM = new SecureRandom();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if ((servletRequest instanceof HttpServletRequest) && (servletResponse instanceof HttpServletResponse)) {
            doFilterInternal((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private static void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("CsrfNonceFilter doFilterInternal for {}", servletRequest.getRequestURI());
        final String nonce = Long.toHexString(SECURE_RANDOM.nextLong());
        final CspResponseWrapper responseWrapper = new CspResponseWrapper(servletResponse, nonce);
        filterChain.doFilter(servletRequest, responseWrapper);
        servletResponse.addHeader("Content-Security-Policy", "script-src 'nonce-%s'".formatted(nonce));
        byte[] body = responseWrapper.getBody();
        servletResponse.setContentLength(body.length);
        servletResponse.getOutputStream().write(body);
    }
}
