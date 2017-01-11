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