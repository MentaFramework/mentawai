package org.mentawai.rule;

import java.util.HashMap;
import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.Input;

/**
 * Make a field required only and only if another
 * field is present in the form and its value be equals the especified.
 * 
 * @author Robert Willian Gil
 */
public class DependentFieldValueRule implements Rule {

	private final static Map<String, DependentFieldValueRule> cache = new HashMap<String, DependentFieldValueRule>();
	
	private final String field1, field1Value, field2;
	
	private final Map<String, String> tokens = new HashMap<String, String>();

	protected DependentFieldValueRule(String field1, String field1Value, String field2) {
		this.field1 = field1;
		this.field1Value = field1Value;
		this.field2 = field2;
		
		tokens.put("field1", field1);
		tokens.put("field2", field2);
		tokens.put("field1Value", field1Value);
	}
	
	/**
	 * 
	 * @param field1 field that will be evaluated
	 * @param field1Value the value that will be compared with the value of first field
	 * @param field2 field that will be required if the value of field1 be equals specified in field1Value
	 * @return The rule
	 */
	public static DependentFieldValueRule getInstance(String field1, String field1Value, String field2) {
		
		StringBuilder sb = new StringBuilder(64);
		
		sb.append(field1).append('_').append(field2).append('_').append(field1Value);
		
		String key = sb.toString();
		
		DependentFieldValueRule er = cache.get(key);
		
		if (er != null) return er;
		
		er = new DependentFieldValueRule(field1, field1Value, field2);
		
		cache.put(key, er);
		
		return er;
	}

	
	public boolean check(String field, Action action) {
    	
    	Input input = action.getInput();
    	
        String f1 = input.getString(field1);
        String f2 = input.getString(field2);
        
        // if first field value be equals especified, the second field is required
        if ( f1 != null && f1.trim().equals(field1Value) ) {
        	return f2 != null && !f2.isEmpty();
        }
        
        return true;
    }
    
    public Map<String, String> getTokens() {
        return tokens;
    }        
    
}
