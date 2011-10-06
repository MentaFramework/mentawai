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
package org.mentawai.util;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
	
   private static final String TOKEN1 = "A1cxxZ";
   
   private static final String TOKEN2 = "Bbsd423xx12asdf44xT";
   
   private static final char DEFAULT_ESCAPE_CHAR = '\\';
   
   private final String s;
   
   private final String escPattern;
   
   private final boolean caseInsensitive;
   
   private final boolean global;
   
   private final boolean substitute;
   
   private final String toSub;
   
   public Regex(String s, String pattern, char escape) {
      
      this.s = s;
      
      this.substitute = pattern.startsWith("s/");
      
      this.global = pattern.endsWith("/gi") || pattern.endsWith("/ig") || pattern.endsWith("/g");
      
      this.caseInsensitive = pattern.endsWith("/gi") || pattern.endsWith("/ig") || pattern.endsWith("/i");
      
      pattern = removeSlashes(pattern);
      
      if (!substitute) {
      
         if (escape != DEFAULT_ESCAPE_CHAR) {
         
            this.escPattern = changeEscapeChar(pattern, escape);
         
         } else {
         
            this.escPattern = pattern;
         }
         
         this.toSub = null;
         
      } else {
    	  
    	  String token = null;
    	  
    	  if (!pattern.contains(TOKEN1)) {
    		  
    		  token = TOKEN1;
    		  
    	  } else if (!pattern.contains(TOKEN2)) {
    		  
    		  token = TOKEN2;
    		  
    	  } else {
    		  
    		  throw new RuntimeException("Cannot use mentawai Regex with this pattern!");
    	  }
    	  
    	 // this is to allow '/' both in pattern and sub expression...
    	 // Necessary so that split returns only 2 items...
    	 // '/' must be escaped!
    	 String newPattern = pattern.replaceAll("\\" + escape + "/", token);
    	 
    	 boolean changed = false;
    	 
    	 if (!newPattern.equals(pattern)) {
    		 
    		 pattern = newPattern;
    		 
    		 changed = true;
    	 }
         
         String[] parts = pattern.split("/");
         
         if (parts.length == 1 && pattern.endsWith("/")) {
        	 
        	 String save = parts[0];
        	 
        	 parts = new String[2];
        	 parts[0] = save;
        	 parts[1] = "";
        	 
         } else if (parts.length != 2) {
            
            throw new IllegalArgumentException("Bad substitute pattern: " + pattern);
         }
         
         if (changed) {
         
        	 this.toSub = parts[1].replaceAll(token, "/");
        	 
         } else {
        	 
        	 this.toSub = parts[1];
         }
         
         if (escape != DEFAULT_ESCAPE_CHAR) {
            
        	if (changed) {
        		
        		this.escPattern = changeEscapeChar(parts[0], escape).replaceAll(token, "\\/");
        		
        	} else {
        	 
        		this.escPattern = changeEscapeChar(parts[0], escape);
        		
        	}
         
         } else {
        	 
        	if (changed) {
        		
        		this.escPattern = parts[0].replaceAll(token, "\\/");
        		
        	} else {
         
        		this.escPattern = parts[0];
        		
        	}
         }
      }
      
   }
   
   public Regex(String s, String pattern) {
      
      this(s, pattern, DEFAULT_ESCAPE_CHAR);
      
   }
   
   private static String removeSlashes(String s) {
      
      if (s.startsWith("s/")) {
         
         s = s.substring(2, s.length());
         
      } else if (s.startsWith("/")) {
         
         s = s.substring(1, s.length());
      }
      
      if (s.endsWith("/gi") || s.endsWith("/ig")) {
         
         s = s.substring(0, s.length() - 3);
         
      } else if (s.endsWith("/g") || s.endsWith("/i")) {
         
         s = s.substring(0, s.length() - 2);
         
      } else if (s.endsWith("/")) {
         
         s = s.substring(0, s.length() - 1);
      }
      
      return s;
   }
   
   private static String changeEscapeChar(String s, char esc) {
      
      return s.replace(esc, '\\');
   }
   
   public String substitute() {
      
      Pattern p = null;
      
      if (caseInsensitive) {
         
         p = Pattern.compile(escPattern, Pattern.CASE_INSENSITIVE);
         
      } else {
         
         p = Pattern.compile(escPattern);
      }
      
      Matcher m = p.matcher(s);
      
      if (global) {
         
         return m.replaceAll(toSub);
         
      } else {
         
         return m.replaceFirst(toSub);
      }
      
   }
   
   public boolean matches() {
	   
      Pattern p = null;
      
      if (caseInsensitive) {
         
         p = Pattern.compile(escPattern, Pattern.CASE_INSENSITIVE);
         
      } else {
         
         p = Pattern.compile(escPattern);
      }
      
      Matcher m = p.matcher(s);
      
      return m.find();
   }
   
   public String[] match() {
      
      Pattern p = null;
      
      if (caseInsensitive) {
         
         p = Pattern.compile(escPattern, Pattern.CASE_INSENSITIVE);
         
      } else {
         
         p = Pattern.compile(escPattern);
      }
      
      Matcher m = p.matcher(s);
      
      List<String> res = new LinkedList<String>();
      
	  while(m.find()) {
    		  
		  int x = m.groupCount();
    	         
		  for(int i=0;i<x;i++) {
    	            
			  res.add(m.group(i + 1));
		  }
		  
		  if (!global) break; // just once...
	  }
	  
      return res.toArray(new String[res.size()]);
      
   }
   
   public static String[] match(String s, String pattern) {

	   return match(s, pattern, DEFAULT_ESCAPE_CHAR);
   }
   
   public static boolean matches(String s, String pattern) {
	   
	   return matches(s, pattern, DEFAULT_ESCAPE_CHAR);
   }
   
   public static String[] match(String s, String pattern, char escapeChar) {
	   
	      Regex r = new Regex(s, pattern, escapeChar);
	      
	      return r.match();
   }
   
   public static boolean matches(String s, String pattern, char escapeChar) {
	   
	      Regex r = new Regex(s, pattern, escapeChar);
	      
	      return r.matches();
   }
   
   public static String sub(String s, String pattern) {
      
	   return sub(s, pattern, DEFAULT_ESCAPE_CHAR);
   }
   
   public static String sub(String s, String pattern, char escapeChar) {
	   
      Regex r = new Regex(s, pattern, escapeChar);
	      
      return r.substitute();	   
   }
   
   public static String escapeValue(String value, String escapeChar) {
	   
	   return value.replaceAll("/", escapeChar + "/");
   }
   
   public static void main(String[] args) {
	   
      System.out.println("Starting...");
      
      if (Regex.matches("pelaSACOasdfdasfasf", "/saco/i")) {
    	  
    	  System.out.println("OK !!!!!!!!");
      }
      
      String[] s = Regex.match("se11io!pe22la!sa33co", "/(#d+#w+)/g" , '#');
      
      if (s != null) {
         
         int x = s.length;
         
         for(int i=0;i<x;i++) {
            
            System.out.println(s[i]);
         }
         
      } else {
         
         System.out.println("NOT FOUND!");
      }
      
      String res = Regex.sub("sergio33pela44", "s/#d+/AA/gi", '#');
      
      System.out.println("RES: " + res);
      
      res = Regex.sub("sergioAAAfodaAAA", "s/AAA//g");
      
      System.out.println(res);
      
      System.out.println("Testing sub for /");
      
      res = Regex.sub("sergioAAAbola", "s/AAA/#/Z#/Z#//g", '#');
      
      System.out.println(res);
      
      res = Regex.sub("sergioAAAbola", "s/AAA/\\/Z\\/Z\\//g");
      
      System.out.println(res);
      
      res = Regex.sub("sergioAAAbola", "s/\\/AAA/\\/Z\\/Z\\//g");
      
      System.out.println(res);
      
      res = Regex.sub("sergio/AAA/bola", "s/\\/AAA\\//\\/Z\\/Z\\//g");
      
      System.out.println(res);
      
      res = Regex.sub("sergio/AAA/bola", "s/#/AAA#//#/Z#/Z#//g", '#');
      
      System.out.println(res);
      
      res = Regex.sub("sergio/AAA/bola", "s/#/AAA#//Z#ZZ/g", '#');
      
      System.out.println(res);
      
      
      
   }
}