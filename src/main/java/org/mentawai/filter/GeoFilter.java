package org.mentawai.filter;

import org.mentaregex.Regex;
import org.mentawai.core.Action;
import org.mentawai.core.Context;
import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;
import org.mentawai.util.IOUtils;

public class GeoFilter implements Filter {
	
	private static final String PARAM = "IP_COUNTRY";

	@Override
    public String filter(InvocationChain chain) throws Exception {
		
		Action action = chain.getAction();
		Context session = action.getSession();
		
		if (!session.hasAttribute(PARAM)) {
			Input input = action.getInput();
			String ip = input.getProperty("remoteAddr");
			String url = "http://api.easyjquery.com/ips/?ip=" + ip + "&full=true";
			String country = "?";
			try {
				String contents = IOUtils.readURL(url);
				String[] matches = Regex.match(contents, "/\\\"Country\\\"\\:\\\"(.+?)\\\"/");
				if (matches != null && matches.length == 1) {
					country = matches[0];
				}
			} catch(Throwable e) {
				// ignore...
			}
			
			session.setAttribute(PARAM, country);
		}
		
		return chain.invoke();
    }

	@Override
    public void destroy() {
	    
    }
}