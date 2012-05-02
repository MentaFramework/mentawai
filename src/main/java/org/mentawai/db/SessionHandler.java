package org.mentawai.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mentacontainer.Factory;
import org.mentacontainer.Interceptor;

public class SessionHandler implements Factory, Interceptor<Session> {
	
	private final SessionFactory sessionFactory;
	private final ConnectionHandler connHandler;
	
	public SessionHandler(SessionFactory sf) {
		
		this.sessionFactory = sf;
		
		this.connHandler = new ConnectionHandler() {

			@Override
            public <T> T getInstance() {
				try {
					return (T) getConnection();
				} catch(SQLException e) {
					throw new org.mentawai.util.RuntimeException(e);
				}
			}

			@Override
            public Class<? extends Object> getType() {
				return Connection.class;
            }

			@Override
            public void onCreated(Connection createdObject) {
				// NOOP
            }

			@Override
            public void onCleared(Connection clearedObject) {
				release(clearedObject);
            }

			@Override
            public Connection getConnection() throws SQLException {
				return new SessionConnection(getSession());
            }

			@Override
            public void release(Connection conn) {
				try {
					if (conn!= null) conn.close(); // this will close the underlying SESSION object
				} catch(Exception e) {
					e.printStackTrace();
				}
            }

			@Override
            public void destroy() {
	            // NOOP
            }

			@Override
            public String getStatus() {
	            return "No Status Available (Connection from a SessionHandler!)";
            }

			@Override
            public void exec(Exec exec) {
				Connection conn = null;
				try {
					conn = getConnection();
					exec.exec(conn);
				} catch(SQLException e) {
					throw new org.mentawai.util.RuntimeException("Cannot execute command with connection!", e);
				} finally {
					release(conn);
				}
            }
		};
	}
	
	public ConnectionHandler getConnHandler() {
		return connHandler;
	}
	
	public Session getSession() {
		
		return sessionFactory.openSession();
	}
	
	public <T> T getInstance() {
		
		return (T) getSession();
			
	}
	
	public void release(Session session) {
		
		session.close();
	}
	
	public void onCleared(Session session) {
		
		release(session);
	}
	
	public void onCreated(Session session) {
		
	}
	
	public Class<? extends Object> getType() {
		
		return Session.class;
	}
}