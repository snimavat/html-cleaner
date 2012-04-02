import org.codehaus.groovy.grails.commons.ControllerArtefactHandler
import org.jsoup.safety.Whitelist;
import org.springframework.util.Assert;

import grails.plugin.htmlcleaner.HtmlCleaner;
import grails.plugin.htmlcleaner.WhitelistBuilder


class HtmlCleanerGrailsPlugin {
    def version = "0.1"
    def grailsVersion = "1.3.7 > *"
    def dependsOn = [:]
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]
    def author = "Sudhir Nimavat"
    def authorEmail = "sudhir_nimavat@yahoo.com"
    def title = "Html Cleaner"
    def description = "whitelist based html cleaner based on jsoup"
    def documentation = "http://snimavat.github.com/html-cleaner/guide/index.html"
	def license = 'APACHE'
	def scm = [system: 'GitHub', url: 'https://github.com/snimavat/html-cleaner']
	
	def observe = ['controllers']
	
	private final Map<String, Whitelist> whitelists = [:]
	
    def doWithWebDescriptor = { xml ->

    }

    def doWithSpring = {
		buildWhitelists(application)
		HtmlCleaner.class.metaClass.getWhitelists = {
			return Collections.unmodifiableMap(whitelists)
		}
		htmlCleaner(HtmlCleaner) 
    }

    def doWithDynamicMethods = { ctx ->
	
		//add cleanHtml method to all controllers
		application.controllerClasses.each { controllerClass ->
			addCleanHtmlMethod(controllerClass, application)
		}
		
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
		addCleanHtmlMethod(event.source, event.application)
		if (application.isControllerClass(event.source)) {

			def controllerClass = application.getControllerClass(event.source?.name)

			// If no GrailsClass can be found, i.e. 'controllerClass' is null, then this is a new controller.
			if (controllerClass == null) {
				controllerClass = application.addArtefact(ControllerArtefactHandler.TYPE, event.source)
			}
			
			addCleanHtmlMethod(controllerClass, application)
			return
		}
		return 
    }

    def onConfigChange = { event ->
		buildWhitelists(event.application)
		doWithDynamicMethods()
    }
	

	private void buildWhitelists(def application) {
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
	
	private String addCleanHtmlMethod(def clazz, def application) {
		HtmlCleaner cleaner = application.mainContext.getBean("htmlCleaner")
		Assert.notNull(cleaner, "HtmlCleaner spring bean should have been configured at this point")
		clazz.metaClass.cleanHtml = {String unsafe, String whitelist ->
			return cleaner.cleanHtml(unsafe, whitelist)
		}
	}
}
