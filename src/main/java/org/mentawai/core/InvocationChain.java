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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.mentawai.filter.MethodParamFilter;
import org.mentawai.jruby.JRubyInterpreter;
import org.mentawai.jruby.RubyAction;
import org.mentawai.util.DebugServletFilter;
import org.mentawai.util.InjectionUtils;

/**
 * When an action is executed, a chain of filters is created.
 * The last step of any InvocationChain is the action.
 * An action may have one or more filters and global filters.
 * 
 * @author Sergio Oliveira
 */
public class InvocationChain {
	
	private LinkedList<Filter> filters = new LinkedList<Filter>();
	private Action action;
    private String innerAction = null;
    private final String actionName;
    private ActionConfig actionConfig;

	/**
	 * Creates an InvocationChain for this action.
	 * 
	 * @param action The action for what this InvocationChain will be created.
	 */
	public InvocationChain(String actionName, Action action, ActionConfig ac) {
		
		this.actionName = actionName;
		
		this.action = action;
		
		this.actionConfig = ac;
	}
   
	public ActionConfig getActionConfig() {
		
		return actionConfig;
	}
	
   public Filter getFilter(Class<? extends Filter> filterClass) {
      
      Iterator<Filter> iter = filters.iterator();
      
      while(iter.hasNext()) {
         
         Filter f = iter.next();
         
         if (filterClass.isAssignableFrom(f.getClass())) {
            
            return f;
         }
      }
      
      return null;
   }

	/**
	 * Gets the action of this InvocationChain
	 * 
	 * @return The action of this InvocationChain
	 */
	public Action getAction() {
		return action;
	}
	
	/**
	 * Check whether this action is a PojoAction to return its pojo.
	 * 
	 * @return the Pojo or null if not pojo action
	 * @since 1.10
	 */
	public Object getPojo() {
		
		if (action instanceof PojoAction) {
			
			PojoAction pa = (PojoAction) action;
			
			return pa.getPojo();
		}
		
		return null;
	}

	void addFilter(Filter filter) {
		filters.add(filter);
	}

	void addFilters(List<Filter> list) {
		filters.addAll(list);
	}
   
   void clearFilters() {
      filters.clear();
   }
    
