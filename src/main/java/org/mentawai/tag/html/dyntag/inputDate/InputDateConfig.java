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
package org.mentawai.tag.html.dyntag.inputDate;

import javax.servlet.jsp.JspException;

import org.mentawai.core.Controller;
import org.mentawai.tag.html.dyntag.BaseConfig;
import org.mentawai.tag.html.dyntag.inputDate.listener.InputDateListener;

public class InputDateConfig extends BaseConfig {
	
	private static final long serialVersionUID = 1L;
	
	private String lang = "br";
		
	/** Method to build tag */	
    protected StringBuffer buildTag() {
    	StringBuffer results = new StringBuffer();
    	
    	if (InputDateListener.LIST_PATH_FILES == null) {
    		
    		// listener failed... force the load...
    		InputDateListener listener = new InputDateListener();
    		listener.contextInitialized(Controller.getApplication());
    	}
    	
    	String[] scripts = buldImportJsFile(InputDateListener.LIST_PATH_FILES).toString().split("\n");
    	for (String s : scripts) {
    		if (s.indexOf("br.js") > 0 || s.indexOf("en.js") > 0 
    				|| s.indexOf("de.js") > 0 || s.indexOf("es.js") > 0) {
    			
    			if (s.indexOf(getLang() + ".js") > 0) {
    				results.append(s);
    			}
    			
    		} else {
    			results.append(s);
    		}
    	}
    	
    	results.append(buldImportCssFile(InputDateListener.LIST_PATH_FILES).toString());
    	return results;  
	}
	
	public String getStringToPrint() throws JspException {
		StringBuffer results = new StringBuffer(200);
		results.append(buildTag().toString());
		return results.toString();
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
}