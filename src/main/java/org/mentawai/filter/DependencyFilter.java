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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.FilterException;
import org.mentawai.core.InvocationChain;
import org.mentawai.util.FindMethod;
import org.mentawai.util.InjectionUtils;

public class DependencyFilter implements Filter {
	
	public static final int INPUT = 1;
    public static final int  OUTPUT = 2;
    public static final int SESSION = 3;
    public static final int APPLICATION = 4;

    private Map<String, AccessibleObject> cache = Collections.synchronizedMap(new HashMap<String, AccessibleObject>());
    private boolean tryField = true;
    private boolean throwException = false;
	
	private String source_key, target_key, attribute;
	private int source_scope = INPUT;
    private int target_scope = INPUT;
    
    public DependencyFilter(String source_key, String target_key) {
        this(source_key, target_key, source_key);
    }
    
	public DependencyFilter(String source_key, String target_key, String attribute){
        this.source_key = source_key;
        this.target_key = target_key;
        this.attribute = attribute;
	}

	public DependencyFilter(String source_key, int source_scope, String target_key, String attribute){
        this(source_key, target_key, attribute);
		this.source_scope = source_scope;
	}
	
	public DependencyFilter(String source_key, int source_scope, String target_key, int target_scope, String attribute){
        this(source_key, source_scope, target_key, attribute);
        this.target_scope = target_scope;
	}    
    
    public String toString() {
     
        StringBuffer sb = new StringBuffer(128);
        
        sb.append("DependencyFilter: sourceKey=").append(source_key).append(" targetKey=").append(target_key);
        sb.append(" attribute=").append(attribute).append(" sourceScope=").append(getScope(source_scope));
        sb.append(" targetScope=").append(getScope(target_scope));
        
        return sb.toString();
    }
    
    private String getScope(int scope) {
        switch(scope) {
            case INPUT:
                return "INPUT";
            case OUTPUT:
                return "OUTPUT";
            case SESSION:
                return "SESSION";
            case APPLICATION:
                return "APPLICATION";
        }
        return "?";
    }
    
    public void setTryField(boolean tryField) {
        this.tryField = tryField;
    }
    
    public void setThrowException(boolean throwException) {
        this.throwException = throwException;
    }

	public String filter(InvocationChain chain) throws Exception {
		Action a = chain.getAction();

        Object target = null;

		switch (target_scope) {
			case INPUT:
				target = a.getInput().getValue(target_key);
				break;
			case OUTPUT:
				target = a.getOutput().getValue(target_key);
				break;
			case SESSION:
				target = a.getSession().getAttribute(target_key);
				break;
			case APPLICATION:
				target = a.getApplication().getAttribute(target_key);
				break;
			default:
				throw new IllegalArgumentException("Invalid target scope!");
		}
		
		if (target == null) {
			
			if (throwException) throw new FilterException("Cannot find target: " + target_key + " / " + target_scope);
			
			return chain.invoke();
			
		}
        
		Object source = null;

		switch (source_scope) {
			case INPUT:
				source = a.getInput().getValue(source_key);
				break;
			case OUTPUT:
				source = a.getOutput().getValue(source_key);
				break;
			case SESSION:
				source = a.getSession().getAttribute(source_key);
				break;
			case APPLICATION:
				source = a.getApplication().getAttribute(source_key);
				break;
			default:
				throw new IllegalArgumentException("Invalid source scope!");
		}
		
		if (source == null) {
			
			if (throwException) throw new FilterException("Cannot find source: " + source_key + " / " + source_scope);
			
			return chain.invoke();
		}
        
		boolean ok = setValue(target, attribute, source);
		
        if (throwException && !ok) {
        	throw new FilterException("Cannot inject source in target: " + source_key + " / " + target_key + " / " + attribute);
        }
        
		return chain.invoke();
	}

	public void destroy() { }

	/*
	 * Use reflection to set a property in the bean
	 */
	private boolean setValue(Object bean, String name, Object value) {
        
		try {
			StringBuffer sb = new StringBuffer(30);
			sb.append("set");
			sb.append(name.substring(0,1).toUpperCase());
			if (name.length() > 1) sb.append(name.substring(1));
            
            String methodName = sb.toString();
            
            if (!cache.containsKey(name)) {
                Method m = null;
                Field f = null;
                try {
                    m = FindMethod.getMethod(bean.getClass(), methodName, new Class[] { value.getClass() });
                } catch(Exception e) {
                    // try primitive...
                    Class primitive = InjectionUtils.getPrimitiveFrom(value);
                    if (primitive != null) {
                        try {
                            m = bean.getClass().getMethod(methodName, new Class[] { primitive });
                        } catch(Exception ex) {
                            //ex.printStackTrace();
                        }
                    }
                    
                    if (m == null && tryField) {
                        // try field...
                        f = InjectionUtils.getField(bean, name);
                        if (f != null) {
                            f.setAccessible(true);
                        }
                    }
                }
                if (m != null) {
                    cache.put(name, m);
                    m.setAccessible(true);
                } else {
                    cache.put(name, f);
                }
            }    

            Object obj = cache.get(name);
            
            if (obj == null) {
                return false;
            }
            
            if (obj instanceof Method) {
                Method m = (Method) obj;
                m.invoke(bean, new Object[] { value });
            } else if (obj instanceof Field) {
                Field f = (Field) obj;
                f.set(bean, value);
            }            
            return true;
            
		} catch(Exception e) {
			e.printStackTrace();
		}
        
        return false;
	}
}