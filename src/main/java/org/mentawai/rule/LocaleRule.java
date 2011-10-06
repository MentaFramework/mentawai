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

import java.util.Locale;
import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.Input;

/**
 * A rule that depends on the user locale for validation.
 *
 * @author Sergio Oliveira
 */
public abstract class LocaleRule implements Rule {
    
    /**
     * Check a single input value from this action considering its locale.
     * Override this method to implement a locale rule for validation.
     *
     * @param value The value to validate.
     * @param loc The locale to consider in the validation.
     * @return true if the validation was successful.
     */	
	public abstract boolean check(String value, Locale loc);
    
    public boolean check(String field, Action action) {
    	
        Input input = action.getInput();
        
        Locale loc = action.getLocale();
        
        Object value = input.getValue(field);
        
        if (value == null || value.toString().trim().equals("")) {
        	
        	// if we got to this point, it means that there is no RequiredRule
        	// in front of this rule. Therefore this field is probably an OPTIONAL
        	// field, so if it is NULL or EMPTY we don't want to do any
        	// futher validation and return true to allow it.
        	
        	return true; // may be optional
        }
        
        return check(value.toString().trim(), loc);
    }
    
    public Map<String, String> getTokens() {
        return null;
    }    
	
}
