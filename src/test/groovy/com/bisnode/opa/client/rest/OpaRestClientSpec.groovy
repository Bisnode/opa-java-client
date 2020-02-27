package com.bisnode.opa.client.rest

import com.bisnode.opa.client.OpaClientException
import com.bisnode.opa.client.OpaConfiguration
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import java.net.http.HttpClient
import java.net.http.HttpResponse

import static com.bisnode.opa.client.rest.ContentType.Values.APPLICATION_JSON
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.any
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo

class OpaRestClientSpec extends Specification {

    private static int PORT = 8181

    private String url = "http://localhost:$PORT"
    @Shared
    private WireMockServer wireMockServer = new WireMockServer(PORT)
    @Subject
    private OpaRestClient client

    def setupSpec() {
        wireMockServer.start()
    }

    def cleanupSpec() {
        wireMockServer.stop()
    }

    def setup() {
        client = new OpaRestClient(new OpaConfiguration(url), HttpClient.newHttpClient(), new ObjectMapper())
    }

    def 'should remove trailing slashes from request URL to OPA'() {
        given:
          def path = '/v1/path/with/trailing/slash/'
          wireMockServer
                  .stubFor(any(anyUrl())
                          .willReturn(aResponse()
                                  .withStatus(200)
                                  .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                  .withBody('{}')))
        when:
          def request = client.getBasicRequestBuilder(path).GET().build()
          client.sendRequest(request, HttpResponse.BodyHandlers.discarding())
        then:
          wireMockServer.verify(getRequestedFor(urlEqualTo('/v1/path/with/trailing/slash')))
    }

    def 'should remove extra slashes from request URL to OPA'() {
        given:
          def path = '/v1/path/with///extra//slash'
          wireMockServer
                  .stubFor(any(anyUrl())
                          .willReturn(aResponse()
                                  .withStatus(200)
                                  .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                  .withBody('{}')))
        when:
          def request = client.getBasicRequestBuilder(path).GET().build()
          client.sendRequest(request, HttpResponse.BodyHandlers.discarding())
        then:
          wireMockServer.verify(getRequestedFor(urlEqualTo('/v1/path/with/extra/slash')))
    }

    def 'should throw OpaClientException when url is null'() {
        given:
          wireMockServer
                  .stubFor(any(anyUrl())
                          .willReturn(aResponse()
                                  .withStatus(200)
                                  .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                  .withBody('{}')))
        when:
          def request = client.getBasicRequestBuilder(null).GET().build()
          client.sendRequest(request, HttpResponse.BodyHandlers.discarding())
        then:
          thrown(OpaClientException)
    }

    def 'should prefix endpoint path with slash when missing'() {
        given:
          wireMockServer
                  .stubFor(any(anyUrl())
                          .willReturn(aResponse()
                                  .withStatus(200)
                                  .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                                  .withBody('{}')))
        when:
          def request = client.getBasicRequestBuilder('v1/path/with/missing/slash').GET().build()
          client.sendRequest(request, HttpResponse.BodyHandlers.discarding())
        then:
          wireMockServer.verify(getRequestedFor(urlEqualTo('/v1/path/with/missing/slash')))
    }

}
