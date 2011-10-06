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
 * A validation rule applied with a regular expression pattern.
 *
 * @author Sergio Oliveira
 */
public class RegexRule extends BasicRule {
	
	private final static Map<String, RegexRule> cache = new HashMap<String, RegexRule>();
	
	private String pattern;
	
    /**
     * Creates a RegexRule with the given regex pattern.
     *
     * @param pattern The regex pattern.
     */
	public RegexRule(String pattern) {
		this.pattern = pattern;
	}
	
	public static RegexRule getInstance(String pattern) {
		
		RegexRule rr = cache.get(pattern);
		
		if (rr != null) return rr;
		
		rr = new RegexRule(pattern);
		
		cache.put(pattern, rr);
		
		return rr;
		
	}
	
	
	public Map<String, String> getTokens() {
		return null;
	}
	
	public boolean check(String value) {
		
		if (!value.matches(pattern)) return false;
		
		return true;
	}
}
