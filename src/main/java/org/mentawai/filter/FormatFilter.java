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
import java.util.Locale;
import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.FilterException;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.Output;
import org.mentawai.formatter.Formatter;
import org.mentawai.formatter.FormatterManager;

/**
 * A filter to format the action output values.
 * 
 * Use this filter if you want to format some of your output values before displaying them in the view.
 * 
 * @author Sergio Oliveira
 */
public abstract class FormatFilter implements Filter {
	
	private Map<String, Object> formatters = new HashMap<String, Object>();
	
	private boolean isLoaded = false;
	
	public FormatFilter() {
	}
	
	/*
	 * Lazy initialization of converters! 
	 */
	private Map<String, Object> getFormatters() {
		
		if (isLoaded) return formatters;
		
		synchronized(this) {
			
			if (!isLoaded) {
			
				initFormatters();
			
				isLoaded = true;
				
			}
		}
		
		return formatters;
	}
	
    /**
     * Override this abstract method to add formatters for the output fields you want to format.
     */
	public abstract void initFormatters();
	
    /**
     * Adds a formatter for the given field.
     *
     * @param field The field to format.
     * @param formatter The formatter to use.
     * @return This FormatFilter object (<i>this</i>) so you can chain methods
     */
	public FormatFilter add(String field, Formatter formatter) {
		formatters.put(field, formatter);
		return this;
	}
    
    /**
     * Adds a formatter which was defined in the FormatterManager.
     * 
     * @param field
     * @param formatterName The name of the formatter defined in the FormatterManager.
     * @return This FormatFilter object (<i>this</i>) so you can chain methods
     */
	public FormatFilter add(String field, String formatterName) {
        formatters.put(field, formatterName);
        return this;
    }
    
	public String filter(InvocationChain chain) throws Exception {
		
		String result = chain.invoke();
		
		Action action = chain.getAction();
		
		Locale loc = action.getLocale();
		
		Output output = action.getOutput();
		
		Map<String, Object> formatters = getFormatters();
		
		Iterator<String> iter = formatters.keySet().iterator();
		
		while(iter.hasNext()) {
			
			String field = iter.next();
			
			Object toFormat = output.getValue(field);
			
			if (toFormat == null) continue;
            
            Object o = formatters.get(field);
            
            Formatter f = null;
            
            if (o instanceof String) {
                
                String s = (String) o;
                
                f = FormatterManager.getFormatter(s);
                
                if (f == null) throw new FilterException("Cannot find a formatter with this name: " + s);
                
            } else if (o instanceof Formatter) {
                
                f = (Formatter) o;
            
            }
			
			String formatted = f.format(toFormat, loc);
			
			output.setValue(field, formatted);
		}
		
		return result;
	}
    
    public void destroy() { }
}
		