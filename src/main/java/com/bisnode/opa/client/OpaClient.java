package com.bisnode.opa.client;


import com.bisnode.opa.client.data.OpaDataApi;
import com.bisnode.opa.client.data.OpaDataClient;
import com.bisnode.opa.client.policy.OpaPolicyApi;
import com.bisnode.opa.client.policy.OpaPolicyClient;
import com.bisnode.opa.client.query.OpaQueryApi;
import com.bisnode.opa.client.query.OpaQueryClient;
import com.bisnode.opa.client.rest.ObjectMapperFactory;
import com.bisnode.opa.client.rest.OpaRestClient;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import java.net.http.HttpClient;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class OpaClient implements OpaQueryApi, OpaDataApi, OpaPolicyApi {

    @Delegate
    private final OpaQueryApi opaQueryApi;
    @Delegate
    private final OpaDataApi opaDataApi;
    @Delegate
    private final OpaPolicyApi opaPolicyApi;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        OpaConfiguration opaConfiguration;

        public Builder opaConfiguration(String url) {
            this.opaConfiguration = new OpaConfiguration(url);
            return this;
        }

        public OpaClient build() {
            HttpClient httpClient = HttpClient.newHttpClient();
            OpaRestClient opaRestClient = new OpaRestClient(opaConfiguration, httpClient, ObjectMapperFactory.getInstance().create());
            return new OpaClient(new OpaQueryClient(opaRestClient), new OpaDataClient(opaRestClient), new OpaPolicyClient(opaRestClient));
        }
    }
}
