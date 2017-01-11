package org.grails.plugins.htmlcleaner

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.jsoup.Jsoup
import org.springframework.util.Assert

@CompileStatic
class HtmlCleaner {

	String defaultWhiteList

	void setDefaultWhiteList(String str) {
		Assert.hasText(str, "Whitelist name must not be empty")
		defaultWhiteList= str
	}

	@CompileDynamic
	String cleanHtml(String unsafe, String whitelist) {
		if(!unsafe) { return unsafe }
		Assert.hasText(whitelist, "Whitelist name must not be empty")

		if(!whitelists[whitelist]) {
			throw new RuntimeException("Whitelist [${whitelist}] is not defined")
		}
		return Jsoup.clean(unsafe, whitelists[whitelist])
	}

	/**
	 * cleanHtml based on defaultWhitelist
	 *
	 * @param unsafe
	 * @return
	 */
	String cleanHtml(String unsafe) {
		if(!unsafe) { return unsafe }

		if(!defaultWhiteList) {
			throw new RuntimeException("Default Whitelist is not specified in configuration")
		}

		return cleanHtml(unsafe, defaultWhiteList)
	}
}
