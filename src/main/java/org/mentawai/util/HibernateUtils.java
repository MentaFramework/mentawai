package org.mentawai.util;

import java.lang.reflect.Method;
import java.sql.Connection;

import org.hibernate.Session;

public class HibernateUtils {
	
	private static Method method = null;
	
	public static Connection getConnectionFrom(Session session) {
		if (method == null) {
			try {
				method = session.getClass().getMethod("connection");
			} catch(java.lang.Exception e) {
				throw new org.mentawai.util.RuntimeException("Cannot find getConnection method!", e);
			}
		}
		
		try {
			return (Connection) method.invoke(session, (Object[]) null);
		} catch(java.lang.Exception e) {
			throw new org.mentawai.util.RuntimeException("Error getting connection from session!", e);
		}
	}
}
