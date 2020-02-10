package com.bisnode.opa.client.rest

import com.fasterxml.jackson.databind.DeserializationFeature
import spock.lang.Specification

class ObjectMapperFactorySpec extends Specification {

    def 'should create ObjectMapperFactory instance only once'() {
        when:
          def firstCall = ObjectMapperFactory.getInstance()
          def secondCall = ObjectMapperFactory.getInstance()
        then:
          firstCall == secondCall
    }

    def 'should set FAIL_ON_UNKNOWN_PROPERTIES to false in created ObjectMapper'() {
        when:
          def result = ObjectMapperFactory.getInstance().create()
        then:
          !result.deserializationConfig.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

}
