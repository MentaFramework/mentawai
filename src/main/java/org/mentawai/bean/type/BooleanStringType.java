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
package org.mentawai.bean.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mentawai.bean.DBType;

public class BooleanStringType implements DBType {
	
	private final String sTrue;
	
	private final String sFalse;
	
	public BooleanStringType() {
		
		this("T", "F");
	}
	
	public BooleanStringType(String sTrue, String sFalse) {
		
		this.sTrue = sTrue;
		
		this.sFalse = sFalse;
		
	}
	
	public boolean equals(Object obj) {
		
		if (obj instanceof BooleanStringType) {
			
			BooleanStringType bt = (BooleanStringType) obj;
			
			if (bt.sTrue.equals(this.sTrue) && bt.sFalse.equals(this.sFalse)) {
				
				return true;
			}
		}
		
		return false;
	}
	
	public String toString() {
		
		return this.getClass().getSimpleName();
	}
	
	protected boolean getBooleanValue(String s) throws SQLException {

		if (s == null) return false;
		
		if (s.equals("T")) return true;
		
		if (s.equals("F")) return false;
		
		if (s.equals("0")) return false;
		
		if (s.equals("1")) return true;
		
		if (s.equals("true")) return true;
		
		if (s.equals("false")) return false;
		
		throw new SQLException("value is not a boolean: " + s);
		
	}
	
	private static Boolean getBoolValue(boolean b) {
		
		if (b) return Boolean.TRUE;
		
		return Boolean.FALSE;
	}
	
	public Object getFromResultSet(ResultSet rset, int index) throws SQLException {
		
		String s = rset.getString(index);
		
		boolean b = getBooleanValue(s);
		
		return getBoolValue(b);
	}

	public Object getFromResultSet(ResultSet rset, String field) throws SQLException {
		
		String s = rset.getString(field);
		
		boolean b = getBooleanValue(s);
		
		return getBoolValue(b);
	}
	
	
	public Class<? extends Object> getTypeClass() {
		
		return Boolean.class;
	}
	
	public void bindToStmt(PreparedStatement stmt, int index, Object value) throws SQLException {
		
		if (value == null) {
		
			stmt.setString(index, null);
			
		} else if (value instanceof Boolean) {
			
			boolean b = ((Boolean) value).booleanValue();
			
			String s = b ? sTrue : sFalse;
		
			stmt.setString(index, s);
			
		} else {
			
			throw new IllegalArgumentException("value is not a boolean!");
		}
		
	}
}