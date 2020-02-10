package com.bisnode.opa.client.query;

import lombok.Value;

@Value
class OpaQueryForDocumentResponse<T> {
    T result;
}
