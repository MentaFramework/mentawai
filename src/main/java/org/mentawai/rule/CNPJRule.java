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
  
import java.util.regex.Pattern;
  
 /**
  * A rule to validate a CNPJ.
  * 
  * @author Robert Anderson N. de Oliveira
  */
 public class CNPJRule extends BasicRule {
	 
	 private static CNPJRule cache = null;
	 
	 public static CNPJRule getInstance() {
		 
		 if (cache != null) return cache;
		 
		 cache = new CNPJRule();
		 
		 return cache;
	 }
  
    public boolean check(String value) {
        
    	value = value.replaceAll("\\D","");
        
        char[] digits = value.toCharArray();
  
        if (digits.length != 14) return false;
  
        if(Pattern.matches("^" + digits[0] + "{14}$", value)) return false;
  
        int j,n; 
        
        int[] factors = new int[] {6,5,4,3,2,9,8,7,6,5,4,3,2};
        
        for (j = 0, n = 0; j < 12; n += Character.getNumericValue(digits[j++]) * factors[j]);
        
        if(Character.getNumericValue(digits[12]) != (((n %= 11) < 2) ? 0 : 11 - n)) return false;
 
        for (j = 0, n = 0; j <= 12; n += Character.getNumericValue(digits[j]) * factors[j++]);
        
        if(Character.getNumericValue(digits[13]) != (((n %= 11) < 2) ? 0 : 11 - n)) return false;
                 
        return true;
    }
 }