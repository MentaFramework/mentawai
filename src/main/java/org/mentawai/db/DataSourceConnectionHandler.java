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
package org.mentawai.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * A connection handler backed up by a <i>javax.sql.DataSource</i>.
 * Use this class if you have a DataSource in hand and want to create a ConnectionHandler.
 *
 * @author Sergio Oliveira
 */
public class DataSourceConnectionHandler implements ConnectionHandler {
    
    private DataSource ds = null;
    
    /**
     * Creates a ConnectionHandler backed up by the given DataSource.
     *
     * @param ds The DataSource to use.
     */
    public DataSourceConnectionHandler(DataSource ds) {
        this.ds = ds;
    }
    
    public Connection getConnection() throws SQLException {
        Connection conn = ds.getConnection();
        if (conn == null) throw new SQLException("Pool returned null !!!");
        return conn;
    }
    
    public String getStatus() {
    	
    	return "Not yet implemented!";
    }
    
    public Object getBean() throws InstantiationException {
    	
    	try {
    		
    		return getConnection();
    		
    	} catch(Exception e) {
    		
    		throw new RuntimeException(e);
    	}
    }
    
    public <T> T getInstance() {
    	
    	try {
    		
    		return (T) getConnection();
    		
    	} catch(Exception e) {
    		
    		throw new RuntimeException(e);
    	}
    }
    
    public void onCleared(Connection conn) {
    	
    	release(conn);
    }
    
    public void onCreated(Connection conn) {
    	
    }
    
    public Class<? extends Object> getType() {
    	
    	return Connection.class;
    }
    
    public void release(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void destroy() {
    	
    	// is it possible to destroy/close a generic data source implementation?
    	
    	// subclasses may choose to override this method...
    }
}

        
