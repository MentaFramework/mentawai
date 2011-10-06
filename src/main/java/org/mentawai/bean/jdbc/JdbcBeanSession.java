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
package org.mentawai.bean.jdbc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.mentawai.bean.BeanConfig;
import org.mentawai.bean.BeanException;
import org.mentawai.bean.BeanSession;
import org.mentawai.bean.DBField;
import org.mentawai.bean.DBType;
import org.mentawai.bean.JoinConfig;
import org.mentawai.bean.type.AutoIncrementType;
import org.mentawai.bean.type.AutoTimestampType;
import org.mentawai.bean.type.DateType;
import org.mentawai.bean.type.TimeType;
import org.mentawai.bean.type.TimestampType;
import org.mentawai.core.ApplicationManager;
import org.mentawai.transaction.JdbcTransaction;
import org.mentawai.transaction.Transaction;
import org.mentawai.util.InjectionUtils;

public class JdbcBeanSession implements BeanSession {
	
	public static boolean DEBUG = false;
	
	protected IdentityHashMap<Object, Map<String, Value>> loaded = new IdentityHashMap<Object, Map<String, Value>>();
	
	protected Connection conn;
	
	protected final ApplicationManager appManager;
	
	public JdbcBeanSession() {
		
		this.appManager = ApplicationManager.getInstance();
	}
	
	public JdbcBeanSession(Connection conn) {
		
		this();
		
		this.conn = conn;
	}
	
	public void setConn(Connection conn) {
		
		this.conn = conn;
		
	}
	
	public void setConnection(Connection conn) {
		
		this.conn = conn;
	}
	
	public Connection getConnection() {
		
		return conn;
	}
	
	protected String getNow() {
		
		return null;
	}
	
	public Transaction beginTransaction() throws Exception {
		
		if (conn == null) throw new BeanException("conn == null when creating a transaction!");
		
		Transaction t = new JdbcTransaction(conn);
		
		t.begin();
		
		return t;
	}
	
	protected static Object getValueFromBean(Object bean, String fieldName) throws Exception {
		
		return getValueFromBean(bean, fieldName, null);
		
	}
	
	protected static Object getValueFromBean(Object bean, String fieldName, Method m) throws Exception {
		
		if (m == null) {
		
			m = InjectionUtils.findMethodToGet(bean.getClass(), fieldName);
			
		}
		
		if (m == null) throw new BeanException("Cannot find method to get field from bean: " + fieldName);
		
		Object value = null;
		
		value = m.invoke(bean, (Object[]) null);
		
		return value;
	}
	
	private static void checkPK(Object value, DBField dbField) throws BeanException {
		
		if (value == null) {
			
			throw new BeanException("pk is missing: " + dbField);
			
		} else if (value instanceof Number) {
		
			Number n = (Number) value;
				
			if (n.doubleValue() <= 0) {
				
				throw new BeanException("Number pk is missing: " + dbField);
			}
		}
	}
	
	public boolean load(Object bean) throws Exception {
		
		BeanConfig bc = appManager.getBeanConfig(bean.getClass());
		
		if (bc == null) throw new BeanException("Cannot find bean config: " + bean.getClass());
		
		if (bc.getNumberOfFields() == 0) throw new BeanException("BeanConfig has zero fields: " + bc);
		
		StringBuilder sb = new StringBuilder(32 * bc.getNumberOfFields());
		
		sb.append("SELECT ");
		
		Iterator<DBField> iter = bc.fields();
		
		int count = 0;
		
		while(iter.hasNext()) {
			
			String fieldName = iter.next().getDbName();
			
			if (count++ > 0) sb.append(',');
			
			sb.append(fieldName);
			
		}
		
		sb.append(" FROM ").append(bc.getTableName()).append(" WHERE ");
		
		if (!bc.hasPK()) throw new BeanException("Cannot load bean without a PK!");
		
		iter = bc.pks();
		
		count = 0;
		
		List<Value> values = new LinkedList<Value>();
		
		while(iter.hasNext()) {
			
			DBField dbField = iter.next();
			
			String fieldName = dbField.getName();
			
			String dbFieldName = dbField.getDbName();
			
			Object value = getValueFromBean(bean, fieldName);
			
			checkPK(value, dbField);
			
			if (count++ > 0) sb.append(" AND ");
			
			sb.append(dbFieldName).append("=?");
			
			values.add(new Value(dbField, value));
			
		}
		
		if (values.isEmpty()) throw new BeanException("Bean is empty: " + bean + " / " + bc);
		
		if (conn == null) throw new BeanException("Connection is null!");
		
		PreparedStatement stmt = null;
		
		ResultSet rset = null;
		
		
		try {
			
			if (DEBUG) System.out.println("LOAD SQL: " + sb.toString());
			
			stmt = conn.prepareStatement(sb.toString());
			
			Iterator<Value> iter2 = values.iterator();
			
			int index = 0;
			
			while(iter2.hasNext()) {
				
				Value v = iter2.next();
				
				v.field.getType().bindToStmt(stmt, ++index, v.value);
				
			}
			
			rset = stmt.executeQuery();
			
			index = 0;
			
			Map<String, Value> fieldsLoaded = new HashMap<String, Value>();
			
			if (rset.next()) {
				
				iter = bc.fields();
				
				while(iter.hasNext()) {
					
					DBField f = iter.next();
					
					String fieldName = f.getName();
					
					DBType type = f.getType();
					
					Object value = type.getFromResultSet(rset, ++index);
					
					injectValue(bean, fieldName, value, type.getTypeClass());
					
					fieldsLoaded.put(fieldName, new Value(f, value));
				}
				
			} else {
				
				return false;
			}
			
			if (rset.next()) throw new BeanException("Load returned more than one row!");
			
			loaded.put(bean, fieldsLoaded);
			
			return true;
			
		} finally {
			
			if (rset != null) try { rset.close(); } catch(Exception e) { }
			
			if (stmt != null) try { stmt.close(); } catch(Exception e) { }
		}
	}
	
