package org.mentawai.filter;

import javax.servlet.http.HttpServletRequest;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.RequestInput;
import org.mentawai.core.RestController;

public class RestRequestFilter implements Filter {
	
	public static final String REST_PARAMS_KEY = "restParams";

	@Override
    public String filter(InvocationChain chain) throws Exception {
	    Action a = chain.getAction();
	    Input input = a.getInput();
	    
	    if (input instanceof RequestInput) {
	    	
	    	RequestInput ri = (RequestInput) input;
	    	
	    	HttpServletRequest req = ri.getRequest();
	    	
	    	String uri = RestController.getRequestURI(req);
	    	
	    	String[] s = uri.split("\\/");
	    	
	    	if (s.length > 1) {
	    		
	    		String restParams = uri.substring(s[0].length() + 1, uri.length());
	    		
	    		input.setValue(REST_PARAMS_KEY, restParams);
	    	}
	    }
	    
	    return chain.invoke();
    }

	@Override
    public void destroy() {
	    
    }
}