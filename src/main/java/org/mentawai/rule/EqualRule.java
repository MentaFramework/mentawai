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

/**
 * A validation rule that compares two action input fields for equality.
 *
 * @author Sergio Oliveira
 */
public class EqualRule extends CrossRule {
	
	private final static Map<String, EqualRule> cache = new HashMap<String, EqualRule>();
	
	private final String[] fields;
	
    /**
     * Creates a EqualRule for comparing the fields given.
     *
     * @param field1 The first field
     * @param field2 The second file
     */
	public EqualRule(String field1, String field2) {
		this.fields = new String[] { field1, field2 };
	}
	
	public static EqualRule getInstance(String field1, String field2) {
		
		StringBuilder sb = new StringBuilder(64);
		
		sb.append(field1).append('_').append(field2);
		
		String key = sb.toString();
		
		EqualRule er = cache.get(key);
		
		if (er != null) return er;
		
		er = new EqualRule(field1, field2);
		
		cache.put(key, er);
		
		return er;
	}
	
	protected String[] getFieldsToValidate() {
		
		return fields;
	}
	
	public boolean check(String[] values) {
		
		return values[0].equals(values[1]);
	}
	
	public Map<String, String> getTokens() {
		return null;
	}
}
