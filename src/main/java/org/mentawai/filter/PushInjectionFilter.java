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
import java.text.DateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.FilterException;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.PojoAction;
import org.mentawai.util.FindMethod;

/**
 * A filter that tries to inject the input values in the action through setters. (Ex. <i>setUsername()</i>, <i>setPassword()</i>, etc.)
 * It can also inject the input value directly in the attribute, even if it is a private field.
 * Use this filter if you don't want to deal with the action input object and instaed you want to inject its values in the action.
 * This filter tries to inject all the input values in the action.
 * If enabled (default), it will also try to convert the values automatically by looking into the action field by reflection.
 *
 * @author Sergio Oliveira
 */
public class PushInjectionFilter implements Filter {
    
    private Map<Class, Map> methods = Collections.synchronizedMap(new HashMap<Class, Map>());
    private Map<Class, Map> methodsConverted = Collections.synchronizedMap(new HashMap<Class, Map>());
    private boolean tryField = true;
    private boolean convert = true;
    
    /**
     * Creates an PushInjectionFilter that can be used by any action class.
     * You may use this filter per action or as a global filter.
     */
    public PushInjectionFilter() { }
    
    /**
     * Creates an PushInjectionFilter that can be used by any action class.
     * You may use this filter per action or as a global filter.
     * If tryField is true and it cannot find a setter for the input value,
     * it will try to directly access the attribute, even if it is a private field.
     *
     * @param tryField A flag indicating whether this filter should try to access private attributes.
     */    
    public PushInjectionFilter(boolean tryField) {
        this.tryField = tryField;
    }
    
    /**
     * Creates an PushInjectionFilter that can be used by any action class.
     * You may use this filter per action or as a global filter.
     * If tryField is true and it cannot find a setter for the input value,
     * it will try to directly access the attribute, even if it is a private field.
     * If convert flag is true (default), it will try to automatically convert.
     *
     * @param tryField A flag indicating whether this filter should try to access private attributes.
     */    
    public PushInjectionFilter(boolean tryField, boolean convert) {
        this(tryField);
        this.convert = convert;
    }    
    
    /*
     * Find a field, even if it is private...
     */
    static Field getField(Object target, String name) {
        Field fields[] = target.getClass().getDeclaredFields();
        for(int i=0;i<fields.length;i++) {
            if (name.equals(fields[i].getName())) {
                return fields[i];
            }
        }
        return null;
    }
    
