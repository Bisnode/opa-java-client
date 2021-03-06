package com.bisnode.opa.client.data;

import com.bisnode.opa.client.OpaClientException;
import com.bisnode.opa.client.rest.ContentType;
import com.bisnode.opa.client.rest.OpaRestClient;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @see com.bisnode.opa.client.data.OpaDataApi
 */
public class OpaDataClient implements OpaDataApi {
    private static final String DATA_ENDPOINT = "/v1/data/";

    private final OpaRestClient opaRestClient;

    public OpaDataClient(OpaRestClient opaRestClient) {
        this.opaRestClient = opaRestClient;
    }

    @Override
    public void createOrOverwriteDocument(OpaDocument document) {
        try {
            HttpRequest request = opaRestClient.getBasicRequestBuilder(DATA_ENDPOINT + document.getPath())
                    .PUT(HttpRequest.BodyPublishers.ofString(document.getContent()))
                    .header(ContentType.HEADER_NAME, ContentType.Values.APPLICATION_JSON)
                    .build();

            opaRestClient.sendRequest(request, HttpResponse.BodyHandlers.discarding());
        } catch (OpaClientException exception) {
            throw exception;
        } catch (Exception e) {
            throw new OpaClientException(e);
        }
    }
}
