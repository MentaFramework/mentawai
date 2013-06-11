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
		String[] criptedValues = input.getStrings( fieldCriptedName );
		
		// if field does not exist this check pass, use RequiredRule to assert that it will required
		if(criptedValues == null) {
			return true;	
		}
		
		try {
			MentaCript mc;
			
			if(useCommon) {
				mc = MentaCript.getCommonInstance();
			} else {
				mc = MentaCript.getInstance((SessionContext) action.getSession());
			}
			
			if(criptedValues.length == 1) {
				input.setValue(field, mc.decript(criptedValues[0]));
			} else {
				
				String[] decriptedValues = new String[criptedValues.length];
				for (int i = 0; i < criptedValues.length; i++) {
					decriptedValues[i] = mc.decript(criptedValues[i]);
				}
				input.setValue(field, decriptedValues);
			}
			
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
