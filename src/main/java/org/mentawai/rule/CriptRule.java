package org.mentawai.rule;

import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.Input;
import org.mentawai.core.SessionContext;
import org.mentawai.cript.DecriptException;
import org.mentawai.cript.MentaCript;

/**
 * 
 * A rule that validate if cripted fields are ok and already decript them.
 * 
 * @author Robert Willian Gil
 *
 */
public class CriptRule implements Rule {

	private static CriptRule cache = null;
	private static CriptRule cacheCommon = null;
	private boolean useCommon = false;
	
	private CriptRule() {
	}
	
	/**
	 * Get an instance that use one key per session
	 * @return CriptRule instance
	 */
	public static CriptRule getInstance() {
		if (cache != null) return cache;
		
		cache = new CriptRule();
		return cache;
		
	}
	
	/**
	 * Get an instance that use one key pre configured always
	 * @return CriptRule instance
	 */
	public static CriptRule getCommonInstance() {
		if (cacheCommon != null) return cacheCommon;
		
		cacheCommon = new CriptRule();
		cacheCommon.useCommon = true;
		return cacheCommon;
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
			MentaCript mc;
			
			if(useCommon) {
				mc = MentaCript.getCommonInstance();
			} else {
				mc = MentaCript.getInstance((SessionContext) action.getSession());
			}
			
			input.setValue(field, mc.decript(criptedValue));
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
