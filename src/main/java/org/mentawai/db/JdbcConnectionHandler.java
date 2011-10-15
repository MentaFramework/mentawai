package org.mentawai.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionHandler extends AbstractConnectionHandler {
	
	private final String url;
	private final String user;
	private final String pass;
	
	public JdbcConnectionHandler(String url, String user, String pass) {
		this.url = url;
		this.user = user;
		this.pass = pass;
	}
	
	public JdbcConnectionHandler(String driver, String url, String user, String pass) {
		this(url, user, pass);
		
		try {
			
			Class.forName(driver);
			
		} catch(Exception e) {
			
			throw new RuntimeException(e);
		}
	}
	
	public Connection getConnection() throws SQLException {
		
		return DriverManager.getConnection(url, user, pass);
	}
	
    public String getStatus() {
    	
    	return "This is not a pool, so nothing to show here!";
    }
	
	public void release(Connection conn) {
		
		try {
			if (conn != null) conn.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void destroy() { }
	
	public <T> T getInstance() {
		
		try {
		
			return (T) getConnection();
			
		} catch(Exception e) {
			
			throw new RuntimeException(e);
		}
	}
	
	public Class<? extends Object> getType() {
		
		return Connection.class;
	}
	
	public void onCleared(Connection conn) {
		
		release(conn);
	}
	
	public void onCreated(Connection conn) {
		
	}
}