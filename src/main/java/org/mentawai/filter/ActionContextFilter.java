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
import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.InputWrapper;
import org.mentawai.core.InvocationChain;
import org.mentawai.message.MessageManager;

public class ActionContextFilter extends InputWrapper implements Filter {
   
   private final String name;
   
   public ActionContextFilter(String name) {
      
      this.name = name;
      
   }
   
   public String filter(InvocationChain chain) throws Exception {
	   
	   Action action = chain.getAction();
	
	   super.setInput(action.getInput());
	
	   action.setInput(this);
	   
	   return chain.invoke();
   }
   
	@Override
	public Object getValue(String name) {
		
		if (name.equals(this.name)) {
			
			Object value = super.getValue(name);
			
			if (value != null) return value;
			
			Map<String, Object> pojoContext = new HashMap<String, Object>();
			
			pojoContext.put("application", super.getValue("application"));
			
			pojoContext.put("session", super.getValue("session"));
			
			pojoContext.put("output", super.getValue("output"));
			
			pojoContext.put("input", this);
			
			pojoContext.put("isPost", super.getValue("isPost"));
			
			pojoContext.put("cookies", super.getValue("cookies"));
			
			pojoContext.put("locale", super.getValue("locale"));
			
			pojoContext.put("headers", super.getValue("headers"));
			
			pojoContext.put(MessageManager.MESSAGES, super.getValue(MessageManager.MESSAGES));
			
			pojoContext.put(MessageManager.ERRORS, super.getValue(MessageManager.ERRORS));
			
			pojoContext.put(MessageManager.FIELDERRORS, super.getValue(MessageManager.FIELDERRORS));
			
			setValue(name, pojoContext);
			
			return pojoContext;
			
		} else {
			
			return super.getValue(name);
		}
	}
   
   public void destroy() { 
      
   }
   
}