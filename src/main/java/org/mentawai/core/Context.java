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
 * Describes the behavior of a Mentawai context.
 * A context has attributes that can be any java object.
 * Each attribute is associated to the context by a name.
 * A context can be invalidated.
 * 
 * @author Sergio Oliveira
 */
public interface Context {
	
	/**
	 * Gets an attribute value associated with the given name.
	 * 
	 * @param name The name of the attribute.
	 * @return The value of the attribute or null if it doesn't exist.
	 */
	public Object getAttribute(String name);
	
	/**
	 * Sets an attribute value associated with the given name.
	 * If the attribute already exists, overwrite it.
	 * 
	 * @param name The name of the attribute.
	 * @param value The value of the attribute.
	 */
	public void setAttribute(String name, Object value);
	
	/**
	 * Removes an attribute associated with the given name.
	 * 
	 * @param name The name of the attribute.
	 */
	public void removeAttribute(String name);
	
	/**
	 * Resets this context.
	 * All values are discarded and a new context is internally created.
	 */
	public void reset();
	
	/**
	 * Returns true is an attribute exists with this name.
	 * 
	 * @param name The name of the attribute.
	 * @return true if the attribute exists
	 */
	public boolean hasAttribute(String name);
	
	/**
	 * Return an iterator with all the attribute names in this context.
	 * 
	 * @return an iterator with the attribute names
	 */
	public Iterator<String> keys();
}
