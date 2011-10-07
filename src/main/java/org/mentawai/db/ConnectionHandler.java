/*
 * Mentawai Web Framework http://mentawai.lohis.com.br/ Copyright (C) 2005 Sergio Oliveira Jr. (sergio.oliveira.jr@gmail.com)
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.mentawai.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.mentacontainer.Factory;
import org.mentacontainer.Interceptor;

/**
 * This interface describes the behaviour of a Mentawai database connection provider. Classes implementing this interface may or may not use
 * an underlying connection pool. This is a transparent way to get a connection to a database with Mentawai.
 * 
 * @author Sergio Oliveira
 */
public interface ConnectionHandler extends Factory, Interceptor<Connection> {

	/**
	 * Returns a Connection to the database. Notice that it can never return null. If no connection can be acquired, then a SQLException is
	 * thrown.
	 * 
	 * @return A connection to the database.
	 * @throws SQLException if there were problems trying to acquire the connection.
	 */
	public Connection getConnection() throws SQLException;

	/**
	 * Release this connection. If this connection handler is using a database pool, the connection is returned to the pool. If this
	 * connection handler is not using a database pool, the connection is closed. If the connection passed as an argument is null, nothing
	 * is done.
	 * 
	 * @param conn The connection to release.
	 */
	public void release(Connection conn);

	/**
	 * Destroy the connection pool, closing and cleaning all connections. This is important for restarting the web context.
	 * 
	 * @since 1.11
	 */
	public void destroy();

	/**
	 * Print the status of this connection pool if any.
	 * 
	 * @return The status
	 */
	public String getStatus();

}
