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
package org.mentawai.tools;

import java.lang.reflect.*;

public class BeanConfig {
	
	private static void printFormat() {
		
		System.out.println("format: java org.mentawai.tools.BeanConfig <full-classname>");
	}
	
	public static void main(String[] args) throws Exception {
		
		if (args.length == 0) {
			
			printFormat();
			
			return;
		}
		
		BeanConfig bc = new BeanConfig();
		
		bc.print(args[0]);

	}
	
	public void print(String className) throws Exception {
		
		
		Class<? extends Object> klass;
		
		try {
			
			klass = Class.forName(className);
			
		} catch(Exception e) {
			
			System.out.println("Error trying to resolve classname: " + className);
			
			e.printStackTrace();
			
			return;
		}
		
		Field[] fields = klass.getDeclaredFields();
		
		for(int i=0;i<fields.length;i++) {
			
			Field f = fields[i];
			
			if (Modifier.isStatic(f.getModifiers())
				|| Modifier.isFinal(f.getModifiers())) {
				
				continue;
			}
			
			Class<? extends Object> type = f.getType();
			
			String name = f.getName();
			
			System.out.println("\t.field(\"" + name + "\", \tDBTypes." + getDBType(type) + ")");
			
		}
	}
	
	protected String getDBType(Class<? extends Object> type) {
		
		if (type.equals(String.class)) {
			
			return "STRING";
			
		} else if (type.equals(Integer.class) || type.equals(int.class)) {
			
			return "INTEGER";
			
		} else if (type.equals(Double.class) || type.equals(double.class)) {
			
			return "DOUBLE";
			
		} else if (type.equals(Float.class) || type.equals(float.class)) {
			
			return "FLOAT";
			
		} else if (type.equals(Long.class) || type.equals(long.class)) {
			
			return "LONG";
			
		} else if (type.equals(java.sql.Date.class) || type.equals(java.util.Date.class)) {
			
			return "DATE";
			
		} else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
			
			return "BOOLEANINT";
		}
		
		return "????";
		
	}
	
	
}