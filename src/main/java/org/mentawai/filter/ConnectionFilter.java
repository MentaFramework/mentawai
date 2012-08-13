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
package org.mentawai.filter;

import java.sql.Connection;
import java.sql.SQLException;

import org.mentacontainer.Container;
import org.mentawai.core.Action;
import org.mentawai.core.AfterConsequenceFilter;
import org.mentawai.core.ApplicationManager;
import org.mentawai.core.Consequence;
import org.mentawai.core.InputWrapper;
import org.mentawai.core.InvocationChain;
import org.mentawai.db.ConnectionHandler;

/**
 * @author Sergio Oliveira
 */
public class ConnectionFilter extends InputWrapper implements
        AfterConsequenceFilter {

    public static final String KEY = "conn";

    private final ConnectionHandler connHandler;

    private String connKey;

    private ThreadLocal<Connection> conn = new ThreadLocal<Connection>();

    public ConnectionFilter(String connKey, ConnectionHandler connHandler) {

        super();

        this.connKey = connKey;

        this.connHandler = connHandler;

    }

    public ConnectionFilter(ConnectionHandler connHandler) {

        this(KEY, connHandler);
    }

    public ConnectionFilter(ConnectionHandler connHandler, String connKey) {

        this(connKey, connHandler);
    }
    
    public void setKey(String key) {
        
        this.connKey = key;
        
    }

    public String filter(InvocationChain chain) throws Exception {
    	
        Action action = chain.getAction();
        
        super.setInput(action.getInput());

        action.setInput(this);

        return chain.invoke();
    }

    public void afterConsequence(Action action, Consequence c,
            boolean conseqExecuted, boolean actionExecuted, String result) {

        Connection conn = this.conn.get();
        
        this.conn.remove();
        
        if (conn != null) {
        	
            removeValue(connKey);
            
            connHandler.release(conn);
            
        } else { 
        	
        	// FIXME: Not sure when conn would be null here....
        	
        	Container container = ApplicationManager.getContainer();
        	
        	if (container != null) {
        		
        		conn = container.clear(connKey);
        		
        		if (conn != null) {
        			
        			connHandler.release(conn);
        		}
        	}
        }

    }

    public Object getValue(String key) {
        
        if (key.equals(connKey)) {
        	
            Connection c = conn.get();
            
            if (c == null) {

                try {
                	
                	c = (Connection) super.getValue(connKey);
                	
                	if (c == null) {
                		
                		c = connHandler.getConnection();
                	}
    
                    conn.set(c);
    
                    setValue(connKey, c);
    
                } catch (SQLException e) {
    
                    throw new RuntimeException(
                            "Error while getting the connection from de connection handler \'"
                                    + connHandler + "\': " + e, e);
    
                }
            }
            
            return c;
        }

        return super.getValue(key);

    }

    public void destroy() {
    	
    	connHandler.destroy();
    }
}