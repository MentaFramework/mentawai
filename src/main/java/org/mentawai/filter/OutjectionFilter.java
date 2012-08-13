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

import java.lang.reflect.Method;

import org.mentawai.core.Action;
import org.mentawai.core.AfterConsequenceFilter;
import org.mentawai.core.Consequence;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.Output;
import org.mentawai.core.OutputWrapper;
import org.mentawai.core.PojoAction;
import org.mentawai.jruby.JRubyInterpreter;
import org.mentawai.jruby.RubyAction;

/**
 * A filter that takes all the properties of the action and place them in the output,
 * so that you don't have to call output.setValue() manually.
 * 
 * Note that for plain ations this filter uses the method <i>action.getClass().getDeclaredMethods()</i> 
 * in order to find the getters, in other words,
 * it will only find getters of the base class and not from its superclasses. This is ok because
 * action inheritance is not very common.
 * 
 * If you are using a POJO action or a model-driven action it will use the method getMethods instead.
 * 
 * For ModelDriven actions, the method <i>action.getModel().getClass().getMethods()</i> is used instead, 
 * pretty much like in the OVFilter.
 * 
 * @author Sergio Oliveira
 */
public class OutjectionFilter extends OutputWrapper implements AfterConsequenceFilter {
	
	private ThreadLocal<Action> action = new ThreadLocal<Action>();
	
    /**
     * Creates a OutjectionFilter.
     */
	public OutjectionFilter() { }
        
    
    private String adjustName(String name) {
        StringBuffer sb = new StringBuffer(name.length() - 3);
        sb.append(name.substring(3, 4).toLowerCase());
        sb.append(name.substring(4, name.length()));
        return sb.toString();
    }
    
    @Override
    public Object getValue(String key) {
    	
    	Object value = super.getValue(key);
    	
    	if (value != null) {
    		
    		return value;
    	}
    	
    	// this will only be called when we have a RubyAction...
    	
    	Action action = this.action.get();
    	
    	if (action == null) throw new IllegalStateException("No action was found!");
    	
    	if (!(action instanceof RubyAction)) throw new IllegalStateException("Action is not a Ruby Action!");
    	
    	long rubyActionId = ((RubyAction) action).getRubyActionId();
    	
    	JRubyInterpreter ruby = JRubyInterpreter.getInstance();
    	
    	StringBuilder sb = new StringBuilder(64);
    	sb.append("Mentawai::JRuby::Utils.get_prop(").append(rubyActionId).append(",'").append(key).append("')");
    	
    	value = ruby.eval(sb.toString());
    	
    	if (value != null) {
    		
    		super.setValue(key, value);
    		
    		return value;
    		
    	} else {
    		
    		return super.getValue(key);
    	}
    }
    
	public String filter(InvocationChain chain) throws Exception {
        
        String result = chain.invoke();
        
		Action action = chain.getAction();
        Output output = action.getOutput();
        
        boolean isModelDriven = false;
        
        boolean isPojoAction = false;
        
        boolean isRubyAction = false;
        
        if (action instanceof RubyAction) {
        	
        	isRubyAction = true;
        	
        } else if (action instanceof ModelDriven) {
        	
            isModelDriven = true;
            
        } else if (action instanceof PojoAction) {
        	
        	isPojoAction = true;
        }
        
        if (isRubyAction) {
        	
        	// similar to IoCFilter... place a new Output so the view
        	// can have access to the ruby object properties...
        	
        	this.action.set(action);
        	
        	super.setOutput(action.getOutput());
        	
        	action.setOutput(this);
        	
        } else {
        
	        Method [] methods = null;
	        
	        if (isModelDriven) {
	        	
	            ModelDriven md = (ModelDriven) action;
	            
	            methods = md.getModel().getClass().getMethods();
	            
	        } else if (isPojoAction) {
	        	
	        	PojoAction pa = (PojoAction) action;
	        	
	        	methods = pa.getPojo().getClass().getMethods();
	        	
	        } else {
	        	
	            methods = action.getClass().getDeclaredMethods();
	        }
	
	        for(int i=0;i<methods.length;i++) {
	            String name = methods[i].getName();
	            if (name.length() > 3 && name.startsWith("get") && methods[i].getParameterTypes().length == 0) {
	            	
	                if (name.equals("getClass")) continue;
	                
	                try {
	                    methods[i].setAccessible(true);
	                    
	                    Object value = null;
	                    
	                    if (isModelDriven) {
	                        
	                        ModelDriven md = (ModelDriven) action;
	                        
	                        value = methods[i].invoke(md.getModel(), (Object[]) null);
	                        
	                    } else if (isPojoAction) {
	                    	
	                    	PojoAction pa = (PojoAction) action;
	                    	
	                    	value = methods[i].invoke(pa.getPojo(), (Object[]) null);
	                        
	                    } else {
	                    
	                        value = methods[i].invoke(action,  new Object[0]);
	                        
	                    }
	
	                    output.setValue(adjustName(name), value);
	                    
	                } catch(Exception e) {
	                    System.err.println("Error calling method in OutputFilter: " + name);
	                    e.printStackTrace();
	                }
	            }
	        }
        }
		return result;
	}
    
    public void destroy() { }


	@Override
    public void afterConsequence(Action action, Consequence c, boolean conseqExecuted, boolean actionExecuted, String result) {
		if (action instanceof RubyAction) {
			// remember to clear the thread local
			super.removeOutput();
		}
    }
}
		