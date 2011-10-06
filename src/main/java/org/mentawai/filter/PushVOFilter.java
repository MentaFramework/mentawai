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
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;
import org.mentawai.util.FindMethod;

/**
 * A filter that tries to populate a bean with the action input values.<br>
 * 
 * Use this filter if you want to provide your action with a ready-to-use bean instead of 
 * a bunch of action input values.<br>
 * 
 * This filter tries to inject all the action input values in a Java object through setters.<br>
 * 
 * It can also inject directly in the bean's attributes, even if the field is private.<br>
 * 
 * And it can also use prefix to separate attributes from multiple objects.<br>
 * 
 * If enabled, it will try to automatically convert the fields beforing injecting
 * based on the field type, if it can find one.
 * 
 * Right now it only tries to convert booleans, ints, doubles and short dates, but you can extend this if you need to.
 *
 * @author Sergio Oliveira
 */
public class PushVOFilter implements Filter {
	
	private Class<? extends Object> klass;
    private String key = null;
    private Map<String, AccessibleObject> cache = Collections.synchronizedMap(new HashMap<String, AccessibleObject>());
    private Map<String, AccessibleObject> cacheConverted = Collections.synchronizedMap(new HashMap<String, AccessibleObject>());
    private boolean tryField = true;
    private String prefix = null;
    private boolean convert = true;
	
    /**
     * Creates a PushVOFilter that will try to create an object with the given class
     * and populate this object with the action input values.
     *
     * @param klass The class to use to create the object.
     */
	public PushVOFilter(Class<? extends Object> klass) {
		this.klass = klass;
	}
    
    /**
     * Creates a PushVOFilter that will try to create an object with the given class
     * and populate this object with the action input values.
     * If tryField is true and it cannot find a setter for the input value,
     * it will try to directly access the attribute, even if it is a private field.
     *
     * @param klass The class to use to create the object.
     * @param tryField A flag indicating whether this filter should try to access private attributes.
     */    
     public PushVOFilter(Class klass, boolean tryField) {
		this(klass);
        this.tryField = tryField;
	}
    
    
    /**
     * Creates a PushVOFilter that will try to create an object with the given class
     * and populate this object with the action input values.
     * The object will be placed in the action input with the given key.
     *
     * @param klass The class to use to create the object.
     * @param key The key name used to place the object in the action input.
     */
    public PushVOFilter(Class klass, String key) {
        this(klass);
        this.key = key;
    }
    
    /**
     * Creates a PushVOFilter that will try to create an object with the given class
     * and populate this object with the action input values.
     * The object will be placed in the action input with the given key.
     * If tryField is true and it cannot find a setter for the input value,
     * it will try to directly access the attribute, even if it is a private field.
     *
     * @param klass The class to use to create the object.
     * @param key The key name used to place the object in the action input.
     * @param tryField A flag indicating whether this filter should try to access private attributes.     
     */
    public PushVOFilter(Class klass, String key, boolean tryField) {
        this(klass, key);
        this.tryField = tryField;
    }
    
    /**
     * Creates a PushVOFilter that will try to create an object with the given class
     * and populate this object with the action input values.
     * The object will be placed in the action input with the given key.
     * If tryField is true and it cannot find a setter for the input value,
     * it will try to directly access the attribute, even if it is a private field.
     * If prefix is not null, it will look for prefix.attribute and remove the prefix before injecting.
     *
     * @param klass The class to use to create the object.
     * @param key The key name used to place the object in the action input.
     * @param tryField A flag indicating whether this filter should try to access private attributes.
     * @param prefix The prefix that will come on every attribute. (Ex. user.name)     
     */
    public PushVOFilter(Class klass, String key, boolean tryField, String prefix) {
        this(klass, key, tryField);
        this.prefix = prefix;
    }
    
    /**
     * Creates a PushVOFilter that will try to create an object with the given class
     * and populate this object with the action input values.
     * The object will be placed in the action input with the given key.
     * If tryField is true and it cannot find a setter for the input value,
     * it will try to directly access the attribute, even if it is a private field.
     * If prefix is not null, it will look for prefix.attribute and remove the prefix before injecting.
     * If convert is true, it will try to automatically convert to the object field class.
     *
     * @param klass The class to use to create the object.
     * @param key The key name used to place the object in the action input.
     * @param tryField A flag indicating whether this filter should try to access private attributes.
     * @param convert A flag to indicate whether we should try to convert or not.
     * @param prefix The prefix that will come on every attribute. (Ex. user.name)     
     */
    public PushVOFilter(Class klass, String key, boolean tryField, boolean convert, String prefix) {
        this(klass, key, tryField);
        this.prefix = prefix;
        this.convert = convert;
    }           
    
