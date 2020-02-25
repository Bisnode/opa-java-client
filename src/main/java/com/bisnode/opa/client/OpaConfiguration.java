package com.bisnode.opa.client;

import java.beans.ConstructorProperties;
import java.util.Objects;

/**
 * Contains all configuration needed to set up {@link OpaClient}
 */
public final class OpaConfiguration {
    private final String url;

    /**
     * @param url base URL to OPA server, containing protocol, and port (eg. http://localhost:8181)
     */
    @ConstructorProperties({"url"})
    public OpaConfiguration(String url) {
        this.url = url;
    }

    /**
     * @return url base URL to OPA server, containing protocol, and port
     */
    public String getUrl() {
        return this.url;
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
