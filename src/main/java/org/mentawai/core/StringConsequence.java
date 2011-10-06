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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mentawai.util.DebugServletFilter;

/**
 * Returns a String as the result of an action.
 * 
 * @author Sergio Oliveira Jr.
 */
public class StringConsequence implements Consequence {
    
    public static final String KEY = "string";
    
    private String key = KEY;
    
	public StringConsequence() {
	    
		
    }
    
	public StringConsequence(String key) {
		
		this.key = key;
    }    
    
	public void execute(Action a, String result, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException {
		
		Output output = a.getOutput();

		res.setContentType("text/plain");

		PrintWriter out = null;
		
		try {
			
			out = res.getWriter();
			
		} catch (IOException e) {
			
			throw new ConsequenceException(e);
			
		}
        
        Object value = output.getValue(key);
        
        if (value == null) {
        	
        	Iterator<String> iter = output.keys();
        	
        	while(iter.hasNext()) {
        		
        		String key = iter.next();
        		
        		if (key.equals(DebugServletFilter.DEBUG_KEY)) continue;
        		
        		Object o = output.getValue(key);
        		
        		if (o instanceof String) {
        			
        			value = (String) o;
        			
        			break;
        		}
        	}
        }
        
        if (value == null) throw new ConsequenceException("Cannot find string: " + key);
        
        out.print(value.toString());
        
        out.close();
		
    }
}