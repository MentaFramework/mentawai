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
package org.mentawai.transaction;

import java.sql.Connection;

public class JdbcTransaction implements Transaction {
    
    private boolean active = false;
    
    private Connection conn;
    private boolean oldAutoCommit;
    
    public JdbcTransaction() { 
    	
    }
    
    public JdbcTransaction(Connection conn) {
        this.conn = conn;
    }
    
    public void setConnection(Connection conn) {
    	
        this.conn = conn;
    }
    
    public void setConn(Connection conn) {
    	
    	setConnection(conn);
    }
    
    public Connection getConn() {
    	return conn;
    }
    
    public Connection getConnection() {
    	return conn;
    }
    
    public void begin() throws Exception {
        if (!active) {
        	if (conn == null) throw new IllegalStateException("JdbcTransaction does not have a connection!");
        	oldAutoCommit = conn.getAutoCommit();
        	conn.setAutoCommit(false);
        	active = true;
        }
    }
    
    public void commit() throws Exception {
    	if (!active) throw new IllegalStateException("Tried to commit but transaciton is not active!");
        conn.commit();
    }
    
    public void rollback() throws Exception {
    	if (!active) throw new IllegalStateException("Tried to rollback but transaciton is not active!");
        conn.rollback();
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void end() throws Exception {
    	if (conn != null) conn.setAutoCommit(oldAutoCommit);
    	active = false;
    }
}

        
        