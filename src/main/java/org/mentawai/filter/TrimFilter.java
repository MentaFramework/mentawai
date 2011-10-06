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

import java.util.Iterator;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;

public class TrimFilter implements Filter {
   
   public String filter(InvocationChain chain) throws Exception {
      
      Action action = chain.getAction();
      
      Input input = action.getInput();
      
      Iterator<String> iter = input.keys();
      
      while(iter.hasNext()) {
         
         String key = iter.next();
         
         Object obj = input.getValue(key);
         
         if (obj instanceof String) {
            
            String s = (String) obj;
            
            if (s.startsWith(" ") || s.endsWith(" ")) {
               
               input.setValue(key, s.trim());
            }
         }
      }
      
      return chain.invoke();
   }
   
   public void destroy() { }
   
}