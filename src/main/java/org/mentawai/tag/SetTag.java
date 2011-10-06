package org.mentawai.tag;

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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Sergio Oliveira
 */
public class SetTag extends TagSupport {
	
	private String var;
	private String value;
	
	public void setVar(String var) { this.var = var; }
	public void setValue(String value) { this.value = value; }
    
    public int doStartTag() throws JspException {

    	pageContext.setAttribute(var, value);
    	
        return super.doStartTag();
    }

    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}

    