	/**
	 * Invoke and execute the next step in this InvocationChain.
	 * This can be the next filter or the action.
	 * 
	 * @return The result of a filter or the action.
	 */
	public String invoke() throws Exception {
        
        if (Controller.debugMode) DebugServletFilter.debugInputOutput(action);
            
		if (!filters.isEmpty()) {
			Filter f = (Filter) filters.removeFirst();
            
            if (Controller.debugMode) {
                
                StringBuffer sb = DebugServletFilter.getDebug(action);
                
                DebugServletFilter.debug(sb, f);
                
            }
            
			return f.filter(this);
		}
        
        if (Controller.debugMode) {
            
            StringBuffer sb = DebugServletFilter.getDebug(action);
            
            DebugServletFilter.debug(sb, innerAction, false);
            
        }
        
        String result = null;
        
        if (action instanceof RubyAction) {
        	
        	JRubyInterpreter ruby = JRubyInterpreter.getInstance();
        	
        	RubyAction rubyAction = (RubyAction) action;
        	
        	Object rubyObject = rubyAction.getRubyObject();
        	
        	if (innerAction != null) {
        		
        		result = ruby.callAction(rubyObject, innerAction);
        		
        	} else {
        		
        		result = ruby.callAction(rubyObject, "execute");
        	}
        	
        } else {
        
	        Object pojo = null;
	        
	        boolean isPojoAction = false;
	        
	        if (action instanceof PojoAction) {
	        	
	        	PojoAction pa = (PojoAction) action;
	        	
	        	pojo = pa.getPojo();
	        	
	        	isPojoAction = true;
	        	
	        } else {
	        	
	        	pojo = action;
	        }
	        
	    	String methodToExec = innerAction;
	    	
	    	if (methodToExec == null) {
	    		
	    		methodToExec = "execute";
	    	}
	    	
	    	// starting 1.13 you can now execute any method from an action
	    	// not just PojoActions like before...
	    	
	    	Method[] m = pojo.getClass().getMethods();
	    	
	    	for(int i=0;i<m.length;i++) {
	    		
	    		if (!Modifier.isPublic(m[i].getModifiers())) continue;
	    		
	    		if (m[i].getName().equals(methodToExec)) {
	    			
	    			Method theOne = m[i];
	    			
	            	Class<?>[] params = theOne.getParameterTypes();
	            	
	            	Input input = action.getInput();
	            	
	            	Object[] paramValues = new Object[params.length];
	            	
	            	Set<String> paramKeys = new HashSet<String>();
	            	
	            	for(int j=0;j<params.length;j++) {
	            		
	            		boolean found = false;
	            		
	            		// check if we are using the MethodParamFilter!
	            		
	            		List<String> list = (List<String>) input.getValue(MethodParamFilter.PARAM_KEY);
	            		
	            		Iterator<String> keys;
	            		
	            		if (list == null) {
	            			
	            			keys = input.keys();
	            			
	            		} else {
	            			
	            			keys = list.iterator();
	            		}
	            		
	            		while(keys.hasNext()) {
	            			
	            			String key = keys.next();
	            			
	            			if (paramKeys.contains(key)) continue;
	            			
	            			Object o = input.getValue(key);
	            			
	            			if (params[j].isInstance(o)) {
	            				
	            				paramValues[j] = o;
	            				
	            				paramKeys.add(key);
	            				
	            				found = true;
	            				
	            				break;
	            				
	            			} else {
	            				
	            				Object converted = InjectionUtils.tryToConvert(o, params[j], action.getLocale(), true);
	            				
	            				if (converted != null) {
	            					
	            					paramValues[j] = converted;
	            					
	            					paramKeys.add(key);
	            					
	            					found = true;
	            					
	            					break;
	            				}
	            			}
	            		}
	            		
	            		if (!found) {
	            			
	            			// let's try to create an object on the fly here...
	            			// if we have something like add(User u1, User u2) we may get in trouble here,
	            			// but for this case the user should configure the parameters by hand using a
	            			// VOFilter ou MethodParamFilter...
	            			
	            			// The if is because this is suppose to be a POJO, not a java.lang.String for example...
	            			
	            			if (!params[j].getName().startsWith("java.lang.") && !params[j].isPrimitive() && InjectionUtils.hasDefaultConstructor(params[j])) {
	            			
	                			Object obj = action.getInput().getObject(params[j]);
	                			
	                			String key = params[j].getSimpleName().toLowerCase();
	                			
	                			paramKeys.add(key);
	                			
	                			paramValues[j] = obj;
	                			
	                			action.getInput().setValue(key, obj);
	                			
	            			} else {
	            				
	            				// if not found pass NULL value...
	            				
	            				String key = params[j].getSimpleName().toLowerCase();
	            				paramKeys.add(key);
	            				if (params[j].isPrimitive()) {
	            					paramValues[j] = getNullForPrimitive(params[j]);
	            				} else {
	            					paramValues[j] = null;
	            				}
	            			}
	            			
	            		}
	            	}
	            	
	            	Object retval = theOne.invoke(pojo, paramValues);
	            	
	            	if (theOne.getReturnType() == null || theOne.getReturnType().equals(Void.TYPE)) {
	            		
	            		return Action.SUCCESS; // default for void method...
	            		
	            	} else if (theOne.getReturnType() == null || theOne.getReturnType().equals(String.class)) {
	            		
	            		return retval.toString(); // simple string...
	            		
	            	} else {
	            		
	                	// Returning something different than a String, so save the result in the action output...
	                	action.getOutput().setValue(PojoAction.RESULT, retval);
	            		
	            		if (retval == null) {
	            			
	            			return Action.NULL;
	            			
	            		} else {
	            			
	            			return retval.toString();
	            		}
	            	}
	    		}
	    	}
	    	
	    	if (isPojoAction) throw new ActionException("Cannot find method to execute: " + methodToExec);
	    	
			if (innerAction != null) {
				
				// first try an inner class...
				
				Class<Action> klass = getInnerClass(innerAction);
				
				if (klass != null) {
					
					// get constructor...
					
					Constructor c = getConstructor(klass);
					
					if (c == null) throw new ActionException("The inner class for this inner action does not have a constructor: " + innerAction);
					
					// ok, now check whether this is a static inner class or instance inner class...
					
					Class[] paramTypes = c.getParameterTypes();
					
					if (paramTypes.length == 0) {
						
						// static inner class...
						
						try {
							
							Action innerActionClass = klass.newInstance();
							
							initInnerAction(action, innerActionClass);
							
							Method executeMethod = getMethod(innerActionClass, "execute");
							
							if (executeMethod != null) {
								
								result = (String) executeMethod.invoke(innerActionClass, (Object[]) null);
									
							} else {
								
								throw new ActionException("The static inner action class does not have the execute method!");
								
							}
							
							//result = innerActionClass.execute(); // removed from interface on version 1.12
	                 
						} catch(ActionException e) {
							
							throw e;
							
						} catch(Exception e) {
							
							throw new ActionException(e);
						}
						
					} else if (paramTypes.length == 1) {
						
						// instance inner class...
						
						try {
							
							Action innerActionClass =  (Action) c.newInstance(action);
							
							initInnerAction(action, innerActionClass);
							
							Method executeMethod = getMethod(innerActionClass, "execute");
							
							if (executeMethod != null) {
								
								result = (String) executeMethod.invoke(innerActionClass, (Object[]) null);
									
							} else {
								
								throw new ActionException("The innerAction class does not have the execute method!");
								
							}
							
							//result = innerActionClass.execute(); // removed from interface on version 1.12
	                 
	              } catch(ActionException e) {
	                 
	                 throw e;
							
						} catch(Exception e) {
							
							throw new ActionException(e);
						}
						
					}
					
				} else {
				
					Method method = getMethod(innerAction);
					
					if (method != null) {
						try {
							result = (String) method.invoke(action, (Object[]) null);
						} catch(Exception e) {
							throw new ActionException(e);
						}
					} else {
						throw new ActionException("The inner action does not exist: " + innerAction);
					}
				
				}
			} else {
				
				Method method = getMethod(action, "execute");
				
				if (method != null) {
	           
					try {
	              
						result = (String) method.invoke(action, (Object[]) null);
	              
					} catch(Exception e) {
	              
						throw new ActionException(e);
	              
					}
	           
				} else {
	           
					throw new ActionException("The action does not implement the execute method!");
	           
				}
	
				//result = action.execute(); // removed from interface in version 1.12
			}
		
        }
		
        return result;
	}
	