	protected static void injectValue(Object bean, String fieldName, Object value, Class<? extends Object> valueType) throws BeanException {
		
		Method m = InjectionUtils.findMethodToInject(bean.getClass(), fieldName, value == null ? valueType : value.getClass());
		
		if (m == null) {
			
			// try field...
			
			Field field = InjectionUtils.findFieldToInject(bean.getClass(), fieldName, value == null ? valueType : value.getClass());
			
			if (field != null) {
			
				try {
				
					field.set(bean, value);
					
				} catch(Exception e) {
					
					e.printStackTrace();
					
					throw new BeanException(e);
				}
			
			} else {
				
				throw new BeanException("Cannot find field or method to inject: " + bean + " / " + fieldName);
			}
			
		} else {
			
			// try method..
			
			try {
				
				m.invoke(bean, value);
				
			} catch(Exception e) {
				
				e.printStackTrace();
				
				throw new BeanException(e);
			}
		}
	}
	
	protected StringBuilder handleLimit(StringBuilder sb, String orderBy, int limit) {
		
		return sb;
	}
	
	public String buildSelect(Class<? extends Object> beanClass) {
		
		return buildSelectImpl(beanClass, null, null);
	}
	
	public String buildSelect(Class<? extends Object> beanClass, String tablePrefix) {
		
		return buildSelectImpl(beanClass, tablePrefix, null);
		
	}
	
	public String buildSelectMinus(Class<? extends Object> beanClass, String[] minus) {
		
		return buildSelectImpl(beanClass, null, minus);
	}
	
	public String buildSelectMinus(Class<? extends Object> beanClass, String tablePrefix, String[] minus) {
		
		return buildSelectImpl(beanClass, tablePrefix, minus);
	}
	
	private String buildSelectImpl(Class<? extends Object> beanClass, String tablePrefix, String[] minus) {
		
		BeanConfig bc = appManager.getBeanConfig(beanClass);
		
		if (bc == null) return null;
		
		StringBuilder sb = new StringBuilder(32 * bc.getNumberOfFields());
		
		Iterator<DBField> iter = bc.fields();
		
		int count = 0;
		
		while(iter.hasNext()) {
			
			DBField field = iter.next();
			
			String dbField = field.getDbName();
			
			if (minus != null && minus.length > 0) {
				
				String name = field.getName();
				
				if (checkArray(name, minus)) continue;
			}
			
			if (count++ > 0) sb.append(",");
			
			if (tablePrefix != null) {
				
				sb.append(tablePrefix).append('.');
				
				sb.append(dbField).append(' ');
				
				sb.append(tablePrefix).append('_').append(dbField);
				
			} else {
			
				sb.append(dbField);
				
			}
		}
		
		return sb.toString();
		
	}
	
	private static boolean checkArray(String value, String[] array) {
		
		if (array == null) return false;
		
		for(int i=0;i<array.length;i++) {
			
			if (array[i].equals(value)) return true;
		}
		
		return false;
	}
	
	
	public void populateBean(ResultSet rset, Object bean) throws Exception {
		
		populateBeanImpl(rset, bean, null, null);
	}
	
	public void populateBean(ResultSet rset, Object bean, String tablePrefix) throws Exception {
		
		populateBeanImpl(rset, bean, tablePrefix, null);
	}
	
	public void populateBeanMinus(ResultSet rset, Object bean, String[] minus) throws Exception {
		
		populateBeanImpl(rset, bean, null, minus);
	}
	
	public void populateBeanMinus(ResultSet rset, Object bean, String tablePrefix, String[] minus) throws Exception {
		
		populateBeanImpl(rset, bean, tablePrefix, minus);
	}
	
	private void populateBeanImpl(ResultSet rset, Object bean, String tablePrefix, String[] minus) throws Exception {
		
		BeanConfig bc = appManager.getBeanConfig(bean.getClass());
		
		if (bc == null) throw new BeanException("Cannot find bean config: " + bean.getClass());
		
		Iterator<DBField> iter = bc.fields();
		
		StringBuilder sbField = new StringBuilder(32);
		
		while(iter.hasNext()) {
			
			DBField f = iter.next();
			
			String fieldName = f.getName();
			
			if (minus != null && minus.length > 0) {
				
				if (checkArray(fieldName, minus)) continue;
			}
			
			String dbFieldName = f.getDbName();
			
			DBType type = f.getType();
			
			sbField.setLength(0);
			
			if (tablePrefix != null) {
			
				sbField.append(tablePrefix).append('_').append(dbFieldName);
				
			} else {
			
				sbField.append(dbFieldName);
				
			}
			
			Object value = type.getFromResultSet(rset, sbField.toString());
			
			injectValue(bean, fieldName, value, type.getTypeClass());
			
		}
	}
	
	public <E> List<E> loadListMinus(E bean, String[] minus, String orderBy, int limit) throws Exception {
		
		return loadListImpl(bean, minus, orderBy, limit);
	}
	
