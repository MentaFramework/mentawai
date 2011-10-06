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

public class IntegerType implements DBType {
	
	public String toString() {
		
		return this.getClass().getSimpleName();
	}
	
	public Object getFromResultSet(ResultSet rset, int index) throws SQLException {
		
		return rset.getInt(index);
	}
	
	public Object getFromResultSet(ResultSet rset, String field) throws SQLException {
		
		return rset.getInt(field);
	}
	
	
	public Class<? extends Object> getTypeClass() {
		
		return Integer.class;
	}
	
	public void bindToStmt(PreparedStatement stmt, int index, Object value) throws SQLException {
		
		if (value == null) {
			
			stmt.setNull(index, java.sql.Types.INTEGER);
			
		} else if (value instanceof Integer) {
			
			Integer i = (Integer) value;
		
			stmt.setInt(index, i.intValue());
			
		} else {
			
			throw new IllegalArgumentException("value is not an integer!");
		}
		
	}
}