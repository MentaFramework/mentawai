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
 * A rule to validat Strings.
 * It can also check for min and max values.
 *
 * @author Sergio Oliveira
 */
public class StringRule extends BasicRule {
	
	private final static Map<String, StringRule> cache = new HashMap<String, StringRule>();
	
	private boolean noMin = true;
	private boolean noMax = true;
	private int min;
	private int max;
	private Map<String, String> tokens = new HashMap<String, String>();
	
    /**
     * Creates a StringRule.
     */
	public StringRule() { }
	
    /**
     * Creates a StringRule with a min value.
     *
     * @param min The min value to validate.
     */
	public StringRule(int min) {
		if (min < 0) throw new IllegalArgumentException("min cannot be negative for strings!");
		noMin = false;
		this.min = min;
		tokens.put("min", String.valueOf(min));
	}
	
    /**
     * Creates a StringRule with a min and max values.
     *
     * @param min The min value to validate.
     * @param max The max value to validate.
     */
	public StringRule(int min, int max) {
		this(min);
		if (max <= 0) throw new IllegalArgumentException("max cannot be negative or zero for strings!");
		noMax = false;
		this.max = max;
		tokens.put("max", String.valueOf(max));
	}
	
	public static StringRule getInstance() {
		
		String key = "null_null";
		
		StringRule ir = cache.get(key);
		
		if (ir != null) return ir;
		
		ir = new StringRule();
		
		cache.put(key, ir);
		
		return ir;
	}
	
	public static StringRule getInstance(int min) {
		
		StringBuilder sb = new StringBuilder(16);
		
		sb.append(min).append("_null");
		
		String key = sb.toString();
		
		StringRule ir = cache.get(key);
		
		if (ir != null) return ir;
		
		ir = new StringRule(min);
		
		cache.put(key, ir);
		
		return ir;
	}
	
	public static StringRule getInstance(int min, int max) {
		
		StringBuilder sb = new StringBuilder(16);
		
		sb.append(min).append('_').append(max);
		
		String key = sb.toString();
		
		StringRule ir = cache.get(key);
		
		if (ir != null) return ir;
		
		ir = new StringRule(min, max);
		
		cache.put(key, ir);
		
		return ir;
	}
	
	
	public Map<String, String> getTokens() {
		return tokens;
	}
	
	public boolean check(String value) {
		
		if (!noMin && value.length() < min) return false;
		
		if (!noMax && value.length() > max) return false;
		
		return true;
	}
}
