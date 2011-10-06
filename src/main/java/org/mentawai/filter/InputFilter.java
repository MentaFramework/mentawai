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
import org.mentawai.core.FilterException;
import org.mentawai.core.Input;
import org.mentawai.core.MapInput;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.PojoAction;
import org.mentawai.util.InjectionUtils;

/**
 * This filter will inject the action input as a Map<String, Object> in the
 * pojo action.
 * 
 * @author Sergio Oliveira
 */
public class InputFilter implements Filter {
   
   private final String name;

   public InputFilter(String name) {
      
      this.name = name;
   }

   protected Object getTarget(Action action) throws FilterException {

      if (action instanceof ModelDriven) {

         ModelDriven md = (ModelDriven) action;

         Object model = md.getModel();

         if (model == null)
            throw new FilterException("ModelDriven action cannot return a null model!");

         return model;

      } else if (action instanceof PojoAction) {

         PojoAction pa = (PojoAction) action;

         return pa.getPojo();
      }

      return action;
   }

   public String filter(InvocationChain chain) throws Exception {

      Action action = chain.getAction();

      Object target = getTarget(action);
      
      Input holder = new MapInput();
      
      holder.setValue(name, action.getInput());

      InjectionUtils.getObject(target, holder, action.getLocale(), false, null, false, false, false);

      return chain.invoke();
   }

   public void destroy() {
   }
}
