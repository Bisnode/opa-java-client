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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof OpaConfiguration)) return false;
        final OpaConfiguration other = (OpaConfiguration) o;
        final Object this$url = this.getUrl();
        final Object other$url = other.getUrl();
        return Objects.equals(this$url, other$url);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $url = this.getUrl();
        result = result * PRIME + ($url == null ? 43 : $url.hashCode());
        return result;
    }

    public String toString() {
        return "OpaConfiguration(url=" + this.getUrl() + ")";
    }
}
