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
package org.mentawai.tag.html.dyntag.label;

import javax.servlet.jsp.JspException;

import org.mentawai.tag.html.dyntag.BaseTag;

public class Label extends BaseTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String forr = null;
	
	/** Method to build tag 
	 * @throws JspException */
    protected StringBuffer buildTag() throws JspException {
    	StringBuffer results = new StringBuffer("<label");
		prepareAttribute(results, "for",getForr());
		prepareAttribute(results, "id",getId());
		prepareAttribute(results, "class",getKlass());
		prepareAttribute(results, "style","text-align:"+getTextAlign()+";"+getKlassStyle());
		results.append(this.prepareEventHandlers());	
		results.append(">");
		if((getKeyI18n()!= null) && (!getKeyI18n().equals(""))){
			results.append(getI18nByKey(getKeyI18n(),isNoPrefix()));	
		}
		else{
			results.append(getValue());
		}
		results.append("</label>");
		return results;
	}
    
    public String getStringToPrint() throws JspException {
		StringBuffer results = new StringBuffer(200);
		results.append(buildTag().toString());		
		return results.toString();
	}
    
    /** Method to prepare events JavaScript in compoenent */
	protected String prepareEventHandlers() {
		StringBuffer handlers = new StringBuffer();
		prepareMouseEvents(handlers);
		prepareKeyEvents(handlers);
		prepareFocusEvents(handlers);
		return handlers.toString();
	}

	/**
	 * @return Returns the forr.
	 */
	public String getForr() {
		return forr;
	}

	/**
	 * @param forr The forr to set.
	 */
	public void setForr(String forr) {
		this.forr = forr;
	}
	
}
