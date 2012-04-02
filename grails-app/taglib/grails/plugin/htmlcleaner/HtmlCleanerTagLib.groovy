package grails.plugin.htmlcleaner

class HtmlCleanerTagLib {
	static final namespace = 'hc'
	
	HtmlCleaner htmlCleaner
	
	/**
	 * @attr unsafe - the html string to clean
	 * @attr whitelist - Name of whitelist that should be used to clean the html.
	 */
	def cleanHtml = {attrs, body ->
		if (!attrs.whitelist) throwTagError("Tag [cleanHtml] is missing required attribute [whitelist]")
		if(attrs.unsafe) {
			out << htmlCleaner.cleanHtml(attrs.unsafe, attrs.whitelist)
		}
	}
	
}
