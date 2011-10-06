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
package org.mentawai.tag.html.dyntag.buttonAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.mentawai.tag.html.dyntag.BaseTag;

/**
 * @author Alex Fortuna
 */
public class ButtonAction extends BaseTag {
	
	private static final long serialVersionUID = 1L;
	
	private String action = null;
	
	private boolean submit = true;
	
	/** Method to build tag 
	 * @throws JspException*/
    protected StringBuffer buildTag() throws JspException {
    	StringBuffer results = new StringBuffer("<input");
		prepareAttribute(results, "type","button");
		prepareAttribute(results, "name",getName());
		prepareAttribute(results, "id",getId());
		prepareAttribute(results, "class",getKlass());
		prepareAttribute(results, "style",getKlassStyle());
		if((getDisabled()!= null) && (getDisabled().equals("true"))){
		   results.append(" disabled=\"disabled\"");	
		}
		prepareAttribute(results, "title",getTitle());
		if((getKeyI18n() != null) && (!getKeyI18n().equals(""))){
		    prepareAttribute(results, "value",getI18nByKey(getKeyI18n(),isNoPrefix()));	
		}
		else{
			prepareAttribute(results, "value",getValue());
		}
		results.append(this.prepareEventHandlers());
		results.append(" />");
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
		this.prepareMouseEvents(handlers);
		//Set null avoid duplicated javascript 
		setOnclick(null);
		prepareKeyEvents(handlers);
		prepareFocusEvents(handlers);
		return handlers.toString();
	}
	
	
	protected void prepareMouseEvents(StringBuffer handlers) {
		if((getAction() != null) && (!getAction().equals(""))){
		    //Changed String 'onclick' for StringBuffer
			StringBuffer onclick = new StringBuffer("");
		    if(getOnclick() != null){
		    	onclick.append(getOnclick()+";");	
		    }
		    
		    // changed by robert gil
		    // this.form get the right parent form
		    //onclick.append("document.forms[0].action='" + getAction() + "';document.forms[0].submit()");
		    if(submit)
		    	onclick.append("this.form.action='" + getAction() + "';this.form.submit()");
		    else 
		    	onclick.append("window.location='" + getAction() + "'");
		    
			setOnclick(onclick.toString());
		}
        prepareAttribute(handlers, "onclick", getOnclick());
        prepareAttribute(handlers, "ondblclick", getOndblclick());
        prepareAttribute(handlers, "onmouseover", getOnmouseover());
        prepareAttribute(handlers, "onmouseout", getOnmouseout());
        prepareAttribute(handlers, "onmousemove", getOnmousemove());
        prepareAttribute(handlers, "onmousedown", getOnmousedown());
        prepareAttribute(handlers, "onmouseup", getOnmouseup());
    }
	
	
	public void setSubmit(boolean submit) {
		this.submit = submit;
	}
	
	public boolean getSubmit() {
		return submit;
	}

	/**
	 * @return Returns the action.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action The action to set.
	 */
	public void setAction(String action) {
		
		// inserted by robert gil
		if( !action.startsWith("/") ) action = "/" + action;
		
		String context = ((HttpServletRequest) pageContext.getRequest()).getContextPath();
		
		
		this.action = context + action;
	}
	
}