	private <E> E checkUnique(List<E> list) throws Exception {
		
		if (list == null || list.size() == 0) {
			
			return null;
			
		} else if (list.size() > 1) {
			
			throw new BeanException("Query returned more than one bean!");
			
		} else {
			
			return list.get(0);
		}
	}
	
	public <E> List<E> loadList(E bean, String orderBy, int limit) throws Exception {
		
		return loadListImpl(bean, null, orderBy, limit);
	}
	
	private <E> List<E> loadListImpl(E bean, String[] minus, String orderBy, int limit) throws Exception {
		
		if (limit == 0) return new ArrayList<E>();
		
		BeanConfig bc = appManager.getBeanConfig(bean.getClass());
		
		if (bc == null) throw new BeanException("Cannot find bean config: " + bean.getClass());
		
		StringBuilder sb = new StringBuilder(32 * bc.getNumberOfFields());
		
		Iterator<DBField> iter = bc.fields();
		
		sb.append("SELECT ");
		
		int count = 0;
		
		while(iter.hasNext()) {
			
			DBField field = iter.next();
			
			String dbField = field.getDbName();
			
			String name = field.getName();
			
			if (checkArray(name, minus)) continue;
			
			if (count++ > 0) sb.append(",");
			
			sb.append(dbField);
		}
		
		sb.append(" FROM ").append(bc.getTableName()).append(" ");
		
		iter = bc.fields();
		
		count = 0;
		
		List<Value> values = new LinkedList<Value>();
		
		while(iter.hasNext()) {
			
			DBField field = iter.next();
			
			String dbField = field.getDbName();
			
			Method m = InjectionUtils.findMethodToGet(bean.getClass(), field.getName());
			
			if (m == null) throw new BeanException("Cannot find method to get field from bean: " + field.getName());
			
			Class<? extends Object> returnType = m.getReturnType();
			
			Object value = getValueFromBean(bean, field.getName(), m);
			
			if (!isSet(value, returnType)) continue;
			
			if (count++ > 0) {
				
				sb.append(" AND ");
				
			} else {
				
				sb.append(" WHERE ");
			}
			
			sb.append(dbField).append("=?");
			
			values.add(new Value(field, value));
		}
		
		if (orderBy != null) {
			
			sb.append(" order by ").append(orderBy).append(" ");
			
		}
		
		sb =  handleLimit(sb, orderBy, limit);
		
		PreparedStatement stmt = null;
		
		ResultSet rset = null;
		
		try {
			
			String sql = sb.toString();
			
			if (DEBUG) System.out.println("LOAD LIST: " + sql);
			
			stmt = conn.prepareStatement(sql);
			
			Iterator<Value> iter2 = values.iterator();
			
			int index = 0;
			
			while(iter2.hasNext()) {
				
				Value v = iter2.next();
				
				v.field.getType().bindToStmt(stmt, ++index, v.value);
				
			}
			
			rset = stmt.executeQuery();
			
			List<E> results = new LinkedList<E>();
			
			Class<? extends Object> beanKlass = bean.getClass();
			
			int total = 0;
			
			while(rset.next()) {
				
				iter = bc.fields();
				
				index = 0;
				
				E item = (E) beanKlass.newInstance(); // not sure how to handle this generics here...
			
				while(iter.hasNext()) {
					
					DBField f = iter.next();
					
					String fieldName = f.getName();
					
					if (checkArray(fieldName, minus)) continue;
					
					DBType type = f.getType();
					
					Object value = type.getFromResultSet(rset, ++index);
					
					injectValue(item, fieldName, value, type.getTypeClass());
				}
				
				results.add(item);
				
				total++;
				
				if (limit > 0 && total == limit) {
					
					// if limit fails, we will count anyways... (does not hurt!)
					
					return results;
				}
			}
			
			return results;
			
		} finally {
			
			if (rset != null) try { rset.close(); } catch(Exception e) { }
			
			if (stmt != null) try { stmt.close(); } catch(Exception e) { }
		}
	}
	
	protected boolean isSet(Object value, Class<? extends Object> returnType) {
		
		if (value != null) {
			
			if (returnType.equals(boolean.class) && value instanceof Boolean) {
				
				// if Boolean consider TRUE to be set and FALSE to be not set (false = default value)
				
				boolean b = ((Boolean) value).booleanValue();
				
				return b;
				
			} else if (returnType.equals(char.class) && value instanceof Character) {
				
				// if Character, cast to int and assume set if different than 0...
				
				int c = (int) ((Character) value).charValue();
				
				return c != 0;
				
			} else if (returnType.isPrimitive() && !returnType.equals(boolean.class) && !returnType.equals(char.class) && value instanceof Number) {
				
				// if number consider everything different than zero to be set...
				
				Number n = (Number) value;
				
				if (n.intValue() != 0) {
					
					return true;
				}
				
			} else {
				
				// for all other objects, assume set if different than NULL...
				
				return true;
			}
		}
		
		return false;
	}

	public int update(Object bean) throws Exception {
		
		return update(bean, true);
	}
	
