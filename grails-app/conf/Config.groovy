htmlcleaner  {

	defaultWhiteList = "simpleText"

	whitelists = {
		whitelist("sample") {
			startwith "none"
			allow "b", "p", "img"
			allow "span"
			allow("a") {
				attributes "href"
                protocols attribute: 'href', value: 'http'
				enforce attribute:"rel", value:"nofollow"
			}
		}
	}
}

log4j = {
	error 'org.codehaus.groovy.grails',
	      'org.springframework',
	      'org.hibernate',
	      'net.sf.ehcache.hibernate'
}
