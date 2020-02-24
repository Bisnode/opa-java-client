package com.bisnode.opa.client.query;

/**
 * This is the interface responsible for OPA Query API @see <a href=https://www.openpolicyagent.org/docs/latest/rest-api/#query-api>OPA Query API docs</a>
 */
public interface OpaQueryApi {
    /**
     * <p>Executes simple query for document
     * </p>
     *
     * @param queryForDocumentRequest request containing information needed for querying
     * @param responseType            class of response to be returned
     * @return response from OPA mapped to specified class
     * @since 0.0.1
     */
    <R> R queryForDocument(QueryForDocumentRequest queryForDocumentRequest, Class<R> responseType);
}
