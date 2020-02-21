package com.bisnode.opa.client.rest;

import com.bisnode.opa.client.OpaClientException;
import com.bisnode.opa.client.OpaConfiguration;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RequiredArgsConstructor
public class OpaRestClient {

    private final OpaConfiguration opaConfiguration;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public HttpRequest.Builder getBasicRequestBuilder(String endpoint) {
        return HttpRequest.newBuilder(URI.create(opaConfiguration.getUrl() + endpoint));
    }

    public HttpRequest.BodyPublisher getJsonBodyPublisher(Object body) {
        return JsonBodyPublisher.of(body, objectMapper);
    }

    public <T> JsonBodyHandler<T> getJsonBodyHandler(JavaType responseType) {
        return new JsonBodyHandler<>(responseType, objectMapper);
    }

    public <T> HttpResponse<T> sendRequest(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) throws IOException, InterruptedException {
        HttpResponse<T> response = httpClient.send(request, bodyHandler);
        if (response.statusCode() >= 300) {
            throw new OpaClientException("Error in communication with OPA server, status code: " + response.statusCode());
        }
        return response;
    }
}
