package org.mentawai.coc;

import org.mentawai.core.Consequence;
import org.mentawai.core.Forward;

/**
 * The default consequence provider used by Mentawai controller 
 * when autoView is set to true, which is the default anyways.
 * 
 * @author Sergio Oliveira Jr.
 */
public class DefaultConsequenceProvider implements ConsequenceProvider {
   
   public Consequence getConsequence(String action, Class<? extends Object> actionClass, String result, String innerAction) {
	   
	   StringBuilder sb = new StringBuilder(128);
		
	   sb.append("/").append(action).append("/");
		
		if (innerAction != null) {
		    
		    sb.append(innerAction).append(".jsp");
		    
		} else {
			
			sb.append("index.jsp");
		}
		
		Consequence c = new Forward(sb.toString());
		
		return c;
   }
}
