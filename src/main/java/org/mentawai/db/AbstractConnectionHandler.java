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
}