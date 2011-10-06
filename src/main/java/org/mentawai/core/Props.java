package org.mentawai.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Props {
	
	public static String CHARSET = "UTF-8";
	
	private final Properties def = new Properties();
	
	private final Properties prop = new Properties();
	
	private final Properties local = new Properties();
	
	public Props() { }
	
	public void loadLocal(InputStream is) throws IOException {
		
		InputStreamReader isr = null;
		
		try {
			
			isr = new InputStreamReader(is, CHARSET);
			
			local.load(isr);
			
		} finally {
			
			if (isr != null) try { isr.close(); } catch(Exception e) { }
		}
		
	}
	
	public void loadDefault(InputStream is) throws IOException {
		
		InputStreamReader isr = null;
		
		try {
			
			isr = new InputStreamReader(is, CHARSET);
			
			def.load(isr);
			
		} finally {
			
			if (isr != null) try { isr.close(); } catch(Exception e) { }
		}
		
	}
	
	public void load(InputStream is) throws IOException {
		
		InputStreamReader isr = null;
		
		try {
			
			isr = new InputStreamReader(is, CHARSET);
			
			prop.load(isr);
			
		} finally {
			
			if (isr != null) try { isr.close(); } catch(Exception e) { }
		}
	}
	
	private String get(String key) {
		
		if (local.containsKey(key)) {
			
			return local.getProperty(key);
		}
		
		if (prop.containsKey(key)) {
			
			return prop.getProperty(key);
		}
		
		if (def.containsKey(key)) {
			
			return def.getProperty(key);
		}
		
		return null;
	}
	

    public String getString(String key) {
        
        return get(key);
    }
    
    public int getInt(String key) {
    	
    	String v = get(key);
    	
    	if (v == null) throw new NullPointerException("Cannot find value for key: " + key);
        
        return Integer.parseInt(v);
    }
    
    public boolean getBoolean(String key) {
        
    	String v = get(key);
    	
    	if (v == null) throw new NullPointerException("Cannot find value for key: " + key);
    	
        return Boolean.parseBoolean(v);
    }
    
    public boolean has(String key) {
    	
    	return get(key) != null;
    }
    
}