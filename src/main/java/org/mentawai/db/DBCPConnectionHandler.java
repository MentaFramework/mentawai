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

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * A connection handler that uses the Commons DBCP connection pool. (http://jakarta.apache.org/commons/dbcp/)
 * To use this class you must have the DBCP jars in your /WEB-INF/lib directory.
 * You may access the underlying DBCP's BasicDataSource to configure the pool, before you start calling getConnection().
 *
 * @author Sergio Oliveira
 */
public class DBCPConnectionHandler extends AbstractConnectionHandler {
    
    private DataSource bds;
  
    /**
     * Constructs a DBCPConnectionHandler with DBCP's BasicDataSource.
     *
     * @param driver The JDBC driver class name to use.
     * @param url The JDBC url to connect to the database.
     * @param user The database username to use.
     * @param pass The database password to use.
     * @throws IllegalStateException If the Commons DBCP jars are not in the /WEB-INF/lib directory or if the JDBC driver cannot be loaded.
     */
    public DBCPConnectionHandler(String driver, String url, String user, String pass) {
        
        Class klass = null;
        
        try {
            klass = Class.forName("org.apache.commons.dbcp.BasicDataSource");
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException("Commons DBCP cannot be found! You probably did not put the DBCP jars in your /WEB-INF/lib directory!");
        }
        
        try {
            Class.forName(driver);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException("Cannot find jdbc driver " + driver + "! You probably did not put your JDBC driver in your /WEB-INF/lib directory!");
        }
        
        // by reflection to avoid external link library!
        try {
            Object bds = klass.newInstance();
            setValue(bds, "driverClassName", driver);
            setValue(bds, "username", user);
            setValue(bds, "password", pass);
            setValue(bds, "url", url);
            this.bds = (DataSource) bds;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error trying to setup the DBCP's BasicDataSource: " + e.getMessage(), e);
        }
    }
    
    public String getStatus() {
    	
    	return "Not yet implemented!";
    }
    
	/*
	 * Use reflection to set a property in the bean
	 */
	private void setValue(Object bean, String name, Object value) throws Exception {
        StringBuffer sb = new StringBuffer(30);
        sb.append("set");
        sb.append(name.substring(0,1).toUpperCase());
        if (name.length() > 1) sb.append(name.substring(1));
        Method m = bean.getClass().getMethod(sb.toString(), new Class[] { value.getClass() });
        if (m != null) {
            m.setAccessible(true);
            m.invoke(bean, new Object[] { value });
        }
	}    
    
    /**
     * Gets the underlying DBCP's BasicDataSource.
     * You should cast this data source to DBCP's BasicDataSource.
     *
     * @return The BasicDataSource as a DataSource.
     */
    public DataSource getBasicDataSource() {
        return bds;
    }
    
    public Connection getConnection() throws SQLException {
        Connection conn = bds.getConnection();
        if (conn == null) throw new SQLException("Pool returned null !!!");
        return conn;
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
    	
    	BasicDataSource dataSource = (BasicDataSource) bds;
    	
    	try {
    	
    		dataSource.close();
    		
    	} catch(Exception e) {
    		
    		e.printStackTrace();
    	}
    }
}