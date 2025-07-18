package com.bisnode.opa.client


import com.bisnode.opa.client.query.QueryForDocumentRequest
import com.bisnode.opa.client.rest.ContentType
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import spock.lang.Shared
import spock.lang.Specification

import static com.bisnode.opa.client.rest.ContentType.Values.APPLICATION_JSON
import static com.github.tomakehurst.wiremock.client.WireMock.*

class OpaClientBuilderSpec extends Specification {

    private static int PORT = 8181
    private static String url = "http://localhost:$PORT"

    @Shared
    private WireMockServer wireMockServer = new WireMockServer(PORT)

    def setupSpec() {
        wireMockServer.start()
    }

    def cleanupSpec() {
        wireMockServer.stop()
    }

    def 'should configure OpaClient with custom ObjectMapper'() {

        given:
        def objectMapper = Spy(ObjectMapper)
        def path = 'someDocument'
        def endpoint = "/v1/data/$path"
        wireMockServer
                .stubFor(post(urlEqualTo(endpoint))
                        .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                .withBody('{"result": {"authorized": true}}')))
        def opaClient = OpaClient.builder()
                .opaConfiguration(url)
                .objectMapper(objectMapper)
                .build();

        when:
        opaClient.queryForDocument(new QueryForDocumentRequest([shouldPass: true], path), Object.class)

        then:
        1 * objectMapper.writeValueAsString(_)
    }

    def 'should revert to default ObjectMapper if null ObjectMapper supplied'() {
        given:
        def path = 'someDocument'
        def endpoint = "/v1/data/$path"
        wireMockServer
                .stubFor(post(urlEqualTo(endpoint))
                        .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                .withBody('{"result": {"authorized": true}}')))
        def opaClient = OpaClient.builder()
                .opaConfiguration(url)
                .objectMapper(null)
                .build();

        when:
        def result = opaClient.queryForDocument(new QueryForDocumentRequest([shouldPass: true], path), Map.class)

        then:
        result != null
        result.get("authorized") == true
    }

    def 'should include single header in requests'() {
        given:
        def path = 'someDocument'
        def endpoint = "/v1/data/$path"
        def headerName = 'X-API-Key'
        def headerValue = 'secret-api-key'
        wireMockServer
                .stubFor(post(urlEqualTo(endpoint))
                        .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON))
                        .withHeader(headerName, equalTo(headerValue))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                .withBody('{"result": {"authorized": true}}')))
        def opaClient = OpaClient.builder()
                .opaConfiguration(url)
                .header(headerName, headerValue)
                .build();

        when:
        opaClient.queryForDocument(new QueryForDocumentRequest([shouldPass: true], path), Object.class)

        then:
        wireMockServer.verify(postRequestedFor(urlEqualTo(endpoint))
                .withHeader(headerName, equalTo(headerValue)))
    }

    def 'should include multiple headers in requests'() {
        given:
        def path = 'someDocument'
        def endpoint = "/v1/data/$path"
        def headers = [
                'X-API-Key': 'secret-api-key',
                'X-Tenant-ID': 'tenant-123',
                'Authorization': 'Bearer token123'
        ]
        wireMockServer
                .stubFor(post(urlEqualTo(endpoint))
                        .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON))
                        .withHeader('X-API-Key', equalTo('secret-api-key'))
                        .withHeader('X-Tenant-ID', equalTo('tenant-123'))
                        .withHeader('Authorization', equalTo('Bearer token123'))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                .withBody('{"result": {"authorized": true}}')))
        def opaClient = OpaClient.builder()
                .opaConfiguration(url)
                .headers(headers)
                .build();

        when:
        opaClient.queryForDocument(new QueryForDocumentRequest([shouldPass: true], path), Object.class)

        then:
        wireMockServer.verify(postRequestedFor(urlEqualTo(endpoint))
                .withHeader('X-API-Key', equalTo('secret-api-key'))
                .withHeader('X-Tenant-ID', equalTo('tenant-123'))
                .withHeader('Authorization', equalTo('Bearer token123')))
    }

    def 'should combine individual header with multiple headers'() {
        given:
        def path = 'someDocument'
        def endpoint = "/v1/data/$path"
        def headers = [
                'X-API-Key': 'secret-api-key',
                'X-Tenant-ID': 'tenant-123'
        ]
        wireMockServer
                .stubFor(post(urlEqualTo(endpoint))
                        .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON))
                        .withHeader('X-API-Key', equalTo('secret-api-key'))
                        .withHeader('X-Tenant-ID', equalTo('tenant-123'))
                        .withHeader('Authorization', equalTo('Bearer token123'))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                .withBody('{"result": {"authorized": true}}')))
        def opaClient = OpaClient.builder()
                .opaConfiguration(url)
                .headers(headers)
                .header('Authorization', 'Bearer token123')
                .build();

        when:
        opaClient.queryForDocument(new QueryForDocumentRequest([shouldPass: true], path), Object.class)

        then:
        wireMockServer.verify(postRequestedFor(urlEqualTo(endpoint))
                .withHeader('X-API-Key', equalTo('secret-api-key'))
                .withHeader('X-Tenant-ID', equalTo('tenant-123'))
                .withHeader('Authorization', equalTo('Bearer token123')))
    }
}
