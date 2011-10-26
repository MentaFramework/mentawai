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

import java.lang.reflect.Method;
import java.util.Map;

import org.mentawai.util.FindMethod;

/**
 * A validation rule that calls a method by reflection to validate.
 * Good for validation that requires database access for example.
 *
 * @author Sergio Oliveira
 */
public class MethodRule extends BasicRule {
	
	private final Object target;
	private final String methodName;
	
	private Method methodWithParam = null;
	private Method methodWithoutParam = null;
	
	public MethodRule(Object target, String methodName) {
		this.target = target;
		this.methodName = methodName;
	}
	
	public static MethodRule getInstance(Object target, String methodName) {
		
		/**
		 * Attention: Because the fact that the target will change every time (different actions)
		 * we cannot cache this rule.
		 */
		
		return new MethodRule(target, methodName);
	}
	
	
	public Map<String, String> getTokens() {
		return null;
	}
	
	public boolean check(String value) {
		
		// first try method WITHOUT any parameter...
		
		try {
			
			if (methodWithoutParam == null) {
			
				methodWithoutParam = FindMethod.getMethod(target.getClass(), methodName, null);
			
			}
			
			return (Boolean) methodWithoutParam.invoke(target, (Object[]) null);
			
		} catch(Exception e) { }
		
		// now with parameter...
		
		try {
			
			if (methodWithParam == null) {
				
				methodWithParam = FindMethod.getMethod(target.getClass(), methodName, new Class[] { String.class });
			}
			
			return (Boolean) methodWithParam.invoke(target, value);
			
		} catch(Exception e) { }

		throw new RuntimeException("Cannot find method to invoke: " + methodName);
	}
}
