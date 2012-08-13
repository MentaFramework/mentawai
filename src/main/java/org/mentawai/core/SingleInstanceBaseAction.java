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

import java.util.Locale;

/**
 * This is the base class for single instance actions, in other words, 
 * those actions that will be instantiated only once and shared among all requests.
 * Sometimes, when you looking for every performance drop, you may not want to create a new action instance for every request.
 * This class keeps the action data in thread locals,
 * so that each thread has its own input, output, session, application and locale.
 * The actions extending this class must be configurated with a SingleInstanceActionConfig.
 * It is your responsibility to make your action thread-safe, otherwise you should stick with the BaseAction class.
 * 
 * @author Sergio Oliveira
 */
public abstract class SingleInstanceBaseAction extends BaseAction {
    
    private ThreadLocal<Input> input = new ThreadLocal<Input>();
    private ThreadLocal<Output> output = new ThreadLocal<Output>();
    private ThreadLocal<Context> session = new ThreadLocal<Context>();
    private ThreadLocal<Context> application = new ThreadLocal<Context>();
    private ThreadLocal<Locale> loc = new ThreadLocal<Locale>();
	
    /**
     * Creates a SingleInstanceBaseAction.
     */
	public SingleInstanceBaseAction() { }
    
	public void removeAll() {
		input.remove();
		output.remove();
		session.remove();
		application.remove();
		loc.remove();
	}
	
	public void setInput(Input input) {
		this.input.set(input);
	}
	
	public void setOutput(Output output) {
		this.output.set(output);
	}
	
	public void setSession(Context session) {
		this.session.set(session);
	}
	
	public void setApplication(Context application) {
		this.application.set(application);
	}
	
	public void setLocale(Locale loc) {
		this.loc.set(loc);
	}
    
	public Input getInput() {
		return input.get();
	}
	
	public Output getOutput() {
		return output.get();
	}
	
	public Context getSession() {
		return session.get();
	}
	
	public Context getApplication() {
		return application.get();
	}
	
	public Locale getLocale() {
		return loc.get();
	}
}

	