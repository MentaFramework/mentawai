package org.mentawai.ajaxtag.responses;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

class IncludeWrapper extends HttpServletResponseWrapper {

	private StringWriter writer;

	public IncludeWrapper(HttpServletResponse response) {
		super(response);
		writer = new StringWriter();
	}

	public PrintWriter getWriter() {
		return new PrintWriter(writer);
	}

	public String toString() {
		return writer.toString();
	}
	
	/**
	 * @deprecated
	 */
	@Override
	public void setStatus(int sc, String status) {
		
		super.setStatus(sc, status);
	}
	
	/**
	 * @deprecated
	 */
	@Override
	public String encodeUrl(String url) {
		
		return super.encodeUrl(url);
	}
	
	/**
	 * @deprecated
	 */
	@Override
	public String encodeRedirectUrl(String url) {
		
		return super.encodeRedirectUrl(url);
	}

}
