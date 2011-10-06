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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.mentawai.tag.util.ConditionalTag;
import org.mentawai.tag.util.ListContext;

/**
 * @author Sergio Oliveira
 */
public class IsEmpty extends ConditionalTag {
	
	private String test = null;
	
	public void setTest(String test) {
		this.test = test;
	}
    
    public boolean testCondition() throws JspException {
    	
		Tag parent = findAncestorWithClass(this, ListContext.class);
		
        if (parent != null && test == null) {
        	
			ListContext tag = (ListContext) parent;
            List<Object> list = tag.getList();
			return list == null || list.size() == 0;
			
		} else {
			
			if (test != null) {
				
				Object value = Out.getValue(test, pageContext, false);
				
				if (value == null) return true; // should do this ???
				
				if (value instanceof Collection) {
					
					Collection col = (Collection) value;
					
					return col.isEmpty();
					
				} else if (value instanceof Map) {
					
					Map map = (Map) value;
					
					return map.isEmpty();
					
				} else if (value instanceof Object[]) {
					
					Object[] array = (Object[]) value;
					
					return array.length == 0;
					
				} else if (value instanceof String) {
					
					String s = (String) value;
					
					return s.trim().length() == 0;
					
				} else if (value instanceof Number) {
					
					Number num = (Number) value;
					
					return num.intValue() == 0;
				}
				
				throw new JspException("IsEmpty: Value " + test + " is not a Collection, Array, String, Number or a Map!");
				
			}
		}

		throw new JspException("IsEmpty: Could not find list context !!!");
	}
}

    
