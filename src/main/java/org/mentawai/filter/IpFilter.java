package org.mentawai.filter;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;

public class IpFilter implements Filter {
	
	private final String[] ips;
	private String blockedResult = Action.BLOCKED;
	
	public IpFilter(String ... ips) {
		if (ips == null || ips.length == 0) throw new IllegalArgumentException("IpFilter must have at least one IP!");
		this.ips = ips;
	}
	
	public void setBlockedResult(String res) {
		this.blockedResult = res;
	}
	
	public void destroy() { }
	
	public String filter(InvocationChain chain) throws Exception {
		
		Action a = chain.getAction();
		
		Input in = a.getInput();
		
		 String ip = in.getProperty("remoteAddr");
		 
		 boolean allow = false;
		 
		 for(int i=0;i<ips.length;i++) {
			 if (ip.equals(ips[i])) {
				 allow = true;
				 break;
			 }
		 }
		 
		 if (!allow) return blockedResult;
		 
		 return chain.invoke();
	}
	
}