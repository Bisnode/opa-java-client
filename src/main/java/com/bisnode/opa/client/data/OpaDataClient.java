package com.bisnode.opa.client.data;

import com.bisnode.opa.client.OpaClientException;
import com.bisnode.opa.client.rest.ContentType;
import com.bisnode.opa.client.rest.OpaRestClient;
import lombok.RequiredArgsConstructor;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RequiredArgsConstructor
public class OpaDataClient implements OpaDataApi {
    private static final String DATA_ENDPOINT = "/v1/data/";

    private final OpaRestClient opaRestClient;

    @Override
    public void createOrOverwriteDocument(OpaDocument document) {
        try {
            HttpRequest request = opaRestClient.getBasicRequestBuilder(DATA_ENDPOINT + document.getPath())
                    .PUT(HttpRequest.BodyPublishers.ofString(document.getContent()))
                    .header(ContentType.HEADER_NAME, ContentType.Values.APPLICATION_JSON)
                    .build();

            opaRestClient.sendRequest(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            throw new OpaClientException(e);
        }
    }
}
