package grails.plugin.htmlcleaner

import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist

class HtmlCleanerTests extends GroovyTestCase {

	private static final String UNSAFE_WITH_TABLE_TAG = '''
		<p>A sample paragraph
           <b>containing <span>various tags </span>
           <a href="http://www.google.com">A link </a>
           <i>A tag that will be removed</i>
        </p>
	'''

	HtmlCleaner htmlCleaner

	private Whitelist whitelist = Whitelist.none()

	protected void setUp() {
		//setup a whitelist same as the 'sample' whitelist defined in config
		whitelist.addTags('b','p','img','span','a')
		whitelist.addAttributes('a', 'href')
		whitelist.addProtocols('a', 'href', 'http')
		whitelist.addEnforcedAttribute('a', 'rel', 'nofollow')
	}

	void testCleanHtml() {
		def result = htmlCleaner.cleanHtml(UNSAFE_WITH_TABLE_TAG, "sample")
		assertEquals(Jsoup.clean(UNSAFE_WITH_TABLE_TAG, whitelist), result)
	}

	void testCleanHtmlWithDefaultWhiteList() {
		def result = htmlCleaner.cleanHtml(UNSAFE_WITH_TABLE_TAG)
		assertEquals(Jsoup.clean(UNSAFE_WITH_TABLE_TAG, Whitelist.simpleText()), result)
	}
}
