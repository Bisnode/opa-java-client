package com.bisnode.opa.client;


import com.bisnode.opa.client.data.OpaDataApi;
import com.bisnode.opa.client.data.OpaDataClient;
import com.bisnode.opa.client.data.OpaDocument;
import com.bisnode.opa.client.policy.OpaPolicy;
import com.bisnode.opa.client.policy.OpaPolicyApi;
import com.bisnode.opa.client.policy.OpaPolicyClient;
import com.bisnode.opa.client.query.OpaQueryApi;
import com.bisnode.opa.client.query.OpaQueryClient;
import com.bisnode.opa.client.query.QueryForDocumentRequest;
import com.bisnode.opa.client.rest.ObjectMapperFactory;
import com.bisnode.opa.client.rest.OpaRestClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.Objects;

/**
 * Opa client featuring {@link OpaDataApi}, {@link OpaQueryApi} and {@link OpaPolicyApi}
 */
public class OpaClient implements OpaQueryApi, OpaDataApi, OpaPolicyApi {

    private final OpaQueryApi opaQueryApi;
    private final OpaDataApi opaDataApi;
    private final OpaPolicyApi opaPolicyApi;

    private OpaClient(OpaQueryApi opaQueryApi, OpaDataApi opaDataApi, OpaPolicyApi opaPolicyApi) {
        this.opaQueryApi = opaQueryApi;
        this.opaDataApi = opaDataApi;
        this.opaPolicyApi = opaPolicyApi;
    }

    /**
     * @return builder for {@link OpaClient}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * @see com.bisnode.opa.client.query.OpaQueryApi
     */
    public <R> R queryForDocument(QueryForDocumentRequest queryForDocumentRequest, Class<R> responseType) {
        return this.opaQueryApi.queryForDocument(queryForDocumentRequest, responseType);
    }

    /**
     * @see com.bisnode.opa.client.data.OpaDataApi
     */
    public void createOrOverwriteDocument(OpaDocument document) {
        this.opaDataApi.createOrOverwriteDocument(document);
    }

    /**
     * @see com.bisnode.opa.client.policy.OpaPolicyApi
     */
    public void createOrUpdatePolicy(OpaPolicy policy) {
        this.opaPolicyApi.createOrUpdatePolicy(policy);
    }

    /**
     * Builder for {@link OpaClient}
     */
    public static class Builder {
        private OpaConfiguration opaConfiguration;

        /**
         * @param url URL including protocol and port
         */
        public Builder opaConfiguration(String url) {
            this.opaConfiguration = new OpaConfiguration(url);
            return this;
        }

        public OpaClient build() {
            Objects.requireNonNull(opaConfiguration, "build() called without opaConfiguration provided");
            HttpClient httpClient = HttpClient.newBuilder()
                    .version("https".equals(URI.create(opaConfiguration.getUrl()).getScheme()) ?
                            HttpClient.Version.HTTP_2 :
                            HttpClient.Version.HTTP_1_1)
                    .build();
            OpaRestClient opaRestClient = new OpaRestClient(opaConfiguration, httpClient, ObjectMapperFactory.getInstance().create());
            return new OpaClient(new OpaQueryClient(opaRestClient), new OpaDataClient(opaRestClient), new OpaPolicyClient(opaRestClient));
        }
    }
}
