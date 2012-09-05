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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

import org.mentawai.core.Action;
import org.mentawai.core.ApplicationManager;
import org.mentawai.core.Filter;
import org.mentawai.core.InputWrapper;
import org.mentawai.core.InvocationChain;
import org.mentawai.ioc.Dependency;
import org.mentawai.util.InjectionUtils;

import static org.mentalog.Log.*;

/**
 * A filter that will do AUTO-WIRING of dependencies in a totaly transparent way.
 * 
 * @author Sergio Oliveira
 */
public class AutoWiringFilter extends InputWrapper implements Filter {
	
	private static final String NAME = "AutoWiringFilter";

    private Map<String, AccessibleObject> cache = new HashMap<String, AccessibleObject>();

    private Map<Object, Object> received = new WeakHashMap<Object, Object>();

    private boolean tryField = true;

    private final String sourceKey;

	private final String attrName;

    private final Class<? extends Object> sourceClass;
    
    private boolean newVersion = false;

    public AutoWiringFilter(boolean tryField) {
    	
    	this.newVersion = true;
    	
    	this.sourceKey = null;
    	
    	this.attrName = null;
    	
    	this.sourceClass = null;
    	
    	this.tryField = tryField;
    	
    }
    
    public AutoWiringFilter() {
    	
    	this(true);
    }

    public String toString() {

        StringBuffer sb = new StringBuffer(128);
        
        if (newVersion) {
        	
        	sb.append("AutoWiringFilter: tryField=").append(tryField);
        	
        } else {

        	sb.append("AutoWiringFilter (deprecated): sourceKey=").append(sourceKey).append(" attrName=").append(attrName).append(" sourceClass=").append(sourceClass.getName());
        	sb.append(" tryField=").append(tryField);
        	
        }

        return sb.toString();
    }

    public void setTryField(boolean tryField) {
        this.tryField = tryField;
    }

	public String filter(InvocationChain chain) throws Exception {

		Action a = chain.getAction();

		setInput(a.getInput());

		a.setInput(this);

		return chain.invoke();

	}

    /*
     * Check if the target class has the dependency. Cache for performance!
     */
    private Object getMethodOrField(Class<? extends Object> target) {

        String className = target.getName();

        // first check cache...

        Object obj = null;

        synchronized(cache) {

            obj = cache.get(className);

        }

        if (obj != null) return obj;

        Method m = InjectionUtils.findMethodToInject(target, attrName, sourceClass);
        
        Debug.log(NAME, "Finding method:", "target=", target.getName(), "attrName=", attrName, "sourceClass=", sourceClass.getName(), "methodFound=", m == null ? "NULL" : m.getName());

        if (m != null) {

            synchronized(cache) {

                cache.put(className, m);
            }

            return m;

        }
        
        if (tryField) {

            Field f = InjectionUtils.findFieldToInject(target, attrName, sourceClass);
            
            Debug.log(NAME, "Finding field:", "target=", target.getName(), "attrName=", attrName, "sourceClass=", sourceClass.getName(), "methodFound=", f == null ? "NULL" : f.getName());
    
            if (f != null) {
    
                synchronized(cache) {
    
                    cache.put(className, f);
    
                }
    
                return f;
            }
        }

        synchronized(cache) {

            cache.put(className, null);

        }

        return null;

    }

    /*
     * Check if this object has already received the dependency, because we don't want to
     * inject every time. Injecting just once is the correct behaviour.
     */
    private boolean hasAlreadyReceived(Object target) {

        synchronized(received) {

            return received.containsKey(target);

        }
    }

    /*
     * Flag that this object has already received the dependency.
     */
    private void setAlreadyReceived(Object target) {

        synchronized(received) {

            received.put(target, null);

        }
    }


	public void setValue(String key, Object value) {

	    super.setValue(key, value);

	    //"toutch" the object to inject the dependencies
	    getValue(key);

	}
	
