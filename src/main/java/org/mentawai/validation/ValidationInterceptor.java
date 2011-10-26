package org.mentawai.validation;

public interface ValidationInterceptor {
	
	public boolean beforeValidation(String method);
	
	public void afterValidation(String method, boolean wasOk);
	
}