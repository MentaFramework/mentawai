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

import java.util.Locale;

import org.mentawai.core.Action;
import org.mentawai.core.Context;
import org.mentawai.core.Input;
import org.mentawai.core.Output;

public class RubyAction implements Action {
	
	private JRubyInterpreter ruby = JRubyInterpreter.getInstance();
	
	private Input input;
	private Output output;
	private Context session;
	private Context application;
	private Locale locale;
	private Context cookies;
	
	private final Object rubyObject;
	private final long rubyActionId;
	private final String actionName;
	
	private static final ThreadLocal<Action> action = new ThreadLocal<Action>();
	
	public RubyAction(long rubyActionId, Object rubyObject, String actionName) {
		
		this.rubyObject = rubyObject;
		this.rubyActionId = rubyActionId;
		
		String[] s = actionName.split("\\:\\:");
		
		this.actionName = s[s.length - 1];
		
		action.set(this);
	}
	
	public void removeAction() {
		this.action.remove();
	}
	
	public Object getRubyObject() {
		
		return rubyObject;
	}
	
	public long getRubyActionId() {
		
		return rubyActionId;
	}
	
	public static Action getAction() {
		
		return action.get();
	}
	
	public String getActionName() {
		return actionName;
	}
	
	public String execute() throws Exception {
		
		throw new UnsupportedOperationException("This action is not meant to be executed!");
	}
	
	public void setInput(Input input) {
		
		this.input = input;
		
		if (ruby.respondTo(rubyObject, "setInput")) {

			ruby.call(rubyObject, "setInput", input);
		}
	}
	
	public void setOutput(Output output) {
		
		this.output = output;
		
		if (ruby.respondTo(rubyObject, "setOutput")) {
			
			ruby.call(rubyObject, "setOutput", output);
		}
	}
	
	public void setSession(Context context) {
		
		this.session = context;
		
		if (ruby.respondTo(rubyObject, "setSession")) {
			
			ruby.call(rubyObject, "setSession", context);
		}

	}
	
	public void setApplication(Context context) {
		
		this.application = context;
		
		if (ruby.respondTo(rubyObject, "setApplication")) {
			
			ruby.call(rubyObject, "setApplication", context);
		}
	}
    
    public void setCookies(Context context) {
    	
    	this.cookies = context;
    	
		if (ruby.respondTo(rubyObject, "setCookies")) {
			
			ruby.call(rubyObject, "setCookies", context);
		}
    }
	
	 public void setLocale(Locale loc) {
		 
		 this.locale = loc;
		 
		if (ruby.respondTo(rubyObject, "setLocale")) {
		
			ruby.call(rubyObject, "setLocale", loc);
		}
	 }
     
	public Input getInput() {
		
		return input;
	}
	
	public Output getOutput() {
		
		return output;
	}
	
	public Context getSession() {
		
		return session;
	}
	
	public Context getApplication() {
		
		return application;
	}
    
    public Context getCookies() {
    	
    	return cookies;
    }
	
	 public Locale getLocale() {
		 
		return locale; 
	 }

	 @Override
	 protected void finalize() {
		 
		 // free the ruby object to ruby's GC...
		 
		 JRubyInterpreter ruby = JRubyInterpreter.getInstance();
		 ruby.removeAction(rubyActionId);
		 
	 }
}

	