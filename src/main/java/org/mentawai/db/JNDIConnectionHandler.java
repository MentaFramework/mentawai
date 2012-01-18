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
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * A connection handler that looks for a DataSource in a JNDI context.
 * Use this class if you want to create a connection handler backed up by a DataSource which is bound to a JNDI context.
 *
 * @author Sergio Oliveira
 */
public class JNDIConnectionHandler extends AbstractConnectionHandler {
	
    private static Map<String, ConnectionHandler> single = new Hashtable<String, ConnectionHandler>();
    
    private DataSource ds = null;
    private String poolname = null;
    
    private JNDIConnectionHandler(String poolname) {
		try {
            Context ctx = new InitialContext();
            this.ds = (DataSource) ctx.lookup("java:comp/env/" + poolname);
            this.poolname = poolname;
		} catch(NamingException e) {
			throw new RuntimeException(e);
		}
    }
    
    public Connection getConnection() throws SQLException {
        Connection conn = ds.getConnection();
        if (conn == null) throw new SQLException("Pool (" + poolname + ") returned null !!!");
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
    
    public void release(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Gets a ConnectionHandler with a DataSource bound to the JNDI context with the given name.
     *
     * @param poolname The name with which the DataSource is bound to the JNDI context.
     * @return A ConnectionHandler from cache or a new one if this is the first call with this name.
     */
    public static ConnectionHandler getInstance(String poolname) {
        ConnectionHandler ch = single.get(poolname);
        if (ch == null) {
            synchronized(single) {
                ch = single.get(poolname);
                if (ch == null) {
                    ch = new JNDIConnectionHandler(poolname);
                    single.put(poolname, ch);
                }
            }
        }
        return ch;
    }
    
    public void destroy() {
    	
    	// is it possible to destroy/close a generic data source implementation?
    	
    	// subclasses may choose to override this method...
    }
    
}

        
