package org.mentawai.rule;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.mentawai.core.Action;
import org.mentawai.core.Input;

public class FileSizeRule implements Rule {
	
	private final static Map<String,FileSizeRule> cache = new HashMap<String, FileSizeRule>();
	
	private final int min;
	private final int max;
	private Map<String, String> tokens = new HashMap<String, String>();
	
	private FileSizeRule(int max) {
		this.min = 0;
		this.max = max;
		tokens.put("max", String.valueOf(max));
		tokens.put("min", String.valueOf(min));
	}
	
	private FileSizeRule(int min, int max) {
		this.min = min;
		this.max = max;
		tokens.put("max", String.valueOf(max));
		tokens.put("min", String.valueOf(min));
	}
	
	public static FileSizeRule getInstance(int min, int max) {
		
		StringBuilder sb = new StringBuilder(8);
		
		sb.append(min).append("_").append(max);
		
		String s = sb.toString();
		
		FileSizeRule fsr = cache.get(s);
		
		if (fsr != null) return fsr;
		
		fsr = new FileSizeRule(min, max);
		
		cache.put(s, fsr);
		
		return fsr;
		
	}
	
	public static FileSizeRule getInstance(int max) {
		
		return getInstance(0, max);
	}
	
    public boolean check(String field, Action action) {
    	
        Input input = action.getInput();
        
        Object value = input.getValue(field);
        
        if (value == null || value.toString().trim().equals("")) {
        	
        	// if we got to this point, it means that there is no RequiredRule
        	// in front of this rule. Therefore this field is probably an OPTIONAL
        	// field, so if it is NULL or EMPTY we don't want to do any
        	// futher validation and return true to allow it.
        	
        	return true; // may be optional
        }
        
        if (value instanceof FileItem) {
        	
        	FileItem fi = (FileItem) value;
        	
        	long size = fi.getSize();
        	
        	if (size < min || size > max) return false;
        	
        	return true;
        	
        } else {
        	
        	throw new org.mentawai.util.RuntimeException("Bad file type for upload: " + value.getClass());
        }
    }
    
    public Map<String, String> getTokens() {
        return tokens;
    }
}