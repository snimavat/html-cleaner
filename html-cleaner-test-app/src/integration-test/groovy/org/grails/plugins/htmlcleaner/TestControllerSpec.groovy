package org.grails.plugins.htmlcleaner

import grails.test.mixin.integration.Integration
import grails.util.GrailsWebMockUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.RequestContextHolder
import spock.lang.Specification

@Integration
class TestControllerSpec extends Specification {

	@Autowired
	WebApplicationContext ctx

	@Autowired
	TestController testController

	@Autowired
	HtmlCleaner htmlCleaner

	def setup() {
		GrailsWebMockUtil.bindMockWebRequest(ctx)
	}

	def cleanup() {
		RequestContextHolder.resetRequestAttributes()
	}

	def "test controllers can clean"() {
		expect:
		htmlCleaner != null
		htmlCleaner.defaultWhiteList == "none"
		htmlCleaner.whitelists['test-sample'] != null

		when:
		String unsafe = "<p>test</p>"
		Map result = testController.doCleanWithDefaultWhiteList(unsafe)

		then:
		noExceptionThrown()

		result.cleaned == "test"

		when:
		result = testController.doCleanWithSpecifiedWhiteList(unsafe, "test-sample")

		then:
		result.cleaned == unsafe
	}
}
