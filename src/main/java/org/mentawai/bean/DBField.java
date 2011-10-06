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
package org.mentawai.bean;

public class DBField {
	
	private final String name;
	
	private final DBType type;
	
	private final String dbName;
	
	private final boolean isPK;
	
	private final String foreignName;
	
	private boolean defaultToNow = false;
	
	public DBField(String name, DBType type) {
		
		this(name, name, type, false, null);
	}
	
	public DBField(String name, DBType type, boolean isPK) {
		
		this(name, name, type, isPK, isPK ? name : null);
	}
	
	public DBField(String name, DBType type, boolean isPK, String foreignName) {
		
		this(name, name, type, isPK, isPK ? foreignName : null);
		
	}
	
	public DBField(String name, String dbName, DBType type) {
		
		this(name, dbName, type, false, null);
	}
	
	public DBField(String name, String dbName, DBType type, boolean isPK) {
		
		this(name, dbName, type, isPK, isPK ? dbName : null);
	}
	
	public DBField(String name, String dbName, DBType type, boolean isPK, String foreignName) {
		
		this.name = name;
		
		this.dbName = dbName;
		
		this.type = type;
		
		this.isPK = isPK;
		
		this.foreignName = foreignName;
	}
	
	public void setDefaultToNow(boolean b) {
		
		this.defaultToNow = b;
	}
	
	public boolean isDefaultToNow() {
		
		return defaultToNow;
	}
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder(32);
		
		sb.append("DBField: ").append(name).append(" type=").append(type).append(" dbName=").append(dbName);
		
		return sb.toString();
	}
	
	public int hashCode() {
		
		return name.hashCode();
	}
	
	public boolean equals(Object obj) {
		
		if (obj instanceof DBField) {
			
			DBField f = (DBField) obj;
			
			if (f.name.equalsIgnoreCase(this.name)) return true;
		}
		
		return false;
	}
	
	public String getName() {
		
		return name;
		
	}
	
	public String getForeignName() {
		
		return foreignName;
	}
	
	public DBType getType() {
		
		return type;
	}
	
	public String getDbName() {
		
		return dbName;
	}
	
	public boolean isPK() {
		
		return isPK;
	}
	
	
}