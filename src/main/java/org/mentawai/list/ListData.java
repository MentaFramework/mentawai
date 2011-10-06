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
package org.mentawai.list;

import java.util.List;
import java.util.Locale;

/**
 * Defines how a data list works.
 *
 * @author Sergio Oliveira
 */
public interface ListData {
	
    /**
     * Returns the string value of the list data item with the given id in the given locale.
     *
     * @param id The id of the list data item.
     * @param loc The locale of the list data item.
     * @return The string value for the list data item.
     */
	public String getValue(String id, Locale loc);
	
	/**
	 * Returns the string value for the list data item with the default locale.
	 * 
	 * If there is no value for the default locale, use any locale available to
	 * return the value.
	 * 
	 * @param id
	 * @return The string value of the list data item.
	 */
	public String getValue(String id);
	
    /**
     * Returns the string value of the list data item with the given id in the given locale.
     *
     * @param id The id of the list data item.
     * @param loc The locale of the list data item.
     * @return The string value for the list data item.
     */
	public String getValue(int id, Locale loc);
	
	/**
	 * Returns the string value of the list data item with the given id with the default locale.
	 * 
	 * @param id The id of the list data item
	 * @return The string value of the list data item
	 */
	public String getValue(int id);
    
    /**
     * Returns a list of ListItem in the given locale.
     *
     * @param loc The locale of the ListItems.
     * @return A list of ListItems.
     */
	public List<ListItem> getValues(Locale loc);
	
	/**
	 * Returns a list of ListItem for the default locale.
	 * 
	 * If there is no list for the default locale, then
	 * try any locale in order to return a list.
	 * 
	 * @return A list of ListItems.
	 */
	public List<ListItem> getValues();
	
    
    /**
     * Returns the name of this list.
     *
     * @return The name of the list.
     */
	public String getName();
    
    /**
     * Returns the size of this list.
     * Note: the size does not depend on the number of locales.
     * 
     * @return The size of the list.
     */
	public int size();
	
}