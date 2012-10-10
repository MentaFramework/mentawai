package org.mentawai.filter;

import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;

/**
 * Based on my discussion with Ricardo Wolosker about parameter injection security, when updating beans.
 * 
 * It will remove from the input parameters that we do not want to allow to be updated. For example: id, active, etc.
 * 
 * @author Sergio Oliveira Jr.
 */
public class BlacklistParamFilter implements Filter {
	
	private final String[] blacklist;
	
	public BlacklistParamFilter(String ... blacklist) {
		this.blacklist = blacklist;
	}

	@Override
    public String filter(InvocationChain chain) throws Exception {
		Input input = chain.getAction().getInput();
		for(String param : blacklist) {
			if (input.has(param)) {
				input.removeValue(param);
			}
		}
		return chain.invoke();
    }

	@Override
    public void destroy() {
		// NOOP
    }
	
	
}
