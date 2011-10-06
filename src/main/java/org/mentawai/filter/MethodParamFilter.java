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

import java.util.ArrayList;
import java.util.List;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;

/**
 * This filter will place a java.util.List with the action input keys we
 * want to use as the parameters of a Pojo Action method.
 * 
 * It simply takes the list of params passed, create a list with them, and place this
 * list in the action input with the PARAM_KEY key value, so that it can be later
 * accessed by the InvocationChain.
 * 
 * @author Sergio Oliveira Jr
 */
public class MethodParamFilter implements Filter {
	
	public static String PARAM_KEY = "_params"; 
		
    
    private final List<String> list;
    
	public MethodParamFilter(String ... params) {
		
		super();
		
		list = new ArrayList<String>(params.length);
		
		for(String p: params) {
			
			list.add(p);
		}
	}
    
	public String filter(InvocationChain chain) throws Exception {
		
		Action a = chain.getAction();
		
		Input input = a.getInput();
		
		input.setValue(PARAM_KEY, list);
		
		return chain.invoke();
        
	}
	
	public void destroy() { 
    
    }
	
    public String toString() {
        
        StringBuffer sb = new StringBuffer(128);
    
        sb.append("MethodParamFilter: ");
        
        for(int i=0;i<list.size();i++) {
        	
        	if (i > 0) sb.append(", ");
        	
        	sb.append(list.get(i));
        }
            
        return sb.toString();
        
    }
	
}