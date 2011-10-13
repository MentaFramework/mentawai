package org.mentawai.util;

import org.mentawai.log.Warn;

public class SystemUtils {
	
	public static boolean getBoolean(String name, boolean def) {
		
		String s = getValue(name);
		
		if (s == null) return def;
		
		return s.equals("true");
	}
	
	public static boolean getBoolean(String name) {
		
		return getBoolean(name, false);
	}
	
	public static String getString(String name, String def) {
		
		String s = getValue(name);
		
		if (s == null) return def;
		
		return s;
	}
	
	public static String getString(String name) {
		
		return getString(name, null);
	}
	
	private static String getValue(String name) {
		
		String s = null;

		try {
			
			s = System.getenv(name);
			
		} catch(SecurityException e) {
			Warn.log("Cannot access environment variable (\"" + name + "\"). Call to System.getenv(\"" + name + "\") failed! Check your SecurityManager.");
		}
		
		if (s != null) return s;
		
		try {
			
			s = System.getProperty(name);
			
		} catch(SecurityException e) {
			Warn.log("Cannot access system property (\"" + name + "\"). Call to System.getProperty(\"" + name + "\") failed! Check your SecurityManager.");
		}
		return s;
	}
}