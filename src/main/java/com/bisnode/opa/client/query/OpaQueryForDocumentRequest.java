package com.bisnode.opa.client.query;

import java.beans.ConstructorProperties;
import java.util.Objects;

final class OpaQueryForDocumentRequest {
    private final Object input;

    @ConstructorProperties({"input"})
    public OpaQueryForDocumentRequest(Object input) {
        this.input = input;
    }

    public Object getInput() {
        return this.input;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpaQueryForDocumentRequest that = (OpaQueryForDocumentRequest) o;
        return Objects.equals(input, that.input);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input);
    }

    @Override
    public String toString() {
        return "OpaQueryForDocumentRequest{" +
                "input=" + input +
                '}';
    }
}
