package org.mentawai.db.mysql;

import org.mentawai.db.BoneCPConnectionHandler;


public class MySQLBoneCPConnectionHandler extends BoneCPConnectionHandler {
	
	private static final String DEFAULT_TEST_QUERY = "/* ping */ SELECT 1";
	
	public MySQLBoneCPConnectionHandler(String driver, String url, String user, String pass) {
		super(driver, url, user, pass, DEFAULT_TEST_QUERY);
	}
}