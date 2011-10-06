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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.mentawai.util.InjectionUtils;

/**
 * A simple Output implementation backed up by a java.util.HashMap.
 * You may use this class as a mock for testing.
 * 
 * @author Sergio Oliveira
 */
public class MapOutput implements Output {
	
	private Map<String, Object> map = new HashMap<String, Object>();
	
	private int counter = 0;
	
	public MapOutput() { }
	
	public void setValue(String name, Object value) {
		map.put(name, value);
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
	
	public Iterator<String> keys() {
		return map.keySet().iterator();
	}
	
	public boolean has(String key) {
		
		return map.containsKey(key);
	}
}
	
