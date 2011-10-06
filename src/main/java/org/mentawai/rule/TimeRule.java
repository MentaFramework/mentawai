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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
 
 /**
  * @author Jocenildo Paraizo
  */
 public class TimeRule extends LocaleRule {
	 
	 private static final Map<String, TimeRule> cache = new HashMap<String, TimeRule>();
     
     private static final int STYLE = DateFormat.SHORT;
     
     private int style = STYLE;
     
     /**
      * Creates a rule using the default DateFormat style (SHORT).
      */
     public TimeRule() {
     }
     
     /**
      * Creates a rule using the given DateFormat style.
      *
      * @param style The DateFormat style. (Ex: DateFormat.HOUR_OF_DAY0_FIELD, DateFormat.SHORT, etc.)
      */
     public TimeRule(int style) {
         this.style = style;
     }
     
     public static TimeRule getInstance() {
    	 
    	 String key = "null";
    	 
    	 TimeRule tr = cache.get(key);
    	 
    	 if (tr != null) return tr;
    	 
    	 tr = new TimeRule();
    	 
    	 cache.put(key, tr);
    	 
    	 return tr;
     }
     
     public static TimeRule getInstance(int style) {
    	 
    	 String key = String.valueOf(style);
    	 
    	 TimeRule tr = cache.get(key);
    	 
    	 if (tr != null) return tr;
    	 
    	 tr = new TimeRule(style);
    	 
    	 cache.put(key, tr);
    	 
    	 return tr;
    	 
     }
     
     public boolean check(String value, Locale locale) {
    	 
         DateFormat df = DateFormat.getTimeInstance(style, locale);
         
         df.setLenient(false);
         
         try {
        	 
             df.parse(value);
             
             return true;
             
         } catch(ParseException e) {
        	 
             return false;
         }
     }
     
     public Map<String, String> getTokens() {
         return null;
     }
     
 }