    public String toString() {
        StringBuffer sb = new StringBuffer(128);
        sb.append("PushVOFilter: Class=").append(klass.getName()).append(" Key=").append(key != null ? key : klass.getName());
        sb.append(" TryField=").append(tryField);
        return sb.toString();
    }
	
	private Object createObject() {
		try {
			return klass.newInstance();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * Use reflection to set a property in the bean
	 */
	private boolean setValueByReflection(Object bean, String name, Object value, Locale loc, boolean converted) {
        
		try {
			StringBuffer sb = new StringBuffer(30);
			sb.append("set");
			sb.append(name.substring(0,1).toUpperCase());
			if (name.length() > 1) sb.append(name.substring(1));
            
            String methodName = sb.toString();
            
            Map<String, AccessibleObject> cache = null;
            
            if (converted) cache = cacheConverted;
            else cache = this.cache;
            
            if (!cache.containsKey(name)) {
                Method m = null;
                Field f = null;
                try {
    			    //m = klass.getMethod(methodName, new Class[] { value.getClass() });
                    m = FindMethod.getMethod(klass, methodName, new Class[] { value.getClass() });
                } catch(Exception e) {
                    //e.printStackTrace();
                    
                    // try primitive...
                    Class primitive = PushInjectionFilter.getPrimitiveFrom(value);
                    if (primitive != null) {
                        try {
                            m = klass.getMethod(methodName, new Class[] { primitive });
                        } catch(Exception ex) {
                            //ex.printStackTrace();
                        }
                    }
                    
                    if (m == null && tryField) {
                        // try field...
                        f = PushInjectionFilter.getField(bean, name);
                        if (f != null) {
                            f.setAccessible(true);
                        }
                    }
                }
                if (m != null) {
                    cache.put(name, m);
                    m.setAccessible(true);
                } else {
                    cache.put(name, f);
                }
            }    

            Object obj = cache.get(name);
            if (obj instanceof Method) {
                Method m = (Method) obj;
                m.invoke(bean, new Object[] { value });
                return true;
            } else if (obj instanceof Field) {
                Field f = (Field) obj;
                f.set(bean, value);
                return true;
            }                
		} catch(Exception e) {
			//e.printStackTrace();
		}
		
		return false;
	}
	
	public String filter(InvocationChain chain) throws Exception {
		Action action = chain.getAction();
		Input input = action.getInput();
		Object bean = createObject();
		if (bean != null) {
			Iterator<String> iter = input.keys();
			
			while(iter.hasNext()) {
				
				String name = (String) iter.next();
				
				String beanName = name;
				
				if (prefix != null) {
					
					if (name.startsWith(prefix)) {
					
						String[] s = name.split("\\.");
						
						if (s.length == 2 && s[0].equals(prefix)) {
							
							beanName = s[1];
							
						} else {
							
							continue;
						}
					
					} else {
						
						continue;

					}
				}
				
				Object value = input.getValue(name);
                if (value == null) continue;
				setValue(bean, beanName, value, action.getLocale());
			}
			// set the bean in the action input
            if (key != null) {
                input.setValue(key, bean);    
            } else {
			    input.setValue(klass.getName(), bean);
            }
		}
		return chain.invoke();
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
    
    
	/*
	 * Use reflection to set a property in the bean
	 */
	protected boolean setValue(Object bean, String name, Object value, Locale loc) {
		
		// first try the old way
		
		boolean ok = setValueByReflection(bean, name, value, loc, false);
		
		if (convert && !ok && value != null && value instanceof String) {
			
			// try to convert based on the field
			
			Field f = PushInjectionFilter.getField(bean, name);
			
            if (f != null) {
            	
                f.setAccessible(true);
                
                Object converted = getConvertedValue(f.getType().getName(), (String) value, loc);
                
                if (converted != null) {
                	
                	ok = setValueByReflection(bean, name, converted, loc, true);
                }
                
            }
		}
		
		return ok;
	}	
    
    public void destroy() { }
}
		