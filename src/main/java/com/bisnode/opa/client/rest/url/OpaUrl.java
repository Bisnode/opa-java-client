package com.bisnode.opa.client.rest.url;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;

/**
 * Contains request URL which is then used to call OPA server
 * Provides methods useful in URL validation/manipulation
 */
public class OpaUrl {
    private static final Logger log = LoggerFactory.getLogger(OpaUrl.class);
    private final String value;

    /**
     * Creates OpaUrl
     *
     * @param serverUrl URL of OPA Server
     * @param endpoint  endpoint path
     * @return created OpaUrl
     */
    public static OpaUrl of(String serverUrl, String endpoint) {
        return new OpaUrl(urlOf(serverUrl, endpoint));
    }

    private static String urlOf(String url, String endpoint) {
        return url + "/" + Optional.ofNullable(endpoint).orElseThrow(() -> new InvalidEndpointException("Invalid endpoint: " + endpoint));
    }

    /**
     * @return String value of OPA URL
     */
    public String getValue() {
        return value;
    }

    /**
     * @return Normalized version of URL, removing multiple and trailing slashes
     */
    public OpaUrl normalized() {
        String normalizedValue = normalize(value);
        return new OpaUrl(normalizedValue);
    }

    /**
     * @return OpaUrl transformed to URI
     */
    public URI toUri() {
        return URI.create(value);
    }

    private String normalize(String inputUrl) {
        String normalized = removeExtraSlashes()
                .andThen(removeTrailingSlash())
                .apply(inputUrl.trim());
        if (!inputUrl.equals(normalized)) {
            log.warn("Supplied URL [{}] is malformed, has to be normalized", inputUrl);
        }
        return normalized;
    }

    private Function<String, String> removeTrailingSlash() {
        return (input) -> input.endsWith("/") ? input.substring(0, input.length() - 1) : input;
    }

    private Function<String, String> removeExtraSlashes() {
        return (input) -> input.replaceAll("([^:])//+", "$1/");
    }

    private OpaUrl(String value) {
        this.value = value;
    }

}
