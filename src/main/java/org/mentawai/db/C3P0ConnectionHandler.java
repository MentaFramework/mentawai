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

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * A connection handler that uses the C3P0 connection pool. (http://sourceforge.net/projects/c3p0)
 * To use this class you must have the C3P0 jar in your /WEB-INF/lib directory.
 * You may access the underlying C3P0's ComboPooledDataSource to configure the pool, before you start calling getConnection().
 *
 * @author Sergio Oliveira
 */
public class C3P0ConnectionHandler extends AbstractConnectionHandler {
	
	public static boolean DEBUG = false;
	public static String TEST_QUERY = "select 1";
	
    private ComboPooledDataSource cpds;
    
    /**
     * Constructs a C3P0ConnectionHandler with C3P0's ComboPooledDataSource.
     *
     * @param driver The JDBC driver class name to use.
     * @param url The JDBC url to connect to the database.
     * @param user The database username to use.
     * @param pass The database password to use.
     * @throws IllegalStateException If the C3P0 jar is not in the /WEB-INF/lib directory or if the JDBC driver cannot be loaded.
     */    
    public C3P0ConnectionHandler(String driver, String url, String user, String pass) {
    	
    	
    	ComboPooledDataSource cpds = new ComboPooledDataSource();
    	
        try {
            Class.forName(driver);
            cpds.setDriverClass(driver);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException("Cannot find jdbc driver " + driver + "! You probably did not put your JDBC driver in your /WEB-INF/lib directory!");
        } catch(PropertyVetoException e) {
        	e.printStackTrace();
        	throw new IllegalStateException("Cannot set jdbc driver " + driver + "!");
        }
        
        
        cpds.setUser(user);
        cpds.setPassword(pass);
        cpds.setJdbcUrl(url);
        
        // now take care of broken pipe
        // Source: http://netbeando.blogspot.com/2009/03/broken-pipe-solucao-correta.html
        
        cpds.setMinPoolSize(3);
        cpds.setAcquireIncrement(3);
        cpds.setMaxPoolSize(20);
        cpds.setInitialPoolSize(3);
        
        cpds.setIdleConnectionTestPeriod(60);
        cpds.setMaxStatements(0); // disabled prepared statement cache
        cpds.setPreferredTestQuery(TEST_QUERY);
        cpds.setCheckoutTimeout(1000 * 2); // timeout after 2 seconds if getConnection blocks....
        
        cpds.setMaxIdleTime(60 * 5);
        cpds.setMaxIdleTimeExcessConnections(60 * 2);
            
        this.cpds = cpds;            
    }
    
    public String getStatus() {
    	
    	try {
    		
    		StringBuilder sb = new StringBuilder(256);
    		
    		sb.append("Busy: ").append(cpds.getNumBusyConnectionsDefaultUser());
    		
    		sb.append(" / Idle: ").append(cpds.getNumIdleConnectionsDefaultUser());
    		
    		sb.append(" / Total: ").append(cpds.getNumConnectionsDefaultUser());
    		
    		return sb.toString();
    		
    	} catch(Exception e) {
    		
    		throw new RuntimeException(e);
    	}
    }
    
    /**
     * Gets the underlying C3P0's ComboPooledDataSource.
     *
     * @return The ComboPooledDataSource
     */    
    public ComboPooledDataSource getComboPooledDataSource() {
        return cpds;
    }
    
    public Connection getConnection() throws SQLException {
        Connection conn = cpds.getConnection();
        
        if (DEBUG) {
        	
        	new Exception("===> GOT CONN: " + conn).printStackTrace();
        	
        }
        
        if (conn == null) throw new SQLException("Pool returned null !!!");
        
        return conn;
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
    	
    	if (DEBUG) {
    		
    		new Exception("===> BYE CONN: " + conn).printStackTrace();
    	}
    	
        try {
            if (conn != null) conn.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public <T> T getInstance() {
    	
    	try {
    	
    		return (T) getConnection();
    		
    	} catch(Exception e) {
    		
    		throw new RuntimeException(e);
    	}
    }
    
    public Object getBean() throws InstantiationException {
    	
    	try {
    	
    		return getConnection();
    		
    	} catch(Exception e) {
    		
    		throw new RuntimeException(e);
    	}
    }
    
    public void destroy() {
    	
    	try {
    	
    		//dataSource.close();
    		DataSources.destroy( cpds );
    		
    	} catch(Exception e) {
    		
    		e.printStackTrace();
    	}
    }
}