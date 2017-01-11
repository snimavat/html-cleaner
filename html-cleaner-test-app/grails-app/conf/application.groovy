htmlcleaner  {

	defaultWhiteList = "none"

	whitelists = {
		whitelist("test-sample") {
			startwith "none"
			allow "b", "p", "img"
			allow "span"
		}
	}
}