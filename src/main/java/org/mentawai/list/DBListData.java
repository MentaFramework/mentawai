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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.mentawai.db.ConnectionHandler;
import org.mentawai.util.JPAHelper;

/**
 * The ListData that will load its ListItems from a database.
 * 
 * All you have to do is provide:
 * 
 * - the name of the key column
 * - the name of the value column
 * - the name of the locale column
 * - the name of the table
 * - the column to sort
 * 
 * Or if you database table is not localized:
 * 
 * - the name of the key column
 * - the name of the value column
 * - the name of the table
 * - the single locale to use for that list 
 * 
 * @author Sergio Oliveira Jr.
 * @since 1.9
 *
 */
public class DBListData implements ListData, Runnable {
	
	private LocalizedListData data;
	
	private SimpleListData noI18nData;
	
	private final boolean isLocalized;
	
	private final String name, keyColumn, valueColumn, localeColumn, tableName, orderByColumn;
	
	private Thread thread = null;
	
	private long threadTime;
	
	private volatile boolean bThread;
	
	private ConnectionHandler connHandler;
	
	private String where = null;
	
	/**
	 * If your database table is localized, in other words, if it has a locale column to define the locale, use this constructor.
	 * 
	 * @param name
	 * @param keyColumn
	 * @param valueColumn
	 * @param localeColumn
	 * @param tableName
	 * @param orderByColumn
	 */
	public DBListData(String name, String keyColumn, String valueColumn, String localeColumn, String tableName, String orderByColumn) {
		
		this.name = name;
		
		this.isLocalized = true;
		
		this.keyColumn = keyColumn;
		
		this.valueColumn = valueColumn;
		
		this.localeColumn = localeColumn;
		
		this.tableName = tableName;
		
		this.orderByColumn = orderByColumn;
	}
	
	/**
	 * If your database table is not localized, in other words, if it does not care about locales and have only one list in one locale, use this constructor.
	 * 
	 * @param name
	 * @param keyColumn
	 * @param valueColumn
	 * @param tableName
	 * @param orderByColumn
	 */
	public DBListData(String name, String keyColumn, String valueColumn, String tableName, String orderByColumn) {
		
		this.name = name;
		
		this.isLocalized = false;
		
		this.keyColumn = keyColumn;
		
		this.valueColumn = valueColumn;
		
		this.localeColumn = null;
		
		this.tableName = tableName;
		
		this.orderByColumn = orderByColumn;
	}
	
	public DBListData(String name) {
		this(name, "ID");
	}
	
	public DBListData(String name, String orderBy) {
		this(name, "ID", "VALUE", name, orderBy);
	}
	
	public boolean isLocalized() {
		
		return isLocalized;
	}
	
