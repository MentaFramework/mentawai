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

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.FilterException;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;
import org.mentawai.db.ConnectionHandler;

/**
 * A filter to serve a ready-to-use database connection to its actions.
 *
 * @author Sergio Oliveira
 */
public class PushConnectionFilter implements Filter {
    
    /** The default key name to use when placing the connection in the action input. */
    public static final String KEY = "conn";
	
    private ConnectionHandler connHandler;
    private String conn_key = KEY;
	
    /**
     * Creates a ConnectionFilter with the given ConnectionHandler.
     *
     * @param connHandler The connection handler to use for this filter.
     */
	public PushConnectionFilter(ConnectionHandler connHandler) {
        this.connHandler = connHandler;
    }
    
    /**
     * Creates a ConnectionFilter with the given ConnectionHandler and connection key.
     * This key will be used to place the connection in the action input.
     *
     * @param connHandler The connection handler to use for this filter.
     * @param conn_key The name of the key to use when placing the connection in the action input.
     */
    public PushConnectionFilter(ConnectionHandler connHandler, String conn_key) {
        this(connHandler);
        this.conn_key = conn_key;
    }
	
	public String filter(InvocationChain chain) throws Exception {
		Action action = chain.getAction();
		Input input = action.getInput();
        
        Connection conn = null;
        try {
            conn = connHandler.getConnection();
            input.setValue(conn_key, conn);
            return chain.invoke();
        } catch(SQLException e) {
            throw new FilterException(e);
        } finally {
            input.removeValue(conn_key);
            connHandler.release(conn);
        }
	}
    
    public void destroy() { }
}
		