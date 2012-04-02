package grails.plugin.htmlcleaner

import java.util.Map;

import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist

public class HtmlCleaner {
	
	public htmlCleaner() { }
		
	public cleanHtml(String unsafe, String whitelist) {
		if(!unsafe) { return unsafe }
				
		if(!whitelists[whitelist]) {
			throw new RuntimeException("Whitelist [${whitelist}] is not defined")
		}
		return Jsoup.clean(unsafe, whitelists[whitelist])
	}
	
}