	public int update(Object bean, boolean dynUpdate) throws Exception {
		
		Map<String, Value> fieldsLoaded = loaded.get(bean);

		BeanConfig bc = appManager.getBeanConfig(bean.getClass());
		
		if (bc == null) throw new BeanException("Cannot find bean config: " + bean.getClass());
		
		if (bc.getNumberOfFields() == 0) throw new BeanException("BeanConfig has zero fields: " + bc);
		
		StringBuilder sb = new StringBuilder(32 * bc.getNumberOfFields());
		
		sb.append("UPDATE ").append(bc.getTableName()).append(" SET ");
		
		Iterator<DBField> iter = bc.fields();
		
		int count = 0;
		
		List<Value> values = new LinkedList<Value>();
		
		while(iter.hasNext()) {
			
			DBField dbField = iter.next();
			
			if (dbField.isPK()) continue;
			
			DBType type = dbField.getType();
			
			if (type instanceof AutoIncrementType) continue;
			
			if (type instanceof AutoTimestampType) continue;
			
			String fieldName = dbField.getName();
			
			String dbFieldName = dbField.getDbName();
			
			Method m = InjectionUtils.findMethodToGet(bean.getClass(), fieldName);
			
			if (m == null) throw new BeanException("Cannot find method to get field from bean: " + fieldName);
			
			Class<? extends Object> returnType = m.getReturnType();
			
			Object value = getValueFromBean(bean, fieldName, m);
			
			boolean update = false;
			
			if (!dynUpdate) {
				
				update = true;
				
			} else if (fieldsLoaded != null) {
				
				Value v = fieldsLoaded.get(fieldName);
				
				if (v != null) {
					
					if (value == null && v.value != null) {
						
						// nulling a field...
						
						update = true;
						
					} else if (value != null && v.value == null) {
						
						// normal stuff... was null not is not null anybmore...
						
						update = true;
						
					} else if (value == null && v.value == null) {
						
						// both null, nothing happens...
						
						update = false;
						
					} else {
						
						update = !value.equals(v.value);
					}
					
				}
				
			} else {
				
				update = isSet(value, returnType);
			}
			
			if (update) {
			
				if (count++ > 0) sb.append(',');
				
				sb.append(dbFieldName).append("=?");
				
				values.add(new Value(dbField, value));
				
			}
		}
		
		if (count == 0) return 1;
		
		sb.append(" WHERE ");
		
		if (!bc.hasPK()) throw new BeanException("Cannot update bean without a PK!");
		
		iter = bc.pks();
		
		count = 0;
		
		while(iter.hasNext()) {
			
			DBField dbField = iter.next();
			
			String fieldName = dbField.getName();
			
			String dbFieldName = dbField.getDbName();
			
			Object value = getValueFromBean(bean, fieldName);
			
			if (value == null) {
				
				throw new BeanException("pk is missing: " + dbField);
				
			} else if (value instanceof Number) {
			
				Number n = (Number) value;
					
				if (n.doubleValue() <= 0) {
					
					throw new BeanException("Number pk is missing: " + dbField);
				}
				
			}
			
			if (count++ > 0) sb.append(" AND ");
			
			sb.append(dbFieldName).append("=?");
			
			values.add(new Value(dbField, value));
			
		}
		
		if (values.isEmpty()) throw new BeanException("Bean is empty: " + bean + " / " + bc);
		
		if (conn == null) throw new BeanException("Connection is null!");
		
		PreparedStatement stmt = null;
		
		try {
			
			if (DEBUG) System.out.println("UPDATE SQL: " + sb.toString());
			
			stmt = conn.prepareStatement(sb.toString());
			
			Iterator<Value> iter2 = values.iterator();
			
			int index = 0;
			
			while(iter2.hasNext()) {
				
				Value v = iter2.next();
				
				v.field.getType().bindToStmt(stmt, ++index, v.value);
				
			}
			
			int x = stmt.executeUpdate();
			
			if (x > 1) throw new BeanException("update modified more than one line: " + x);
			
			if (x == 0) return 0;
			
			if (fieldsLoaded != null) {
			
				iter2 = values.iterator();
				
				while(iter2.hasNext()) {
					
					Value v = iter2.next();
					
					if (v.field.isPK()) continue;
					
					Value vv = fieldsLoaded.get(v.field.getName());
					
					if (vv != null) {
						
						vv.value = v.value;
					}
				}
			}
			
			return 1;
			
		} finally {
			
			if (stmt != null) try { stmt.close(); } catch(Exception e) { }
		}		
	}
	
