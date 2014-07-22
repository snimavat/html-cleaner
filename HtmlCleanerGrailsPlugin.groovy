import grails.plugin.htmlcleaner.HtmlCleaner
import grails.plugin.htmlcleaner.WhitelistBuilder

import org.jsoup.safety.Whitelist
import org.springframework.util.Assert

class HtmlCleanerGrailsPlugin {
	def version = "0.2"
	def grailsVersion = "1.3.7 > *"
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

	def doWithSpring = {
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

	def doWithDynamicMethods = { ctx ->
		//add cleanHtml method to all controllers
		application.controllerClasses.each { controllerClass ->
			addCleanHtmlMethod(controllerClass, application)
		}
	}

	def onChange = { event ->
		if (!(event.source instanceof Class)) {
			return
		}

		if (application.isControllerClass(event.source)) {
			def controllerClass = application.getControllerClass(event.source.name)
			if (controllerClass) {
				addCleanHtmlMethod(controllerClass, event.application)
			}
		}
	}

	def onConfigChange = { event ->
		buildWhitelists(event.application)
		doWithDynamicMethods()
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

	private String addCleanHtmlMethod(clazz, application) {
		HtmlCleaner cleaner = application.mainContext.getBean("htmlCleaner")
		Assert.notNull(cleaner, "HtmlCleaner spring bean should have been configured at this point")

		clazz.metaClass.cleanHtml = {String unsafe, String whitelist ->
			return cleaner.cleanHtml(unsafe, whitelist)
		}

		clazz.metaClass.cleanHtml = {String unsafe ->
			return cleaner.cleanHtml(unsafe)
		}
	}
}
