package com.example.android

import spock.lang.Specification


class SanityCheckTest extends Specification {

	def 'Should perform simple calculations correctly'() {
		expect:
		2 + 2 == 4
	}
}
