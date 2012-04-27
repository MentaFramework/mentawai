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
package org.mentawai.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.mentawai.util.InjectionUtils;

/**
 * A simple Output implementation backed up by a java.util.HashMap.
 * This class also implements the java.util.Map interface so it can be used with JSP Expression Language.
 * This class wraps the HttpServletResponse but does not use it. You may access it through the getResponse() method.
 * Most of map operations are not supported.
 * 
 * @author Sergio Oliveira
 */
public class ResponseOutput implements Output, Map<String, Object> {
	
	private Map<String, Object> map = new LinkedHashMap<String, Object>();
    private HttpServletResponse res;
    
    private int counter = 0;
	
	public ResponseOutput(HttpServletResponse res) {
        this.res = res;
    }
    
    public HttpServletResponse getResponse() {
        return res;
    }
	
	public void setValue(String name, Object value) {
		map.put(name, value);
	}
	
	public boolean has(String name) {
		return map.containsKey(name);
	}
	
	public String add(Object value) {
		counter++;
		String key = "_" + counter;
		setValue(key, value);
		return key;
	}
    
	public Object getValue(String name) {
		return map.get(name);
	}
	
	public void removeValue(String name) {
		map.remove(name);
	}
	
	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	public void setObject(Object bean) {
		setObject(bean, null);
	}
	
	public void setObject(Object bean, String prefix) {
		InjectionUtils.setObject(bean, this, prefix, true);
	}	
    
    public String toString() {
     
        StringBuffer sb = new StringBuffer(1024);
        
        Iterator<String> iter = keys();
        
        while(iter.hasNext()) {
            
            String name = (String) iter.next();
            
            Object value = getValue(name);
            
            String s = value != null ? value.toString() : "null";
            
            s = s.replace('\n', ' ');
            
            sb.append(name).append(" = ").append(s).append("\n");
            
        }
        
        return sb.toString();
    }
	
	public Iterator<String> keys() {
		return map.keySet().iterator();
	}
    
    public void clear() {
        map.clear();
    }
    
    public boolean containsKey(Object key) {
        if (key instanceof String) {
            return map.containsKey(key);
        }
        throw new IllegalArgumentException();
    }
    
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }
    
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }
    
    public Object get(Object key) {
        if (key instanceof String) {
            return getValue((String) key);
        }
        throw new IllegalArgumentException();
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public Object put(String key, Object value)  {
        if (key instanceof String) {
            setValue((String) key, value);
            return value;
        }
        throw new IllegalArgumentException();
    }

    public void putAll(Map<? extends String,? extends Object> t) {
        map.putAll(t);
    }
    
    public Object remove(Object key) {
        if (key instanceof String) {
            String s = (String) key;
            Object obj = getValue(s);
            if (obj != null) {
                removeValue(s);
                return obj;
            }
            return null;
        }
        throw new IllegalArgumentException();
    }
    
    public int size() {
        return map.size();
    }
    
    public Collection<Object> values() {
        return map.values();
    }
}
	