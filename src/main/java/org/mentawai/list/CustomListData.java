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
package org.mentawai.list;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import org.mentawai.db.ConnectionHandler;

public abstract class CustomListData implements ListData, Runnable {
	
	private ListData listData;
	
	private final String name;
	
	private Thread thread = null;
	
	private long threadTime;
	
	private volatile boolean bThread;
	
	private ConnectionHandler connHandler;
	
	public CustomListData(String name) {
		
		this.name = name;
	}
	
	public void startAutoRefresh(ConnectionHandler connHandler, long time) {
		
		stopAutoRefresh();
		
		this.connHandler = connHandler;
		
		this.threadTime = time;
		
		thread = new Thread(this);
		
		bThread = true;
		
		thread.start();
	}
	
	public void stopAutoRefresh() {
		
		if (thread != null) {
			
			bThread = false;
			
			thread.interrupt();
			
			thread = null;
			
			connHandler = null;
		}
	}
	
	public void run() {
		
		while(bThread) {
			
			try {
				
				Thread.sleep(threadTime);
				
			} catch(Exception e) {
				
				return;
			}
			
			try {
			
				refresh(connHandler);
				
			} catch(SQLException e) {
				
				System.err.println("Unable to refresh list!");
				
				e.printStackTrace();
			}
		}
		
	}
	
	public synchronized int size() {
		
		return listData.size();
	}
	
	public synchronized String getValue(String key, Locale loc) {
		
		return listData.getValue(key, loc);
	}
	
	public synchronized String getValue(String key) {
		
		return listData.getValue(key);
	}
	
	public synchronized String getValue(int key) {
		
		return listData.getValue(key);
	}
	
	public synchronized String getValue(int key, Locale loc) {
		
		return listData.getValue(key, loc);
	}
	
	public synchronized List<ListItem> getValues(Locale loc) {
		
		return listData.getValues(loc);
	}
	
	public synchronized List<ListItem> getValues() {
		
		return listData.getValues();
	}
	
	public String getName() {

		return name;
	}
	
	public void load(ConnectionHandler connHandler) throws SQLException {
		
		Connection conn = null;
		
		try {
			
			conn = connHandler.getConnection();
			
			ListData data = load(conn);
			
			synchronized(this) {
				
				this.listData = data;
			}
			
		} finally {
			
			connHandler.release(conn);
		}
	}
	
	public abstract ListData load(Connection conn) throws SQLException;
	
	public void refresh(ConnectionHandler connHandler) throws SQLException {

		load(connHandler);
	}
	
	public void refresh(Connection conn) throws SQLException {
		
		load(conn);
	}
}

