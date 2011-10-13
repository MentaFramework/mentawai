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

import org.mentacontainer.Container;
import org.mentacontainer.Scope;
import org.mentawai.core.Action;
import org.mentawai.core.AfterConsequenceFilter;
import org.mentawai.core.ApplicationManager;
import org.mentawai.core.Consequence;
import org.mentawai.core.InputWrapper;
import org.mentawai.core.InvocationChain;

public class MentaContainerFilter extends InputWrapper implements AfterConsequenceFilter {
   
   private final Container container;
   
   private final boolean autowireEverything;
   
   public MentaContainerFilter() {
	   this(true);
   }
   
   public MentaContainerFilter(boolean autowireEverything) {
	   this.autowireEverything = autowireEverything;
	   this.container = ApplicationManager.getContainer();
   }
   
   public boolean isAutowireEverything() {
	   return autowireEverything;
   }
   
   public String filter(InvocationChain chain) throws Exception {
	   
	   Action action = chain.getAction();
	
	   super.setInput(action.getInput());
	
	   action.setInput(this);
	   
	   return chain.invoke();
   }
   
   public void afterConsequence(Action action, Consequence c,
           boolean conseqExecuted, boolean actionExecuted, String result) {
	   
	   container.clear(Scope.THREAD);
   }
   
	@Override
	public Object getValue(String name) {
		
		Object value = super.getValue(name);
		
		if (value != null) {
			
			return value;
		}
		
		value = container.get(name);
		
		if (value != null) {
			
			setValue(name, value);
		}
		
		return value;
	}

   
   public void destroy() { 
      
   }
   
}