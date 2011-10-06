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

import java.util.Comparator;

/**
 * Compare two strings, not taking into account accents!
 * 
 * Based on the code found here: http://www.rgagnon.com/javadetails/java-0456.html
 * 
 * @author Sergio Oliveira
 */
public class StringComparator implements Comparator {
	
	public int compare(Object o1, Object o2) {
		String s1 = (String) o1;
		String s2 = (String) o2;
		
		if (s1 == null && s2 == null) return 0;
		if (s1 == null) return -1;
		if (s2 == null) return 1;
		
		s1 = removeAccents(s1).toLowerCase();
		s2 = removeAccents(s2).toLowerCase();
		
		return s1.compareTo(s2);
	}
	
	private static final String PLAIN_ASCII =
		      "AaEeIiOoUu"    // grave
		    + "AaEeIiOoUuYy"  // acute
		    + "AaEeIiOoUuYy"  // circumflex
		    + "AaEeIiOoUuYy"  // tilde
		    + "AaEeIiOoUuYy"  // umlaut
		    + "Aa"            // ring
		    + "Cc"            // cedilla
		    ;

	private static final String UNICODE =
		      "\u00C0\u00E0\u00C8\u00E8\u00CC\u00EC\u00D2\u00F2\u00D9\u00F9"             // grave
		    + "\u00C1\u00E1\u00C9\u00E9\u00CD\u00ED\u00D3\u00F3\u00DA\u00FA\u00DD\u00FD" // acute
		    + "\u00C2\u00E2\u00CA\u00EA\u00CE\u00EE\u00D4\u00F4\u00DB\u00FB\u0176\u0177" // circumflex
		    + "\u00C2\u00E2\u00CA\u00EA\u00CE\u00EE\u00D4\u00F4\u00DB\u00FB\u0176\u0177" // tilde
		    + "\u00C4\u00E4\u00CB\u00EB\u00CF\u00EF\u00D6\u00F6\u00DC\u00FC\u0178\u00FF" // umlaut
		    + "\u00C5\u00E5"                                                             // ring
		    + "\u00C7\u00E7"                                                             // cedilla
		    ;

    // remove accentued from a string and replace with ascii equivalent
    public static String removeAccents(String s) {
    	
       StringBuffer sb = new StringBuffer();
       int n = s.length();
       for (int i = 0; i < n; i++) {
          char c = s.charAt(i);
          int pos = UNICODE.indexOf(c);
          if (pos > -1){
              sb.append(PLAIN_ASCII.charAt(pos));
          }
          else {
              sb.append(c);
          }
       }
       return sb.toString();
       
    }
}
