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

import org.mentawai.core.Action;
import org.mentawai.core.Context;
import org.mentawai.core.Filter;
import org.mentawai.core.InputWrapper;
import org.mentawai.core.InvocationChain;

/**
 * This filter will place the cookie context in the action input.
 * 
 * Note that the CookieContext class is also a Map<String, Object> so
 * it can be easily injected in a pojo action without coupling with the 
 * framework API.
 * 
 * @author Sergio Oliveira Jr.
 */
public class CookiesFilter extends InputWrapper implements Filter {
   
   private final String name;
   
   private ThreadLocal<Action> action = new ThreadLocal<Action>();
   
   public CookiesFilter(String name) {
      
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
			
			Context cookies = action.getCookies();
			
			setValue(name, cookies);
			
			return cookies;
			
		} else {
			
			return super.getValue(name);
		}
	}
   
   public void destroy() { 
      
   }
   
}