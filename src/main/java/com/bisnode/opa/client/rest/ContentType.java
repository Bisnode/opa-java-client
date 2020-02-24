package com.bisnode.opa.client.rest;

/**
 * Content-Type header related information
 */
public interface ContentType {
    interface Values {
        String APPLICATION_JSON = "application/json";
        String TEXT_PLAIN = "text/plain";
    }

    String HEADER_NAME = "Content-Type";
}
