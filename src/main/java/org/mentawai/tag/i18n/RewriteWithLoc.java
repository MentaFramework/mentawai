package org.mentawai.tag.i18n;

import javax.servlet.jsp.JspException;

import org.mentaregex.Regex;
import org.mentawai.i18n.LocaleManager;
import org.mentawai.tag.util.PrintTag;

public class RewriteWithLoc extends PrintTag {
	
	private static String PARAM = LocaleManager.LANG_PARAM;
	
	private String loc = null;
	
	public void setLoc(String loc) {
		this.loc = loc;
	}

	@Override
	public String getStringToPrint() throws JspException {
		
	    String requestUri =	(String) req.getAttribute("javax.servlet.forward.request_uri");
	    requestUri = (requestUri == null) ? req.getRequestURI() : requestUri;
		 
	    String queryString = (String) req.getAttribute("javax.servlet.forward.query_string");
		queryString = (queryString == null) ? req.getQueryString() : queryString;

		
		StringBuilder sb = new StringBuilder(128);
		sb.append(requestUri);
		
		if (queryString == null || queryString.trim().length() == 0) {
			sb.append("?").append(PARAM).append("=").append(loc);
		} else {
			// remove the loc not to duplicate...
			queryString = Regex.sub(queryString, "s/\\&?" + PARAM + "\\=[a-z_]+//i");
			if (queryString.trim().length() == 0) {
				sb.append("?").append(PARAM).append("=").append(loc);
			} else {
				sb.append("?").append(queryString);
				if (!queryString.endsWith("&")) {
					sb.append("&");
				}
				sb.append(PARAM).append("=").append(loc);
			}
		}
		
		return sb.toString();
	}

}
