package com.bisnode.opa.client.query;

public interface OpaQueryApi {
    <R> R queryForDocument(QueryForDocumentRequest queryForDocumentRequest, Class<R> responseType);
}
