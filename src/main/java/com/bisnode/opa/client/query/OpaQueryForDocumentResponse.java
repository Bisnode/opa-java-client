package com.bisnode.opa.client.query;

import java.beans.ConstructorProperties;
import java.util.Objects;

final class OpaQueryForDocumentResponse<T> {
    private final T result;

    @ConstructorProperties({"result"})
    public OpaQueryForDocumentResponse(T result) {
        this.result = result;
    }

    public T getResult() {
        return this.result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpaQueryForDocumentResponse<?> that = (OpaQueryForDocumentResponse<?>) o;
        return Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result);
    }

    @Override
    public String toString() {
        return "OpaQueryForDocumentResponse{" +
                "result=" + result +
                '}';
    }
}
