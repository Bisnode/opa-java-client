package com.bisnode.opa.client.query;


import java.lang.reflect.ParameterizedType;

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
     * @since 0.3.0
     */
    <R> R queryForDocument(QueryForDocumentRequest queryForDocumentRequest, ParameterizedType responseType);

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

    /**
     * <p>Executes simple query for boolean result
     * </p>
     *
     * @param queryForDocumentRequest request containing information needed for querying
     * @return response from OPA mapped to boolean
     * @since 0.4.2
     */
    Boolean queryForBoolean(QueryForDocumentRequest queryForDocumentRequest);
}
