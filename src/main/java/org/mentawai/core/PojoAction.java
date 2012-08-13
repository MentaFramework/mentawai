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

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.mentawai.filter.ApplicationFilter;
import org.mentawai.filter.CookiesFilter;
import org.mentawai.filter.ErrorsFilter;
import org.mentawai.filter.FieldErrorsFilter;
import org.mentawai.filter.HeadersFilter;
import org.mentawai.filter.InputFilter;
import org.mentawai.filter.LocaleFilter;
import org.mentawai.filter.MessagesFilter;
import org.mentawai.filter.OutputFilter;
import org.mentawai.filter.PostOrGetFilter;
import org.mentawai.filter.SessionFilter;
import org.mentawai.message.MessageManager;

/**
 * This is just an object that can hold up everything that makes up an action: input, output, session, etc.
 * 
 * The purpose of this class is to hold the necessary information of an action that does not extend
 * BaseAction or implement Action. It is a plain object.
 * 
 * @author Sergio Oliveira Jr.
 */
public class PojoAction implements Action {
	
	public static String RESULT = "pojoActionResult";
	
	private Input input;
	private Output output;
	private Context session;
	private Context application;
	private Locale locale;
	private Context cookies;
	
	private final Object pojo;
	
	private static final ThreadLocal<Action> action = new ThreadLocal<Action>();
	
	public static List<Filter> getFilterStack() {
		
		List<Filter> filters = new LinkedList<Filter>();
		
		filters.add(new ApplicationFilter("application"));
		filters.add(new SessionFilter("session"));
		filters.add(new InputFilter("input"));
		filters.add(new OutputFilter("output"));
		filters.add(new PostOrGetFilter("isPost"));
		filters.add(new CookiesFilter("cookies"));
		filters.add(new LocaleFilter("locale"));
		filters.add(new MessagesFilter(MessageManager.MESSAGES));
		filters.add(new ErrorsFilter(MessageManager.ERRORS));
		filters.add(new FieldErrorsFilter(MessageManager.FIELDERRORS));
		filters.add(new HeadersFilter("headers"));
		
		return filters;

	}
	
	public PojoAction(Object pojo) {
		
		this.pojo = pojo;
		
		action.set(this);
	}
	
	public void removeAction() {
		action.remove();
	}
	
	public Object getPojo() {
		
		return pojo;
	}
	
	public static Action getAction() {
		
		return action.get();
		
	}
	
	public String execute() throws Exception {
		
		throw new UnsupportedOperationException("This action is not meant to be executed!");
	}
	
	public void setInput(Input input) {
		
		this.input = input;
	}
	
	public void setOutput(Output output) {
		
		this.output = output;
	}
	
	public void setSession(Context context) {
		
		this.session = context;
	}
	
	public void setApplication(Context context) {
		
		this.application = context;
	}
    
    public void setCookies(Context context) {
    	
    	this.cookies = context;
    }
	
	 public void setLocale(Locale loc) {
		 
		 this.locale = loc;
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
}

	