package com.bisnode.opa.client.data;

import java.beans.ConstructorProperties;
import java.util.Objects;

/**
 * Class wrapping OPA document content and path to it
 */
public final class OpaDocument {
    private final String path;
    private final String content;

    /**
     * @param path    Path to the document (eg. "this/is/path/to/document")
     * @param content Content of the document (JSON format)
     */
    @ConstructorProperties({"path", "content"})
    public OpaDocument(String path, String content) {
        this.path = path;
        this.content = content;
    }

    /**
     * Path to the document (eg. "this/is/path/to/document")
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Content of the document (JSON format)
     */
    public String getContent() {
        return this.content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpaDocument that = (OpaDocument) o;
        return Objects.equals(path, that.path) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, content);
    }

    @Override
    public String toString() {
        return "OpaDocument{" +
                "path='" + path + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
