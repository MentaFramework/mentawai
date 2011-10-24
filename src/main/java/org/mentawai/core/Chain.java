/*
 * Mentawai Web Framework http://mentawai.lohis.com.br/
 * Copyright (C) 2005  Sergio Oliveira Jr. (sergio.oliveira.jr@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.mentawai.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An action chaining consequence.
 * 
 * @author Sergio Oliveira
 */
public class Chain implements Consequence {
	
   private ActionConfig ac;
   
   private String innerAction = null;
   
   private Class<? extends Object> actionClass;
	
	/**
	 * Creates a chain consequence for the given ActionConfig.
	 * 
	 * @param ac The ActionConfig to chain.
	 */
	public Chain(ActionConfig ac) {
		this.ac = ac;
	}
   
   /**
    * Creates a chain consequence for the given ActionConfig
    * @param ac
    * @param innerAction
    * @since 1.12
    */
   public Chain(ActionConfig ac, String innerAction) {
      
      this(ac);
      
      this.innerAction = innerAction;
   }
   
   public Chain(Class<? extends Object> klass, String innerAction) {
	   this.actionClass = klass;
	   this.innerAction = innerAction;
   }
   
   public Chain(Class<? extends Object> klass) {
	   this.actionClass = klass;
   }
    
    public void execute(Action a, String result, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException {
   
    	// Lazy load of the ActionConfig (by Ricardo Rufino)
    	if(ac == null){
    		ActionConfig def = new ActionConfig(actionClass, innerAction); // Temp config only to load...
    		
    		// Tenta carregar um configuração já existente, se não encontrar ele usa uma padrão.
    		ac = ApplicationManager.getInstance().getActionConfig(def.getName(), def.getInnerAction());

    		// There was no way, using the standard
    		if(ac == null){
    			ac = def;
    		}
    	}
    	
    	
		Action action = ac.getAction();
        if (action == null) {
            throw new ConsequenceException("Could not load action for chain: " + ac);
        }
        
        /*
         * Because of the new InputWrapper filters, do not re-use the input but copy its values !
         */
        
        Input input = new RequestInput(req, res);
        
        Input old = a.getInput();
        
        Iterator<String> iterOld = old.keys();
        
        while(iterOld.hasNext()) {
            
            String key = (String) iterOld.next();
            
            input.setValue(key, old.getValue(key));
            
        }
        
        action.setInput(input);
        action.setOutput(a.getOutput());
        action.setSession(a.getSession());
        action.setApplication(a.getApplication());
        action.setLocale(a.getLocale());
        action.setCookies(a.getCookies());
        
        Consequence c = null;
        
        List<Filter> filters = new ArrayList<Filter>(32);
        
        boolean conseqExecuted = false;
        
        boolean actionExecuted = false;
        
        StringBuilder returnedResult = new StringBuilder(32);
        
        try {
           
           String innerAction;
           
           if (this.innerAction != null) {
              
              innerAction = this.innerAction;
              
           } else {
              
              innerAction = ac.getInnerAction();
           }
           
            c = Controller.invokeAction(ac, action, innerAction, filters, returnedResult);
            
            actionExecuted = true;
        
            c.execute(action, returnedResult.toString(), req, res);
            
            conseqExecuted = true;
            
        } catch(ConsequenceException e) {
            throw e;
        } catch(Exception e) {
            throw new ConsequenceException(e);
        } finally {
            
        	for(int i = filters.size() - 1; i >= 0; i--) {
                
                Filter f = filters.get(i);
                
                if (f instanceof AfterConsequenceFilter) {
                	
                    AfterConsequenceFilter acf = (AfterConsequenceFilter) f;
                    
                    try {
                    	
                    	String s = returnedResult.toString();
                        
                        acf.afterConsequence(action, c, conseqExecuted, actionExecuted, s.length() > 0 ? s : null);
                        
                    } catch(Exception e) {
                        
                        e.printStackTrace();
                    }
                    
                }
                
            }
        }
    }
    
    public String toString() {
        
        StringBuffer sb = new StringBuffer(128);
        
        sb.append("Chain to ").append(ac);
        
        if (innerAction != null) sb.append(" (innerAction = ").append(innerAction).append(")");
        
        return sb.toString();
    }
}

	
	