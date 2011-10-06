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
package org.mentawai.tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.mentawai.list.ListData;
import org.mentawai.list.ListItem;
import org.mentawai.list.ListManager;
import org.mentawai.tag.util.AbstractListContext;
import org.mentawai.tag.util.Context;
import org.mentawai.tag.util.ListSorter;

/**
 * @author Sergio Oliveira
 */
public class OutList extends AbstractListContext {

   private String value;

   private String orderBy = null;

   private boolean desc = false;
   
   /**
    * If true, indicates that the interaction should be done over the keys and not values, when the data source is a Map (Default = false)
    */
   private boolean iterateOverKeys = false;

   public void setValue(String value) {
      this.value = value;
   }

   public void setOrderBy(String orderBy) {
      this.orderBy = orderBy;
   }

   public void setDesc(boolean decreasing) {
      this.desc = decreasing;
   }
   
   public void setIterateOverKeys(boolean iterateOverKeys) {
	this.iterateOverKeys = iterateOverKeys;
}

   protected String getName() {
      return value;
   }

   public List<Object> getList() throws JspException {
      Tag parent = findAncestorWithClass(this, Context.class);
      
      // TODO: much repeated code, simplify it (by: ricardo rufino)

      if (parent != null) {
         Context ctx = (Context) parent;
         Object obj = ctx.getObject();
         if (obj != null) {
            Object object = Out.getValue(obj, value, false);
            if (object instanceof List) {

               if (orderBy != null)
                  return ListSorter.sort((List<Object>) object, orderBy, desc);

               return (List<Object>) object;

            } else if (object instanceof Object[]) {

               if (orderBy != null)
                  return ListSorter.sort(Arrays.asList((Object[]) object), orderBy, desc);

               return Arrays.asList((Object[]) object);

            } else if (object instanceof Set) {

               // TODO:
               // this is not good, but for now let's do it to support sets...
               // A ListWrapper for a Set would be better to avoid copying...

               Set set = (Set) object;

               List<Object> list = new ArrayList<Object>(set);

               if (orderBy != null)
                  return ListSorter.sort(list, orderBy, desc);

               return list;

            } else if (object instanceof Collection) {

               // TODO:
               // this is not good, but for now let's do it to support sets...
               // A CollectionWrapper for a Collection would be better to avoid
               // copying...

               Collection coll = (Collection) object;

               List<Object> list = new ArrayList<Object>(coll);

               if (orderBy != null)
                  return ListSorter.sort(list, orderBy, desc);

               return list;
               
            } else if( object instanceof Map) {
				Collection coll = (iterateOverKeys ? ((Map) object).keySet() : ((Map) object).values()); 
				List<Object> list = new ArrayList<Object>(coll);

	               if (orderBy != null)
	                  return ListSorter.sort(list, orderBy, desc);

	               return list;
            }
         }
      }

      /*
       * if (action != null) { Output output = action.getOutput(); Object obj =
       * output.getValue(value); if (obj instanceof List) { return (List) obj; }
       * else if (obj instanceof Object[]) { return Arrays.asList((Object[])
       * obj); } }
       */

      Object obj = Out.getValue(value, pageContext, false);
      
      // try list...
      
      if (obj == null) {
    	  
    	  obj = ListManager.getList(value);
      }

      if (obj == null)
         return null;

      if (obj instanceof List) {

         if (orderBy != null)
            return ListSorter.sort((List<Object>) obj, orderBy, desc);

         return (List<Object>) obj;

      } else if (obj instanceof Object[]) {

         if (orderBy != null)
            return ListSorter.sort(Arrays.asList((Object[]) obj), orderBy, desc);

         return Arrays.asList((Object[]) obj);

      } else if (obj instanceof Set) {

         // TODO:
         // this is not good, but for now let's do it to support sets...
         // A ListWrapper for a Set would be better to avoid copying...

         Set set = (Set) obj;

         List<Object> list = new ArrayList<Object>(set);

         if (orderBy != null)
            return ListSorter.sort(list, orderBy, desc);

         return list;

      } else if (obj instanceof Collection) {

         // TODO:
         // this is not good, but for now let's do it to support collection...
         // A CollectionWrapper for a Collection would be better to avoid
         // copying...

         Collection coll = (Collection) obj;

         List<Object> list = new ArrayList<Object>(coll);

         if (orderBy != null)
            return ListSorter.sort(list, orderBy, desc);

         return list;

      } else if( obj instanceof Map) {
			Collection coll = (iterateOverKeys ? ((Map) obj).keySet() : ((Map) obj).values()); 
			List<Object> list = new ArrayList<Object>(coll);
             if (orderBy != null)
                return ListSorter.sort(list, orderBy, desc);

             return list;
             
      } else if (obj instanceof ListData) {
    	  
    	  ListData ld = (ListData) obj;
    	  
    	  List<Object> list = new ArrayList<Object>(ld.size());
    	  
    	  List<ListItem> temp = ld.getValues(loc);
    	  
    	  for(ListItem i : temp) list.add(i);
    	  
    	  return list;
      }

      throw new JspException("OutList: Value " + value + " (" + obj.getClass().getName() + ") is not an instance of List or Object[], Map or Set!");
   }

}