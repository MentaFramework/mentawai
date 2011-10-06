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
import org.mentawai.bean.BeanException;
import org.mentawai.bean.DBField;

public class OracleBeanSession extends JdbcBeanSession {
	
	public OracleBeanSession() {
		
		super();
	}
	
	public OracleBeanSession(Connection conn) {
		
		super(conn);
	}
	
	@Override
	protected String getNow() {
		
		return "sysdate";
	}
	
	/**
	 * Oracle will not SORT first then apply LIMIT.
	 */
	@Override
	protected StringBuilder handleLimit(StringBuilder sb, String orderBy, int limit) {
		
		if (limit == -1) return sb;
		
		StringBuilder sbLimit = new StringBuilder(sb.length() + 32);
		
		if (orderBy != null) {
		
			sbLimit.append("SELECT * FROM (").append(sb.toString()).append(") where rowcount <= ").append(limit);
			
		} else {
			
			sbLimit.append(sb.toString()).append(" AND rownum <= ").append(limit);
		}
		
		return sbLimit;
	}
	
	public void insert(Object bean) throws Exception {
		
		// find sequence field...
		
		BeanConfig bc = appManager.getBeanConfig(bean.getClass());
		
		if (bc == null) throw new BeanException("Cannot find bean config: " + bean.getClass());
		
		DBField seqField = bc.getSequenceField();
		
		if (seqField == null) {
			
			super.insert(bean);
			
			return;
		}
		
		PreparedStatement stmt = null;
		
		ResultSet rset = null;
		
		StringBuilder sb = new StringBuilder(128);
		
		sb.append("select seq_").append(seqField.getDbName()).append("_").append(bc.getTableName());
		
		sb.append(".nextval from ").append(bc.getTableName());
		
		try {
		
			stmt = conn.prepareStatement(sb.toString());
			
			rset = stmt.executeQuery();
			
			if (rset.next()) {
				
				int id = rset.getInt(1);
				
				injectValue(bean, seqField.getName(), id, Integer.class);
			}
			
		} finally {
			
			if (rset != null) try { rset.close(); } catch(Exception e) { }
			
			if (stmt != null) try { stmt.close(); } catch(Exception e) { }
		}
		
		super.insert(bean);
	}
		
}