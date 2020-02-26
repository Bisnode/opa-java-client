package com.bisnode.opa.client

import com.bisnode.opa.client.query.OpaQueryApi
import com.bisnode.opa.client.query.QueryForDocumentRequest
import com.bisnode.opa.client.rest.ContentType
import com.github.tomakehurst.wiremock.WireMockServer
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.bisnode.opa.client.rest.ContentType.Values.APPLICATION_JSON
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo

class QueryingForDocumentSpec extends Specification {

    private static int PORT = 8181
    private static String url = "http://localhost:$PORT"

    @Shared
    private WireMockServer wireMockServer = new WireMockServer(PORT)

    @Subject
    private OpaQueryApi client

    def setupSpec() {
        wireMockServer.start()
    }

    def cleanupSpec() {
        wireMockServer.stop()
    }

    def setup() {
        client = OpaClient.builder().opaConfiguration(url).build()
    }

    def 'should perform successful document evaluation'() {
        given:
          def path = 'someDocument'
          def endpoint = "/v1/data/$path"
          wireMockServer
                  .stubFor(post(urlEqualTo(endpoint))
                          .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON))
                          .willReturn(aResponse()
                                  .withStatus(200)
                                  .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                  .withBody('{"result": {"allow":"true"}}')))
        when:
          def result = client.queryForDocument(new QueryForDocumentRequest([shouldPass: true], path), ValidationResult.class)
        then:
          noExceptionThrown()
          wireMockServer.verify(postRequestedFor(urlEqualTo(endpoint))
                  .withRequestBody(equalToJson('{"input":{"shouldPass": true}}'))
                  .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON)))
          result.allow
    }

    def 'should return empty object when empty json returned from opa'() {
        given:
          def path = 'someDocument'
          def endpoint = "/v1/data/$path"
          wireMockServer
                  .stubFor(post(urlEqualTo(endpoint))
                          .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON))
                          .willReturn(aResponse()
                                  .withStatus(200)
                                  .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                  .withBody('{"result": {}}')))
        when:
          def result = client.queryForDocument(new QueryForDocumentRequest([shouldPass: true], path), ValidationResult.class)
        then:
          noExceptionThrown()
          wireMockServer.verify(postRequestedFor(urlEqualTo(endpoint))
                  .withRequestBody(equalToJson('{"input":{"shouldPass": true}}'))
                  .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON)))
          result.allow == null
    }

    def 'should not fail when returned more properties than mapping requires'() {
        given:
          def path = 'someDocument'
          def endpoint = "/v1/data/$path"
          wireMockServer
                  .stubFor(post(urlEqualTo(endpoint))
                          .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON))
                          .willReturn(aResponse()
                                  .withStatus(200)
                                  .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                  .withBody('{"result": {"authorized": "true", "allow": true}}, "otherStuff": true')))
        when:
          def result = client.queryForDocument(new QueryForDocumentRequest([shouldPass: true], path), ValidationResult.class)
        then:
          noExceptionThrown()
          wireMockServer.verify(postRequestedFor(urlEqualTo(endpoint))
                  .withRequestBody(equalToJson('{"input":{"shouldPass": true}}'))
                  .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON)))
          result.allow
    }

    @Unroll
    def 'should throw OpaClientException on status code = #status'() {
        given:
          def path = 'someDocument'
          def endpoint = "/v1/data/$path"
          wireMockServer
                  .stubFor(post(urlEqualTo(endpoint))
                          .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON))
                          .willReturn(aResponse()
                                  .withStatus(status)
                                  .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)))
        when:
          client.queryForDocument(new QueryForDocumentRequest([shouldPass: true], path), ValidationResult.class)
        then:
          thrown(OpaClientException)
          wireMockServer.verify(postRequestedFor(urlEqualTo(endpoint))
                  .withRequestBody(equalToJson('{"input":{"shouldPass": true}}'))
                  .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON)))
        where:
          status << [400, 404, 500]
    }

    def 'should handle nested classes in response'() {
        given:
          def path = 'someDocument'
          def endpoint = "/v1/data/$path"
          wireMockServer
                  .stubFor(post(urlEqualTo(endpoint))
                          .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON))
                          .willReturn(aResponse()
                                  .withStatus(200)
                                  .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                  .withBody('{"result": {"validationResult": {"allow": true}}}')))
        when:
          def result = client.queryForDocument(new QueryForDocumentRequest([shouldPass: true], path), ComplexValidationResult.class)
        then:
          result.getClass() == ComplexValidationResult.class
          result.validationResult.allow
    }

    def 'should map missing properties to null'() {
        given:
          def path = 'someDocument'
          def endpoint = "/v1/data/$path"
          wireMockServer
                  .stubFor(post(urlEqualTo(endpoint))
                          .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON))
                          .willReturn(aResponse()
                                  .withStatus(200)
                                  .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                  .withBody('{"result": {"validationResult": {"authorized": true}}}')))
        when:
          def result = client.queryForDocument(new QueryForDocumentRequest([shouldPass: true], path), ComplexValidationResult.class)
        then:
          result.getClass() == ComplexValidationResult.class
          result.validationResult.allow == null
    }

    def 'should throw exception when document is empty'() {
        given:
          def path = 'someDocument'
          def endpoint = "/v1/data/$path"
          wireMockServer
                  .stubFor(post(urlEqualTo(endpoint))
                          .withHeader(ContentType.HEADER_NAME, equalTo(APPLICATION_JSON))
                          .willReturn(aResponse()
                                  .withStatus(200)
                                  .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                  .withBody('{}')))
        when:
          client.queryForDocument(new QueryForDocumentRequest([shouldPass: true], path), ComplexValidationResult.class)
        then:
          thrown(OpaClientException)

    }

    static final class ValidationResult {
        Boolean allow
    }

    static final class ComplexValidationResult {
        ValidationResult validationResult
    }

}
