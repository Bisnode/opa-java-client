package com.bisnode.opa.client.rest;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;
import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor
class JsonBodyHandler<T> implements HttpResponse.BodyHandler<Supplier<T>> {

    private final JavaType responseType;
    private final ObjectMapper objectMapper;

    public static <W> HttpResponse.BodySubscriber<Supplier<W>> asJSON(JavaType responseType, ObjectMapper objectMapper) {
        HttpResponse.BodySubscriber<InputStream> upstream = HttpResponse.BodySubscribers.ofInputStream();

        return HttpResponse.BodySubscribers.mapping(upstream, createMapper(responseType, objectMapper));
    }

    private static <W> Function<InputStream, Supplier<W>> createMapper(JavaType responseType, ObjectMapper objectMapper) {
        return is -> () -> mapToJson(responseType, is, objectMapper);
    }

    private static <W> W mapToJson(JavaType responseType, InputStream is, ObjectMapper objectMapper) {
        try (InputStream inputStream = is) {
            return objectMapper.readValue(inputStream, responseType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public HttpResponse.BodySubscriber<Supplier<T>> apply(HttpResponse.ResponseInfo responseInfo) {
        return asJSON(responseType, objectMapper);
    }
}
