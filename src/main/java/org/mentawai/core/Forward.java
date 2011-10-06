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
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A forward web consequence.
 * 
 * @author Sergio Oliveira
 */
public class Forward implements Consequence {
    
    /**
     * The action is set in the request with this name
     */
    public static String ACTION_REQUEST = "action";

    /**
     * The session context is set in the request with this name
     */    
    public static String SESSION_REQUEST = "session";

    /**
     * The application context is set in the request with this name
     */    
    public static String APPLICATION_REQUEST = "application";
    
    /**
     * The action input is set in the request with this name
     */        
    public static String INPUT_REQUEST = "input";
    
    private String url;
	
	/**
	 * Creates a web consequence for the given url.
	 * 
	 * @param url The url for the forward
	 */
	public Forward(String url) {
		this.url = putSlash(url);
		
		String viewDir = ApplicationManager.getViewDir();
		
		if (viewDir != null) {
			
			if (viewDir.endsWith("/") && viewDir.length() > 1) {
				
				viewDir = viewDir.substring(0, viewDir.length() - 1);
			}
			
			this.url = putSlash(viewDir) + this.url; 
		}
		
	}
    
    private String putSlash(String url) {
        if (url != null && !url.startsWith("/")) {
            return "/" + url;
        }
        return url;
    }
    
    public void execute(Action a, String result, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException {
        try {
            // put output values in the request...
            
            if (a != null) {
            
                Output output = a.getOutput();
                Iterator<String> iter = output.keys();
                while(iter.hasNext()) {
                    String key = (String) iter.next();
                    Object value = output.getValue(key);
                    req.setAttribute(key, value);
                }
                
                // put the application in the request...
                req.setAttribute(APPLICATION_REQUEST, a.getApplication());
                
                // put the session in the request...
                req.setAttribute(SESSION_REQUEST, a.getSession());
                
                // put the input in the request...
                req.setAttribute(INPUT_REQUEST, a.getInput());
                
                // put the action in the request...
                req.setAttribute(ACTION_REQUEST, a);
            
            }
            
            forward(this.url, req, res);
            
        } catch(Exception e) {
            throw new ConsequenceException(e);
        }
    }
    
    /**
     * Forward your web application to an URL.
     *
     * @param url The url for the forward.
     * @param req The http servlet request.
     * @param res the http servlet response.
     */
	public static void forward(String url, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ServletContext app = Controller.getApplication();
        RequestDispatcher rd = app.getRequestDispatcher(url);
        rd.forward(req, res);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer(100);
        sb.append("Forward to ").append(url);
        return sb.toString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = putSlash(url);
    }
}