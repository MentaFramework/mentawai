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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.mentawai.util.InjectionUtils;

import static org.mentalog.Log.*;

public class Dependency {
	
	private Map<Object, Object> received = new WeakHashMap<Object, Object>();
	
	private static final String NAME = "Dependency";
	
	private final String target;
	
	private String source;
	
	private final Class<? extends Object> klass;
	
	private Map<String, AccessibleObject> cache = new HashMap<String, AccessibleObject>();
	
	public Dependency(Class<? extends Object> klass, String target, String source) {
		
		this.klass = klass;
		
		this.target = target;
		
		this.source = source;
	}
	
	public Dependency(Class<? extends Object> klass, String target) {
		
		this(klass, target, target);
	}
	
	public Dependency source(String source) {
		
		this.source = source;
		
		return this;
	}
	
	public String getTarget() {
		
		return target;
	}
	
	public String getSource() {
		
		return source;
	}
	
	public Class<? extends Object> getDependencyClass() {
		
		return klass;
	}
	
	public int hashCode() {
		
		return klass.hashCode() * 62 + target.hashCode() * 31 + source.hashCode();
	}
	
	public boolean equals(Object obj) {
		
		if (!(obj instanceof Dependency)) return false;
		
		Dependency d = (Dependency) obj;
		
		if (!d.klass.equals(this.klass)) return false;
		
		if (!d.target.equals(this.target)) return false;
		
		if (!d.source.equals(this.source)) return false;
		
		return true;
	}
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder(128);
		
		sb.append("Dependency: class=").append(klass).append(" target=").append(target);
		
		sb.append(" source=").append(source);
		
		return sb.toString();
	}
	
    public Object getMethodOrField(Class<? extends Object> targetClass, boolean tryField) {

        String className = targetClass.getName();

        // first check cache...
        
        Object obj = null;
        
        synchronized(cache) {

        	obj = cache.get(className);
        	
        }

        if (obj != null) return obj;

        Method m = InjectionUtils.findMethodToInject(targetClass, target, klass);
        
        Debug.log(NAME, "Finding method:", "target=", targetClass.getName(), "attrName=", target, "sourceClass=", klass.getName(), "methodFound=", m == null ? "NULL" : m.getName());

        if (m != null) {
        	
        	synchronized(cache) {

        		cache.put(className, m);
        		
        	}
            
            return m;
        }
        
        if (tryField) {

            Field f = InjectionUtils.findFieldToInject(targetClass, target, klass);
            
            Debug.log(NAME, "Finding field:", "target=", targetClass.getName(), "attrName=", target, "sourceClass=", klass.getName(), "fieldFound=", f == null ? "NULL" : f.getName());
    
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
    public boolean hasAlreadyReceived(Object target) {

        synchronized(received) {

            return received.containsKey(target);

        }
    }
    
    /*
     * Flag that this object has already received the dependency.
     */
    public void setAlreadyReceived(Object target) {

        synchronized(received) {

            received.put(target, null);

        }
    }

}
