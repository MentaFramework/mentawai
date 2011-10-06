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
package org.mentawai.ioc;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.mentawai.util.FindMethod;

/**
 * The default implementation of a Mentawai IoC component.
 * This component allows you to define constructor values and object properties.
 *
 * @author Sergio Oliveira
 */
public class DefaultComponent implements Bean {
    
    private Class<? extends Object> klass;
    private Map<String, Object> props = null;
    private List<Object> initValues = null;
    private Constructor<? extends Object> constructor = null;
    private Map<String, AccessibleObject> cache = null;

    /**
     * Creates a new DefaultComponent for the given class.
     *
     * @param klass The class used to create new instances.
     */
    public DefaultComponent(Class<? extends Object> klass) {
        this.klass = klass;
    }
    
    /**
     * Creates a new DefaultComponent for the given class with the given init (constructor) values.
     * A constructor with the given init values will be called to instantiate the class.
     *
     * @param klass The class used to create new instances.
     * @param initValues The values for the constructor.
     */
    public DefaultComponent(Class<? extends Object> klass, List<Object> initValues) {
        this(klass);
        this.initValues = initValues;
    }
    
    /**
     * Creates a new DefaultComponent for the given class with the given property map.
     * The properties will be injected in the instance with reflection.
     *
     * @param klass The class used to create new instances.
     * @param props The properties that need to be injected in each instance.
     */
    public DefaultComponent(Class<? extends Object> klass, Map<String, Object> props) {
        this(klass);
        this.props = props;
    }

    /**
     * Creates a new DefaultComponent for the given class with the given property map and the given init (constructor) values.
     * The properties will be injected in the instance with reflection.
     * A constructor with the given init values will be called to instantiate the class.
     *
     * @param klass The class used to create new instances.
     * @param initValues The values for the constructor.
     * @param props The properties that need to be injected in each instance.
     */   
    public DefaultComponent(Class<? extends Object> klass, List<Object> initValues, Map<String, Object> props) {
        this(klass);
        this.initValues = initValues;
        this.props = props;
    }
    
	public Class<? extends Object> getType() {
		
		return klass;
	}
    
    /**
     * Adds a property to be set in each new instance of this component's class.
     *
     * @param name The name of the property or attribute.
     * @param value The value of the property or attribute.
     * @return This object
     */
    public DefaultComponent addProperty(String name, Object value) {
        if (props == null) {
            props = new HashMap<String, Object>();
            cache = new HashMap<String, AccessibleObject>();
        }
        props.put(name, value);
        
        return this;
    }
    
    /**
     * Adds the properties from the given map in this component.
     *
     * @param map The properties to add in this component.
     * @return This object
     */
    public DefaultComponent addProperties(Map map) {
        Iterator iter = map.keySet().iterator();
        while(iter.hasNext()) {
            String name = (String) iter.next();
            Object value = map.get(name);
            addProperty(name, value);
        }
        
        return this;
    }
    
    /**
     * Adds a new init value for this component.
     * The init values are used to select a constructor for the component's class.
     *
     * @param value The init value to add to this component.
     * @return This object
     */
    public DefaultComponent addInitValue(Object value) {
        if (initValues == null) {
            initValues = new LinkedList<Object>();
        }
        initValues.add(value);
        
        return this;
    }
    
    /**
     * Adds the init values from the given list to this component's class.
     * The init values are used to select a constructor for the component's class.
     *
     * @param values The init values to add to this component.
     * @return This object
     */
    public DefaultComponent addInitValues(List values) {
        Iterator iter = values.iterator();
        while(iter.hasNext()) {
            addInitValue(iter.next());
        }
        
        return this;
    }
    
    private Class [] getClasses(List<Object> values) {
        Class [] types = new Class[values.size()];
        Iterator<Object> iter = values.iterator();
        int index = 0;
        while(iter.hasNext()) {
            Object value = iter.next();
            if (value != null) {
                types[index++] = value.getClass();
            } else {
                types[index++] = null;
            }
        }
        return types;
    }
    
    private Class [] getPrimitiveClasses(List<Object> values) {
        Class [] types = new Class[values.size()];
        Iterator<Object> iter = values.iterator();
        int index = 0;
        while(iter.hasNext()) {
            Object value = iter.next();
            if (value != null) {
            	
            	Class p = getPrimitiveFrom(value);
            	
            	if (p != null) {
            		
            		types[index++] = p;
            		
            	} else {
            	
            		types[index++] = value.getClass();
            	}
            	
            } else {
                types[index++] = null;
            }
        }
        return types;
    }
    
    private Object [] getValues(List<Object> values) throws InstantiationException {
        Object [] array = new Object[values.size()];
        int index = 0;
        Iterator<Object> iter = values.iterator();
        while(iter.hasNext()) {
            Object obj = iter.next();
            array[index++] = getValue(obj);
        }
        return array;
    }
    
	/*
	 * Use reflection to set a property in the bean
	 */
	private void setValue(Object bean, String name, Object value) throws InstantiationException {
        
		try {
			StringBuffer sb = new StringBuffer(30);
			sb.append("set");
			sb.append(name.substring(0,1).toUpperCase());
			if (name.length() > 1) sb.append(name.substring(1));
            
            String methodName = sb.toString();
            
            if (!cache.containsKey(name)) {
                Method m = null;
                Field f = null;
                try {
    			    //m = klass.getMethod(methodName, new Class[] { value.getClass() });
                    m = FindMethod.getMethod(klass, methodName, new Class[] { value.getClass() });
                } catch(Exception e) {
                    //e.printStackTrace();
                    
                    // try primitive...
                    Class primitive = getPrimitiveFrom(value);
                    if (primitive != null) {
                        try {
                            m = klass.getMethod(methodName, new Class[] { primitive });
                        } catch(Exception ex) {
                            //ex.printStackTrace();
                        }
                    }
                    
                    if (m == null) {
                        // try field...
                        f = getField(bean, name);
                        if (f != null) {
                            f.setAccessible(true);
                        }
                    }
                    
                    if (m == null && f == null) {
                        throw new InstantiationException("Cannot find method or field for property: " + name);
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
            } else if (obj instanceof Field) {
                Field f = (Field) obj;
                f.set(bean, value);
            }                
		} catch(Exception e) {
			throw new InstantiationException("Error trying to set a property with reflection: " + name);
		}
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
    
    private Object getValue(Object obj) throws InstantiationException {
        if (obj instanceof Bean) {
            Bean c = (Bean) obj;
            return c.getBean();
        }
        return obj;
    }
    
    public Object getBean() throws InstantiationException {
        Object obj = null;
        if (initValues != null && initValues.size() > 0) {
            if (constructor == null) {
                try {
                    constructor = klass.getConstructor(getClasses(initValues));
                } catch(Exception e) {
                	
                	try {
                		
                		constructor = klass.getConstructor(getPrimitiveClasses(initValues));
                		
                	} catch(Exception ee) {
                	
                		throw new InstantiationException("Cannot find a constructor for class: " + klass);
                	}
                }
            }
            try {
                obj = constructor.newInstance(getValues(initValues));
            } catch(Exception e) {
                throw new InstantiationException("Cannot create instance from constructor: " + constructor);
            }
        } else {
            try {
                obj = klass.newInstance();
            } catch(Exception e) {
                throw new InstantiationException("Cannot create instance from class: " + klass);
            }
        }
        if (props != null && props.size() > 0) {
            Iterator<String> iter = props.keySet().iterator();
            while(iter.hasNext()) {
                String name = iter.next();
                Object value = props.get(name);
                value = getValue(value);
                setValue(obj, name, value);
            }
        }
        return obj;
    }
}
    