	/*
	 * Use reflection to inject the value in the action.
	 */
	private boolean setValueByReflection(Object target, String name, Object value, Locale loc, boolean converted) {
		try {
			StringBuffer sb = new StringBuffer(30);
			sb.append("set");
			sb.append(name.substring(0,1).toUpperCase());
			if (name.length() > 1) sb.append(name.substring(1));
            
            String methodName = sb.toString();
            
            Class actionClass = target.getClass();
            
            Map<Class, Map> methods = null;
            
            if (converted) methods = methodsConverted;
            else methods = this.methods;
            
            Map<String, AccessibleObject> map = methods.get(actionClass);
            if (map == null) {
                map = new HashMap<String, AccessibleObject>();
                methods.put(actionClass, map);
            }
            
            if (!map.containsKey(name)) {
                Method m = null;
                Field f = null;
                try {
    			    //m = actionClass.getMethod(methodName, new Class[] { value.getClass() });
                    m = FindMethod.getMethod(actionClass, methodName, new Class[] { value.getClass() });
                } catch(Exception e) {
                    //e.printStackTrace();
                    
                    // try primitive...
                    Class primitive = getPrimitiveFrom(value);
                    if (primitive != null) {
                        try {
                            m = actionClass.getMethod(methodName, new Class[] { primitive });
                        } catch(Exception ex) {
                            //ex.printStackTrace();
                        }
                    }
                    
                    if (m == null && tryField) {
                        // try field...
                        f = getField(target, name);
                        if (f != null) {
                            f.setAccessible(true);
                        }
                    }
                    
                }
                if (m != null) {
                    map.put(name, m);
                    m.setAccessible(true);
                } else {
                    map.put(name, f);
                }
            }
            
            Object obj = map.get(name);
            if (obj instanceof Method) {
                Method m = (Method) obj;
                m.invoke(target, new Object[] { value });
                return true;
            } else if (obj instanceof Field) {
                Field f = (Field) obj;
                f.set(target, value);
                return true;
            }
		} catch(Exception e) {
			//e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * You can override this method to code more automatic conversions.
	 * Right now it only converts int, double, booleans and short dates.
	 * 
	 * @param className The type to convert to
	 * @param value The value to convert
	 * @param loc The locale to use for conversion (useful for date)
	 * @return The converted value
	 */
	protected Object getConvertedValue(String className, String value, Locale loc) {
		
		Object newValue = null;
		
		if(className.equals("int") || className.equals("java.lang.Integer")){
			int x = -1;
			try {
				x = Integer.parseInt(value);								
			} catch(Exception e) {
				return null;
			}
			newValue = new Integer(x);
		}
		else if(className.equals("double") || className.equals("java.lang.Double")){
			double x = -1;
			try {
				x = Double.parseDouble(value);								
			} catch(Exception e) {
				return null;
			}
			newValue = new Double(x);
		}						 
		else if(className.equals("boolean") || className.equals("java.lang.Boolean")){
			try {
				int x = Integer.parseInt(value);
				if(x == 1){
					newValue = Boolean.TRUE;
				}else if (x == 0) {
					newValue = Boolean.FALSE;
				} else {
					return null;
				}
			} catch(Exception e) {
				if(value.equalsIgnoreCase("true") || value.equals("on")){
					newValue = Boolean.TRUE;
				}else if (value.equalsIgnoreCase("false")) {
					newValue = Boolean.FALSE;
				} else {
					return null;
				}
			}							
		}
		else if(className.equals("java.util.Date")){
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, loc); //change this.								
			df.setLenient(false);
			try {
				newValue = df.parse(value);
			} catch(Exception e) {
				return null;
			}
		}
		
		return newValue;
	}                	
    
	protected boolean setValue(Object target, String name, Object value, Locale loc) {
		
		// first try the old way
		
		boolean ok = setValueByReflection(target, name, value, loc, false);
		
		if (convert && !ok && value != null && value instanceof String) {
			
			// try to convert based on the field
			
			Field f = PushInjectionFilter.getField(target, name);
			
            if (f != null) {
            	
                f.setAccessible(true);
                
                Object converted = getConvertedValue(f.getType().getName(), (String) value, loc);
                
                if (converted != null) {
                	
                	ok = setValueByReflection(target, name, converted, loc, true);
                }
                
            }
		}
		
		return ok;		

	}	
    
    static Class getPrimitiveFrom(Object w) { 
        if (w instanceof Boolean) { return Boolean.TYPE; } 
        else if (w instanceof Byte) { return Byte.TYPE; } 
        else if (w instanceof Short) { return Short.TYPE; } 
        else if (w instanceof Character) { return Character.TYPE; } 
        else if (w instanceof Integer) { return Integer.TYPE; } 
        else if (w instanceof Long) { return Long.TYPE; } 
        else if (w instanceof Float) { return Float.TYPE; } 
        else if (w instanceof Double) { return Double.TYPE; } 
        return null;
    }
	
	public String filter(InvocationChain chain) throws Exception {
		Action action = chain.getAction();
        
        Object model = null;
        Object pojo = null;
        
        // model-driven...
        if (action instanceof ModelDriven) {
            ModelDriven md = (ModelDriven) action;
            model = md.getModel();
            if (model == null) throw new FilterException("ModelDriven action cannot return a null model!");
            
        } else if (action instanceof PojoAction) {
        	PojoAction pa = (PojoAction) action;
        	pojo = pa.getPojo();
        }
        
		Input input = action.getInput();
        Iterator<String> iter = input.keys();
        while(iter.hasNext()) {
            String name = (String) iter.next();
            Object value = input.getValue(name);
            if (value == null) continue;
            if (model != null) {
                setValue(model, name, value, action.getLocale());
            } else if (pojo != null) {
            	setValue(pojo, name, value, action.getLocale());
            } else {
                setValue(action, name, value, action.getLocale());
            }
        }
		return chain.invoke();
	}
    
    public void destroy() { }
}
		