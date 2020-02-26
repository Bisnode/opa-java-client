package com.bisnode.opa.client

import com.bisnode.opa.client.policy.OpaPolicy
import com.bisnode.opa.client.policy.OpaPolicyApi
import com.bisnode.opa.client.rest.ContentType
import com.github.tomakehurst.wiremock.WireMockServer
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.bisnode.opa.client.rest.ContentType.Values.APPLICATION_JSON
import static com.bisnode.opa.client.rest.ContentType.Values.TEXT_PLAIN
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo
import static com.github.tomakehurst.wiremock.client.WireMock.put
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo

class ManagingPolicySpec extends Specification {

    private static int PORT = 8181;
    private static String url = "http://localhost:$PORT"

    @Shared
    private WireMockServer wireMockServer = new WireMockServer(PORT)
    @Subject
    private OpaPolicyApi client

    def POLICY = """package example
                    default allow = false 

                    allow = true {
                        input.body == "Looks good"
                    }"""

    def setupSpec() {
        wireMockServer.start()
    }

    def cleanupSpec() {
        wireMockServer.stop()
    }

    def setup() {
        client = OpaClient.builder().opaConfiguration(url).build()
    }

    def 'should perform successful policy create or update'() {
        given:
          def policyId = '12345'
          def endpoint = "/v1/policies/$policyId"
          wireMockServer
                  .stubFor(put(urlEqualTo(endpoint))
                          .withHeader(ContentType.HEADER_NAME, equalTo(TEXT_PLAIN))
                          .willReturn(aResponse()
                                  .withStatus(200)
                                  .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                  .withBody('{}')))
        when:
          client.createOrUpdatePolicy(new OpaPolicy(policyId, POLICY))
        then:
          noExceptionThrown()
          wireMockServer.verify(putRequestedFor(urlEqualTo(endpoint))
                  .withRequestBody(equalTo(POLICY))
                  .withHeader(ContentType.HEADER_NAME, equalTo(TEXT_PLAIN)))
    }

    @Unroll
    def 'should throw OpaClientException on status code = #status'() {
        given:
          def policyId = '12345'
          def endpoint = "/v1/policies/$policyId"
          wireMockServer
                  .stubFor(put(urlEqualTo(endpoint))
                          .withHeader(ContentType.HEADER_NAME, equalTo(TEXT_PLAIN))
                          .willReturn(aResponse()
                                  .withStatus(status)
                                  .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                  .withBody('{}')))
        when:
          client.createOrUpdatePolicy(new OpaPolicy(policyId, POLICY))
        then:
          thrown(OpaClientException)
          wireMockServer.verify(putRequestedFor(urlEqualTo(endpoint))
                  .withRequestBody(equalTo(POLICY))
                  .withHeader(ContentType.HEADER_NAME, equalTo(TEXT_PLAIN)))
        where:
          status << [400, 500]
    }
}
