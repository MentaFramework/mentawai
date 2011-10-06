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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.mentawai.converter.ConversionException;
import org.mentawai.converter.Converter;
import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.FilterException;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;

/**
 * @author Sergio Oliveira
 */
public abstract class ConversionFilter implements Filter {
	
	private boolean restore = true;
	
	public ConversionFilter() {
	}
	
	public ConversionFilter(boolean restore) {
		this.restore = restore;
	}
	
	/**
	 * Should this filter restore the old values after the action has executed?
	 * 
	 * @param restore true to restore the values.
	 */
	public void setRestore(boolean restore) {
		this.restore = restore;
	}
	
    /**
     * Override this abstract method to add converters for the fields you want to convert.
     */
	public abstract void prepareConverters(Map<String, Converter> converters, Action action, String innerAction);
	
	private Map<String, Converter> getConverters(Action action, String innerAction) {
		
		Map<String, Converter> converters = new HashMap<String, Converter>();
		
		prepareConverters(converters, action, innerAction);
		
		return converters;
	}
	
	public String filter(InvocationChain chain) throws Exception {
		
		Action action = chain.getAction();
		
		Input input = action.getInput();
		
		Map<String, Object> initialValues = null;
		
		if (restore) {
			initialValues = new HashMap<String, Object>();
		}
		
		Map<String, Converter> converters = getConverters(action, chain.getInnerAction());
		
		try {
			
			Iterator<String> iter = converters.keySet().iterator();
			
			while(iter.hasNext()) {
				
				String field = iter.next();
				
				Converter c = converters.get(field);
				
				Object oldValue = input.getValue(field);
				
				Object converted = c.convert(field, action);
				
				input.setValue(field, converted);
				
				if (restore) initialValues.put(field, oldValue);
			}
			
		} catch(ConversionException e) {
			
			e.printStackTrace();
			
			throw new FilterException(e);
		}
		
		try {
			
			return chain.invoke();
			
		} catch(Exception e) {
			
			throw e;
			
		} finally {
			
			if (restore) {
				
				Iterator<String> iter = initialValues.keySet().iterator();
				
				while(iter.hasNext()) {
					
					String field = iter.next();
					
					Object value = initialValues.get(field);
					
					input.setValue(field, value);
					
				}
			}
		}
	}
    
    public void destroy() { }
}
		