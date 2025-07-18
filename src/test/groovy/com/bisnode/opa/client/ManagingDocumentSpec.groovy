package com.bisnode.opa.client

import com.bisnode.opa.client.data.OpaDataApi
import com.bisnode.opa.client.data.OpaDocument
import com.bisnode.opa.client.query.OpaQueryApi
import com.bisnode.opa.client.rest.ContentType
import com.bisnode.opa.client.rest.OpaServerConnectionException
import com.github.tomakehurst.wiremock.WireMockServer
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.bisnode.opa.client.rest.ContentType.Values.APPLICATION_JSON
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo
import static com.github.tomakehurst.wiremock.client.WireMock.put
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo

class ManagingDocumentSpec extends Specification {

    private static int PORT = 8181

    private String url = "http://localhost:$PORT"
    @Shared
    private WireMockServer wireMockServer = new WireMockServer(PORT)
    @Subject
    private OpaDataApi client = OpaClient.builder().opaConfiguration(url).build()

    def DOCUMENT = '{"example": {"flag": true}}'

    def setupSpec() {
        wireMockServer.start()
    }

    def cleanupSpec() {
        wireMockServer.stop()
    }

    def 'should perform successful policy create or update'() {
        given:
          def documentPath = 'somePath'
          def endpoint = "/v1/data/$documentPath"
          wireMockServer
                  .stubFor(put(urlEqualTo(endpoint))
                          .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON))
                          .willReturn(aResponse()
                                  .withStatus(200)
                                  .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                  .withBody('{}')))
        when:
          client.createOrOverwriteDocument(new OpaDocument(documentPath, DOCUMENT))
        then:
          noExceptionThrown()
          wireMockServer.verify(putRequestedFor(urlEqualTo(endpoint))
                  .withRequestBody(equalTo(DOCUMENT))
                  .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON)))
    }

    @Unroll
    def 'should throw OpaClientException on status code = #status'() {
        given:
          def documentPath = 'somePath'
          def endpoint = "/v1/data/$documentPath"
          wireMockServer
                  .stubFor(put(urlEqualTo(endpoint))
                          .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON))
                          .willReturn(aResponse()
                                  .withStatus(status)
                                  .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                  .withBody('{}')))
        when:
          client.createOrOverwriteDocument(new OpaDocument(documentPath, DOCUMENT))
        then:
          thrown(OpaClientException)
          wireMockServer.verify(putRequestedFor(urlEqualTo(endpoint))
                  .withRequestBody(equalTo(DOCUMENT))
                  .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON)))
        where:
          status << [400, 404, 500]
    }


    def 'should throw OpaServerConnectionException when server is down'() {
        given:
          def documentPath = 'somePath'
          def fakeUrl = 'http://localhost:8182'
          OpaQueryApi newClient = OpaClient.builder().opaConfiguration(fakeUrl).build()
        when:
          newClient.createOrOverwriteDocument(new OpaDocument(documentPath, DOCUMENT))
        then:
          thrown(OpaServerConnectionException)

    }
    
    def 'should include headers in document requests'() {
        given:
          def documentPath = 'somePath'
          def endpoint = "/v1/data/$documentPath"
          def headerName = 'X-API-Key'
          def headerValue = 'secret-key-123'
          def client = OpaClient.builder()
                  .opaConfiguration(url)
                  .header(headerName, headerValue)
                  .build()
          wireMockServer
                  .stubFor(put(urlEqualTo(endpoint))
                          .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON))
                          .withHeader(headerName, equalTo(headerValue))
                          .willReturn(aResponse()
                                  .withStatus(200)
                                  .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                  .withBody('{}')))
        when:
          client.createOrOverwriteDocument(new OpaDocument(documentPath, DOCUMENT))
        then:
          noExceptionThrown()
          wireMockServer.verify(putRequestedFor(urlEqualTo(endpoint))
                  .withHeader(headerName, equalTo(headerValue)))
    }
}
