package org.grails.plugins.htmlcleaner

import groovy.transform.CompileStatic

@CompileStatic
class TestController {

	def doCleanWithDefaultWhiteList(String unsafe) {
		String safe = cleanHtml(unsafe)
		return [cleaned:safe]
	}

	def doCleanWithSpecifiedWhiteList(String unsafe, String whitelist) {
		return [cleaned: cleanHtml(unsafe, whitelist)]
	}
}
