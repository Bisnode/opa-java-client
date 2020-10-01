package com.bisnode.opa.client;

import java.beans.ConstructorProperties;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.Objects;

/**
 * Contains all configuration needed to set up {@link OpaClient}
 */
public final class OpaConfiguration {
    private final String url;
    private final HttpClient.Version httpVersion;

    /**
     * @param url base URL to OPA server, containing protocol, and port (eg. http://localhost:8181)
     */
    @ConstructorProperties({"url"})
    public OpaConfiguration(String url) {
        this.url = url;
        this.httpVersion = "https".equals(URI.create(url).getScheme()) ?
                HttpClient.Version.HTTP_2 :
                HttpClient.Version.HTTP_1_1;
    }

    /**
     * @param url base URL to OPA server, containing protocol, and port (eg. http://localhost:8181)
     * @param httpVersion preferred HTTP version to use for the client
     */
    @ConstructorProperties({"url", "httpVersion"})
    public OpaConfiguration(String url, HttpClient.Version httpVersion) {
        this.url = url;
        this.httpVersion = httpVersion;
    }

    /**
     * @return url base URL to OPA server, containing protocol, and port
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Get HTTP version configured for the client. If not configured will use HTTP2 for "https" scheme
     * and HTTP1.1 for "http" scheme.
     *
     * @return httpVersion configured for use by the client
     */
    public HttpClient.Version getHttpVersion() {
        return this.httpVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpaConfiguration that = (OpaConfiguration) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public String toString() {
        return "OpaConfiguration{" +
                "url='" + url + '\'' +
                '}';
    }
}
