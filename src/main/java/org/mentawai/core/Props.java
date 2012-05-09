package org.mentawai.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.mentaregex.Regex;

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
    
    public List<String> getList(String key) {
    	
    	String v = get(key);
    	
    	if (v == null) throw new NullPointerException("Cannot find value for key: " + key);
    	
    	String[] temp = v.split("\\s*,\\s*");
    	
    	return new ArrayList<String>(Arrays.asList(temp));
    }
    
    public boolean has(String key) {
    	
    	return get(key) != null;
    }
    
    public String getPath(String key) {
    	String s = getString(key);
    	if (File.separatorChar == '/') {
    		return s;
    	}
    	return s.replaceAll("\\/", "\\\\");
    }
    
    public String getAbsolutePath(String key) {
    	String start = ApplicationManager.getRealPath();
    	String s = getPath(key);
    	if (s.startsWith(File.separator)) {
    		return start + s;
    	} else {
    		return start + File.separator + s;
    	}
    }
    
    public Class<? extends Object> getClass(String key) {
    	String className = getString(key);
    	try {
    		return Class.forName(className);
    	} catch(Exception e) {
    		throw new RuntimeException(e);
    	}
    }
    
    private String[] makeArray(String v) {

    	if (v.length() == 0) return new String[0];
    	
    	String[] array = v.split("\\s*,\\s*");
    	
    	for(int i = 0; i < array.length; i++) {
    		String s = array[i];
    		if ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'"))) {
    			array[i] = s.substring(1, s.length() - 1);
    		}
    	}
    	return array;
    }
    
    public String[] getArray(String key) {
    	String v = get(key).trim();
    	
    	if (!v.startsWith("[") || !v.endsWith("]")) throw new IllegalArgumentException("Not an array: " + v);
    	
    	v = v.substring(1, v.length() - 1).trim();
    	
    	return makeArray(v);
    }
    
    public List<String[]> getArrays(String key) {
    	
    	String v = get(key).trim();
    	
    	if (!v.startsWith("[") || !v.endsWith("]")) throw new IllegalArgumentException("Not an array: " + v);
    	
    	v = v.substring(1, v.length() - 1).trim();
    	
    	String[] s = Regex.match(v, "/\\[(.*?)\\]/g");
    	
    	List<String[]> list = new ArrayList<String[]>(s.length);
    	
    	for(String array : s) {
    		list.add(makeArray(array));
    	}
    	
    	return list;
    }
    
}