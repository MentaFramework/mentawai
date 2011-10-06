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


/**
 * A rule to validate number of options. (derived from IntegerRule)
 * 
 * You can also specify min and max values.
 *
 * @author Sergio Oliveira
 */
public class OptionsRule implements Rule {
	
	private boolean noMin = true;
	private boolean noMax = true;
	private int min;
	private int max;
	private Map<String, String> tokens = new HashMap<String, String>();
	
	private static final Map<String, OptionsRule> cache = new HashMap<String, OptionsRule>();
	
    /**
     * Creates a IntegerRule with a min value.
     *
     * @param min The minimum value for the integer.
     */
	public OptionsRule(int min) {
		noMin = false;
		this.min = min;
		tokens.put("min", String.valueOf(min));
	}
	
    /**
     * Creates a IntegerRule with a min and max values.
     *
     * @param min The minium value for the integer.
     * @param max The maximum value for the integer.
     */
	public OptionsRule(int min, int max) {
		this(min);
		noMax = false;
		this.max = max;
		tokens.put("max", String.valueOf(max));
	}
	
	public static OptionsRule getInstance(int min) {
		
		StringBuilder sb = new StringBuilder(16);
		
		sb.append(min).append("_null");
		
		String key = sb.toString();
		
		OptionsRule ir = cache.get(key);
		
		if (ir != null) return ir;
		
		ir = new OptionsRule(min);
		
		cache.put(key, ir);
		
		return ir;
	}
	
	public static OptionsRule getInstance(int min, int max) {
		
		StringBuilder sb = new StringBuilder(16);
		
		sb.append(min).append('_').append(max);
		
		String key = sb.toString();
		
		OptionsRule ir = cache.get(key);
		
		if (ir != null) return ir;
		
		ir = new OptionsRule(min, max);
		
		cache.put(key, ir);
		
		return ir;
	}
	
	public Map<String, String> getTokens() {
		return tokens;
	}
	
	public boolean check(String field, Action action) {
		
		int[] options = action.getInput().getInts(field);
		
		if (options == null) return true; // maybe optional?
		
		if (!noMin && options.length < min) return false;
		
		if (!noMax && options.length > max) return false;
		
		return true;
	}
}