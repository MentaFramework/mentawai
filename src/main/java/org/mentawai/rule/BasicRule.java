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

import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.Input;

/**
 * The simplest validation rule that validate a single action input field.
 * 
 * @author Sergio Oliveira
 */
public abstract class BasicRule implements Rule {

    /**
     * Check a single input value from this action.
     * Override this method to implement a basic rule for validation.
     *
     * @param value The value to validate.
     * @return true if the validation was successful.
     */
	public abstract boolean check(String value);
    
    public boolean check(String field, Action action) {
    	
        Input input = action.getInput();
        
        Object value = input.getValue(field);
        
        if (value == null || value.toString().trim().equals("")) {
        	
        	// if we got to this point, it means that there is no RequiredRule
        	// in front of this rule. Therefore this field is probably an OPTIONAL
        	// field, so if it is NULL or EMPTY we don't want to do any
        	// futher validation and return true to allow it.
        	
        	return true; // may be optional
        }
        
        return check(value.toString().trim());
    }
    
    public Map<String, String> getTokens() {
        return null;
    }
}
