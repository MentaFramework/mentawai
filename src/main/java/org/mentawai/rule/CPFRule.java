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
  * A rule to validate a CPF.
  * 
  * @author Robert Anderson N. de Oliveira
  */
 public class CPFRule extends BasicRule{
	 
	 private static CPFRule cache = null;
	 
	 public static CPFRule getInstance() {
		 
		 if (cache != null) return cache;
		 
		 cache = new CPFRule();
		 
		 return cache;
	 }
 
    public boolean check(String value) {
        
    	value = value.replaceAll("\\D","");
        
        char[] digits = value.toCharArray();
 
        if (digits.length != 11) return false;
 
        if(Pattern.matches("^" + digits[0] + "{11}$", value)) return false;
 
        int j,n,i; 
        
        for(j = 10, n = 0, i = 0; j >= 2; n +=  Character.getNumericValue(digits[i++]) * j--);
        
        if(Character.getNumericValue(digits[9]) != (((n %= 11) < 2) ? 0 : 11 - n)) return false;
 
        for(j = 11, n = 0, i = 0; j >= 2; n += Character.getNumericValue(digits[i++]) * j--);
        
        if(Character.getNumericValue(digits[10]) != (((n %= 11) < 2) ? 0 : 11 - n)) return false;       
 
        return true;
    }
 }