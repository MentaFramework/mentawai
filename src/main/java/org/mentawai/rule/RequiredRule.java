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
 * A validation rule that checks if an action input value is present, in other workds, not null and not empty!
 *
 * @author Sergio Oliveira
 */
public class RequiredRule implements Rule {
	
	private static RequiredRule cache = null;
	
	public static RequiredRule getInstance() {
		
		if (cache != null) return cache;
		
		cache = new RequiredRule();
		
		return cache;
		
	}
	
	public boolean check(String field, Action action) {
		
		Input input = action.getInput();
		
		Object value = input.getValue(field);
		
		if (value == null || value.toString().trim().equals("")) return false;
		
		return true;
		
	}
	
	public Map<String, String> getTokens() {
		
		return null;
		
	}
}
