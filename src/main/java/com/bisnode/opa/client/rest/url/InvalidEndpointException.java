package com.bisnode.opa.client.rest.url;

import com.bisnode.opa.client.OpaClientException;

public class InvalidEndpointException extends OpaClientException {
    public InvalidEndpointException() {
        super();
    }

    public InvalidEndpointException(String message) {
        super(message);
    }

    public InvalidEndpointException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEndpointException(Throwable cause) {
        super(cause);
    }

    protected InvalidEndpointException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
