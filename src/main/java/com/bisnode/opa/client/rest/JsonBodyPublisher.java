package com.bisnode.opa.client.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpRequest;
import java.nio.ByteBuffer;
import java.util.concurrent.Flow;

class JsonBodyPublisher implements HttpRequest.BodyPublisher {

    private final HttpRequest.BodyPublisher stringBodyPublisher;

    public static HttpRequest.BodyPublisher of(Object body, ObjectMapper objectMapper) throws JsonProcessingException {
        return new JsonBodyPublisher(body, objectMapper);
    }

    public JsonBodyPublisher(Object body, ObjectMapper objectMapper) throws JsonProcessingException {
        this.stringBodyPublisher = HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body));
    }

    @Override
    public long contentLength() {
        return stringBodyPublisher.contentLength();
    }

    @Override
    public void subscribe(Flow.Subscriber<? super ByteBuffer> subscriber) {
        stringBodyPublisher.subscribe(subscriber);
    }
}
