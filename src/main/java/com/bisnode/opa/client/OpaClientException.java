package com.bisnode.opa.client;

public class OpaClientException extends RuntimeException {
    public OpaClientException() {
        super();
    }

    public OpaClientException(String message) {
        super(message);
    }

    public OpaClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpaClientException(Throwable cause) {
        super(cause);
    }

    protected OpaClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
