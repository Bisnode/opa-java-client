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
     * @param path  Path to the document (eg. "this/is/path/to/document")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryForDocumentRequest that = (QueryForDocumentRequest) o;
        return Objects.equals(input, that.input) &&
                Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, path);
    }

    @Override
    public String toString() {
        return "QueryForDocumentRequest{" +
                "input=" + input +
                ", path='" + path + '\'' +
                '}';
    }
}
