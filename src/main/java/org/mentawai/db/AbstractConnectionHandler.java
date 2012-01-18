package org.mentawai.db;

import java.sql.Connection;

public abstract class AbstractConnectionHandler implements ConnectionHandler {
	
	@Override
	public void exec(Exec exec) {
		Connection conn = null;
		try {
			conn = getConnection();
			exec.exec(conn);
		} catch(Exception e) {
			throw new RuntimeException(e);
		} finally {
			release(conn);
		}
	}
	
	@Override
    public <T> T getInstance() {
		try {
	    	
    		return (T) getConnection();
    		
    	} catch(Exception e) {
    		
    		throw new RuntimeException(e);
    	}
    }

	@Override
    public Class<? extends Object> getType() {
		return Connection.class;
    }

	@Override
    public void onCreated(Connection createdObject) {
		// nothing here
    }

	@Override
    public void onCleared(Connection clearedObject) {
	    release(clearedObject);
    }
}