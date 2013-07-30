package grails.plugin.htmlcleaner

import grails.test.GrailsUnitTestCase

class WhitelistBuilderTests extends GrailsUnitTestCase {

	private WhitelistBuilder builder = new WhitelistBuilder()

	def confSuccess = {
		whitelist("sample") {
			startwith "basic"
			allow "b", "p", "img"
			allow("a") {
				attributes "class", "style", "href"
				enforce attribute:"rel", value:"nofollow"
			}
		}

		whitelist("sample2") {
			startwith "none"
			allow "b", "p", "img"
			allow("a") {
				attributes "class", "style", "href"
				enforce attribute:"rel", value:"nofollow"
			}
		}

		whitelist("sample3") {
			startwith "sample2"
			allow "span"
		}
	}

	def confWhitelistStartsWithUndefinedWhitelist = {
		whitelist("sample") {
			startwith "undefined"
			allow "b", "p", "img"
		}
	}

	def confWhitelistWithResorvedName = {
		whitelist("none") {
			startwith "basic"
			allow "b", "p", "img"
		}
	}

	void testWhitelistBuildSuccess() {
		def whiteLists = builder.build(confSuccess)
		assertNotNull(whiteLists)
		assertNotNull(whiteLists['sample'])
		assertNotNull(whiteLists['sample2'])
		assertNotNull(whiteLists['sample3'])
	}

	void testWhitelistStartsWithUndefinedWhitelistShouldFail() {
		try {
			def whiteLists = builder.build(confWhitelistStartsWithUndefinedWhitelist)
			fail("build() should fail if whitelist starts with an undefined whitelist")
		}catch(RuntimeException ex) {
			assertEquals("Whitelist [undefined] is not defined", ex.message)
		}
	}

	void testWhitelistWithReservedNamesShouldFails() {
		try {
			def whiteLists = builder.build(confWhitelistWithResorvedName)
			fail("build() should fail if whitelist with reserved names are found")
		}catch(RuntimeException ex) {
			assertEquals("Whitelist name [none] is reserved", ex.message)
		}
	}
}
