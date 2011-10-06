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
import java.util.Locale;
import java.util.Map;

/**
 * A simple Input that can be used for testing.
 *
 * @author Sergio Oliveira
 */
public class MapInput extends AbstractInput {

    private Map<String, String> headers = null;
    private Map<String, Object> values;
    private Map<String, String> properties = null;
    private Locale loc = Locale.ENGLISH;

    public MapInput() {
        this.values = new HashMap<String, Object>();
    }

    public MapInput(Map<String, Object> values) {
        this.values = values;
    }
    
    public void setLocale(Locale loc) {
    	this.loc = loc;
    }
    
    public void setHeader(String name, String value) {
        if (headers == null) headers = new HashMap<String, String>();
        headers.put(name, value);
    }

	public String getHeader(String name) {
        if (headers != null) {
            return headers.get(name);
        }
        return null;
    }

	public Iterator<String> getHeaderKeys() {

		if (headers == null) headers = new HashMap<String, String>();

		return headers.keySet().iterator();

	}

	public void setProperty(String name, String value) {
		if (properties == null) properties = new HashMap<String, String>();
		properties.put(name, value);
	}

	public String getProperty(String name) {
		if (properties != null) {
			return properties.get(name);
		}

		return null;
	}
	
	public boolean hasValue(String name) {
		
		return values.containsKey(name);
	}
	
	public boolean has(String key) {
		return hasValue(key);
	}
	
	/**
	 * @deprecated use getString instead
	 */
	public String getStringValue(String name) {
		return getString(name);
    }
	
	public String getString(String name) {
        Object value = values.get(name);
        if (value != null) return value.toString();
        return null;
    }

	public Iterator<String> keys() {
		return values.keySet().iterator();
	}

	public void removeValue(String name) {
		values.remove(name);
	}

	/**
	 * @deprecated use getStrings instead
	 */
   	public String[] getStringValues(String name) {
   		return getStrings(name);
	}
   	
   	public String[] getStrings(String name) {
		Object obj = values.get(name);
		if (obj == null) return null;
		if (obj instanceof String []) {
			return (String []) obj;
		} else if (obj instanceof String) {
			String [] s = new String[1];
			s[0] = (String) obj;
			return s;
		}
		throw new InputException("Error trying to get a String []: " + name);   		
   	}

	public void setValue(String name, Object value) {
		values.put(name, value);
	}

	public Object getValue(String name) {
		return values.get(name);
	}

	protected Locale getLocale() {
		return loc;
	}
 
}
