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
package org.mentawai.filter;

import org.mentawai.core.Action;
import org.mentawai.core.AfterConsequenceFilter;
import org.mentawai.core.ApplicationManager;
import org.mentawai.core.Consequence;
import org.mentawai.core.Context;
import org.mentawai.core.Filter;
import org.mentawai.core.FilterException;
import org.mentawai.core.InputWrapper;
import org.mentawai.core.InvocationChain;
import org.mentawai.ioc.ActionComponent;
import org.mentawai.ioc.Bean;
import org.mentawai.ioc.ScopeComponent;

/**
 * Autowiring filter for IoC. This is basically a pull approach for IoC, in other words,
 * if you don't ask for the component, it will not be instantiated.
 * 
 * On the other hand, the PushIoCFilter (old IoCFilter) has a push approach, in other words, it will 
 * instantiate (according to scope) and place the object in the action input, even if you don't need it.
 * 
 * @author Davi Luan Carneiro
 */
public class IoCFilter extends InputWrapper implements AfterConsequenceFilter {
    
    public static final int REQUEST = PushIoCFilter.REQUEST;
    public static final int SESSION = PushIoCFilter.SESSION;
    public static final int APPLICATION = PushIoCFilter.APPLICATION;
    
    private ThreadLocal<Action> action = new ThreadLocal<Action>();
    private ThreadLocal<ActionComponent> actionComponent = new ThreadLocal<ActionComponent>();
    
    private Filter oldone = null;
    
	public IoCFilter() {
		super();
	}
    
    public IoCFilter(Bean comp, String key) {
        
        oldone = new PushIoCFilter(comp, key);
        
    }
    
    public IoCFilter(Bean comp, String key, int scope) {
        
        oldone = new PushIoCFilter(comp, key, scope);
        
    }    
	
	public String filter(InvocationChain chain) throws Exception {
        
        if (oldone != null) {
            
            return oldone.filter(chain);
            
        }
		
		Action a = chain.getAction();
		
		action.set(a);
		
		super.setInput(a.getInput());
		
		a.setInput(this);
		
		return chain.invoke();
	}
	
	public void destroy() { 
    
        if (oldone != null) oldone.destroy();
    
    }
    
    public String toString() {
        
        StringBuffer sb = new StringBuffer(128);
    
        if (oldone != null) {
            
            sb.append("IocFilter (deprecated one)");
            
        } else {
            
            sb.append("IoCFilter");
            
        }
        
        return sb.toString();
        
    }
	
	private Context getSession() {
		
		Action a = action.get();
		
		return a.getSession();
		
	}
	
	private Context getApplication() {
		
		Action a = action.get();
		
		return a.getApplication(); 
		
	}
   
   private Action getAction() {
      
      return action.get();
   }
	
	public Object getValue(String key) {
     
        Object obj = super.getValue(key);
        
        if (obj != null) {
            
            return obj;
            
        }
        
        try {
            
            Bean c = ApplicationManager.getInstance().getComponent(key);
            
            if (c == null) return null;
            
            if (c instanceof ActionComponent) {
               
               // inject action if this is an action component
               // (depends on the action to do its job...)
               
               ActionComponent ac = (ActionComponent) c;
               
               actionComponent.set(ac);
               
               ac.setAction(getAction());
               
               ac.setKey(key);
               
               obj = c.getBean();
               
               if (obj != null) {
                  
                  super.setValue(key, obj);
                  
               }
               
               return obj;
               
            } else if (c instanceof ScopeComponent) {
                
                int scope = ((ScopeComponent) c).getScope();
                
                if (scope == APPLICATION) {
    
                    obj = getApplication().getAttribute(key);
                    
                    if (obj == null) {
                        obj = c.getBean();
                        getApplication().setAttribute(key, obj);
                    }
                    
                } else if (scope == SESSION) {
                    
                    obj = getSession().getAttribute(key);
                    if (obj == null) {
                        obj = c.getBean();
                        getSession().setAttribute(key, obj);
                    }
                    
                } else if (scope == REQUEST) {
    
                    obj = super.getValue(key);
                    
                    if (obj == null) {
                        obj = c.getBean();
                        super.setValue(key, obj);
                    }
                    
                } else {
                    
                    throw new FilterException("Invalid scope for IoCInput: " + scope);
                }
                
                super.setValue(key, obj);
                
                return obj;
                
            } else {
               
               // regular component...
               
               obj = c.getBean();
               
               if (obj != null) {
                  
                  super.setValue(key, obj);
                  
               }
               
               return obj;
            }
            
		} catch (Exception e) {
            
            e.printStackTrace();
            
            throw new RuntimeException("IoCFilter threw an Exception: " + e.getMessage(), e);
            
        }
	}

	@Override
    public void afterConsequence(Action action, Consequence c, boolean conseqExecuted, boolean actionExecuted, String result) {
		this.action.remove();
		ActionComponent ac = this.actionComponent.get();
		if (ac != null) {
			ac.removeAll();
		}
		this.actionComponent.remove();
    }
}