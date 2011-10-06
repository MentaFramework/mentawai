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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InputWrapper;
import org.mentawai.core.InvocationChain;

public class HeadersFilter extends InputWrapper implements Filter {
   
   private final String name;
   
   private ThreadLocal<Action> action = new ThreadLocal<Action>();
   
   public HeadersFilter(String name) {
      
      this.name = name;
      
   }
   
   public String filter(InvocationChain chain) throws Exception {
	   
	   Action action = chain.getAction();
	
	   super.setInput(action.getInput());
	
	   action.setInput(this);
	   
	   this.action.set(action);
	   
	   return chain.invoke();
   }
   
	@Override
	public Object getValue(String name) {
		
		if (name.equals(this.name)) {
			
			Object value = super.getValue(name);
			
			if (value != null) return value;
			
			Action action = this.action.get();
			
			if (action == null) throw new IllegalStateException("Action cannot be null here!");
			
			Input input = action.getInput();
			
			Map<String, String> headers = new HashMap<String, String>();
			
			Iterator<String> iter = input.getHeaderKeys();
			
			while(iter.hasNext()) {
				
				String key = iter.next();
				
				headers.put(key, input.getHeader(key));
			}
			
			setValue(name, headers);
			
			return headers;
			
		} else {
			
			return super.getValue(name);
		}
	}
   
   public void destroy() { 
      
   }
   
}