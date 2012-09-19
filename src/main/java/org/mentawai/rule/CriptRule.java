package org.mentawai.rule;

import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.Input;
import org.mentawai.cript.MentaCript;
import org.mentawai.cript.DecriptException;

/**
 * 
 * A rule that validate if cripted fields are ok and already decript them.
 * 
 * @author Robert Willian Gil
 *
 */
public class CriptRule implements Rule {

	private static CriptRule cache = null;
	
	public static CriptRule getInstance() {
		
		if (cache != null) return cache;
		
		cache = new CriptRule();
		
		return cache;
		
	}
	
	public boolean check(String field, Action action) {
		
		Input input = action.getInput();
		
		String fieldCriptedName = MentaCript.PREFIX_CRIPT_TAG + field;
		String criptedValue = input.getString( fieldCriptedName );
		
		// if field does not exist this check pass, use RequiredRule to assert that it will required
		if(criptedValue == null) {
			return true;	
		}
		
		try {
			input.setValue(field, MentaCript.decript(criptedValue));
			return true;
		} catch (DecriptException e) {
			return false;
		}
		
	}
	
	public Map<String, String> getTokens() {
		return null;
	}
	
	protected String removePrefix(String s){
		return s.replaceAll(MentaCript.PREFIX_CRIPT_TAG, "");
	}

}
