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

import java.util.HashMap;
import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.Input;

/**
 * Make a field required only and only if another
 * field is present in the form.
 * 
 * @author Sergio Oliveira Jr.
 */
public class DependentRule implements Rule {
	
	private final static Map<String, DependentRule> cache = new HashMap<String, DependentRule>();
	
	private final String field1, field2;
	
	private final Map<String, String> tokens = new HashMap<String, String>();
	
	public DependentRule(String field1, String field2) {
		this.field1 = field1;
		this.field2 = field2;
		
		tokens.put("field1", field1);
		tokens.put("field2", field2);
	}
	
	public static DependentRule getInstance(String field1, String field2) {
		
		StringBuilder sb = new StringBuilder(64);
		
		sb.append(field1).append('_').append(field2);
		
		String key = sb.toString();
		
		DependentRule er = cache.get(key);
		
		if (er != null) return er;
		
		er = new DependentRule(field1, field2);
		
		cache.put(key, er);
		
		return er;
	}

	
    public boolean check(String field, Action action) {
    	
    	Input input = action.getInput();
    	
        String f1 = input.getString(field1);
        String f2 = input.getString(field2);
        
        boolean firstFieldPresent = f1 != null && !f1.trim().equals("");
        
        if (firstFieldPresent) { // then second field must be present...
        	boolean secondFieldPresent = f2 != null && !f2.trim().equals("");
        	if (!secondFieldPresent) return false;
        }
        return true;
    }
    
    public Map<String, String> getTokens() {
        return tokens;
    }        
	
}
