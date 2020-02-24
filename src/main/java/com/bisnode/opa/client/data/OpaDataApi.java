package com.bisnode.opa.client.data;

/**
 * This is the interface responsible for OPA Data Api, please see <a href=https://www.openpolicyagent.org/docs/latest/rest-api/#data-api>OPA Data API docs</a>
 */
public interface OpaDataApi {
    /**
     * <p>Updates or creates new OPA document
     * </p>
     *
     * @param document document to be created/updated
     * @since 0.0.1
     */
    void createOrOverwriteDocument(OpaDocument document);
}
