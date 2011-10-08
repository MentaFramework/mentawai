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

import org.mentawai.filter.AuthenticationFilter;
import org.mentawai.filter.AuthorizationFilter;
import org.mentawai.filter.ExceptionFilter;

/**
 * Describes a Mentawai action, the central idea of the framework architecture.
 * An action has an input (org.mentawai.core.Input) and an output (org.mentawai.core.Output).
 * An action generates a result (java.lang.String) after it is executed. The result is usually SUCCESS or ERROR.
 * For each result there is a consequence (org.mentawai.core.Consequence). The consequences for a web application are usually FORWARD or REDIRECT.
 * An action has access to contexts (org.mentawai.core.Context). The contexts for a web application are usually a SessionContext or a ApplicationContext.
 *
 * @author Sergio Oliveira
 */
public interface Action {
    
   /** The action execution was successful. */    
   public static final String SUCCESS = "success";
    
    /** The action execution generated an error. */
	public static final String ERROR = "error";
   
	public static final String SHOW = "show";
   
   public static final String LIST = "list";
   
   public static final String INDEX = "index";
   
   public static final String MAIN = "main";
   
   public static final String TEST = "test";
   
   public static final String JSP = "jsp";
   
   public static final String NULL = "null";
   
   public static final String CREATED = "created";
   
   public static final String UPDATED = "updated";
   
   public static final String REMOVED = "removed";
   
   public static final String ALREADY = "already";
   
   public static final String BLOCKED = "blocked";
   
   public static final String XML = "xml";
   
   public static final String JSON = "json";
   
   public static final String HTML = "html";
   
   public static final String ADD = "add";
   
   public static final String EDIT = "edit";
	
   public static final String NEXT = "next";
   
   public static final String BACK = "back";
   
   public static final String AJAX = "ajax";
   
   public static final String STREAM = "stream";
  
   public static final String ACCESSDENIED = AuthorizationFilter.ACCESSDENIED;
   
   public static final String LOGIN = AuthenticationFilter.LOGIN;

   public static final String EXCEPTION = ExceptionFilter.EXCEPTION;
   
   
	
	/**
	 * Executes the action, returning a result.
	 * 
	 * REMOVED FROM INTERFACE IN VERSION 1.12
	 * 
	 * @return a string with the result
	 * @throws Exception if these action cannot be executed for some reason
	 */
	//public String execute() throws Exception;
	
	/**
	 * Sets the input for this action.
	 * 
	 * @param input the input to be set
	 */
	public void setInput(Input input);
	
	/**
	 * Sets the output for this action.
	 * 
	 * @param output the output to be set
	 */
	public void setOutput(Output output);
	
	/**
	 * Sets the session context for this action.
	 * 
	 * @param context The context to be set
	 */
	public void setSession(Context context);
	
	/**
	 * Sets the application context for this action.
	 * 
	 * @param context The context to be set
	 */
	public void setApplication(Context context);
    
    /**
     * Sets the cookie context for this action.
     * 
     * @param context The cookie context to be set
     */
    public void setCookies(Context context);
	
	/**
	 * Sets the user locale for this action.
	 *
	 * @param loc The user locale to set.
	 */
	 public void setLocale(Locale loc);
     
	/**
	 * Gets the action input.
	 * 
	 * @return The action input
	 */
	public Input getInput();
	
	/**
	 * Gets the action output.
	 * 
	 * @return The action output
	 */
	public Output getOutput();
	
	/**
	 * Gets this action session context.
	 * 
	 * @return The session context
	 */
	public Context getSession();
	
	/**
	 * Gets this action application context.
	 * 
	 * @return The application context
	 */
	public Context getApplication();
    
    /**
     * Gets the cookie context for this action.
     * 
     * @return The cookie context
     * @since 1.2
     */
    public Context getCookies();
	
	/**
	 * Gets the use locale for this action.
	 *
	 * @return The user locale
     * @since 1.2
	 */
	 public Locale getLocale();
}

	