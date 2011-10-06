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

import org.mentawai.core.Action;
import org.mentawai.core.Context;
import org.mentawai.core.Filter;
import org.mentawai.core.FilterException;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;
import org.mentawai.ioc.Bean;

/**
 * @author Sergio Oliveira
 */
public class PushIoCFilter implements Filter {
    
    public static final int ACTION = 10;
    public static final int REQUEST = 11;
    public static final int SESSION = DependencyFilter.SESSION;
    public static final int APPLICATION = DependencyFilter.APPLICATION;
    
    private Bean comp;
    private int scope = ACTION;
    private String key;
    
    public PushIoCFilter(Bean comp, String key) {
        this.comp = comp;
        this.key = key;
    }
    
    public PushIoCFilter(Bean comp, String key, int scope) {
        this(comp, key);
        this.scope = scope;
    }
    
    public String toString() {
        
        StringBuffer sb = new StringBuffer(128);
        
        sb.append("PushIoCFilter: Component=").append(comp).append(" Key=").append(key).append(" Scope=");
        sb.append(getScope(scope));
        
        return sb.toString();
    }
    
    private String getScope(int scope) {
        switch(scope) {
            case ACTION:
                return "ACTION";
            case REQUEST:
                return "REQUEST";
            case SESSION:
                return "SESSION";
            case APPLICATION:
                return "APPLICATION";
        }
        return "?";
    }
    
	public String filter(InvocationChain chain) throws Exception {
		Action action = chain.getAction();
        Input input = action.getInput();
        Context session = action.getSession();
        Context application = action.getApplication();
        
        try {
        
            if (scope == APPLICATION) {
                Object obj = application.getAttribute(key);
                if (obj == null) {
                    obj = comp.getBean();
                    application.setAttribute(key, obj);
                }
                input.setValue(key, obj);
            } else if (scope == SESSION) {
                Object obj = session.getAttribute(key);
                if (obj == null) {
                    obj = comp.getBean();
                    session.setAttribute(key, obj);
                }
                input.setValue(key, obj);
            } else if (scope == REQUEST) {
                Object obj = input.getValue(key);
                if (obj == null) {
                    obj = comp.getBean();
                    input.setValue(key, obj);
                }
            } else if (scope == ACTION) {
                input.setValue(key, comp.getBean());
            } else {
                throw new FilterException("Invalid scope for IoCFilter: " + scope);
            }
        
        } catch(InstantiationException e) {
            throw new FilterException(e);
        }
        
        return chain.invoke();
	}
    
    public void destroy() { }
}
		