	private static Object getNullForPrimitive(Class<? extends Object> target) {

		if (target.equals(int.class)) return Integer.valueOf(-1);

		if (target.equals(boolean.class)) return Boolean.FALSE;

		if (target.equals(byte.class)) return new Byte((byte) -1);

		if (target.equals(short.class)) return Short.valueOf((short) -1);

		if (target.equals(char.class)) return Character.valueOf((char) -1);

		if (target.equals(long.class)) return Long.valueOf((long) -1);

		if (target.equals(float.class)) return Float.valueOf((float) -1);

		if (target.equals(double.class)) return Double.valueOf((double) -1);

		throw new IllegalArgumentException("Bad target: " + target);
	}
	
	/**
	 * Initialize inner action class contexts with the main action contexts...
	 *  
	 * @param mainAction The main action.
	 * @param innerAction The inner action object. (inner class)
	 * @since 1.9
	 */
	protected void initInnerAction(Action mainAction, Action innerAction) {
		
		innerAction.setInput(mainAction.getInput());
		innerAction.setOutput(mainAction.getOutput());
		innerAction.setSession(mainAction.getSession());
		innerAction.setApplication(mainAction.getApplication());
		innerAction.setCookies(mainAction.getCookies());
		innerAction.setLocale(mainAction.getLocale());
		
	}
	
	private Constructor getConstructor(Class<Action> klass)  {
		
		Constructor c = null;
		
		try {
		
			c = klass.getConstructor((Class[]) null);
			
		} catch(NoSuchMethodException e) {

			// do nothing... not found...
			
		}
			
		if (c != null) return c;
		
		try {
			
			c = klass.getConstructor(action.getClass());
			
		} catch(NoSuchMethodException e) {
			
			// do nothing... not found...
			
		}
		
		return c;
		
	}
	
	private Class<Action> getInnerClass(String innerAction) {
		
		Class[] classes = action.getClass().getClasses();
		
		for (Class clazz : classes) {
			
			String simpleName = clazz.getSimpleName();
			
			if(Action.class.isAssignableFrom(clazz) && (simpleName.equals(innerAction) || simpleName.equalsIgnoreCase(innerAction))){
				
				return (Class<Action>) clazz;
				
			}
		}
		
		return null;
	}
	
	private Method getMethod(String innerAction) {
		
		return getMethod(action, innerAction);
	}
	
	private Method getMethod(Object action, String innerAction) {
		
		try {
			Method m = action.getClass().getMethod(innerAction, (Class[]) null);
			if (m != null) {
				return m;
			}			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Sets an inner action to be executed.
	 * An inner action is a method inside the action implementation that can be executed instead of the execute() method.
	 *
	 * @param innerAction The name of the method to be executed as an inner action
	 */
	public void setInnerAction(String innerAction) {
		this.innerAction = innerAction;
	}
    
    /**
     * Returns the inner action being executed in the invocation chain.
     *
     * @return The innner action or null if there is no inner action being executed.
     * @since 1.2.1
     */
    public String getInnerAction() {
        return innerAction;
    }

    /**
     * Returns the name of the action being executed in the invocation chain.
     * 
     * @return The action name like HelloMentawai
     * @since 1.8
     */
    public String getActionName() {
    	return actionName;
    }
    
    /**
     * Returns the filters of this invocation chain.
     * 
     * @return all filters of this invocation chain
     * @since 1.4
     */
    public List<Filter> getFilters() {
        
        return filters;
        
    }
}