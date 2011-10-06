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

import org.jruby.RubyObject;

/**
 * A simple wrapper class to encapsulate a Ruby object
 * and make calls to it through a JRubyInterpreter.
 *  
 * @see org.mentawai.jruby.JRubyInterpreter 
 * @author Sergio Oliveira Jr.
 */
public class JRubyWrapper {
	
	private final RubyObject rubyObject;
	
	/**
	 * Creates a JRubyWrapper for the ruby object passed.
	 * 
	 * @param rubyObject The ruby object to encapsulate
	 */
	public JRubyWrapper(RubyObject rubyObject) {
		this.rubyObject = rubyObject;
	}
	
	public Object call(String methodName) {
		JRubyInterpreter ruby = JRubyInterpreter.getInstance();
		return ruby.call(rubyObject, methodName);
	}
	
	public Object call(String methodName, Class<? extends Object> returnType) {
		JRubyInterpreter ruby = JRubyInterpreter.getInstance();
		return ruby.call(rubyObject, methodName, returnType);
	}
	
	public Object call(String methodName, Class<? extends Object> returnType, Object ... params) {
		JRubyInterpreter ruby = JRubyInterpreter.getInstance();
		return ruby.call(rubyObject, methodName, returnType, params);
	}
	
	public Object call(String methodName, Object ... params) {
		JRubyInterpreter ruby = JRubyInterpreter.getInstance();
		return ruby.call(rubyObject, methodName, params);
	}
	
	// static methods...
	public Object callStatic(String methodName) {
		JRubyInterpreter ruby = JRubyInterpreter.getInstance();
		return ruby.call(rubyObject.getMetaClass(), methodName);
	}
	
	public Object callStatic(String methodName, Class<? extends Object> returnType) {
		JRubyInterpreter ruby = JRubyInterpreter.getInstance();
		return ruby.call(rubyObject.getMetaClass(), methodName, returnType);
	}
	
	public Object callStatic(String methodName, Class<? extends Object> returnType, Object ... params) {
		JRubyInterpreter ruby = JRubyInterpreter.getInstance();
		return ruby.call(rubyObject.getMetaClass(), methodName, returnType, params);
	}
	
	public Object callStatic(String methodName, Object ... params) {
		JRubyInterpreter ruby = JRubyInterpreter.getInstance();
		return ruby.call(rubyObject.getMetaClass(), methodName, params);
	}
	
	public boolean respondTo(String methodName) {
		JRubyInterpreter ruby = JRubyInterpreter.getInstance();
		return ruby.respondTo(rubyObject, methodName);
	}
	
	public Object set(String propName, Object value) {
		JRubyInterpreter ruby = JRubyInterpreter.getInstance();
		return ruby.set(rubyObject, propName, value);
	}
	
	/**
	 * Gets the ruby object of this wrapper.
	 * 
	 * @return The ruby object
	 */
	public Object getRubyObject() {
		return rubyObject;
	}
	
	
}