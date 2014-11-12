package grails.plugin.htmlcleaner

import org.jsoup.safety.Whitelist

class WhitelistBuilder {

	private static final List RESERVED_WHITELISTS = ['none', 'simpleText', 'basic', 'basicWithImages', 'relaxed']
	private final Map whiteLists = [:]

	private Whitelist currentWhitelist
	private String currentTag

	Map<String, Whitelist> build(Closure c) {
		if(c) {
			c.delegate = this
			c.resolveStrategy = Closure.DELEGATE_ONLY
			c.call()
		}
		return whiteLists
	}

	def whitelist(String name, Closure c) {
		if(!name) {
			throw new RuntimeException("Whitelist must have a name")
		}
		if(RESERVED_WHITELISTS.contains(name)) {
			throw new RuntimeException("Whitelist name [${name}] is reserved")
		}
		if(c) {
			c.delegate = this
			c.resolveStrategy = Closure.DELEGATE_ONLY
			c.call()
			whiteLists[name] = currentWhitelist
		}
	}

	def startwith(String name) {
		currentWhitelist = null
		switch (name) {
			case "basic":
				currentWhitelist = Whitelist.basic()
				break
			case "none":
				currentWhitelist = Whitelist.none()
				break
			case "simpleText":
				currentWhitelist = Whitelist.simpleText()
				break
			case "basicWithImages":
				currentWhitelist = Whitelist.basicWithImages()
				break
			case "relaxed":
				currentWhitelist = Whitelist.relaxed()
				break
			default:
				currentWhitelist = whiteLists[name]
		}
		if(!currentWhitelist) {
			throw new RuntimeException("Whitelist [${name}] is not defined")
		}
	}

	def allow(String[] tags) {
		tags.each {
			currentWhitelist.addTags(it)
		}
	}

	def allow(String name, Closure c) {
		if(name) {
			currentWhitelist.addTags(name)
			currentTag = name
		}
		if(c) {
			c.delegate = this
			c.resolveStrategy = Closure.DELEGATE_ONLY
			c.call()
		}
	}

	def attributes(String[] attrs) {
		attrs.each {
			currentWhitelist.addAttributes(currentTag, it)
		}
	}

	def enforce(Map attr) {
		if(attr) {
			currentWhitelist.addEnforcedAttribute(currentTag, attr.attribute, attr.value)
		}
	}

    def protocols(Map map) {
        if (!map?.attribute || !map.value) {
            return
        }

        List values = map.value instanceof String ? [ map.value ] : map.value

        values.each { String protocol ->
            currentWhitelist.addProtocols(currentTag, map.attribute, protocol)
        }
    }
}