	public void insert(Object bean) throws Exception {
		
		BeanConfig bc = appManager.getBeanConfig(bean.getClass());
		
		if (bc == null) throw new BeanException("Cannot find bean config: " + bean.getClass());
		
		if (bc.getNumberOfFields() == 0) throw new BeanException("BeanConfig has zero fields: " + bc);
		
		StringBuilder sb = new StringBuilder(32 * bc.getNumberOfFields());
		
		sb.append("INSERT INTO ").append(bc.getTableName()).append("(");
		
		Iterator<DBField> iter = bc.pks();
		
		int count = 0;
		
		List<Value> values = new LinkedList<Value>();
		
		while(iter.hasNext()) {
			
			DBField dbField = iter.next();
			
			String fieldName = dbField.getName();
			
			String dbFieldName = dbField.getDbName();
			
			DBType type = dbField.getType();
			
			if (type instanceof AutoIncrementType) continue;
			
			if (type instanceof AutoTimestampType) continue;
			
			Object value = getValueFromBean(bean, fieldName);
			
			if (count++ > 0) sb.append(',');
			
			sb.append(dbFieldName);
			
			values.add(new Value(dbField, value));
		}
		
		iter = bc.fields();
		
		while(iter.hasNext()) {
			
			DBField dbField = iter.next();
			
			if (dbField.isPK()) continue;
			
			String fieldName = dbField.getName();
			
			String dbFieldName = dbField.getDbName();
			
			DBType type = dbField.getType();
			
			if (type instanceof AutoIncrementType) continue;
			
			if (type instanceof AutoTimestampType) continue;
			
			Object value = getValueFromBean(bean, fieldName);
			
			boolean isSysdate = false;
			
			if (type instanceof DateType || type instanceof TimeType || type instanceof TimestampType) {
				
				isSysdate = dbField.isDefaultToNow() && value == null;
				
				if (isSysdate && getNow() == null) {
					
					// using JdbcBeanSession without getNow() implemented...
						
					value = new java.util.Date();
				}
			}
			
			if (count++ > 0) sb.append(',');
			
			sb.append(dbFieldName);
			
			values.add(new Value(dbField, value, isSysdate));
		}
		
		if (count == 0) throw new BeanException("There is nothing to insert!");
		
		sb.append(") VALUES(");
		
		/*
		for(int i=0;i<count;i++) {
			
			if (i > 0) sb.append(',');
			
			sb.append('?');
		}
		*/
		
		Iterator<Value> valuesIter = values.iterator();
		
		int i = 0;
		
		while(valuesIter.hasNext()) {
			
			Value v = valuesIter.next();
			
			if (i > 0) sb.append(',');
			
			if (v.isSysdate && getNow() != null) {
				
				sb.append(getNow());
				
			} else {
				
				sb.append('?');
			}
			
			i++;
		}
		
		sb.append(')');
		
		if (values.isEmpty()) throw new BeanException("Bean is empty: " + bean + " / " + bc);
		
		if (conn == null) throw new BeanException("Connection is null!");
		
		PreparedStatement stmt = null;
		
		try {
			
			if (DEBUG) System.out.println("INSERT SQL: " + sb.toString());
			
			stmt = conn.prepareStatement(sb.toString());
			
			Iterator<Value> iter2 = values.iterator();
			
			int index = 0;
			
			Map<String, Value> fieldsLoaded = new HashMap<String, Value>();
			
			while(iter2.hasNext()) {
				
				Value v = iter2.next();
				
				if (v.isSysdate && getNow() != null) continue;
				
				v.field.getType().bindToStmt(stmt, ++index, v.value);
				
				fieldsLoaded.put(v.field.getName(), v);
				
			}
			
			int x = stmt.executeUpdate();
			
			if (x > 1) throw new BeanException("insert modified more than one line: " + x);
			
			if (x == 0) throw new BeanException("Nothing was inserted! Insert returned 0 rows!");
			
			loaded.put(bean, fieldsLoaded);
			
		} finally {
			
			if (stmt != null) try { stmt.close(); } catch(Exception e) { }
		}		

	}
	
	public boolean delete(Object bean) throws Exception {
		
		BeanConfig bc = appManager.getBeanConfig(bean.getClass());
		
		if (bc.getNumberOfFields() == 0) throw new BeanException("BeanConfig has zero fields: " + bc);
		
		StringBuilder sb = new StringBuilder(32 * bc.getNumberOfFields());
		
		sb.append("DELETE FROM ").append(bc.getTableName()).append(" WHERE ");
		
		if (!bc.hasPK()) throw new BeanException("Cannot delete bean without a PK!");
		
		Iterator<DBField> iter = bc.pks();
		
		List<Value> values = new LinkedList<Value>();
		
		int count = 0;
		
		while(iter.hasNext()) {
			
			DBField dbField = iter.next();
			
			String fieldName = dbField.getName();
			
			String dbFieldName = dbField.getDbName();
			
			Object value = getValueFromBean(bean, fieldName);
			
			if (value == null) {
				
				throw new BeanException("pk is missing: " + dbField);
				
			} else if (value instanceof Number) {
			
				Number n = (Number) value;
					
				if (n.doubleValue() <= 0) {
					
					throw new BeanException("Number pk is missing: " + dbField);
				}
				
			}
			
			if (count++ > 0) sb.append(" AND ");
			
			sb.append(dbFieldName).append("=?");
			
			values.add(new Value(dbField, value));
			
		}
		
		if (values.isEmpty()) throw new BeanException("Bean is empty: " + bean + " / " + bc);
		
		if (conn == null) throw new BeanException("Connection is null!");
		
		PreparedStatement stmt = null;
		
		try {
			
			if (DEBUG) System.out.println("DELETE SQL: " + sb.toString());
			
			stmt = conn.prepareStatement(sb.toString());
			
			Iterator<Value> iter2 = values.iterator();
			
			int index = 0;
			
			while(iter2.hasNext()) {
				
				Value v = iter2.next();
				
				v.field.getType().bindToStmt(stmt, ++index, v.value);
				
			}
			
			int x = stmt.executeUpdate();
			
			if (x > 1) throw new BeanException("delete modified more than one line: " + x);
			
			if (x == 0) return false;
			
			loaded.remove(bean);
			
			return true;
			
		} finally {
			
			if (stmt != null) try { stmt.close(); } catch(Exception e) { }
		}		
	}
	
