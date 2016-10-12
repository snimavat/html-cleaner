if (System.getenv('TRAVIS')) {
	grails.project.repos.grailsCentral.username = System.getenv('GRAILS_CENTRAL_USERNAME')
	grails.project.repos.grailsCentral.password = System.getenv('GRAILS_CENTRAL_PASSWORD')
}

grails.project.work.dir = '.grails'
grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()
		mavenLocal()
		mavenCentral()
	}

	dependencies {
		compile 'org.jsoup:jsoup:1.9.2'
	}

	plugins {
		build(":release:3.1.2", ":rest-client-builder:2.1.1") { export = false } 
	}
}
