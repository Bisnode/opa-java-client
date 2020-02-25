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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpaPolicy opaPolicy = (OpaPolicy) o;
        return Objects.equals(id, opaPolicy.id) &&
                Objects.equals(content, opaPolicy.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content);
    }

    @Override
    public String toString() {
        return "OpaPolicy{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
