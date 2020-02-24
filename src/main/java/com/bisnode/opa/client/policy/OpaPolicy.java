package com.bisnode.opa.client.policy;

import java.beans.ConstructorProperties;
import java.util.Objects;

/**
 * Class wrapping OPA document content and path to it
 */
public final class OpaPolicy {
    private final String id;
    private final String content;

    /**
     * @param id      Id of the policy to update, or id of newly created policy
     * @param content Content of the policy (written in Rego)
     */
    @ConstructorProperties({"id", "content"})
    public OpaPolicy(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof OpaPolicy)) return false;
        final OpaPolicy other = (OpaPolicy) o;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (!Objects.equals(this$id, other$id)) return false;
        final Object this$content = this.getContent();
        final Object other$content = other.getContent();
        return Objects.equals(this$content, other$content);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $content = this.getContent();
        result = result * PRIME + ($content == null ? 43 : $content.hashCode());
        return result;
    }

    public String toString() {
        return "OpaPolicy(id=" + this.getId() + ", content=" + this.getContent() + ")";
    }
}
