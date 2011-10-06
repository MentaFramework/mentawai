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
 * A validation rule that checks for words.
 *
 * @author Sergio Oliveira
 */
public class ContainsRule extends BasicRule {
	
	private String[] words;
	
	private final static Map<String, ContainsRule> cache = new HashMap<String, ContainsRule>();
	
    /**
     * Creates a RegexRule with the given regex pattern.
     *
     * @param pattern The regex pattern.
     */
	public ContainsRule(String ... words) {
		this.words = words;
	}
	
	private static String getKey(String ... words) {
		
		StringBuilder sb = new StringBuilder(64);
		
		for(String w : words) {
			
			sb.append(w).append("_");
		}
		
		sb.delete(sb.length() - 1, sb.length());
		
		return sb.toString();
	}
	
	public static ContainsRule getInstance(String ... words) {
		
		String key = getKey(words);
		
		ContainsRule cr = cache.get(key);
		
		if (cr != null) return cr;
		
		cr = new ContainsRule(words);
		
		cache.put(key, cr);
		
		return cr;
		
	}
	
	
	public Map<String, String> getTokens() {
		return null;
	}
	
	public boolean check(String value) {
		
		value = value.toLowerCase().trim();
		
		for(String w : words) {
			
			w = w.toLowerCase();
			
			if (value.equals(w)) return false;
			
			if (value.startsWith(w + " ")) return false;
			
			if (value.endsWith(" " + w)) return false;
			
			if (value.contains(" " + w + " ")) return false;
		}
		
		return true;
	}
}
