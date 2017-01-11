package org.grails.plugins.htmlcleaner

import groovy.transform.CompileStatic
import org.jsoup.safety.Whitelist

@CompileStatic
class WhitelistBuilder {

	private static final List RESERVED_WHITELISTS = ['none', 'simpleText', 'basic', 'basicWithImages', 'relaxed']
	private final Map<String, Whitelist> whiteLists = [:]

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

	void whitelist(String name, Closure c) {
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

	void startwith(String name) {
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

	void allow(String[] tags) {
		tags.each { String tag ->
			currentWhitelist.addTags(tag)
		}
	}

	void allow(String name, Closure c) {
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

	void attributes(String[] attrs) {
		attrs.each { String attr ->
			currentWhitelist.addAttributes(currentTag, attr)
		}
	}

	void enforce(Map<String, String> attr) {
		if(attr) {
			currentWhitelist.addEnforcedAttribute(currentTag, attr.attribute, attr.value)
		}
	}

    void protocols(Map<String, Object> map) {
        if (!map?.attribute || !map.value) {
            return
        }

        List<String> values = (List) (map.value instanceof String ? [ map.value ] : map.value)

        values.each { String protocol ->
            currentWhitelist.addProtocols(currentTag, (String)map.attribute, protocol)
        }
    }
}
