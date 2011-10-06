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

import org.mentawai.converter.ConversionException;
import org.mentawai.converter.Convertable;
import org.mentawai.converter.Converter;
import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.FilterException;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;

/**
 * A filter to perform conversions of the action input values.
 * 
 * This filter has the exact same behaviour of the ConversionFilter except that
 * it will get the conversion information from the action if it implements the
 * Convertable interface.
 * 
 * @author Sergio Oliveira
 */
public class ConverterFilter implements Filter {

   private boolean restore = true;

   /**
    * Creates a ConverterFilter.
    */
   public ConverterFilter() {

   }

   /**
    * Creates a ConverterFilter.
    * 
    * @param restore
    *           Restore the converted values after the action execution?
    * @since 1.2
    */
   public ConverterFilter(boolean restore) {
      this.restore = restore;
   }

   /**
    * Should this filter restore the old values after the action has executed?
    * 
    * @param restore
    *           true to restore the values.
    * @since 1.2
    */
   public void setRestore(boolean restore) {
      this.restore = restore;
   }

   public String filter(InvocationChain chain) throws Exception {
      
      Action action = chain.getAction();
      
      Object pojo = chain.getPojo();
      
      Object actionImpl = pojo != null ? pojo : action;

      if (!(actionImpl instanceof Convertable)) {
         return chain.invoke();
      }

      Input input = action.getInput();

      Map<String, Object> initialValues = null;

      if (restore) {
         initialValues = new HashMap<String, Object>();
      }

      Map<String, Converter> converters = new HashMap<String, Converter>();

      Convertable convertable = (Convertable) actionImpl;

      convertable.prepareConverters(converters, chain.getInnerAction());

      try {
         
         Iterator iter = converters.keySet().iterator();
         
         while (iter.hasNext()) {
            
            String field = (String) iter.next();
            
            Converter c = (Converter) converters.get(field);

            Object oldValue = input.getValue(field);

            Object converted = c.convert(field, action);

            input.setValue(field, converted);

            if (restore) initialValues.put(field, oldValue);
         }
         
      } catch (ConversionException e) {
         
         e.printStackTrace();
         
         throw new FilterException(e);
      }

      try {
         
         return chain.invoke();
         
      } catch (Exception e) {
         
         throw e;
         
      } finally {
         
         if (restore) {

            Iterator<String> iter = initialValues.keySet().iterator();

            while (iter.hasNext()) {

               String field = iter.next();

               Object value = initialValues.get(field);

               input.setValue(field, value);

            }
         }
      }
   }

   public void destroy() { 
      
   }

}
