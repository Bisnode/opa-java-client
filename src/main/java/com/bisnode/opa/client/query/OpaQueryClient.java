package com.bisnode.opa.client.query;

import com.bisnode.opa.client.OpaClientException;
import com.bisnode.opa.client.rest.ContentType;
import com.bisnode.opa.client.rest.OpaRestClient;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    /**
     * <p>Executes simple query for document
     * </p>
     *
     * @param queryForDocumentRequest request containing information needed for querying
     * @param responseType            class of response to be returned
     * @return response from OPA mapped to specified class
     * @since 0.0.1
     */
    public <R> R queryForDocument(QueryForDocumentRequest queryForDocumentRequest, Class<R> responseType){
        return internalQueryForDocument(queryForDocumentRequest,responseType);
    }

    @Override
    public <R> R queryForDocument(QueryForDocumentRequest queryForDocumentRequest, ParameterizedType responseType) {
        return internalQueryForDocument(queryForDocumentRequest,responseType);
    }

    private <R> R internalQueryForDocument(QueryForDocumentRequest queryForDocumentRequest, Type responseType) {
        try {
            OpaQueryForDocumentRequest opaQueryForDocumentRequest = new OpaQueryForDocumentRequest(queryForDocumentRequest.getInput());

            HttpRequest request = opaRestClient.getBasicRequestBuilder(EVALUATE_POLICY_ENDPOINT + queryForDocumentRequest.getPath())
                    .header(ContentType.HEADER_NAME, ContentType.Values.APPLICATION_JSON)
                    .POST(opaRestClient.getJsonBodyPublisher(opaQueryForDocumentRequest))
                    .build();

            JavaType opaResponseType;
            if(responseType instanceof ParameterizedType)
            {
                ParameterizedType parameterizedType = (ParameterizedType) responseType;
                List<? extends Class<?>> collect = Arrays.stream(parameterizedType.getActualTypeArguments()).map(type -> {
                    try {
                        return Class.forName(type.getTypeName());
                    }
                    catch (ClassNotFoundException e) {
                       throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

                Class<?>[] classes = collect.toArray(new Class<?>[collect.size()]);
                JavaType opaType = TypeFactory.defaultInstance().constructParametricType(Class.forName(parameterizedType.getRawType().getTypeName()),classes);
                opaResponseType = TypeFactory.defaultInstance().constructParametricType(OpaQueryForDocumentResponse.class, opaType);
            }
            else
            {
                opaResponseType = TypeFactory.defaultInstance().constructParametricType(OpaQueryForDocumentResponse.class, Class.forName(responseType.getTypeName()));
            }

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
