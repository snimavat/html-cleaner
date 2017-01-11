package org.grails.plugins.htmlcleaner

import grails.plugins.Plugin
import org.jsoup.safety.Whitelist

class HtmlCleanerGrailsPlugin extends Plugin {
	def version = "3.0.0.1"
	def grailsVersion = "3.0.0 > *"
	def title = "Html Cleaner"
	def author = "Sudhir Nimavat"
	def authorEmail = "sudhir_nimavat@yahoo.com"
	def description = "whitelist based html cleaner based on jsoup"
    def developers = [ [ name: "Igor Shults", email: "igor.shults@gmail.com" ] ]

	def documentation = "http://snimavat.github.com/html-cleaner/guide/index.html"
	def license = 'APACHE'
	def issueManagement = [system: 'GitHub', url: 'https://github.com/snimavat/html-cleaner/issues']
	def scm = [system: 'GitHub', url: 'https://github.com/snimavat/html-cleaner']

	def observe = ['controllers']

	private final Map<String, Whitelist> whitelists = [:]

	@Override
	Closure doWithSpring() { { ->
		def application = grailsApplication
		buildWhitelists(application)
		HtmlCleaner.metaClass.getWhitelists = {
			return Collections.unmodifiableMap(whitelists)
		}

		def pluginConfig = application.config.htmlcleaner
		String defaultWhiteListStr = application.config.htmlcleaner?.defaultWhiteList

		//create a spring bean
		htmlCleaner(HtmlCleaner) {
			if(defaultWhiteListStr) {
				defaultWhiteList = defaultWhiteListStr
			}
		}
	}
	}


	@Override
	void onConfigChange(Map<String, Object> event) {
		buildWhitelists(event.application)
	}

	private void buildWhitelists(application) {
		WhitelistBuilder builder = new WhitelistBuilder()

		def pluginConfig = application.config.htmlcleaner
		def whitelistsClosure = pluginConfig?.whitelists

		//Clear all existing whitelists
		whitelists.clear()

		if(whitelistsClosure && whitelistsClosure instanceof Closure) {
			whitelists.putAll(builder.build(whitelistsClosure))
		}

		//Add default whitelists
		whitelists['none'] = Whitelist.none()
		whitelists['basic'] = Whitelist.basic()
		whitelists['simpleText'] = Whitelist.simpleText()
		whitelists['basicWithImages'] = Whitelist.basicWithImages()
		whitelists['relaxed'] = Whitelist.relaxed()
	}
}
