package org.mentawai.cript;

public class DecriptException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DecriptException(String v) {
		super(String.format("The value \"%s\"cannot be decripted.", v ));
	}
	
}