	public Object getValue(String key) {
		
		if (!newVersion) return getValueOld(key);
		
        Object target = super.getValue(key);

        if (target != null) {
        	
    		Iterator<Dependency> iter = ApplicationManager.getInstance().getDependencies();
    		
    		while(iter.hasNext()) {
    			
    			Dependency d = iter.next();

	            // has dependency ?
	            Object obj = d.getMethodOrField(target.getClass(), tryField);
	            
	            Debug.log(NAME, "Has dependency?", "target =", target.getClass().getName(), "key =", key, "depFound =", obj); 
	
	            if (obj != null) {
	            	
	            	boolean hasAlreadyReceived = d.hasAlreadyReceived(target);
	            	
	            	Debug.log(NAME, "HasAlreadyReceived?", "target =", target, "hasAlreadyReceived =", hasAlreadyReceived);
	            	
	                // has already received the dependency?
	                if (!hasAlreadyReceived) {
	                	
	                	String sourceKey = d.getSource();
	                	
                		// get dependency from action input
	                	
                		if (sourceKey.equals(key)) {
                			
                			// cannot depend on itself...
                			// also avoid recusive StackOverflow...
                			
                			continue;
                			
                		}
                		
                		Object source = getValue(sourceKey);
                		
                		boolean isAssignable = source != null && d.getDependencyClass().isAssignableFrom(source.getClass());
	                		
	                	
	                    Debug.log(NAME, "isAssignable?", "source =", source == null ? "NULL" : source.getClass().getName(), "isAssignable =", isAssignable, "sourceKey =", sourceKey == null ? "NULL" : sourceKey);
	
	                    // check if we can find the dependency and if it is assignable to the target dependency
	                    if (isAssignable) {
	
	                        if (obj instanceof Method) {
	
	                            Method m = (Method) obj;
	
	                            try {
	                            	
	                            	Debug.log(NAME, "Injecting thru method!", "method =", m.getName(), "target =", target, "source =", source);
	
	                                // inject
	                                m.invoke(target, source);
	
	                                // flag it has received
	                                d.setAlreadyReceived(target);
	                                
	                                Debug.log(NAME, "Injecting thru method succeeded!");
	
	                            } catch(Exception e) {
	                            	
	                            	Debug.log(NAME, "Injecting thru method failed!", "exception =", e.getMessage());
	                            	
	                                throw new org.mentawai.util.RuntimeException("AutoWiringFilter cannot inject dependency: method = " + (m != null ? m.getName() : "NULL") + " / source = " + (source != null ? source : "NULL") + " / target = " + target, e, true);
	                            	
	                            }
	
	                        } else if (obj instanceof Field) {
	
	                            Field f = (Field) obj;
	
	                            try {
	                            	
	                            	Debug.log(NAME, "Injecting thru field!", "field =", f.getName(), "target =", target, "source =", source);
	
	                                // inject in the field !!!!
	                                f.set(target, source);
	
	                                // flag it has received
	                                d.setAlreadyReceived(target);
	                                
	                                Debug.log(NAME, "Injecting thru field succeeded!");
	
	                            } catch(Exception e) {
	                            	
	                            	Debug.log(NAME, "Injecting thru field failed!", "exception =", e.getMessage());
	                            	
	                            	throw new org.mentawai.util.RuntimeException("AutoWiringFilter cannot inject dependency: field = " + (f != null ? f.getName() : "NULL") + " / source = " + (source != null ? source : "NULL") + " / target = " + target, e, true);
	
	                            }
	                        }
	                    }
	                }
	            }
	            
	            Debug.log(""); // jump line...
    		}
        }

        return target; // return target nicely with all the dependencies injected
	}


	public Object getValueOld(String key) {

        Object target = super.getValue(key);

        if (target != null) {

            // has dependency ?
            Object obj = getMethodOrField(target.getClass());
            
            Debug.log(NAME, "Has dependency?", "target =", target.getClass().getName(), "key =", key, "depFound =", obj); 

            if (obj != null) {
            	
            	boolean hasAlreadyReceived = hasAlreadyReceived(target);
            	
            	Debug.log(NAME, "HasAlreadyReceived?", "target =", target.getClass().getName(), "hasAlreadyReceived =", hasAlreadyReceived);

                // has already received the dependency?
                if (!hasAlreadyReceived) {

                    // get dependency from action input
                    Object source = super.getValue(sourceKey);
                    
                    boolean isAssignable = source != null && sourceClass.isAssignableFrom(source.getClass());
                    
                    Debug.log(NAME, "isAssignable?", "source =", source == null ? "NULL" : source.getClass().getName(), "isAssignable =", isAssignable, "sourceKey =", sourceKey);

                    // check if we can find the dependency and if it is assignable to the target dependency
                    if (isAssignable) {

                        if (obj instanceof Method) {

                            Method m = (Method) obj;

                            try {
                            	
                            	Debug.log(NAME, "Injecting thru method!", "method =", m.getName(), "target =", target.getClass().getName(), "source =", source);

                                // inject
                                m.invoke(target, new Object[] { source });

                                // flag it has received
                                setAlreadyReceived(target);
                                
                                Debug.log(NAME, "Injecting thru method succeeded!");

                            } catch(Exception e) {
                            	
                            	Debug.log(NAME, "Injecting thru method failed!", "exception =", e.getMessage());
                            	
                                throw new org.mentawai.util.RuntimeException("AutoWiringFilter cannot inject dependency: method = " + (m != null ? m.getName() : "NULL") + " / source = " + (source != null ? source : "NULL") + " / target = " + target, e, true);
                            	
                            }

                        } else if (obj instanceof Field) {

                            Field f = (Field) obj;

                            try {
                            	
                            	Debug.log(NAME, "Injecting thru field!", "field =", f.getName(), "target =", target.getClass().getName(), "source =", source);

                                // inject in the field !!!!
                                f.set(target, source);

                                // flag it has received
                                setAlreadyReceived(target);
                                
                                Debug.log(NAME, "Injecting thru field succeeded!");

                            } catch(Exception e) {
                            	
                            	Debug.log(NAME, "Injecting thru field failed!", "exception =", e.getMessage());
                            	
                            	throw new org.mentawai.util.RuntimeException("AutoWiringFilter cannot inject dependency: field = " + (f != null ? f.getName() : "NULL") + " / source = " + (source != null ? source : "NULL") + " / target = " + target, e, true);

                            }
                        }
                    }
                }
            }
        }

        return target; // return target nicely with the dependency injected
	}
	
	public void destroy() { }

}