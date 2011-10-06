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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.FilterException;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;

public class CollectionFilter implements Filter {
	
	private int maxAllowed = 128;
	
	private final String attrName;
	
	public CollectionFilter(String attrName) {
		
		this.attrName = attrName;
		
	}
	
	public CollectionFilter(String attrName, int maxAllowed) {
		
		this(attrName);
		
		this.maxAllowed = maxAllowed;
	}
	
	public String filter(InvocationChain chain) throws Exception {
		
		Action action = chain.getAction();
		
		Input input = action.getInput();
		
		Iterator<String> iter = input.keys();
		
		Object[] list = new Object[maxAllowed];
		
		int len = attrName.length();
		
		int max = 0;
		
		while(iter.hasNext()) {
			
			String key = iter.next();
			
			if (!key.startsWith(attrName) || key.length() <= len) continue;
			
			int index;
			
			try {
				
				index = parseNumberFromKey(attrName, key);
				
			} catch(NumberFormatException e) {
				
				continue;
			}
			
			if (index >= maxAllowed) throw new FilterException("Number too big: " + index + " (max = " + max + ")");
			
			if (index > max) max = index;
			
			list[index] = input.getValue(key);
		}
		
		// build a list out of max and object array...
		
		Collection<Object> coll = buildCollection(list, max); 
		
		input.setValue(attrName, coll);
		
		return chain.invoke();
	}
	
	protected Collection<Object> buildCollection(Object[] list, int max) {
		
		List<Object> coll = new ArrayList<Object>(max);
		
		for(int i=0;i<max;i++) {
			
			coll.add(list[i]);
		}
		
		return coll;
		
	}
	
	protected int parseNumberFromKey(String attrName, String key) throws NumberFormatException {
		
		int len = attrName.length();
		
		String number = key.substring(len);
		
		return Integer.parseInt(number);
	}
	
	public void destroy() { }
	
}