package org.mentawai.tag.util;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ListSorter implements Comparator<Object> {

   private String campo;
   
   public ListSorter(String campo) {
	   
	  if(campo == null || campo.isEmpty())
		  throw new IllegalArgumentException("Invalid property for ordenate: \"" + campo + "\"");
	   
      this.campo = campo;
   }

   public int compare(Object first, Object second) {
	   
	   if( campo.contains(".") ) {
		   
		  String[] fields = campo.split("\\.");
		  
		  Object firstValueSwap = first;
		  Object secondValueSwap = second;
		  
		  for (int i = 0; i < fields.length; i++) {
			
			  if( firstValueSwap != null )
				  firstValueSwap = getValue(firstValueSwap, fields[i]);
			  
			  if( secondValueSwap != null )
				  secondValueSwap = getValue(secondValueSwap, fields[i]);
			  
		  }
		  
		  return compareBy(firstValueSwap, secondValueSwap);
		  
       }
	   else {
		   return compareBy( getValue(first, campo), getValue(second, campo) );
	   }
	   
   }
   
   private Object getValue(Object obj, String field) {
	   
	   Method getMethod = null;

	   if( obj instanceof Collection ) {	// if be a collection, get the first object as example
		   
		   Collection c = (Collection) obj;
		   
		   if( c.isEmpty() )
			   return null;
		   
		   obj = c.iterator().next();
		   
	   }

       try {
          getMethod = obj.getClass().getMethod("get".concat(field.substring(0, 1).toUpperCase()).concat(field.substring(1)), (Class[]) null);
       } catch (Exception e) {
          throw new IllegalStateException(e + " for ordenate.");
       }

       try {
          obj = getMethod.invoke(obj, (Object[]) null);
       } catch (Exception e) {
          obj = null;
       }

       return obj;
       
   }
   
   public int compareBy(Object primeiro, Object segundo) {

      if (primeiro == null) {
         return -1;

      } else {

         if (segundo == null) {
            return 1;

         } else {

            if (primeiro == null && segundo == null)
               return 0;

            if (primeiro == null)
               return -1;

            if (segundo == null)
               return 1;

            // Number
            if (primeiro instanceof Number) {
               return new Double(String.valueOf(primeiro)).compareTo(new Double(String.valueOf(segundo)));
            }

            // Date
            if (primeiro instanceof Date) {
               return ((Date) primeiro).compareTo((Date) segundo);
            }

            // String
            if (primeiro instanceof String) {

               try {
                  Double d = Double.parseDouble(String.valueOf(primeiro));
                  return d.compareTo(new Double(String.valueOf(segundo)));

               } catch (Exception e) {
                  // REALY IS STRING

                  return String.valueOf(primeiro).compareToIgnoreCase(String.valueOf(segundo));
               }
            }
            
         }

      }

      return -1;
   }

   /**
    * Metodo responsavel por retorna uma lista de Object ordenada pelo nome do
    * campo informado
    * 
    * @param lista
    * @param campo
    * @param reverse
    * @return list sorted
    */
   public static List<Object> sort(List<Object> lista, String campo, boolean reverse) {
      
      if ((lista != null) && !lista.isEmpty() && (campo != null) && !(campo.trim().length() == 0)) {

         ListSorter o = new ListSorter(campo);
         
         Collections.sort(lista, o);

         if (reverse) Collections.reverse(lista);
      }

      return lista;
   }
   
}