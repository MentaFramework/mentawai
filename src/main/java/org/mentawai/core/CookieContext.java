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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A cookie management abstraction into a Mentawai context.
 * 
 * Note that this class is smart enough to keep track of which cookies were added, which ones were removed
 * and which ones did not suffer any modification and should not be sent again to the browser.
 * 
 * @author Sergio Oliveira
 * @since 1.2
 */
public class CookieContext implements Context, Map<String, Object> {
    
    private HttpServletRequest req;
    private HttpServletResponse res;
    
    /**
     * Creates a new CookieContext for this request and response.
     * 
     * @param req The request from where to get the cookies.
     * @param res The response where to put the new cookies.
     */
    public CookieContext(HttpServletRequest req, HttpServletResponse res) {
        this.req = req;
        this.res = res;
    }
    
    /**
     * Return the cookie value with the given name, if present.
     * 
     * @param name The name of the cookie to return
     * @return The cookie value as a String. 
     */
    public Object getAttribute(String name) {
        
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            
            for (int i = 0; i < cookies.length; i++) {
                
                if (cookies[i].getName().equals(name)) {
                    
                    return cookies[i].getValue();
                }
            }
        }        
        
        return null;
    }
    
    public Iterator<String> keys() {
    	
    	List<String> list = new ArrayList<String>();
    	
    	Cookie[] cookies = req.getCookies();
    	
    	if (cookies != null) {
    		
            for (int i = 0; i < cookies.length; i++) {
            	
            	list.add(cookies[i].getName());
                
            }
    		
    	}
    	
    	return list.iterator();
    	
    }
    
    /**
     * Sets a cookie to send to the client in the response. 
     * 
     * Note that this method can take a Cookie as the value as well as a String.
     * 
     * If you pass a String, a Cookie object is created with default values for path and max age.
     * 
     * If you pass a Object other than a Cookie or a String, its toString() method its called and
     * taken as the cookie value.
     * 
     * @param name The name of this cookie
     * @param value The cookie object or the value of the cookie as a String
     */
    public void setAttribute(String name, Object value) {
        
        if (value instanceof Cookie) {
        	
        	Cookie c = (Cookie) value;
        	
        	if (!c.getName().equals(name)) {
        		throw new IllegalStateException("Cookie name does not match the key in the context: " + name + " != " + c.getName());
        	}
            
            res.addCookie(c);
            
        } else {
        
            Cookie c = new Cookie(name, value.toString());
            
            c.setMaxAge(31104000);
            
            c.setPath("/");
            
            res.addCookie(c);
        }
    }
    
    /**
     * Tell the browser to remove the given cookie.
     * 
     * @param name The name of the cookie to remove.
     */
    public void removeAttribute(String name) {
        
        Cookie c = new Cookie(name, "");
        c.setMaxAge(0);
        
        res.addCookie(c);
    }
    
    public void reset() {
        
        throw new UnsupportedOperationException("reset() is not supported by CookieContext !");
    }
    
    public boolean hasAttribute(String name) {
        
        return getAttribute(name) != null;
        
    }
    
    //// Map methods...
    
    public void clear() {
    	
    	throw new UnsupportedOperationException();
    }
    
    public boolean containsKey(Object key) {
    	
    	return getAttribute(key.toString()) != null;
    }
    
    public boolean containsValue(Object value) {
    	
    	if (!(value instanceof String)) return false;
    	
    	String v = value.toString();
    	
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            
            for (int i = 0; i < cookies.length; i++) {
            	
            	String cookie = cookies[i].getValue();
            	
            	if (cookie != null && cookie.equals(v)) {
            		
            		return true;
            	}
            }
        }
        
        return false;
    }
    
    public Set<Map.Entry<String,Object>> entrySet() {
    	
    	throw new UnsupportedOperationException();
    }
    
    public Object get(Object key) {
    	
    	if (key == null) return null;
    	
    	Object value = getAttribute(key.toString());
    	
    	if (value == null) return null;
    	
    	return value.toString();
    }
    
    public boolean isEmpty() {
    	
    	Cookie[] cookies = req.getCookies();
    	
    	return cookies == null || cookies.length == 0;
    }
    
    public Set<String> keySet() {
    	
    	Set<String> keys = new HashSet<String>();
    	
    	Iterator<String> iter = keys();
    	
    	while(iter.hasNext()) {
    		
    		keys.add(iter.next());
    	}
    	
    	return keys;
    }
    
    public String put(String key, Object value) {
    	
    	setAttribute(key, value);
    	
    	return null;
    }
    
    public void putAll(Map<? extends String,? extends Object> t) {
    	
    	throw new UnsupportedOperationException();
    }
    
    public String remove(Object key) {
    	
    	removeAttribute(key.toString());
    	
    	return null;
    }
    
    public int size() {
    	
    	Cookie[] cookies = req.getCookies();
    	
    	if (cookies == null) return 0;
    	
    	return cookies.length;
    }
    
    public Collection<Object> values() {
    	
    	Cookie[] cookies = req.getCookies();
    	
    	if (cookies == null || cookies.length == 0) {
    		
    		return new ArrayList<Object>(0);
    	}
    	
    	List<Object> list = new ArrayList<Object>(cookies.length);
    	
    	for(int i=0;i<cookies.length;i++) {
    		
    		list.add(cookies[i].getValue());
    	}
    	
    	return list;
    }
    
}