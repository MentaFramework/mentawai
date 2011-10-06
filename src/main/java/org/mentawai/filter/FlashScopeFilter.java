package org.mentawai.filter;

import java.util.Iterator;

import org.mentawai.core.Action;
import org.mentawai.core.Context;
import org.mentawai.core.Filter;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.Output;

public class FlashScopeFilter implements Filter {
	
	public static final String KEY = "_flash";
	
	private static final String _KEY = KEY + ".";
	
	public String filter(InvocationChain chain) throws Exception {
		
		// take from session, clear session and add to output...
		
		Action action = chain.getAction();
		
		Context session = action.getSession();
		
		Output output = action.getOutput();
		
		if (isValid(session)) {
		
    		Iterator<String> iter = session.keys();
    		
    		while(iter.hasNext()) {
    			
    			String key = iter.next();
    			
    			if (key.startsWith(_KEY)) {
    				
    				Object value = session.getAttribute(key);
    				
    				session.removeAttribute(key);
    				
    				String[] s = key.split("\\.");
    				
    				if (s.length == 2 && s[1].length() > 0) {
    					
    					output.setValue(s[1], value);
    					
    				}
    			}
    		}
		}
		
		String result = chain.invoke();
		
		// save in session any flash
		
		session = action.getSession();
		
		if (isValid(session)) {
		
    		Iterator<String> iter = output.keys();
    		
    		while(iter.hasNext()) {
    			
    			String key = iter.next();
    			
    			if (key.startsWith(_KEY)) {
    				
    				Object value = output.getValue(key);
    				
    				session.setAttribute(key, value);
    			}
    		}
		}

		return result;
	}
	
	private boolean isValid(Context session) {
		
		try {
			
			session.getAttribute("dummy");
			
			return true;
			
		} catch(IllegalStateException e) {
			
			return false;
		}
	}
	
	public void destroy() { }
}