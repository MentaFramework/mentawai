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
import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;

/**
 * This filter will place a http header value in the action input.
 * 
 * The header value will come from the action input method getHeader.
 * 
 * @author Sergio Oliveira Jr.
 */
public class HeaderFilter implements Filter {
   
   private final String name;
   
   private final String header;
   
   public HeaderFilter(String name) {
      
      this(name, name);
   }
   
   public HeaderFilter(String name, String header) {
      
      this.name = name;
      
      this.header = header;
   }
   
   public String filter(InvocationChain chain) throws Exception {
      
      Action action = chain.getAction();
      
      Input input = action.getInput();
      
      String headerValue = input.getHeader(header);
         
      input.setValue(name, headerValue);
      
      return chain.invoke();
         
   }
   
   public void destroy() { 
      
   }
   
}