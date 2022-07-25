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
}