	public void setWhere(String where) {
		
		if (where == null || where.trim().equals("")) {
			
			this.where = null;
			
		} else {
			
			String s = where.trim().toLowerCase();
			
			if (s.startsWith("where")) {
				
				this.where = where.trim();
				
			} else {
				
				this.where = "where " + where.trim();
			}
		}
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
	
	private void checkList(Object obj) {
		
		if (obj == null) throw new IllegalStateException("List is not loaded: " + name);
	}
	
	public synchronized int size() {
		
		if (isLocalized) {
			
			checkList(data);
			
			return data.size();
			
		} else {
			
			checkList(noI18nData);
			
			return noI18nData.size();
		}
		
	}
	
	public synchronized String getValue(String key, Locale loc) {
		
		if (isLocalized) {
			
			checkList(data);
		
			return data.getValue(key, loc);
			
		} else {
			
			checkList(noI18nData);
			
			return noI18nData.getValue(key, loc);
		}
	}
	
	public synchronized String getValue(String key) {
		
		if (isLocalized) {
			
			return data.getValue(key);
			
		} else {
			
			return noI18nData.getValue(key);
		}
		
	}
	
	public synchronized String getValue(int key) {
		
		return getValue(String.valueOf(key));
	}
	
	public synchronized String getValue(int key, Locale loc) {
		
		if (isLocalized) {
			
			checkList(data);
		
			return data.getValue(key, loc);
			
		} else {
			
			checkList(noI18nData);
			
			return noI18nData.getValue(key, loc);
		}
		
	}
	
	public synchronized List<ListItem> getValues(Locale loc) {
		
		if (isLocalized) {
			
			checkList(data);
			
			return data.getValues(loc);
			
		} else {
			
			checkList(noI18nData);
		
			return noI18nData.getValues(loc);
			
		}
	}
	
	public synchronized List<ListItem> getValues() {
		
		if (isLocalized) {
			
			return data.getValues();
			
		} else {
			
			return noI18nData.getValues();
		}
		
	}
	
	public String getName() {

		return name;
	}
	
	/**
	 * Override this method to map whatever you have in your database to a Java locale.
	 * 
	 * Ex: 
	 * 
	 * pt_BR can return new Locale("pt_BR") (default implementation)
	 * 
	 * "1" = new Locale("pt_BR");
	 * "2" = new Locale("en_US");
	 * etc...
	 * 
	 * @param loc
	 * @return The locale corresping to the String loc in your database
	 */
	protected Locale getLocaleFromString(String loc) {
		
		StringTokenizer st = new StringTokenizer(loc, "_");
		
		int x = st.countTokens();
		
		if (x == 1) {
			
			return new Locale(st.nextToken());
			
		} else if (x == 2) {
			
			return new Locale(st.nextToken(), st.nextToken());
			
		} else if (x == 3) {
			
			return new Locale(st.nextToken(), st.nextToken(), st.nextToken());
			
		} else {
			
			throw new IllegalArgumentException("Bad locale: " + loc);
		}
	}
	
	public void load(ConnectionHandler connHandler) {
		
		Connection conn = null;
		
		try {
			
			conn = connHandler.getConnection();
			
			load(conn);
			
		} catch(SQLException e) {
			
			throw new RuntimeException(e);
			
		} finally {
			
			connHandler.release(conn);
		}
	}
	
	protected String buildSQL(boolean isLocalized) {
		
		if (isLocalized) {
			
			return "select " + keyColumn + "," + valueColumn + "," + localeColumn + " from " + tableName + " " + (where != null ? where : "") + " order by " + orderByColumn;
			
		} else {
			
			return "select " + keyColumn + "," + valueColumn + " from " + tableName + " " + (where != null ? where : "") + " order by " + orderByColumn;
		}

	}
	
	/**
	 * Call this method passing a db connection to load the lists from database!
	 * 
	 * The SQL statement that will be constructed:
	 * 
	 * "select " + keyColumn + "," + valueColumn + "," + localeColumn + " from " + tableName + " order by " + orderByColumn;
	 * 
	 * or if localeColumn is null and a single locale (unique) is provided
	 * 
	 * "select " + keyColumn + "," + valueColumn + " from " + tableName + " order by " + orderByColumn;
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	public void load(Connection conn) {
		
		Map<String, Locale> locales = new HashMap<String, Locale>();
		
		LocalizedListData data = null; 
		
		SimpleListData noI18nData = null;
		
		if (isLocalized) {
			
			data = new LocalizedListData(name);
			
		} else {
			
			noI18nData = new SimpleListData(name);
		}

		
		String sql = buildSQL(data != null);
		
		PreparedStatement stmt = null;
		ResultSet rset = null;
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			rset = stmt.executeQuery();
			
			while(rset.next()) {
				
				String key = rset.getString(1);
				
				String value = rset.getString(2);
				
				Locale locale;
				
				if (data != null) {
					
					String loc = rset.getString(3);
					
					locale = locales.get(loc);
					
					if (locale == null) {
						
						locale = getLocaleFromString(loc);
						
						if (locale == null) throw new IllegalArgumentException("method getLocaleFromString returned null for: " + loc);
						
						locales.put(loc, locale);
					}
					
					data.add(key, value, locale);

				} else {
					
					noI18nData.add(key, value);
				}
			}
			
			synchronized(this) {
				
				this.data = data;
				
				this.noI18nData = noI18nData;
			}
			
		} catch(SQLException e) {
			
			throw new RuntimeException(e);
			
		} finally {
			
			try { if (rset != null) rset.close(); } catch(Exception e) { }
			try { if (stmt != null) stmt.close(); } catch(Exception e) { }
		}
	}
	
	/**
	 * load the lists from database from JPA Entity Manager
	 * 
	 * warning: you NEED a defined persistence unit !
	 */
	@SuppressWarnings("unchecked")
	public void loadUsingJPA() {
		
		if (!JPAHelper.existsJPAPersistenceUnit()) 
			throw new IllegalArgumentException("undefined JPA Persistence Unit! Please use JPAHelper.setPersistenceUnit in your ApplicationManager.");
		
		Map<String, Locale> locales = new HashMap<String, Locale>();
		
		LocalizedListData data = null; 
		
		SimpleListData noI18nData = null;
		
		if (isLocalized) {
			
			data = new LocalizedListData(name);
			
		} else {
			
			noI18nData = new SimpleListData(name);
		}
		
		String sql = buildSQL(data != null);
		
		// get list from database into a List<Object[]>
		EntityManager em = JPAHelper.getEntityManagerForQueries();
        Query query = em.createQuery(sql);
        List<Object[]> result = query.getResultList();
        JPAHelper.closeEntityManagerForQueries(em);

        // convert  List<Object[]> to List<ListDataItemJPA>
        ArrayList<ListDataItemJPA> listDataItemResult = new ArrayList<ListDataItemJPA>();
        for (Object[] resultElement : result) {
        	String key = null;
        	if (resultElement[0] instanceof String) {
			  key =(String)resultElement[0];	
			}
        	else
        	{
  			  key = String.valueOf(resultElement[0]);	
        	}
        	String value = null;
        	if (resultElement[1] instanceof String) {
        		value =(String)resultElement[1];	
			}
        	else
        	{
        		value = String.valueOf(resultElement[1]);	
        	}
            
            if (isLocalized) {
    			String loc = (String)resultElement[2];
    			listDataItemResult.add(new ListDataItemJPA(key,value,loc));
    		}
    		else
             listDataItemResult.add(new ListDataItemJPA(key,value));
        }
        
    	// convert list back to ListData
		for (ListDataItemJPA listDataItem : listDataItemResult){
		 
				String key =  listDataItem.getKey();
				String value = listDataItem.getValue();
				Locale locale;
				
				if (data != null) {
					
					String loc = listDataItem.getLoc();
					
					locale = locales.get(loc);
					
					if (locale == null) {
						
						locale = getLocaleFromString(loc);
						
						if (locale == null) throw new IllegalArgumentException("method getLocaleFromString returned null for: " + loc);
						
						locales.put(loc, locale);
					}
					
					data.add(key, value, locale);

				} else {
					
					noI18nData.add(key, value);
				}
			}
			
			synchronized(this) {
				
				this.data = data;
				
				this.noI18nData = noI18nData;
			}
			
	}
	
	
	public void refresh(ConnectionHandler connHandler) throws SQLException {
		
		Connection conn = null;
		
		try {
			
			conn = connHandler.getConnection();
			
			refresh(conn);
			
		} finally {
			
			connHandler.release(conn);
		}
	}
	
	/**
	 * The only difference of this method to the load(conn) method is that it
	 * will clear everything before loading...
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	public void refresh(Connection conn) throws SQLException {
		
		load(conn);
	}
}

