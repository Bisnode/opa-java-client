package com.bisnode.opa.client.rest;

import com.bisnode.opa.client.OpaClientException;
import com.bisnode.opa.client.OpaConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

/**
 * Class wrapping Java's HttpClient in OPA and JSON requests
 */
public class OpaRestClient {

    private final OpaConfiguration opaConfiguration;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OpaRestClient(OpaConfiguration opaConfiguration, HttpClient httpClient, ObjectMapper objectMapper) {
        this.opaConfiguration = opaConfiguration;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Create {@link java.net.http.HttpRequest.Builder} with configured url using provided endpoint
     *
     * @param endpoint desired opa endpoint
     * @throws OpaClientException if URL or endpoint is invalid
     */
    public HttpRequest.Builder getBasicRequestBuilder(String endpoint) {
        String url = opaConfiguration.getUrl() + "/" + Optional.ofNullable(endpoint).orElseThrow(() -> new OpaClientException("Invalid endpoint: " + endpoint));
        String normalizedUrl = UrlNormalizer.normalize(url).orElseThrow(() -> new OpaClientException("Invalid url: " + url));
        return HttpRequest.newBuilder(URI.create(normalizedUrl));
    }

    /**
     * Gets {@link java.net.http.HttpRequest.BodyPublisher} that is capable of serializing to JSON
     *
     * @param body object to be serialized
     */
    public HttpRequest.BodyPublisher getJsonBodyPublisher(Object body) throws JsonProcessingException {
        return JsonBodyPublisher.of(body, objectMapper);
    }

    /**
     * Gets {@link JsonBodyHandler} that will deserialize JSON to desired class type
     *
     * @param responseType desired  response type
     * @param <T>          desired response type
     */
    public <T> JsonBodyHandler<T> getJsonBodyHandler(JavaType responseType) {
        return new JsonBodyHandler<>(responseType, objectMapper);
    }

    /**
     * Sends provided request and returns response mapped using {@link java.net.http.HttpResponse.BodyHandler}
     *
     * @param request     request to be sent
     * @param bodyHandler handler that indicates how to transform incoming body
     * @param <T>         Type of returned body
     * @return response from HttpRequest
     * @throws IOException          is propagated from {@link HttpClient}
     * @throws InterruptedException is propagated from {@link HttpClient}
     */
    public <T> HttpResponse<T> sendRequest(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) throws IOException, InterruptedException {
        HttpResponse<T> response = httpClient.send(request, bodyHandler);
        if (response.statusCode() >= 300) {
            throw new OpaClientException("Error in communication with OPA server, status code: " + response.statusCode());
        }
        return response;
    }
}
