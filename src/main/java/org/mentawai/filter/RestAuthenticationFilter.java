package org.mentawai.filter;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.FilterException;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;
import org.mentawai.util.StringUtils;

public abstract class RestAuthenticationFilter implements Filter {
	
	@Override
    public String filter(InvocationChain chain) throws Exception {
		
		Action action = chain.getAction();
		
		if (action instanceof AuthenticationFree) {
			return chain.invoke();
		}
		
		Input input = action.getInput();
		
		String a = input.getHeader("Authorization");
		
		if (a == null || a.equals("")) return Action.LOGIN;
		
		if (!a.startsWith("Basic ")) {
			throw new FilterException("Authentication method not supported: " + a);
		}
		
		a = a.substring("Basic ".length());
		
		String s = StringUtils.decodeBase64(a);
		
		String[] t = s.split("\\:");
		
		String username, password;
		
		if (t.length == 1) {
			throw new FilterException("Bad basic authentication value: " + a);
		} else if (t.length == 2) {
			username = t[0];
			password = t[1];
		} else {
			// password has ':'
			username = t[0];
			password = "";
			for(int i = 1; i < t.length; i++) {
				if (i != 1) password += ":";
				password += t[i];
			}
		}
		
		Object sessionUser = authenticate(username, password);
		
		if (sessionUser == null) return Action.LOGIN;
		
		action.getSession().setAttribute("sessionUser", sessionUser);
		
		return chain.invoke();
    }
	
	protected abstract Object authenticate(String username, String password);
		
	@Override
    public void destroy() {
	    
    }
}