package org.mentawai.tag.i18n;

import javax.servlet.jsp.JspException;

import org.mentaregex.Regex;
import org.mentawai.tag.util.PrintTag;

public class RewriteWithLoc extends PrintTag {
	
	private String loc = null;
	
	public void setLoc(String loc) {
		this.loc = loc;
	}

	@Override
	public String getStringToPrint() throws JspException {
		
		String pageUrl = req.getRequestURI();
		String queryString = req.getQueryString();
		
		StringBuilder sb = new StringBuilder(128);
		sb.append(pageUrl);
		
		if (queryString == null || queryString.trim().length() == 0) {
			sb.append("?loc=").append(loc);
		} else {
			// remove the loc not to duplicate...
			queryString = Regex.sub(queryString, "s/loc\\=[a-z_]+//i");
			if (queryString.trim().length() == 0) {
				sb.append("?loc=").append(loc);
			} else {
				sb.append("?").append(queryString);
				sb.append("&loc=").append(loc);
			}
		}
		
		return sb.toString();
	}

}
