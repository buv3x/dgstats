package com.datascience.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class LoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoggingClientHttpRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("HTTP {} {}", request.getMethod(), request.getURI());
            if (body.length > 0) {
                log.debug("Request body size: {} bytes", body.length);
            }
        }
        ClientHttpResponse response = execution.execute(request, body);
        if (log.isDebugEnabled()) {
            log.debug("HTTP {} {} -> {}", request.getMethod(), request.getURI(), response.getStatusCode());
        }
        return response;
    }
}
