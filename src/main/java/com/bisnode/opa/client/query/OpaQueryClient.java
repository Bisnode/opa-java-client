package com.bisnode.opa.client.query;

import com.bisnode.opa.client.OpaClientException;
import com.bisnode.opa.client.rest.ContentType;
import com.bisnode.opa.client.rest.OpaRestClient;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.net.http.HttpRequest;
import java.util.Objects;

/**
 * @see com.bisnode.opa.client.query.OpaQueryApi
 */
public class OpaQueryClient implements OpaQueryApi {
    private static final String EVALUATE_POLICY_ENDPOINT = "/v1/data/";
    private static final String EMPTY_RESULT_ERROR_MESSAGE = "Result is empty, it may indicate that document under path [%s] does not exist";

    private final OpaRestClient opaRestClient;

    public OpaQueryClient(OpaRestClient opaRestClient) {
        this.opaRestClient = opaRestClient;
    }

    @Override
    public <R> R queryForDocument(QueryForDocumentRequest queryForDocumentRequest, Class<R> responseType) {
        try {
            OpaQueryForDocumentRequest opaQueryForDocumentRequest = new OpaQueryForDocumentRequest(queryForDocumentRequest.getInput());

            HttpRequest request = opaRestClient.getBasicRequestBuilder(EVALUATE_POLICY_ENDPOINT + queryForDocumentRequest.getPath())
                    .header(ContentType.HEADER_NAME, ContentType.Values.APPLICATION_JSON)
                    .POST(opaRestClient.getJsonBodyPublisher(opaQueryForDocumentRequest))
                    .build();

            JavaType opaResponseType = TypeFactory.defaultInstance().constructParametricType(OpaQueryForDocumentResponse.class, responseType);
            R result = opaRestClient.sendRequest(request, opaRestClient.<OpaQueryForDocumentResponse<R>>getJsonBodyHandler(opaResponseType))
                    .body()
                    .get()
                    .getResult();
            if (Objects.isNull(result)) {
                throw new OpaClientException(String.format(EMPTY_RESULT_ERROR_MESSAGE, queryForDocumentRequest.getPath()));
            }
            return result;
        } catch (OpaClientException exception) {
            throw exception;
        } catch (Exception e) {
            throw new OpaClientException(e);
        }
    }
}
