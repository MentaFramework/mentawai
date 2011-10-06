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

import org.mentawai.tag.util.AbstractContext;
import org.mentawai.tag.util.Context;

/**
 * @author Sergio Oliveira
 */
public class OutBean extends AbstractContext {
	
	private String value;
	
	public void setValue(String value) {
		this.value = value;
	}
    
    protected String getName() {
        return value;
    }
	
	public Object getObject() throws JspException {
		
        Tag parent = findAncestorWithClass(this, Context.class);
        
        if (parent != null) {
        	
            Context ctx = (Context) parent;
            
            Object obj = ctx.getObject();
            
            if (obj != null) {
            	
                Object object = Out.getValue(obj, value, false);
                
                if (object != null) {
                    return object;
                }
            }
        }
        
        /*
		if (action != null) {
			Output output = action.getOutput();
			return output.getValue(value);
		}
		*/
        
        return Out.getValue(value, pageContext, false);
        
		//throw new JspException("OutBean: No value can be found for " + value);
	}
	
}

		
		
		
		