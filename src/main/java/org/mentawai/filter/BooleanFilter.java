package org.mentawai.filter;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;

public class BooleanFilter implements Filter {
	
	private final String[] fields;
	
	public BooleanFilter(String ... fields) {
		this.fields = fields;
	}
	
	public void destroy() { }
	
	public String filter(InvocationChain chain) throws Exception {
		
		Action action = chain.getAction();
		
		Input input = action.getInput();
		
		String method = input.getProperty("method");
		
		boolean isPost = method != null && method.equalsIgnoreCase("POST");
		
		if (isPost) {
		
			for(String field: fields) {
				
				if (input.hasValue(field)) {
					
					input.setValue(field, true);
					
				} else {
					
					input.setValue(field, false);
				}
				
			}
		}
		
		return chain.invoke();
	}
	
}