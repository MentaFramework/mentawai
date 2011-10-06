package org.mentawai.coc;

import org.mentawai.core.Action;
import org.mentawai.core.Consequence;
import org.mentawai.core.Forward;

/**
 * This consequence provider was build for backward compatibility with the old
 * auto view schema, in other words, if you were using the old auto view schema
 * you can use the consequence provider to simulate the same behaviour when 
 * choosing your consequences.
 * 
 * @author Sergio Oliveira Jr.
 */
public class OldAutoViewConsequenceProvider implements ConsequenceProvider {
   
   public Consequence getConsequence(String action, Class<? extends Object> actionClass, String result, String innerAction) {
	   
	   if (result == null) return null;
    
	   StringBuilder sb = new StringBuilder(128);
		
	   sb.append("/").append(action).append("/");
		
		if (innerAction != null) {
		    
		    sb.append(innerAction).append(".");
		}
		
		if (result.equals(Action.SUCCESS)) {
		    
		    sb.append("ok.jsp");
		    
		} else {
		    
		    sb.append(result).append(".jsp");
		}
		
		Consequence c = new Forward(sb.toString());
		
		return c;
   }
}
