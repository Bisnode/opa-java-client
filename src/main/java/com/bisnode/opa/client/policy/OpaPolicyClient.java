package com.bisnode.opa.client.policy;

import com.bisnode.opa.client.OpaClientException;
import com.bisnode.opa.client.rest.ContentType;
import com.bisnode.opa.client.rest.OpaRestClient;
import lombok.RequiredArgsConstructor;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @see com.bisnode.opa.client.policy.OpaPolicyApi
 */
@RequiredArgsConstructor
public class OpaPolicyClient implements OpaPolicyApi {
    public static final String POLICY_ENDPOINT = "/v1/policies/";

    private final OpaRestClient opaRestClient;

    @Override
    public void createOrUpdatePolicy(OpaPolicy policy) {
        try {
            HttpRequest request = opaRestClient.getBasicRequestBuilder(POLICY_ENDPOINT + policy.getId())
                    .header(ContentType.HEADER_NAME, ContentType.Values.TEXT_PLAIN)
                    .PUT(HttpRequest.BodyPublishers.ofString(policy.getContent()))
                    .build();

            opaRestClient.sendRequest(request, HttpResponse.BodyHandlers.discarding());

        } catch (Exception e) {
            throw new OpaClientException(e);
        }
    }
}
