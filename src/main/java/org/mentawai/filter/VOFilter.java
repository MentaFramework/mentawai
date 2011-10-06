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

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.FilterException;
import org.mentawai.core.InvocationChain;
import org.mentawai.util.InjectionUtils;

/**
 * @author Sergio Oliveira
 */
public class VOFilter implements Filter {
	
	public static char PREFIX_SEPARATOR = '.';
    
    private final Class targetClass;
    private String objectKey = null;
    private String prefix = null;
    private boolean tryField = true;
    private boolean convert = true;
    private boolean convertNullToFalse = true;
    
    public VOFilter(Class klass) {
        
        this.targetClass = klass;
        
    }
    
     public VOFilter(Class klass, boolean tryField) {
         
        this(klass);
        
        this.tryField = tryField;
    }
    
    
    public VOFilter(Class klass, String key) {
        
        this(klass);
        
        this.objectKey = key;
    }
    
    public VOFilter(String key, Class klass) {
    	
    	this(klass, key);
    	
    }
    
    public VOFilter(Class klass, String key, boolean tryField) {
        
        this(klass, key);
        
        this.tryField = tryField;
    }

    public VOFilter(Class klass, String key, boolean tryField, String prefix) {

        this(klass, key, tryField);
        
        this.prefix = prefix;

    }
    
    public VOFilter(String key, Class klass, String prefix) {
    	
    	this(klass, key, true, prefix);
    	
    }
    
    public VOFilter(Class klass, String key, boolean tryField, boolean convert, String prefix) {
        
        this(klass, key, tryField, prefix);
        
        this.convert = convert;
        
        
    }   
    
    public void setConvertNullToFalse(boolean convertNullToFalse) {
    	
    	this.convertNullToFalse = convertNullToFalse;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer(128);
        sb.append("VOFilter: Class=").append(targetClass.getName()).append(" Key=").append(objectKey != null ? objectKey : targetClass.getName());
        return sb.toString();
    }
    
    private Object getTarget() throws FilterException {
        
        try {
            
            return targetClass.newInstance();
            
        } catch(Exception e) {
            
            e.printStackTrace();
            
            throw new FilterException(e);
        }        
    }
    
    public String filter(InvocationChain chain) throws Exception {
    	
    	Action action = chain.getAction();
    	
    	Object target = getTarget();
    	
    	InjectionUtils.getObject(target, action.getInput(), action.getLocale(), tryField, prefix, convert, convertNullToFalse, false);
    	
    	setTarget(action, target);
    	
    	return chain.invoke();
    }
    
    
    private void setTarget(Action action, Object target) {
        
        if (objectKey != null) {
            
            action.getInput().setValue(objectKey, target);
            
        } else {
            
            action.getInput().setValue(targetClass.getName(), target);
        }
    }
    
    public void destroy() { }
}
        