package org.grails.plugins.htmlcleaner

import grails.core.GrailsApplication
import grails.test.mixin.integration.Integration
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@Integration
class HtmlCleanerSpec extends Specification {

	private static final String UNSAFE_WITH_TABLE_TAG = '''
		<p>A sample paragraph
           <b>containing <span>various tags </span>
           <a href="http://www.google.com">A link </a>
           <i>A tag that will be removed</i>
        </p>
	'''

	@Autowired
	HtmlCleaner htmlCleaner

	GrailsApplication grailsApplication

	private Whitelist whitelist = Whitelist.none()

	protected void setup() {
		//setup a whitelist same as the 'sample' whitelist defined in config
		whitelist.addTags('b','p','img','span','a')
		whitelist.addAttributes('a', 'href')
		whitelist.addProtocols('a', 'href', 'http')
		whitelist.addEnforcedAttribute('a', 'rel', 'nofollow')
	}

	void testCleanHtml() {
		when:
		def result = htmlCleaner.cleanHtml(UNSAFE_WITH_TABLE_TAG, "sample")

		then:
		Jsoup.clean(UNSAFE_WITH_TABLE_TAG, whitelist) == result
	}

	void testCleanHtmlWithDefaultWhiteList() {
		when:
		def result = htmlCleaner.cleanHtml(UNSAFE_WITH_TABLE_TAG)

		then:
		Jsoup.clean(UNSAFE_WITH_TABLE_TAG, Whitelist.simpleText()) == result
	}
}
