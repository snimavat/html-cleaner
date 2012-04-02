package grails.plugin.htmlcleaner

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist

import groovy.util.GroovyTestCase

class HtmlCleanerTests extends GroovyTestCase {

	private static final String UNSAFE_WITH_TABLE_TAG = '''
		<p>A sample paragraph
           <b>containing <span>various tags </span>
           <a href="http://www.google.com">A link </a>
           <i>A tag that will be removed</i>
        </p>
	'''
	
	HtmlCleaner htmlCleaner 
	
	Whitelist whitelist
		
	void setUp() {		
		//setup a whitelist same as the 'sample' whitelist defined in config
		whitelist = Whitelist.none()
		whitelist.addTags('b','p','img','span','a')
		whitelist.addAttributes('a', 'href')
		whitelist.addEnforcedAttribute('a', 'rel', 'nofollow')
	}
	
	void testCleanHtml() {
		def result = htmlCleaner.cleanHtml(UNSAFE_WITH_TABLE_TAG, "sample")
		assertEquals(Jsoup.clean(UNSAFE_WITH_TABLE_TAG, whitelist), result)
	}	
}
