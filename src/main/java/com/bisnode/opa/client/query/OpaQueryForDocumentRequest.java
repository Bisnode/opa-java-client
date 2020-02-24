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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof OpaQueryForDocumentRequest)) return false;
        final OpaQueryForDocumentRequest other = (OpaQueryForDocumentRequest) o;
        final Object this$input = this.getInput();
        final Object other$input = other.getInput();
        return Objects.equals(this$input, other$input);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $input = this.getInput();
        result = result * PRIME + ($input == null ? 43 : $input.hashCode());
        return result;
    }

    public String toString() {
        return "OpaQueryForDocumentRequest(input=" + this.getInput() + ")";
    }
}
