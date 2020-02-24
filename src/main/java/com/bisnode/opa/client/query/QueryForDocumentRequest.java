package com.bisnode.opa.client.query;

import java.beans.ConstructorProperties;
import java.util.Objects;

/**
 * Class wrapping OPA query response
 */
public final class QueryForDocumentRequest {
    private final Object input;
    private final String path;

    /**
     * @param input Query input
     * @param path Path to the document (eg. "this/is/path/to/document")
     */
    @ConstructorProperties({"input", "path"})
    public QueryForDocumentRequest(Object input, String path) {
        this.input = input;
        this.path = path;
    }

    public Object getInput() {
        return this.input;
    }

    public String getPath() {
        return this.path;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof QueryForDocumentRequest)) return false;
        final QueryForDocumentRequest other = (QueryForDocumentRequest) o;
        final Object this$input = this.getInput();
        final Object other$input = other.getInput();
        if (!Objects.equals(this$input, other$input)) return false;
        final Object this$path = this.getPath();
        final Object other$path = other.getPath();
        return Objects.equals(this$path, other$path);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $input = this.getInput();
        result = result * PRIME + ($input == null ? 43 : $input.hashCode());
        final Object $path = this.getPath();
        result = result * PRIME + ($path == null ? 43 : $path.hashCode());
        return result;
    }

    public String toString() {
        return "QueryForDocumentRequest(input=" + this.getInput() + ", path=" + this.getPath() + ")";
    }
}
