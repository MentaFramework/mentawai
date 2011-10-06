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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.mentawai.bean.type.AutoIncrementType;
import org.mentawai.bean.type.SequenceType;

public class BeanConfig {
	
	private final List<DBField> fieldList = new LinkedList<DBField>();
	
	private final List<DBField> pkList = new LinkedList<DBField>();
	
	private final Map<Join, JoinConfig> joins = new HashMap<Join, JoinConfig>();
	
	private final Class<? extends Object> beanClass;
	
	private final String tableName;
	
	private DBField sequence = null;
	
	private DBField autoincrement = null;
	
	public BeanConfig(Class<? extends Object> beanClass, String tableName) {
		
		this.beanClass = beanClass;
		
		this.tableName = tableName;
	}
	
	public String getTableName() {
		
		return tableName;
	}
	
	public Class<? extends Object> getBeanClass() {
		
		return beanClass;
	}
	
	public JoinConfig getJoinConfig(Class<? extends Object> klass) {
		
		Join j = new Join(this.beanClass, klass);
		
		return joins.get(j);
	}
	
	public BeanConfig addJoin(JoinConfig join) {
		
		Join j = new Join(this.beanClass, join.getBeanClass());
		
		joins.put(j, join);
		
		return this;
	}
	
	public BeanConfig join(JoinConfig join) {
		
		return addJoin(join);
	}
	
	public BeanConfig addJoin(Class<? extends Object> klass, String tableName) {
		
		return addJoin(new JoinConfig(klass, tableName));
	}
	
	public BeanConfig join(Class<? extends Object> klass, String tableName) {
		
		return addJoin(klass, tableName);
	}
	
	public BeanConfig addField(String name, DBType type) {
		
		return addField(name, name, type, false, null);
	}
	
	public BeanConfig addField(String name, String dbName, DBType type) {
		
		return addField(name, dbName, type, false, null);
	}
	
	public BeanConfig addField(String name, DBType type, boolean isPK) {
		
		return addField(name, name, type, isPK, isPK ? name : null);
	}
	
	public BeanConfig addField(String name, DBType type, boolean isPK, String foreignName) {
		
		return addField(name, name, type, isPK, isPK ? foreignName : null);
	}
	
	public BeanConfig addField(String name, String dbName, DBType type, boolean isPK) {
		
		return addField(name, dbName, type, isPK, isPK ? dbName : null);
	}
	
	private DBField findDBField(String name) {
		
		Iterator<DBField> iter = fieldList.iterator();
		
		while(iter.hasNext()) {
			
			DBField f = iter.next();
			
			if (f.getName().equals(name)) return f;
		}
		
		return null;
	}
	
	public BeanConfig addField(String name, String dbName, DBType type, boolean isPK, String foreignName) {
		
		DBField f = new DBField(name, dbName, type, isPK, foreignName);
		
		fieldList.add(f);
		
		if (isPK) {
			
			pkList.add(f);
			
			if (type instanceof SequenceType && sequence == null) {
				
				sequence = f;
				
			} else if (type instanceof AutoIncrementType && autoincrement == null) {
				
				autoincrement = f;
			}
		}
		
		return this;
		
	}
	
	public DBField getAutoIncrementField() {
		
		return autoincrement;
	}
	
	public DBField getSequenceField() {
		
		return sequence;
	}
	
	public BeanConfig field(String name, String dbName, DBType type) {
		
		return addField(name, dbName, type);
	}
	
	public BeanConfig field(String name, String dbName, DBType type, boolean isPK) {
		
		return addField(name, dbName, type, isPK);
	}
	
	public BeanConfig pk(String name, DBType type) {
		
		return addField(name, type, true);
	}
	
	public BeanConfig defaultToNow(String name) {
		
		DBField f = findDBField(name);
		
		if (f == null) throw new IllegalArgumentException("Cannot find field with name: " + name);
		
		f.setDefaultToNow(true);
		
		return this;
	}
	
	public BeanConfig field(String name, String dbName, DBType type, boolean isPK, String foreignName) {
		
		return addField(name, dbName, type, isPK, foreignName);
	}
	
	
	public BeanConfig pk(String name, String dbName, DBType type) {
		
		return addField(name, dbName, type, true);
	}
	
	public BeanConfig field(String name, DBType type) {
		
		return addField(name, type);
	}
	
	public BeanConfig field(String name, DBType type, boolean isPK) {
		
		return addField(name, type, isPK);
	}
	
	public BeanConfig field(String name, DBType type, boolean isPK, String foreignName) {
		
		return addField(name, type, isPK, foreignName);
	}
	
	public BeanConfig pk(String name, DBType type, String foreignName) {
		
		return addField(name, type, true, foreignName);
	}
	
	public BeanConfig pk(String name, String dbName, DBType type, String foreignName) {
		
		return addField(name, dbName, type, true, foreignName);
	}
	
	public int getNumberOfFields() {
		
		return fieldList.size();
	}
	
	public int getNumberOfPKs() {
		
		return pkList.size();
	}
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder(64);
		
		sb.append("BeanConfig: ").append(beanClass.getName()).append(" tableName=").append(tableName);
		
		return sb.toString();
	}
	
	public Iterator<DBField> fields() {
		
		return fieldList.iterator();
	}
	
	public boolean hasPK() {
		
		return !pkList.isEmpty();
	}
	
	public DBField getFirstPK() {
		
		return pkList.get(0);
	}
	
	public Iterator<DBField> pks() {
		
		return pkList.iterator();
	}
	
	private static class Join {
		
		Class<? extends Object> klass1;
		
		Class<? extends Object> klass2;
		
		public Join(Class<? extends Object> k1, Class<? extends Object> k2) {
			
			klass1 = k1;
			
			klass2 = k2;
		}
		
		public int hashCode() {
			
			return klass1.hashCode() * 31 + klass2.hashCode();
		}
		
		public boolean equals(Object obj) {
			
			if (obj instanceof Join) {
				
				Join j = (Join) obj;
				
				if (j.klass1.equals(this.klass1) && j.klass2.equals(this.klass2)) {
					
					return true;
				}
			}
			
			return false;
		}
	}
}