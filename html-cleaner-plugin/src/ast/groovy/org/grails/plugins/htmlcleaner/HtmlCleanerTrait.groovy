package org.grails.plugins.htmlcleaner

import grails.artefact.Enhances
import groovy.transform.CompileStatic
import org.grails.core.artefact.ControllerArtefactHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier

import javax.persistence.Transient


@CompileStatic
@Enhances([ControllerArtefactHandler.TYPE])
trait HtmlCleanerTrait {

	@Transient
	private HtmlCleaner cleaner


	String cleanHtml(String unsafe, String whitelist) {
		return cleaner.cleanHtml(unsafe, whitelist)
	}

	String cleanHtml(String unsafe) {
		return cleaner.cleanHtml(unsafe)
	}

	@Autowired
	@Qualifier("htmlCleaner")
	public void setHtmlCleaner(HtmlCleaner cleaner) {
		this.cleaner = cleaner
	}

}
