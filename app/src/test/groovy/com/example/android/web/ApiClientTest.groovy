package com.example.android.web

import spock.lang.Specification


class ApiClientTest extends Specification {

	def 'Should correctly get client instance'() {
		when:
		ApiClient.getInstance()
		then:
		noExceptionThrown()
	}
}
