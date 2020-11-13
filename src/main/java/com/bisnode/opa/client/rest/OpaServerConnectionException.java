package com.bisnode.opa.client.rest;

import com.bisnode.opa.client.OpaClient;
import com.bisnode.opa.client.OpaClientException;

/**
 * Exception returned by {@link OpaClient}
 * Exception which is thrown when {@link OpaClient} cannot reach Open Policy Agent server.
 */
public class OpaServerConnectionException extends OpaClientException {
    public OpaServerConnectionException() {
    }

    public OpaServerConnectionException(String message) {
        super(message);
    }

    public OpaServerConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpaServerConnectionException(Throwable cause) {
        super(cause);
    }

    public OpaServerConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
