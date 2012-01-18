package org.mentawai.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.Statistics;


public class BoneCPConnectionHandler extends AbstractConnectionHandler {
	
	private final BoneCP pool;
	
	public BoneCPConnectionHandler(String driver, String url, String user, String pass, String testQuery) {
		try {
			Class.forName(driver);
		} catch(ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find jdbc driver " + driver + "! You probably did not put your JDBC driver in your /WEB-INF/lib directory!");
		}
		
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(url);
		config.setUsername(user);
		config.setPassword(pass);
		
		config.setPartitionCount(1);
		config.setMinConnectionsPerPartition(3);
		config.setMaxConnectionsPerPartition(10);
		config.setAcquireIncrement(2);
		
		config.setMaxConnectionAgeInSeconds(10 * 60); // 10 minutes...
		
		if (testQuery != null) {
			config.setIdleConnectionTestPeriodInMinutes(1);
			config.setConnectionTestStatement(testQuery);
		} else {
			config.setIdleConnectionTestPeriodInMinutes(5);
		}
		
		try {
			pool = new BoneCP(config);
		} catch(SQLException e) {
			throw new IllegalStateException("Cannot initialize BoneCP: " + e);
		}
	}
	
	public BoneCPConnectionHandler(String driver, String url, String user, String pass) {
		this(driver, url, user, pass, null);
	}
	
	public BoneCP getBoneCP() {
		return pool;
	}
	
	public Statistics getStatistics() {
		return pool.getStatistics();
	}
	
	public BoneCPConnectionHandler(String driver, BoneCPConfig  config) {
		try {
			Class.forName(driver);
		} catch(ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find jdbc driver " + driver + "! You probably did not put your JDBC driver in your /WEB-INF/lib directory!");
		}
		
		try {
			pool = new BoneCP(config);
		} catch(SQLException e) {
			throw new IllegalStateException("Cannot initialize BoneCP: " + e);
		}
	}

	@Override
    public Connection getConnection() throws SQLException {
		return pool.getConnection();
	}

	@Override
    public void release(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch(SQLException e) {
				throw new IllegalStateException("A connection could not be closed by BoneCP: " + e);
			}
		}
    }

	@Override
    public void destroy() {
	    pool.shutdown();
    }

	@Override
    public String getStatus() {
		
		Statistics stats = pool.getStatistics();
		
		int free = stats.getTotalFree();
		int leased = stats.getTotalLeased();
		
		StringBuilder sb = new StringBuilder(256);
		sb.append("Busy: ").append(leased);
		sb.append(" / Idle: ").append(free);
		sb.append(" / Total: ").append(free + leased);
		
		return sb.toString();
	}
}
