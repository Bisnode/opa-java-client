package com.bisnode.opa.client.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.net.http.HttpRequest;
import java.nio.ByteBuffer;
import java.util.concurrent.Flow;

class JsonBodyPublisher implements HttpRequest.BodyPublisher {

    private final HttpRequest.BodyPublisher stringBodyPublisher;

    public static HttpRequest.BodyPublisher of(Object body, ObjectMapper objectMapper) {
        return new JsonBodyPublisher(body, objectMapper);
    }

    @SneakyThrows
    public JsonBodyPublisher(Object body, ObjectMapper objectMapper) {
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
