package org.mentawai.ioc;

import org.mentacontainer.Factory;

public class WrappedMentaComponent implements Factory {
	
	private Bean bean;
	
	public WrappedMentaComponent(Bean bean) {
		
		this.bean = bean;
	}
	
	public <T> T getInstance() {
		
		try {
		
			T a = (T) bean.getBean();
			
			return a;
			
		} catch(Exception e) {
			
			throw new RuntimeException(e);
		}
	}
	
	public Class<? extends Object> getType() {
		
		return bean.getType();
	}
}