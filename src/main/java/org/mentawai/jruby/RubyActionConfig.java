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
package org.mentawai.jruby;

import org.mentawai.core.Action;
import org.mentawai.core.ActionConfig;

public class RubyActionConfig extends ActionConfig {
	
	private JRubyInterpreter ruby = JRubyInterpreter.getInstance();
	
	private static long ruby_object_id = 0;
	
	private final String ruby_klass_name; 
	
	private static Class<? extends Object> getRubyClass(String name) {
		Object o = JRubyInterpreter.getInstance().eval(name + ".new");
		return o.getClass();
	}
	
	public RubyActionConfig(String klass) {
		super(getRubyClass(klass));
		this.ruby_klass_name = klass;
	}
    

	public RubyActionConfig(String name, String klass) {
		super(name, getRubyClass(klass));
		this.ruby_klass_name = klass;
	}
    
	public RubyActionConfig(String name, String klass, String innerAction) {
		super(name, getRubyClass(klass), innerAction);
		this.ruby_klass_name = klass;
	}
	
	private synchronized static long getNextActionId() {
		
		if (ruby_object_id < Long.MAX_VALUE) {
			
			ruby_object_id++;
			
		} else {
			
			ruby_object_id = 1;
		}
		
		return ruby_object_id;
	}

	
    public Action getAction() {
    	
    	try {
    		
    		long nextActionId = getNextActionId();
    		
    		Object obj = ruby.createAction(nextActionId, ruby_klass_name);
    		
        	return new RubyAction(nextActionId, obj, ruby_klass_name);
    	
    	} catch(Exception e) {
    		
    		e.printStackTrace();
    	}

    	return null;
    }
}

		
	
	
	