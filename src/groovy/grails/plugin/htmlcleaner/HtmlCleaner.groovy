package grails.plugin.htmlcleaner

import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import org.springframework.util.Assert

public class HtmlCleaner {
	
	private String defaultWhiteList
	
	
	public HtmlCleaner() { }
		
	public String getDefaultWhiteList() {
		return defaultWhiteList
	}
	
	public void setDefaultWhiteList(String str) {
		Assert.hasText(str, "Whitelist name must not be empty")
		defaultWhiteList= str
	}
	
	public cleanHtml(String unsafe, String whitelist) {
		if(!unsafe) { return unsafe }
		Assert.hasText(whitelist, "Whitelist name must not be empty")
		
		if(!whitelists[whitelist]) {
			throw new RuntimeException("Whitelist [${whitelist}] is not defined")
		}
		return Jsoup.clean(unsafe, whitelists[whitelist])
	}

	/**
	 * cleanHtml based on defaultWhitelist
	 * 
	 * @param unsafe
	 * @return
	 */
	public cleanHtml(String unsafe) {
		if(!unsafe) { return unsafe }
		
		if(!defaultWhiteList) {
			throw new RuntimeException("Default Whitelist is not specified in configuration")
		}
	
		return cleanHtml(unsafe, defaultWhiteList)
	}

		
}