	public <E> List<E> loadJoin(Object bean, Class<? extends E> klass) throws Exception {
		
		BeanConfig bc = appManager.getBeanConfig(bean.getClass());
		
		if (bc == null) throw new BeanException("Cannot find beanConfig for bean: " + bean.getClass().getName());
		
		JoinConfig join = bc.getJoinConfig(klass);
		
		if (join == null) throw new BeanException("Cannot find join: " + klass);
		
		String joinTableName = join.getTableName();
		
		BeanConfig otherBc = appManager.getBeanConfig(klass);
		
		String otherTableName = otherBc.getTableName();
		
		StringBuilder sb = new StringBuilder(512);
		
		sb.append("SELECT ");
		
		Iterator<DBField> iter = otherBc.fields();
		
		int count = 0;
		
		while(iter.hasNext()) {
			
			DBField f = iter.next();
			
			String dbName = f.getDbName();
			
			if (count++ > 0) sb.append(',');
			
			sb.append(otherTableName).append('.').append(dbName);
		}
		
		sb.append(" FROM ").append(joinTableName).append(",").append(otherTableName);
		
		sb.append(" WHERE ");
		
		if (!bc.hasPK() || !otherBc.hasPK()) throw new BeanException("Cannot load join without a PK!");
		
		iter = bc.pks();
		
		count = 0;
		
		List<Value> values = new LinkedList<Value>();
		
		while(iter.hasNext()) {
			
			DBField dbField = iter.next();
			
			String fieldName = dbField.getName();
			
			String dbFieldName = dbField.getForeignName();
			
			Object value = getValueFromBean(bean, fieldName);
			
			checkPK(value, dbField);
			
			if (count++ > 0) sb.append(" AND ");
			
			sb.append(joinTableName).append('.').append(dbFieldName).append("=?");
			
			values.add(new Value(dbField, value));
		}
		
		// join clause...
		
		iter = otherBc.pks();
		
		while(iter.hasNext()) {
			
			DBField f = iter.next();
			
			String dbName = f.getDbName();
			
			String foreignName = f.getForeignName();
			
			if (count++ > 0) sb.append(" AND ");
			
			sb.append(joinTableName).append('.').append(foreignName);
			
			sb.append('=');
			
			sb.append(otherTableName).append('.').append(dbName);
		}
		
		String sql = sb.toString();
		
		if (DEBUG) System.out.println("JOIN SQL: " + sql);
		
		PreparedStatement stmt = null;
		
		ResultSet rset = null;
		
		if (conn == null) throw new BeanException("conn == null when trying to execute join!");
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			Iterator<Value> iter2 = values.iterator();
			
			int index = 0;
			
			while(iter2.hasNext()) {
				
				Value v = iter2.next();
				
				v.field.getType().bindToStmt(stmt, ++index, v.value);
				
			}
			
			rset = stmt.executeQuery();
			
			List<E> list = new LinkedList<E>();
			
			while(rset.next()) {
				
				Map<String, Value> fieldsLoaded = new HashMap<String, Value>();
				
				iter = otherBc.fields();
				
				//Class<? extends Object> otherClass = otherBc.getBeanClass();
				
				E otherBean = klass.newInstance();
				
				index = 0;
				
				while(iter.hasNext()) {
					
					DBField f = iter.next();
					
					String fieldName = f.getName();
					
					DBType type = f.getType();
					
					Object value = type.getFromResultSet(rset, ++index);
					
					injectValue(otherBean, fieldName, value, type.getTypeClass());
					
					fieldsLoaded.put(fieldName, new Value(f, value));
				}
				
				loaded.put(otherBean, fieldsLoaded);
				
				list.add(otherBean);
			}
			
			return list;

		} finally {
			
			if (rset != null) try { rset.close(); } catch(Exception e) { }
			
			if (stmt != null) try { stmt.close(); } catch(Exception e) { }
		}
	}
	
	public List<Integer> loadJoinIds(Object bean, Class<? extends Object> klass) throws Exception {
		
		BeanConfig bc = appManager.getBeanConfig(bean.getClass());
		
		if (bc == null) throw new BeanException("Cannot find beanConfig for bean: " + bean.getClass().getName());
		
		JoinConfig join = bc.getJoinConfig(klass);
		
		if (join == null) throw new BeanException("Cannot find join: " + klass);
		
		String joinTableName = join.getTableName();
		
		BeanConfig otherBc = appManager.getBeanConfig(klass);
		
		if (otherBc == null) throw new BeanException("Cannot find beanConfig for bean: " + klass);
		
		StringBuilder sb = new StringBuilder(512);
		
		if (!bc.hasPK() || !otherBc.hasPK()) throw new BeanException("Cannot load join without a PK!");
		
		if (otherBc.getNumberOfPKs() > 1) throw new BeanException("Cannot load ids if PK is compound!");
		
		sb.append("SELECT ");
		
		Iterator<DBField> iter = otherBc.pks();
		
		int count = 0;
		
		while(iter.hasNext()) {
			
			DBField f = iter.next();
			
			String dbName = f.getForeignName();
			
			if (count++ > 0) sb.append(',');
			
			sb.append(dbName);
		}
		
		sb.append(" FROM ").append(joinTableName);
		
		sb.append(" WHERE ");
		
		iter = bc.pks();
		
		count = 0;
		
		List<Value> values = new LinkedList<Value>();
		
		while(iter.hasNext()) {
			
			DBField dbField = iter.next();
			
			String fieldName = dbField.getName();
			
			String dbFieldName = dbField.getForeignName();
			
			Object value = getValueFromBean(bean, fieldName);
			
			checkPK(value, dbField);
			
			if (count++ > 0) sb.append(" AND ");
			
			sb.append(dbFieldName).append("=?");
			
			values.add(new Value(dbField, value));
		}
		
		String sql = sb.toString();
		
		if (DEBUG) System.out.println("IDS SQL: " + sql);
		
		PreparedStatement stmt = null;
		
		ResultSet rset = null;
		
		if (conn == null) throw new BeanException("conn == null when trying to execute join!");
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			Iterator<Value> iter2 = values.iterator();
			
			int index = 0;
			
			while(iter2.hasNext()) {
				
				Value v = iter2.next();
				
				v.field.getType().bindToStmt(stmt, ++index, v.value);
				
			}
			
			rset = stmt.executeQuery();
			
			List<Integer> list = new LinkedList<Integer>();
			
			while(rset.next()) {
				
				iter = otherBc.pks();
				
				index = 0;
				
				while(iter.hasNext()) {
					
					DBField f = iter.next();
					
					DBType type = f.getType();
					
					Object value = type.getFromResultSet(rset, ++index);
					
					if (value instanceof Integer) {
						
						list.add((Integer) value);
						
					} else {
						
						throw new BeanException("PK is not integer, so you cannot use loadJoinIds!");
					}
					
				}
			}
			
			return list;

		} finally {
			
			if (rset != null) try { rset.close(); } catch(Exception e) { }
			
			if (stmt != null) try { stmt.close(); } catch(Exception e) { }
		}
	}
	
	public boolean add(Object bean1, Object bean2) throws Exception {
		
		BeanConfig bc1 = appManager.getBeanConfig(bean1.getClass());
		
		BeanConfig bc2 = appManager.getBeanConfig(bean2.getClass());
		
		if (bc1 == null) throw new BeanException("Cannot find beanConfig for bean: " + bean1.getClass().getName());
		
		if (bc2 == null) throw new BeanException("Cannot find beanConfig for bean: " + bean2.getClass().getName());
		
		JoinConfig join = bc1.getJoinConfig(bean2.getClass());
		
		if (join == null) throw new BeanException("Cannot find join: " + bean2.getClass());
		
		if (!bc1.hasPK() || !bc2.hasPK()) throw new BeanException("Cannot add bean without a PK!");
		
		if (bc1.getNumberOfPKs() > 1) throw new BeanException("Cannot load ids if PK is compound!");
		
		if (bc2.getNumberOfPKs() > 1) throw new BeanException("Cannot load ids if PK is compound!");
		
		DBField pk1 = bc1.getFirstPK();
		
		DBField pk2 = bc2.getFirstPK();
		
		String joinTableName = join.getTableName();
		
		StringBuilder sb = new StringBuilder(512);
		
		String pk1DbFieldName = pk1.getForeignName();
		
		String pk2DbFieldName = pk2.getForeignName();
		
		sb.append("SELECT ").append(pk1DbFieldName).append(" FROM ").append(joinTableName);
		
		sb.append(" WHERE ").append(pk1DbFieldName).append("=? AND ").append(pk2DbFieldName).append("=?");
		
		String pk1FieldName = pk1.getName();
		
		String pk2FieldName = pk2.getName();
		
		Object value1 = getValueFromBean(bean1, pk1FieldName);
		
		Object value2 = getValueFromBean(bean2, pk2FieldName);
		
		checkPK(value1, pk1);
		
		checkPK(value2, pk2);
		
		PreparedStatement stmt = null;
		
		ResultSet rset = null;
		
		try {
			
			String sql = sb.toString();
			
			if (DEBUG) System.out.println("ADD: " + sql);
			
			stmt = conn.prepareStatement(sql);
			
			pk1.getType().bindToStmt(stmt, 1, value1);
			
			pk2.getType().bindToStmt(stmt, 2, value2);
			
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				
				return false;
			}
			
		} finally {
			
			if (rset != null) try { rset.close(); } catch(Exception e) { }
			
			if (stmt != null) try { stmt.close(); } catch(Exception e) { }
		}
		
		sb.setLength(0);
		
		sb.append("INSERT INTO ").append(joinTableName).append("(").append(pk1DbFieldName).append(",").append(pk2DbFieldName).append(") ");
		
		sb.append("VALUES(?,?)");
		
		try {
			
			String sql = sb.toString();
			
			if (DEBUG) System.out.println("ADD: " + sql);
			
			stmt = conn.prepareStatement(sql);
			
			pk1.getType().bindToStmt(stmt, 1, value1);
			
			pk2.getType().bindToStmt(stmt, 2, value2);
			
			stmt.executeUpdate();
			
			return true;
			
		}  finally {
			
			if (stmt != null) try { stmt.close(); } catch(Exception e) { }
		}
	}
	
	public boolean remove(Object bean1, Object bean2) throws Exception {
		
		BeanConfig bc1 = appManager.getBeanConfig(bean1.getClass());
		
		BeanConfig bc2 = appManager.getBeanConfig(bean2.getClass());
		
		if (bc1 == null) throw new BeanException("Cannot find beanConfig for bean: " + bean1.getClass().getName());
		
		if (bc2 == null) throw new BeanException("Cannot find beanConfig for bean: " + bean2.getClass().getName());
		
		JoinConfig join = bc1.getJoinConfig(bean2.getClass());
		
		if (join == null) throw new BeanException("Cannot find join: " + bean2.getClass());
		
		if (!bc1.hasPK() || !bc2.hasPK()) throw new BeanException("Cannot add bean without a PK!");
		
		if (bc1.getNumberOfPKs() > 1) throw new BeanException("Cannot load ids if PK is compound!");
		
		if (bc2.getNumberOfPKs() > 1) throw new BeanException("Cannot load ids if PK is compound!");
		
		DBField pk1 = bc1.getFirstPK();
		
		DBField pk2 = bc2.getFirstPK();
		
		String joinTableName = join.getTableName();
		
		String pk1DbFieldName = pk1.getForeignName();
		
		String pk2DbFieldName = pk2.getForeignName();
		
		String pk1FieldName = pk1.getName();
		
		String pk2FieldName = pk2.getName();
		
		Object value1 = getValueFromBean(bean1, pk1FieldName);
		
		Object value2 = getValueFromBean(bean2, pk2FieldName);
		
		checkPK(value1, pk1);
		
		checkPK(value2, pk2);
		
		StringBuilder sb = new StringBuilder(512);
		
		sb.append("DELETE FROM ").append(joinTableName);
		
		sb.append(" WHERE ").append(pk1DbFieldName).append("=? AND ").append(pk2DbFieldName).append("=?");
		
		PreparedStatement stmt = null;
		
		try {
			
			String sql = sb.toString();
			
			if (DEBUG) System.out.println("REMOVE: " + sql);
			
			stmt = conn.prepareStatement(sql);
			
			pk1.getType().bindToStmt(stmt, 1, value1);
			
			pk2.getType().bindToStmt(stmt, 2, value2);
			
			int x = stmt.executeUpdate();
			
			if (x < 0) throw new BeanException("remove returned negative result: " + x); // this should never happen!
			
			if (x > 1) throw new BeanException("remove deleted more than one row: " + x);
			
			if (x == 0) return false;
			
			return true;
			
		} finally {
			
			if (stmt != null) try { stmt.close(); } catch(Exception e) { }
		}
	}
	
	public int countJoin(Object bean, Class<? extends Object> klass) throws Exception {
		
		BeanConfig bc = appManager.getBeanConfig(bean.getClass());
		
		if (bc == null) throw new BeanException("Cannot find beanConfig for bean: " + bean.getClass().getName());
		
		JoinConfig join = bc.getJoinConfig(klass);
		
		if (join == null) throw new BeanException("Cannot find join: " + klass);
		
		String joinTableName = join.getTableName();
		
		BeanConfig otherBc = appManager.getBeanConfig(klass);
		
		if (otherBc == null) throw new BeanException("Cannot find beanConfig for bean: " + klass);
		
		StringBuilder sb = new StringBuilder(512);
		
		if (!bc.hasPK() || !otherBc.hasPK()) throw new BeanException("Cannot load join without a PK!");
		
		if (otherBc.getNumberOfPKs() > 1) throw new BeanException("Cannot load ids if PK is compound!");
		
		sb.append("SELECT ");
		
		Iterator<DBField> iter = otherBc.pks();
		
		int count = 0;
		
		while(iter.hasNext()) {
			
			DBField f = iter.next();
			
			String dbName = f.getForeignName();
			
			if (count++ > 0) sb.append(',');
			
			sb.append("count(").append(dbName).append(")");
		}
		
		sb.append(" FROM ").append(joinTableName);
		
		sb.append(" WHERE ");
		
		iter = bc.pks();
		
		count = 0;
		
		List<Value> values = new LinkedList<Value>();
		
		while(iter.hasNext()) {
			
			DBField dbField = iter.next();
			
			String fieldName = dbField.getName();
			
			String dbFieldName = dbField.getForeignName();
			
			Object value = getValueFromBean(bean, fieldName);
			
			checkPK(value, dbField);
			
			if (count++ > 0) sb.append(" AND ");
			
			sb.append(dbFieldName).append("=?");
			
			values.add(new Value(dbField, value));
		}
		
		String sql = sb.toString();
		
		if (DEBUG) System.out.println("IDS SQL: " + sql);
		
		PreparedStatement stmt = null;
		
		ResultSet rset = null;
		
		if (conn == null) throw new BeanException("conn == null when trying to execute join!");
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			Iterator<Value> iter2 = values.iterator();
			
			int index = 0;
			
			while(iter2.hasNext()) {
				
				Value v = iter2.next();
				
				v.field.getType().bindToStmt(stmt, ++index, v.value);
				
			}
			
			rset = stmt.executeQuery();
			
			if(rset.next()) {
				
				return rset.getInt(1);
				
			} else {
				
				throw new BeanException("count should return at least one row!");
			}
			
		} finally {
			
			if (rset != null) try { rset.close(); } catch(Exception e) { }
			
			if (stmt != null) try { stmt.close(); } catch(Exception e) { }
		}
	}
	
	public <E> List<E> loadList(E bean) throws Exception {
		
		return loadList(bean, null, -1);
	}
	
	public <E> E loadUnique(E bean) throws Exception {
		
		return checkUnique(loadList(bean, null, 2));
	}
	
	public <E> List<E> loadList(E bean, String orderBy) throws Exception {
		
		return loadList(bean, orderBy, -1);
	}
	
	public <E> List<E> loadList(E bean, int limit) throws Exception {
		
		return loadList(bean, null, limit);
	}
	
	public <E> List<E> loadListMinus(E bean, String[] minus) throws Exception {
		
		return loadListMinus(bean, minus, null, -1);
	}
	
	public <E> List<E> loadListMinus(E bean, String[] minus, String orderBy) throws Exception {
		
		return loadListMinus(bean, minus, orderBy, -1);
	}
	
	public <E> List<E> loadListMinus(E bean, String[] minus, int limit) throws Exception {
		
		return loadListMinus(bean, minus, null, limit);
	}
	
	
	private static class Value {
		
		public Object value;
		
		public DBField field;
		
		public boolean isSysdate;
		
		public Value(DBField field, Object value, boolean isSysdate) {
			
			this.field = field;
			
			this.value = value;
			
			this.isSysdate = isSysdate;
		}
		
		public Value(DBField field, Object value) {
			
			this(field, value, false);
		}
	}
	
}