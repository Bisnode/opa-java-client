package com.bisnode.opa.client;

import java.beans.ConstructorProperties;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Contains all configuration needed to set up {@link OpaClient}
 */
public final class OpaConfiguration {
    private final String url;
    private final HttpClient.Version httpVersion;
    private final Map<String, String> headersMap;

    /**
     * @param url base URL to OPA server, containing protocol, and port (eg.
     *            http://localhost:8181)
     */
    @ConstructorProperties({ "url" })
    public OpaConfiguration(String url) {
        this.url = url;
        this.httpVersion = "https".equals(URI.create(url).getScheme()) ? HttpClient.Version.HTTP_2
                : HttpClient.Version.HTTP_1_1;
        this.headersMap = Collections.emptyMap();
    }

    /**
     * @param url         base URL to OPA server, containing protocol, and port (eg.
     *                    http://localhost:8181)
     * @param httpVersion preferred HTTP version to use for the client
     */
    @ConstructorProperties({ "url", "httpVersion" })
    public OpaConfiguration(String url, HttpClient.Version httpVersion) {
        this.url = url;
        this.httpVersion = httpVersion;
        this.headersMap = Collections.emptyMap();
    }

    /**
     * @param url         base URL to OPA server, containing protocol, and port
     *                    (eg.
     *                    http://localhost:8181)
     * @param httpVersion preferred HTTP version to use for the client
     * @param headers     headers to be added to all requests
     */
    @ConstructorProperties({ "url", "httpVersion", "headers" })
    public OpaConfiguration(String url, HttpClient.Version httpVersion, Map<String, String> headers) {
        this.url = url;
        this.httpVersion = httpVersion;
        this.headersMap = headers != null ? Collections.unmodifiableMap(new HashMap<>(headers))
                : Collections.emptyMap();
    }

    /**
     * @return url base URL to OPA server, containing protocol, and port
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Get HTTP version configured for the client. If not configured will use HTTP2
     * for "https" scheme
     * and HTTP1.1 for "http" scheme.
     *
     * @return httpVersion configured for use by the client
     */
    public HttpClient.Version getHttpVersion() {
        return this.httpVersion;
    }

    /**
     * Get headers that will be added to all requests
     *
     * @return unmodifiable map of headers
     */
    public Map<String, String> getHeaders() {
        return this.headersMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OpaConfiguration that = (OpaConfiguration) o;
        return Objects.equals(url, that.url) &&
                Objects.equals(httpVersion, that.httpVersion) &&
                Objects.equals(headersMap, that.headersMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, httpVersion, headersMap);
    }

    @Override
    public String toString() {
        return "OpaConfiguration{" +
                "url='" + url + '\'' +
                ", httpVersion=" + httpVersion +
                ", headers=" + headersMap +
                '}';
    }
}
