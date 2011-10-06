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

import java.util.Iterator;

/**
 * Defines the behavior of an action output.
 * An output is like a map where you can put and remove values by name.
 * 
 * @author Sergio Oliveira
 */
public interface Output {
	
	/**
	 * Sets an output value by name.
	 * 
	 * @param name The name of the value
	 * @param value The value
	 */
	public void setValue(String name, Object value);
	
	/**
	 * Gets an output value by name.
	 * 
	 * @param name The name of the value
	 * @return The value or null if it does not exist
	 */
	public Object getValue(String name);
	
	/**
	 * Removes an output value by name.
	 * 
	 * @param name The name of the value
	 */
	public void removeValue(String name);
	
	/**
	 * Returns true is this output has no values.
	 * 
	 * @return true if this output has no values
	 */
	public boolean isEmpty();
	
	/**
	 * Gets an iterator with the names of each output value.
	 * 
	 * @return An iterator with all the names
	 */
	public Iterator<String> keys();
	
	/**
	 * Sets the properties of the given bean in the action output, in other words,
	 * extract all attributes from the given object and place them in the action
	 * output.
	 * 
	 * @param bean The bean (object) from where to get the properties.
	 */
	public void setObject(Object bean);
	
	/**
	 * Sets the properties of the given bean in the action output, in other words,
	 * extract all attributes from the given object and place them in the action
	 * output. Use the given prefix when placing in the output.
	 *  
	 * @param bean The bean (object) from where to get the properties.
	 * @param prefix The prefix to use when placing the properties in the output.
	 */
	public void setObject(Object bean, String prefix);
	
	/**
	 * Check if the output has this key.
	 * @param key
	 * @return boolean
	 */
	public boolean has(String key);
	
	/**
	 * Add an object with a auto generated key. This can be used when you don't care about the key,
	 * for example for streams.
	 * 
	 * @param value
	 * @return The key used to add this object to the output.
	 */
	public String add(Object value);
	
}
	
