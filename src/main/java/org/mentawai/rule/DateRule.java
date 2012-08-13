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
package org.mentawai.rule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

 /**
  * @author Robert Anderson Nogueira de Oliveira
  */
 public class DateRule extends LocaleRule {
	 
     private static final int STYLE = DateFormat.SHORT;
     
     private int style = STYLE;
     
     private SimpleDateFormat sdf = null;
     
     private Date min = null;
     
     private Date max = null;
     
     private Locale loc = null;
     
    /**
      * Creates a rule using the default DateFormat style (SHORT).
      */
     public DateRule() {
     
     }

     // No cache for DateRule because of the locale!
     public static DateRule getInstance() {
    	 return new DateRule();
     }

     /**
      * Creates a rule using the default DateFormat style (SHORT).
      * 
      * @param min Can be null or the min date.
      * @param max Can be null or the max date.
      */
     public DateRule(Date min, Date max) {
    	 
    	 this.min = min;
    	 
    	 this.max = max;
     }
     
     /**
      * Creates a rule using the given DateFormat style.
      *
      * @param style The DateFormat style. (Ex: DateFormat.SHORT, DateFormat.FULL, etc.)
      */    
     public DateRule(int style) {
         this.style = style;
     }
     
     
     /**
      * Creates a rule using the given DateFormat style.
      * 
      * @param style The DateFormat style. (Ex: DateFormat.SHORT, DateFormat.FULL, etc.)
      * @param min Can be null or the min date.
      * @param max Can be null or the max date.
      */
     public DateRule(int style, Date min, Date max) {
    	 
         this.style = style;
         
         this.min = min;
         
         this.max = max;
     }
     
     
     /**
      * Create a rule using the given SimpleDateFormat.
      * 
      * OBS: Note that this rule, although a LocaleRule, will ignore the locale
      * when validating. That's because a SimpleDateFormat contains a fixed pattern
      * that does not depend on the locale.
      * 
      * @param sdf The SimpleDateFormat to use.
      */
     public DateRule(SimpleDateFormat sdf) {
    	 
    	 this.sdf = sdf;
    	 
     }
     
     public DateRule(String pattern) {
    	 
    	 this(new SimpleDateFormat(pattern));
     }
     
     /**
      * Create a rule using the given SimpleDateFormat.
      * 
      * OBS: Note that this rule, although a LocaleRule, will ignore the locale
      * when validating. That's because a SimpleDateFormat contains a fixed pattern
      * that does not depend on the locale.
      * 
      * @param sdf The SimpleDateFormat to use.
      * @param min Can be null or the min date.
      * @param max Can be null or the max date.
      */
     public DateRule(SimpleDateFormat sdf, Date min, Date max) {
    	 
    	 this.sdf = sdf;
    	 
    	 this.min = min;
    	 
    	 this.max = max;
     }    
     
     public DateRule(String pattern, Date min, Date max) {
    	 
    	 this(new SimpleDateFormat(pattern), min, max);
     }
     
     public boolean check(String value, Locale locale) {
    	 
    	 if (min != null || max != null) {
    	 
    		 loc = locale;
    		 
    	 }
    	 
         DateFormat df = null;
         
         if (sdf != null) {
        	 
        	 df = sdf;
        	 
         } else {
         
        	 df = DateFormat.getDateInstance(style, locale);
        	 
         }
         
         df.setLenient(false);
         
         try {
             Date date = df.parse(value);
             
             if (min != null && date.getTime() < min.getTime()) return false;
             
             if (max != null && date.getTime() > max.getTime()) return false;
             
             return true;
             
         } catch(ParseException e) {
        	 
             return false;
             
         }
     }
     
     public Map<String, String> getTokens() {
    	 
    	 if (min != null || max != null) {
    	 
    		 DateFormat df = DateFormat.getDateInstance(style, loc);
    	 
    		 Map<String, String> map = new HashMap<String, String>();
    		 
    		 if (min != null) {
    			 
    			 map.put("min", df.format(min));
    			 
    		 }
    		 
    		 if (max != null) {
    			 
    			 map.put("max", df.format(max));
    		 }
    	 
    		 return map;
    	 }
    	 
    	 return null;
     }
     
 }