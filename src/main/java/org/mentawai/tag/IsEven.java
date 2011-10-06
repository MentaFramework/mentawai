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
package org.mentawai.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.mentawai.tag.util.ConditionalTag;
import org.mentawai.tag.util.Context;

/**
 * @author Sergio Oliveira
 */
public class IsEven extends ConditionalTag {
    
    private String test = null;
    
    public void setTest(String test) {
    	
        this.test = test;
        
    }
    
    private int getNumber(String s) throws JspException {
    	
    	try {
    		
    		return Integer.parseInt(s);
    		
    	} catch(NumberFormatException e) {
    		
    		throw new JspException("Cannot convert to number inside isEven tag: " + s);
    	}
    }
    
    public boolean testCondition() throws JspException {
        
		if (test != null) {
			
            Object obj = Out.getValue(test, pageContext, false);
            
            if (obj == null) throw new JspException("Cannot find value for isEven tag: " + test);
            
            return getNumber(obj.toString()) % 2 == 0;
			
		}
		
		Tag parent = findAncestorWithClass(this, Context.class);		
        
        if (parent == null) {
        	
        	// value was null and I cannot find a context for tag!
        	
        	throw new JspException("IsEven: Could not find context!");
        	
        }
            
		Context tag = (Context) parent;
		
		Object obj = tag.getObject();
		
		if (obj == null) throw new JspException("IsEven: context returned null!");
		
		return getNumber(obj.toString()) % 2 == 0;
            
	}
}

    