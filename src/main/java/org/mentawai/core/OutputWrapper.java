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
import java.util.Map;
import java.util.Set;

public class OutputWrapper implements Output, Map<String, Object> {
	
	private ThreadLocal<Output> output = new ThreadLocal<Output>();
	
	public OutputWrapper() {  }
	
	public void setOutput(Output output) {
		this.output.set(output);
	}
	
	private Output getOutput() {
		
		Output o = output.get();

		if (o == null) throw new IllegalStateException("OutputWrapper does not have an output!");

		return o;
	}
	
	private Map<String, Object> getMap() {
		
		Output output = getOutput();
		
		if (!(output instanceof Map)) throw new UnsupportedOperationException("Output is not a map!");
		
		return (Map<String, Object>) output;
	}
	
	public void setValue(String name, Object value) {
		getOutput().setValue(name, value);
	}
	
	public boolean has(String name) {
		return getOutput().has(name);
	}
	
	public String add(Object value) {
		return getOutput().add(value);
	}
    
	public Object getValue(String name) {
		return getOutput().getValue(name);
	}
	
	public void removeValue(String name) {
		getOutput().removeValue(name);
	}
	
	public boolean isEmpty() {
		return getOutput().isEmpty();
	}
	
	public void setObject(Object bean) {
		getOutput().setObject(bean, null);
	}
	
	public void setObject(Object bean, String prefix) {
		getOutput().setObject(bean, prefix);
	}	
    
	public Iterator<String> keys() {
		return getOutput().keys();
	}
    
    public void clear() {
        getMap().clear();
    }
    
    public boolean containsKey(Object key) {
    	return getMap().containsKey(key);
    }
    
    public boolean containsValue(Object value) {
        return getMap().containsValue(value);
    }
    
    public Set<Entry<String, Object>> entrySet() {
        return getMap().entrySet();
    }
    
    public Object get(Object key) {
    	return getMap().get(key);
    }

    public Set<String> keySet() {
        return getMap().keySet();
    }

    public Object put(String key, Object value)  {
    	return getMap().put(key, value);
    }

    public void putAll(Map<? extends String,? extends Object> t) {
        getMap().putAll(t);
    }
    
    public Object remove(Object key) {
    	return getMap().remove(key);
    }
    
    public int size() {
        return getMap().size();
    }
    
    public Collection<Object> values() {
        return getMap().values();
    }
}
	