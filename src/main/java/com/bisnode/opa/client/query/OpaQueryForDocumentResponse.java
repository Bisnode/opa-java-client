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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof OpaQueryForDocumentResponse)) return false;
        final OpaQueryForDocumentResponse<?> other = (OpaQueryForDocumentResponse<?>) o;
        final Object this$result = this.getResult();
        final Object other$result = other.getResult();
        return Objects.equals(this$result, other$result);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $result = this.getResult();
        result = result * PRIME + ($result == null ? 43 : $result.hashCode());
        return result;
    }

    public String toString() {
        return "OpaQueryForDocumentResponse(result=" + this.getResult() + ")";
    }
}
