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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mentawai.util.EnumerationToIterator;

/**
 * Encapsulates a HttpSession as a context for Mentawai actions.
 *
 * @author Sergio Oliveira
 */
public class SessionContext implements Context, Map<String, Object> {

	private HttpSession session;
	private HttpServletRequest req;
	private HttpServletResponse res;

	public SessionContext(HttpSession session) {
		this.session = session;
	}
	
	/**
	 * Calls getSession(true) to get the session from this request and creates a context for that session.
	 *
	 * @param req The request from where to get the session.
	 */
	public SessionContext(HttpServletRequest req, HttpServletResponse res) {
		this.req = req;
		this.res = res;
		this.session = req.getSession(true);
	}

	public Object getAttribute(String name) {
		return session.getAttribute(name);
	}

	public void setAttribute(String name, Object value) {
		session.setAttribute(name, value);
	}

	public void removeAttribute(String name) {
		session.removeAttribute(name);
	}

	public void reset() {
		if(req == null) throw new IllegalStateException("Can not be reseted, the request is not valid ! It's only a SessionWrapper at this time.");
			
		session.invalidate();
		session = req.getSession(true);
	}
	
	/**
	 * Get the session ID, internaly is call {@link HttpSession#getId()}
	 * @return this session ID
	 */
	public String getId(){
		return session.getId();
	}
	
	
	/**
	 * Returns the {@link HttpServletRequest} associated with this context.
	 *
	 * @return The {@link HttpServletRequest} associated with this context.
	 */
	public HttpServletRequest getRequest() {
		return req;
	}

	/**
	 * Returns the {@link HttpServletResponse} associated with this context.
	 *
	 * @return The {@link HttpServletResponse} associated with this context.
	 */
	public HttpServletResponse getResponse() {
		return res;
	}

	/**
	 * Returns the HttpSession associated with this context.
	 *
	 * @return The session associated with this context.
	 */
	public HttpSession getSession() {
		return session;
	}

	public Iterator<String> keys() {

		return EnumerationToIterator.get(session.getAttributeNames());

	}

	public boolean hasAttribute(String name) {
		return session.getAttribute(name) != null;
	}

    public void clear() {
    	
    	Iterator<String> iter = keys();
    	
    	while(iter.hasNext()) {
    		
    		removeAttribute(iter.next());
    	}
    }

    /**
     * The implementation is not efficient as it loops through all
     * session values to calculate the size.
     */
    public boolean isEmpty() {
        int size = size();
        return size == 0;
    }

    public boolean containsKey(Object key) {
        if (key instanceof String) {
            return hasAttribute((String) key);
        }
        throw new IllegalArgumentException();
    }

    public boolean containsValue(Object value) {
        
    	Iterator<String> iter = keys();
    	
    	while(iter.hasNext()) {
    		
    		String key = iter.next();
    		
    		Object v = getAttribute(key);
    		
    		if (v != null && v.equals(value)) return true;
    	}
    	
    	return false;
    }

    public Set<Map.Entry<String,Object>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key) {
        if (key instanceof String) {
            return getAttribute((String) key);
        }
        throw new IllegalArgumentException();
    }

    public Set<String> keySet() {
    	
    	int size = size();
    	
    	if (size == 0) return new HashSet<String>(0);
    	
        Set<String> set = new HashSet<String>(size);
        
        Iterator<String> iter = keys();
        
        while(iter.hasNext()) {
        	
        	set.add(iter.next());
        }
        
        return set;
    }

    public Object put(String key, Object value)  {
        setAttribute((String) key, value);
        return value;
    }

    public void putAll(Map<? extends String,? extends Object> t) {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
        if (key instanceof String) {
            String s = (String) key;
            Object obj = getAttribute(s);
            if (obj != null) {
                removeAttribute(s);
                return obj;
            }
            return null;
        }
        throw new IllegalArgumentException();
    }

    public int size() {
    	
        int size = 0;
        
        Iterator<String> iter = keys();
        
        while(iter.hasNext()) {
        	
        	iter.next();
        	
        	size++;
        	
        }
        
        return size;
    }

    public Collection<Object> values() {
        int size = size();
        
        if (size == 0) return new ArrayList<Object>(0);
        
        List<Object> list = new ArrayList<Object>(size);
        
        Iterator<String> iter = keys();
        
        while(iter.hasNext()) {
        	
        	String key = iter.next();
        	
        	list.add(getAttribute(key));
        }
        
        return list;
    }

}
