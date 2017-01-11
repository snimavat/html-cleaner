package org.grails.plugins.htmlcleaner

class HtmlCleanerTagLib {
	static final namespace = 'hc'

	HtmlCleaner htmlCleaner

	/**
	 * Cleans HTML and removes html tags based on whitelist
	 *
	 * @attr unsafe - the html string to clean
	 * @attr whitelist - Name of whitelist that should be used to clean the html. If not specified default whitelist will be used.
	 */
	def cleanHtml = {attrs, body ->
		if (!attrs.whitelist) {
			out << htmlCleaner.cleanHtml(attrs.unsafe)
		} else {
			out << htmlCleaner.cleanHtml(attrs.unsafe, attrs.whitelist)
		}
	}
}
