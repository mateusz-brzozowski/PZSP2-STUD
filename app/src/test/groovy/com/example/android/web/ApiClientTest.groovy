package com.example.android.web

import com.example.model.Concept
import spock.lang.Specification


class ApiClientTest extends Specification {

	def 'Should perform simple calculations correctly'() {
		expect:
		ApiClient.getInstance().getConceptById(1) == new Concept()
	}
}
