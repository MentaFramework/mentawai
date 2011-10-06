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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.FilterException;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.Output;
import org.mentawai.filter.ValidatorFilter.ActionUniqueId;
import org.mentawai.formatter.Formattable;
import org.mentawai.formatter.Formatter;
import org.mentawai.formatter.FormatterManager;

/**
 * A filter to format the action output values.
 *
 * This filter has the exact same behaviour of the FormatFilter except that
 * it will get the formatters from the action if the action implements the Formattable interface.
 * 
 * @author Sergio Oliveira
 */
public class FormatterFilter implements Filter {
	
	private Map<ActionUniqueId, Map> cache = Collections.synchronizedMap(new HashMap<ActionUniqueId, Map>());
	
    /**
     * Creates a new FormatterFilter.
     */
	public FormatterFilter() {
        
	}
	
	public String filter(InvocationChain chain) throws Exception {
		
		String result = chain.invoke();
		
		Action action = chain.getAction();
      
      Object pojo = chain.getPojo();
      
      Object actionImpl = pojo != null ? pojo : action;
		
		Locale loc = action.getLocale();
		
		Output output = action.getOutput();
        
        if (!(actionImpl instanceof Formattable)) {
            return result;
        }
        
		ActionUniqueId actionId = new ActionUniqueId(actionImpl.getClass().getName(), chain.getInnerAction());
		
        Map formatters = cache.get(actionId);
        
        if (formatters == null) {
        	
        	synchronized(this) {
        	
        		if (formatters == null) {
            
		            formatters = new HashMap();
		        
		        	Formattable formattable = (Formattable) actionImpl;
		        
		        	formattable.initFormatters(formatters, chain.getInnerAction());
		        	
		        	cache.put(actionId, formatters);
		        	
        		}
        	}
        }
		
		Iterator iter = formatters.keySet().iterator();
		
		while(iter.hasNext()) {
			
			String field = (String) iter.next();
			
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
                
            } else {
                
                throw new FilterException("Formatter must be a String or a Formatter!");
            }
			
			String formatted = f.format(toFormat, loc);
			
			output.setValue(field, formatted);
		}
		
		return result;
	}
    
    public void destroy() { }
    
}
		