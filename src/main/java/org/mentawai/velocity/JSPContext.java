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
package org.mentawai.velocity;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.context.AbstractContext;

/**
 * @author Sergio Oliveira
 */
class JSPContext extends AbstractContext {
    
    private HashMap<String, Object> map = new HashMap<String, Object>();
    private HttpServletRequest req = null;
    private HttpSession session = null;
    private ServletContext application = null;

    public JSPContext(HttpServletRequest req, ServletContext application) {
        this.req = req;
        this.session = req.getSession(true);
        this.application = application;
    }
    
    private Object findValue(String key) {
        Object value = req.getAttribute(key);
        if (value == null) {
            value = session.getAttribute(key);
            if (value == null) {
                value = application.getAttribute(key);
            }
        }
        return value;
    }

    private Object getByReflection(Object bean, String key) {
        StringBuffer sb = new StringBuffer(key.length() + 3);
        sb.append("get");
        sb.append(key.substring(0, 1).toUpperCase());
        if (key.length() > 1) sb.append(key.substring(1));
        try {
            Method m = bean.getClass().getMethod(sb.toString(), new Class[0]);
            if (m != null) {
                m.setAccessible(true);
                return m.invoke(bean, new Object[0]);
            }
        } catch(Exception e) {
            // cannot find by reflection... not an exception...
        }
        return null;
    }    
    
    private Object getValue(String key) {
        StringTokenizer st = new StringTokenizer(key, ".");
        if (st.countTokens() == 1) {
            return findValue(st.nextToken());
        } else {
            String first = st.nextToken();
            Object value = findValue(first);
            if (value == null) return null;
            while(st.hasMoreTokens()) {
                String next = st.nextToken();
                if (value instanceof Map) {
                    Map map = (Map) value;
                    value = map.get(next);
                } else {
                    value = getByReflection(value, next);
                }
                if (value == null) return null;
            }
            return value;
        }
    }
    

    public Object internalGet(String key) {
        Object value = map.get(key);
        if (value != null) {
            return value;
        } else  {
            value = getValue(key);
            if (value != null) {
                map.put(key, value);
            }
        }
        return value;
    }        

    public Object internalPut(String key, Object value) {
        return map.put(key, value);
    }

    public boolean internalContainsKey(Object key) {
        return map.containsKey(key);
    }

    public Object [] internalGetKeys() {
        return map.keySet().toArray();
    }

    public Object internalRemove(Object key) {
        return map.remove(key);
    }
}