package com.peironcely.springmodifybodyinfilter.filter;

import jakarta.annotation.Nonnull;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@Slf4j
public class CsrfNonceFilter implements Filter {

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
        final FakeResponseWrapper responseWrapper = new FakeResponseWrapper(servletResponse);
        filterChain.doFilter(servletRequest, responseWrapper);
        byte[] body = responseWrapper.getBody();
        if (responseWrapper.getContentType() == null || responseWrapper.getContentType().contains("text/html")) {
            final String nonce = Long.toHexString(SECURE_RANDOM.nextLong());
            responseWrapper.addHeader("Content-Security-Policy", "script-src 'nonce-%s'".formatted(nonce));
            final String encoding = responseWrapper.getCharacterEncoding() == null? StandardCharsets.UTF_8.name() : responseWrapper.getCharacterEncoding();
            body = new String(body, encoding).replace("<script", "<script nonce='%s' ".formatted(nonce)).getBytes(encoding);

        }
        servletResponse.setContentLength(body.length);
        servletResponse.getOutputStream().write(body);
    }
}
