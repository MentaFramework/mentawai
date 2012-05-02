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

import java.util.Iterator;
import java.util.List;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.FilterException;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.PojoAction;
import org.mentawai.jruby.JRubyInterpreter;
import org.mentawai.jruby.RubyAction;
import org.mentawai.util.InjectionUtils;

/**
 * This filter will inject input values into the action or model.
 *
 * @author Sergio Oliveira
 */
public class InjectionFilter implements Filter {
    
    private boolean tryField = true;
    private boolean convert = true;
    private boolean convertNullToFalse = false;
    
    /**
     * Creates a InjectionFilter that can be used by any action class.
     * You may use this filter per action or as a global filter.
     */
    public InjectionFilter() { }
    
    /**
     * Creates an InjectionFilter that can be used by any action class.
     * You may use this filter per action or as a global filter.
     * If tryField is true and it cannot find a setter for the input value,
     * it will try to directly access the attribute, even if it is a private field.
     *
     * @param tryField A flag indicating whether this filter should try to access private attributes.
     */    
    public InjectionFilter(boolean tryField) {
        this.tryField = tryField;
    }
    
    /**
     * Creates an InjectionFilter that can be used by any action class.
     * You may use this filter per action or as a global filter.
     * If tryField is true and it cannot find a setter for the input value,
     * it will try to directly access the attribute, even if it is a private field.
     * If convert flag is true (default), it will try to automatically convert.
     *
     * @param tryField A flag indicating whether this filter should try to access private attributes.
     */    
    public InjectionFilter(boolean tryField, boolean convert) {
        this(tryField);
        this.convert = convert;
    }  
    
    /**
     * Force NULL values to be converted to FALSE booleans (only when type is Boolean or boolean!)
     * 
     * @param tryField
     * @param convert
     * @param convertNullToFalse
     * @since 1.11
     */
    public InjectionFilter(boolean tryField, boolean convert, boolean convertNullToFalse) {
    	this(tryField, convert);
    	this.convertNullToFalse = convertNullToFalse;
    }
    
    protected Object getTarget(Action action) throws FilterException {
       
       Object pojo = null;
       
       if (action instanceof PojoAction) {
          
          PojoAction pa = (PojoAction) action;
          
          pojo = pa.getPojo();
          
       }
       
       Object actionImpl = pojo != null ? pojo : action;
        
        if (actionImpl instanceof ModelDriven) {
            
            ModelDriven md = (ModelDriven) action;
            
            Object model = md.getModel();
            
            if (model == null) throw new FilterException("ModelDriven action cannot return a null model!");
            
            return model;
            
        }
        
        return actionImpl;
    }
    
    public String filter(InvocationChain chain) throws Exception {
    	
    	Action action = chain.getAction();
    	
    	if (action instanceof RubyAction) {
    		
    		JRubyInterpreter ruby = JRubyInterpreter.getInstance();
    		
    		Input input = action.getInput();
    		
    		Object rubyObject = ((RubyAction) action).getRubyObject();
    		
    		List<String> setters = ruby.getSetters(rubyObject);
    		
    		Iterator<String> iter = setters.iterator();
    		
    		while(iter.hasNext()) {
    			
    			String m = iter.next();
    			
    			String m_without = m.substring(0, m.length() - 1); // cut '='
    			
    			Object value = input.getValue(m_without);
    			
    			if (value != null) ruby.call(rubyObject, m, value);
    		}
    		
    		
    	} else {
      
	    	Object target = getTarget(action);
	    	
	    	InjectionUtils.getObject(target, action.getInput(), action.getLocale(), tryField, null, convert, convertNullToFalse, true);
    	}
    	
    	return chain.invoke();
    }
    
    public void destroy() { }
}
        