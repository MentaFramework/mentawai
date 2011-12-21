/*
 * Mentawai Web Framework http://mentawai.lohis.com.br/
 * Copyright (C) 2005  Sergio Oliveira Jr. (sergio.oliveira.jr@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.mentawai.filter;

import javax.servlet.http.HttpServletResponse;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.SessionContext;

/**
 * Filter class CharacterEncodingFilter can change charset and <br> 
 * content type system wide.
 * 
 * @author helio frota
 *
 */
public class CharacterEncodingFilter implements Filter {
	
	/**
	 * Attribute constant UTF_8 of CharacterEncodingFilter.
	 */
	public static final String UTF_8 = "UTF-8";
	/**
	 * Attribute constant ISO_8859_1 of CharacterEncodingFilter.
	 */
	public static final String ISO_8859_1 = "ISO-8859-1";
	/**
	 * Attribute constant TEXT_PLAIN of CharacterEncodingFilter.
	 */
	public static final String TEXT_PLAIN = "text/plain";
	/**
	 * Attribute constant TEXT_HTML of CharacterEncodingFilter.
	 */
	public static final String TEXT_HTML = "text/html";
	/**
	 * Attribute constant TEXT_CSS of CharacterEncodingFilter.
	 */
	public static final String TEXT_CSS = "text/css";
	
	/**
	 * Attribute charSet of CharacterEncodingFilter.
	 */
	private String charSet = "UTF-8";
	/**
	 * Attribute contentType of CharacterEncodingFilter.
	 */
	private String contentType = "text/html";
	
	/**
	 * Default constructor.
	 */
	public CharacterEncodingFilter() {
		
	}
	
	/**
	 * Parametric constructor.
	 * @param charSet String
	 */
    public CharacterEncodingFilter(String charSet) {
		this.charSet = charSet;
	}
    
    /**
	 * Parametric constructor.
	 * @param charSet String
	 */
    public CharacterEncodingFilter(String charSet, String contentType) {
		this.charSet = charSet;
		this.contentType = contentType;
	}
    
	/**
	 * Default filter method.
	 * @param InvocationChain chain
	 */
	public String filter(InvocationChain chain) throws Exception {

		Action action = chain.getAction();
		
		HttpServletResponse httpServletResponse = ((SessionContext) action.getSession()).getResponse();
		
		httpServletResponse.setContentType(contentType + "; charset=" + charSet);
		
		httpServletResponse.setCharacterEncoding(charSet);
		
		return chain.invoke();
	}

	/**
	 * Default destroy method.
	 */
	public void destroy() {

	}
	
}
