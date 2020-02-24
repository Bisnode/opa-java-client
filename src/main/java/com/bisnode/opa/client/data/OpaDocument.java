package com.bisnode.opa.client.data;

import java.beans.ConstructorProperties;
import java.util.Objects;

/**
 * Class wrapping OPA document content and path to it
 */

public final class OpaDocument {
    /**
     * Path to the document (eg. "this/is/path/to/document")
     */
    private final String path;

    /**
     * Content of the document (JSON format)
     */
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

    public String getPath() {
        return this.path;
    }

    public String getContent() {
        return this.content;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof OpaDocument)) return false;
        final OpaDocument other = (OpaDocument) o;
        final Object this$path = this.getPath();
        final Object other$path = other.getPath();
        if (!Objects.equals(this$path, other$path)) return false;
        final Object this$content = this.getContent();
        final Object other$content = other.getContent();
        return Objects.equals(this$content, other$content);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $path = this.getPath();
        result = result * PRIME + ($path == null ? 43 : $path.hashCode());
        final Object $content = this.getContent();
        result = result * PRIME + ($content == null ? 43 : $content.hashCode());
        return result;
    }

    public String toString() {
        return "OpaDocument(path=" + this.getPath() + ", content=" + this.getContent() + ")";
    }
}
