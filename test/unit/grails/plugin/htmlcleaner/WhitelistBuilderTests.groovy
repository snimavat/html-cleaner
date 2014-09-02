package grails.plugin.htmlcleaner

import grails.test.GrailsUnitTestCase
import org.jsoup.safety.Whitelist


class WhitelistBuilderTests extends GrailsUnitTestCase {

	private WhitelistBuilder builder = new WhitelistBuilder()

	def confSuccess = {
		whitelist("sample") {
			startwith "basic"
			allow "b", "p", "img"
			allow("a") {
				attributes "class", "style", "href"
                protocols attribute: 'href', value: 'http'
				enforce attribute:"rel", value:"nofollow"
			}
		}

		whitelist("sample2") {
			startwith "none"
			allow "b", "p", "img"
			allow("a") {
				attributes "class", "style", "href"
                protocols attribute: 'href', value: [ 'http', 'https' ]
				enforce attribute:"rel", value:"nofollow"
			}
		}

		whitelist("sample3") {
			startwith "sample2"
			allow "span"
            allow("a") {
                protocols attribute: 'href', value: 'http'
                protocols attribute: 'href', value: 'https'
                protocols attribute: 'href' // Invalid
                protocols value: 'http' // Invalid
            }
		}
	}

	def confWhitelistStartsWithUndefinedWhitelist = {
		whitelist("sample") {
			startwith "undefined"
			allow "b", "p", "img"
		}
	}

	def confWhitelistWithReservedName = {
		whitelist("none") {
			startwith "basic"
			allow "b", "p", "img"
		}
    }

	void testWhitelistBuildSuccess() {
        Integer numCalled = 0

        // Need to verify addProtocols() getting called properly, but no get method available on Whitelists. MetaClass gets rolled back so should not leak.
        Whitelist.metaClass.addProtocols = { String tagName, String attribute, String protocol ->
            assert tagName == 'a'
            assert attribute == 'href'
            assert protocol in [ 'http', 'https']

            numCalled++
        }

        Map whiteLists = builder.build(confSuccess)

		assertNotNull(whiteLists)
		assertNotNull(whiteLists['sample'])
		assertNotNull(whiteLists['sample2'])
		assertNotNull(whiteLists['sample3'])

        assert numCalled == 5 // One for each valid protocol in the three sample configs.
	}

	void testWhitelistStartsWithUndefinedWhitelistShouldFail() {
        String message = shouldFail(RuntimeException.class) {
            builder.build(confWhitelistStartsWithUndefinedWhitelist)
        }

    	assertEquals("Whitelist [undefined] is not defined", message)
	}

	void testWhitelistWithReservedNamesShouldFails() {
        String message = shouldFail(RuntimeException.class) {
			builder.build(confWhitelistWithReservedName)
        }

        assertEquals("Whitelist name [none] is reserved", message)
	}
}
