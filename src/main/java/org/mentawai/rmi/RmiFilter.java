package org.mentawai.rmi;

import java.io.IOException;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.InputWrapper;
import org.mentawai.core.InvocationChain;

public class RmiFilter extends InputWrapper implements Filter {

	@Override
    public String filter(InvocationChain chain) throws Exception {
		
		Action a = chain.getAction();

		setInput(a.getInput());

		a.setInput(this);

		return chain.invoke();
	}
	
	public Object getValue(String key) {
		
		if (key.startsWith("p") && key.replaceAll("\\d", "").equals("p")) {

			String hex = super.getString(key);
			
			try {
			
				return RmiClient.decode(hex);
				
			} catch(IOException e) {
				
				throw new org.mentawai.util.RuntimeException("Cannot decode parameter: " + hex, e);
			}
			
		} else {
			return super.getValue(key);
		}
	}

	@Override
    public void destroy() {
	    
    }
}