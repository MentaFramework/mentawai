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
 * A rule that compares two or more action input fields for validation.
 *
 * @author Sergio Oliveira
 */
public abstract class CrossRule implements Rule {
	
	/**
	 * Subclasses should implement this method to indicate which fields
	 * from the action input they want to receive for validation.
	 * 
	 * @return String array with the name of the fields
	 */
	protected abstract String[] getFieldsToValidate();
	
    /**
     * Check different values from an action input for the validation.
     * Override this method to implement a cross rule for validation.
     *
     * @param values The values to compare.
     * @return true if the validation was successful.
     */		
	public abstract boolean check(String[] values);
    
    public boolean check(String field, Action action) {
    	
    	Input input = action.getInput();
    	
    	String[] fields = getFieldsToValidate();
    	
    	if (fields == null) throw new NullPointerException("Rule returned NULL for the fields!");
    	
        String[] values = new String[fields.length];
        
        for(int i=0;i<fields.length;i++) {
        	
        	Object o = input.getValue(fields[i]);
        	
        	if (o == null || o.toString().trim().equals("")) return true; // may be optional...
        	
        	values[i] = o.toString(); // no trim here: we are comparing stuff !!!
        }
        
        return check(values);
    }
    
    public Map<String, String> getTokens() {
        return null;
    }        
	
}
