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
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A wrapper for the action input.
 *
 * @author Davi Luan Carneiro
 */
public class InputWrapper implements Input, Map {

	private ThreadLocal<Input> input = new ThreadLocal<Input>();

	public InputWrapper() {

	}

	public InputWrapper(Input input) {
		setInput(input);
	}

	public void setInput(Input input) {
		this.input.set(input);
	}
	
	public Input getRoot() {
		Input i = getInput();
		while(i instanceof InputWrapper) {
			InputWrapper iw = (InputWrapper) i;
			i = iw.getInput();
		}
		return i;
	}
	
	public void removeInput() {
	    input.remove();
	}

	private Input getInput() {

		Input i = input.get();

		if (i == null) throw new IllegalStateException("InputWrapper does not have an input!");

		return i;

	}

	private Map<Object, Object> getMap() {

		Input i = getInput();

		if (!(i instanceof Map)) throw new UnsupportedOperationException("Underlying input is not a map!");

		return (Map<Object, Object>) i;

	}

	public String getHeader(String name) {
		return getInput().getHeader(name);
	}

	/**
	 * @deprecated use getString instead
	 */
	public String getStringValue(String name) {
		return getInput().getString(name);
	}
	
	public String getString(String name) {
		return getInput().getString(name);
	}

	/**
	 * @deprecated use getInt instead
	 */
	public int getIntValue(String name) {
		return getInput().getInt(name);
	}
	
	public int getInt(String name) {
		return getInput().getInt(name);
	}
    
	/**
	 * @deprecated use getInt instead
	 */
    public int getIntValue(String name, int def) {
        return getInt(name, def);
    }
    
    public int getInt(String name, int def) {
    	return getInput().getInt(name, def);
    }
    
    public boolean hasValue(String name) {
    	return getInput().hasValue(name);
    }
    
	public boolean has(String key) {
		return hasValue(key);
	}
    
    /**
     * @deprecated use getLong instead
     */
    public long getLongValue(String name) {
        return getLong(name);
    }
    
    public long getLong(String name) {
    	return getInput().getLong(name);
    }
    
    public long[] getLongs(String name) {
    	return getInput().getLongs(name);
    }
    
    /**
     * @deprecated use getLong instead
     */
    public long getLongValue(String name, long def) {
        return getLong(name, def);
    }
    
    public long getLong(String name, long def) {
    	return getInput().getLong(name, def);
    }
    
    /**
     * @deprecated use getFloat instead
     */
    public float getFloatValue(String name) {
        return getFloat(name);
    }
    
    public float getFloat(String name) {
    	return getInput().getFloat(name);
    }
    
    /**
     * @deprecated use getFloat instead
     */
    public float getFloatValue(String name, float def) {
        return getFloat(name, def);
    }
    
    public float getFloat(String name, float def) {
    	return getInput().getFloat(name, def);
    }
    
    /**
     * @deprecated use getDouble instead
     */
    public double getDoubleValue(String name) {
        return getDouble(name);
    }
    
    public double getDouble(String name) {
    	return getInput().getDouble(name);
    }
    
    /**
     * @deprecated use getDouble instead
     */
    public double getDoubleValue(String name, double def) {
        return getDouble(name, def);
    }
    
    public double getDouble(String name, double def) {
    	return getInput().getDouble(name, def);
    }
    
    /**
     * @deprecated use getBoolean instead
     */
    public boolean getBooleanValue(String name) {
        return getBoolean(name);
    }
    
    public boolean getBoolean(String name) {
    	return getInput().getBoolean(name);
    }
    
    /**
     * @deprecated use getBoolean instead
     */
    public boolean getBooleanValue(String name, boolean def) {
        return getBoolean(name, def);
    }
    
    public boolean getBoolean(String name, boolean def) {
    	return getInput().getBoolean(name, def);
    }

    /**
     * @deprecated use getStrings instead
     */
	public String[] getStringValues(String name) {
		return getInput().getStrings(name);
	}
	
	public String[] getStrings(String name) {
		return getInput().getStrings(name);
	}

	/**
	 * @deprecated use getInts intead
	 */
	public int[] getIntValues(String name) {
		return getInput().getInts(name);
	}
	
	public int[] getInts(String name) {
		return getInput().getInts(name);
	}

	public void setValue(String name, Object value) {
		getInput().setValue(name, value);
	}

	public Object getValue(String name) {
		return getInput().getValue(name);
	}

	public void removeValue(String name) {
		getInput().removeValue(name);
	}

	public Iterator<String> keys() {
		return getInput().keys();
	}

	public Iterator<String> getHeaderKeys() {
		return getInput().getHeaderKeys();
	}

	public String getProperty(String name) {
		return getInput().getProperty(name);
	}
	
    public <E> E getObject(Class<? extends E> klass) {
    	
    	return getInput().getObject(klass);
    }
    
    public <E> E getObject(Class<? extends E> klass, String prefix) {
    	
    	return getInput().getObject(klass, prefix);
    }
    
    public void inject(Object bean) {
    	
    	getInput().inject(bean);
    }
    
    public void inject(Object bean, String prefix) {
    	
    	getInput().inject(bean, prefix);
    }
    
	// MAP METHODS:

    public void clear() {
        getMap().clear();
    }

    public boolean isEmpty() {
        return getMap().isEmpty();
    }

    public boolean containsKey(Object key) {
    	return getMap().containsKey(key);
    }

    public boolean containsValue(Object value) {
        return getMap().containsValue(value);
    }

    public Set entrySet() {
        return getMap().entrySet();
    }

    public Object get(Object key) {
    	return getMap().get(key);
    }

    public Set<Object> keySet() {
        return getMap().keySet();
    }

    public Object put(Object key, Object value)  {
    	return getMap().put(key, value);
    }

    public void putAll(Map map) {
    	getMap().putAll(map);
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

	public Iterator entries() {
        return entrySet().iterator();
	}

	public Date getDate(String name) {
		return getInput().getDate(name);
	}
	
	public Date getDate(String name, String pattern) {
		return getInput().getDate(name, pattern);
	}
	
	public Date getDate(String name, int style) {
		return getInput().getDate(name, style);
	}

	public <E extends Enum<E>> E getEnum(String name, Class<E> enumClass) {
		return getInput().getEnum(name, enumClass);
	}
	
	public <E extends Enum<E>> Set<E> getEnums(String name, Class<E> enumClass) {
		return getInput().getEnums(name, enumClass);
	}
}
