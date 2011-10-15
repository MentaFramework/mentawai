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
	
    private DataSource cpds;
    
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
        Class<? extends Object> klass = null;
        try {
            klass = Class.forName("com.mchange.v2.c3p0.ComboPooledDataSource");
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException("C3P0 cannot be found! You probably did not put the C3P0 jars in your /WEB-INF/lib directory!");
        }
        
        try {
            Class.forName(driver);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException("Cannot find jdbc driver " + driver + "! You probably did not put your JDBC driver in your /WEB-INF/lib directory!");
        }
        
        try {
            Object cpds = klass.newInstance();
            setValue(cpds, "driverClass", driver);
            setValue(cpds, "user", user);
            setValue(cpds, "password", pass);
            setValue(cpds, "jdbcUrl", url);
            setValue(cpds, "maxIdleTime", 5); // make this the default, as to run without this is trouble...
            this.cpds = (DataSource) cpds;            
            
        } catch(Exception e) {
            throw new RuntimeException("Error trying to setup a C3P0 pooled data source:" + e.getMessage(), e);
        }
    }
    
    public void setValue(String param, Object value) {
    	
    	try {
    	
    		setValue(cpds, param, value);
    		
    	} catch(Exception e) {
    		
    		throw new RuntimeException("Error trying to setup a C3P0 pooled data source:" + e.getMessage(), e);
    	}
    }
    
    public String getStatus() {
    	
    	ComboPooledDataSource dataSource = (ComboPooledDataSource) cpds;
    	
    	try {
    		
    		StringBuilder sb = new StringBuilder(256);
    		
    		sb.append("Busy: ").append(dataSource.getNumBusyConnectionsDefaultUser());
    		
    		sb.append(" / Idle: ").append(dataSource.getNumIdleConnectionsDefaultUser());
    		
    		sb.append(" / Total: ").append(dataSource.getNumConnectionsDefaultUser());
    		
    		return sb.toString();
    		
    	} catch(Exception e) {
    		
    		throw new RuntimeException(e);
    	}
    }
    
	/*
	 * Use reflection to set a property in the bean
	 */
	private void setValue(Object bean, String name, Object value) throws Exception {
        StringBuffer sb = new StringBuffer(30);
        sb.append("set");
        sb.append(name.substring(0,1).toUpperCase());
        if (name.length() > 1) sb.append(name.substring(1));
        
        Class<? extends Object> klass = value.getClass();
        
        if (klass.equals(Integer.class)) klass = int.class; // little hack...
        
        Method m = bean.getClass().getMethod(sb.toString(), new Class[] { klass });
        if (m != null) {
            m.setAccessible(true);
            m.invoke(bean, new Object[] { value });
        }
	}        
    
    /**
     * Gets the underlying C3P0's ComboPooledDataSource.
     *
     * @return The ComboPooledDataSource
     */    
    public DataSource getComboPooledDataSource() {
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
    	
    	ComboPooledDataSource dataSource = (ComboPooledDataSource) cpds;
    	
    	try {
    	
    		//dataSource.close();
    		DataSources.destroy( dataSource );
    		
    	} catch(Exception e) {
    		
    		e.printStackTrace();
    	}
    }
}