package com.bisnode.opa.client.rest.url

import spock.lang.Specification

class OpaUrlSpec extends Specification {
    public static final String URL = 'http://localhost:8181/'

    def 'should change multiple extra slashes to single one when normalized'() {
        when:
          OpaUrl opaUrl = OpaUrl.of(URL, '/v1/path/to///document')
          def normalizationResult = opaUrl.normalized().value
        then:
          normalizationResult == 'http://localhost:8181/v1/path/to/document'
    }

    def 'should remove trailing slash when normalized'() {
        when:
          OpaUrl opaUrl = OpaUrl.of(URL, '/v1/path/to/document/')
          def normalizationResult = opaUrl.normalized().value
        then:
          normalizationResult == 'http://localhost:8181/v1/path/to/document'
    }

    def 'should throw InvalidEndpointException when trying to create object with null endpoint'() {
        when:
          OpaUrl.of(URL, null)
        then:
          thrown(InvalidEndpointException)
    }
}
