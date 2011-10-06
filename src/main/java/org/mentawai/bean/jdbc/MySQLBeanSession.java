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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.mentawai.bean.BeanConfig;
import org.mentawai.bean.DBField;

public class MySQLBeanSession extends JdbcBeanSession {
	
	public MySQLBeanSession() {
		
		super();
	}
	
	public MySQLBeanSession(Connection conn) {
		
		super(conn);
	}
	
	@Override
	protected String getNow() {
		
		return "now()";
	}
	
	/**
	 * MySQL is not like Oracle. It will SORT everything first and then apply LIMIT.
	 */
	@Override
	protected StringBuilder handleLimit(StringBuilder sb, String orderBy, int limit) {
		
		if (limit == -1) return sb;
		
		StringBuilder sbLimit = new StringBuilder(sb.length() + 32);
		
		sbLimit.append(sb.toString()).append(" LIMIT ").append(limit);
		
		return sbLimit;
	}
	
	public void insert(Object bean) throws Exception {
		
		super.insert(bean);
		
		// find autoincrement field...
		
		BeanConfig bc = appManager.getBeanConfig(bean.getClass());
		
		DBField autoIncrement = bc.getAutoIncrementField();
		
		if (autoIncrement == null) {
			
			return;
		}
		
		PreparedStatement stmt = null;
		
		ResultSet rset = null;
		
		StringBuilder sb = new StringBuilder("select last_insert_id() from ");
		
		sb.append(bc.getTableName());
		
		try {
		
			stmt = conn.prepareStatement(sb.toString());
			
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				
				int id = rset.getInt(1);
				
				injectValue(bean, autoIncrement.getName(), id, Integer.class);
			}
			
		} finally {
			
			if (rset != null) try { rset.close(); } catch(Exception e) { }
			
			if (stmt != null) try { stmt.close(); } catch(Exception e) { }
		}
	}
		
}