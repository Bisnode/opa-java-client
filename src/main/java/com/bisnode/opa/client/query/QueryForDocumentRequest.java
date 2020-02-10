package com.bisnode.opa.client.query;

import lombok.Value;

@Value
public class QueryForDocumentRequest {
    Object input;
    String path;
}
