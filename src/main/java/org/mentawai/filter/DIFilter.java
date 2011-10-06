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

import org.mentawai.core.Filter;
import org.mentawai.core.InputWrapper;
import org.mentawai.core.InvocationChain;
import org.mentawai.util.Regex;

/**
 * A filter that will do AUTO-WIRING of dependencies in a totaly transparent way.
 * 
 * As of version 1.14 this filter was renamed to AutoWiringFilter, so this class
 * is currently deprecated and will be removed soon! Please rename all your DIFilter()
 * to AutoWiringFilter();
 * 
 * Ex:
 * 
 * filter(new DIFilter()); should be changed to filter(new AutoWiringFilter());
 * 
 * @author Sergio Oliveira Jr.
 */
public class DIFilter extends InputWrapper implements Filter {
	
	private AutoWiringFilter newFilter;
	
    public DIFilter(boolean tryField) {
    	
    	this.newFilter = new AutoWiringFilter(tryField);
    	
    }
    
    public DIFilter() {
    	
    	this.newFilter = new AutoWiringFilter(true);
    }

    public String toString() {
    	
    	String s = newFilter.toString();
    	
    	return Regex.sub(s, "s/AutoWiringFilter/DIFilter (deprecated)/gi");

    }

    public void setTryField(boolean tryField) {
        newFilter.setTryField(tryField);
    }

	public String filter(InvocationChain chain) throws Exception {

		return newFilter.filter(chain);

	}

	public void setValue(String key, Object value) {

		newFilter.setValue(key, value);
		
	}
	
	public Object getValue(String key) {

		return newFilter.getValue(key);
	}


	public Object getValueOld(String key) {

        return newFilter.getValueOld(key);
	}
	
	public void destroy() { newFilter.destroy(); }

}