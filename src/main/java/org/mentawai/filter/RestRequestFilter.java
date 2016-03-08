package org.mentawai.filter;

import java.io.BufferedReader;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.FilterException;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.RequestInput;
import org.mentawai.core.RestController;
import org.mentawai.util.JsonUtils;

public class RestRequestFilter implements Filter {
	
	public static final String REST_PARAMS_KEY = "restParams";
	public static final String REST_MAP_KEY = "restMap";
	public static final String REST_METHOD_KEY = "restMethod";

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
	    	
	    	String method = req.getMethod();
	    	
	    	if (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT")) {
	    		
	    		StringBuilder sb = new StringBuilder();
	    	    BufferedReader reader = req.getReader();
	    	    try {
	    	        String line;
	    	        while ((line = reader.readLine()) != null) {
	    	            sb.append(line).append('\n');
	    	        }
	    	    } finally {
	    	        reader.close();
	    	    }
	    	    
	    	    String contentType = req.getHeader("Content-Type");
	    	    
	    	    if (contentType == null) {
	    	    	contentType = req.getHeader("Accept");
	    	    }
	    	    
	    	    if (contentType == null) {
	    	    	input.setValue(REST_MAP_KEY, sb.toString());
	    	    } else if (contentType.toLowerCase().contains("json")) {
	    	    	Map<String, Object> map = JsonUtils.toMap(sb.toString());
	    	    	input.setValue(REST_MAP_KEY, map);
	    	    } else if (contentType.toLowerCase().contains("xml")) {
	    	    	throw new FilterException("XML is not supported yet!");
	    	    } else {
	    	    	throw new FilterException("Don't know how to accept this: " + contentType);
	    	    }
	    	}
	    	
	    	input.setValue(REST_METHOD_KEY, method.toUpperCase());
	    }
	    
	    return chain.invoke();
    }

	@Override
    public void destroy() {
	    
    }
}