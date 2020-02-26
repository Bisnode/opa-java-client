package com.bisnode.opa.client.rest

import spock.lang.Specification

class UrlNormalizerSpec extends Specification {

    def 'should change multiple extra slashes to single one'() {
        when:
          def normalizationResult = UrlNormalizer.normalize('http://localhost:8181//v1/path/to///document')
        then:
          normalizationResult.get() == 'http://localhost:8181/v1/path/to/document'
    }

    def 'should remove trailing slash'() {
        when:
          def normalizationResult = UrlNormalizer.normalize('http://localhost:8181/v1/path/to/document/')
        then:
          normalizationResult.get() == 'http://localhost:8181/v1/path/to/document'
    }


    def 'should return empty optional when input is null'() {
        when:
          def normalizationResult = UrlNormalizer.normalize(null)
        then:
          normalizationResult.isEmpty()
    }
}
