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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mentawai.action.BaseLoginAction;
import org.mentawai.ajax.AjaxAction;
import org.mentawai.core.Action;
import org.mentawai.core.ActionConfig;
import org.mentawai.core.Context;
import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.SessionContext;
import org.mentawai.jruby.JRubyInterpreter;
import org.mentawai.jruby.RubyAction;

/**
 * A filter to handle user authentcation.
 * You should use this filter to protect your actions from unauthorized access.
 * 
 * @author Sergio Oliveira
 */
public class AuthenticationFilter implements Filter {
	
	public static final String LOGIN = "login";
	public static final String AJAX_DENIED = "ajax_denied";
	public static final String URL_KEY = "url";
	
    /**
     * Creates a new authentication filter.
     */
	public AuthenticationFilter() {	}
	
	public String filter(InvocationChain chain) throws Exception {
      
		Action action = chain.getAction();
      
      Object pojo = chain.getPojo();
      
      Object actionImpl = pojo != null ? pojo : action;
      
		String innerAction = chain.getInnerAction();
      
		Context session = action.getSession();
		
		// check whether this is an internal aciton...
		
		ActionConfig ac = chain.getActionConfig();
		
		if (ac.isInternalOnly()) return chain.invoke();
      
      boolean shouldBypass = false;
      
      if (actionImpl instanceof AuthenticationFree) {
         
         AuthenticationFree af = (AuthenticationFree) actionImpl;
         
         shouldBypass = af.bypassAuthentication(innerAction);
         
      } else if (ac.shouldBypassAuthentication()) {
    	  
    	  shouldBypass = true;
         
      } else if (action instanceof RubyAction) {
    	  
    	  RubyAction ra = (RubyAction) action;
    	  
    	  JRubyInterpreter ruby = JRubyInterpreter.getInstance();
    	  
    	  if (ruby.respondTo(ra.getRubyObject(), "bypassAuthentication")) {
    		  
    		  shouldBypass = (Boolean) ruby.call(ra.getRubyObject(), "bypassAuthentication", Boolean.class, innerAction);
    	  }
      }
      
      if (!shouldBypass) {
         
         Filter f = chain.getFilter(AuthenticationFreeMarkerFilter.class);
         
         if (f != null) {
         
            AuthenticationFreeMarkerFilter aff = (AuthenticationFreeMarkerFilter) f;
            
            shouldBypass = aff.bypassAuthentication(innerAction);
            
         }
      }
		
		if (!shouldBypass) {
		   
			if (!BaseLoginAction.isLogged(session)) {
            
            boolean shouldRedirect = false;
			   
            	if (actionImpl instanceof RedirectAfterLogin) {
				   
				   RedirectAfterLogin ral = (RedirectAfterLogin) actionImpl;
				      
				   shouldRedirect = ral.shouldRedirect(innerAction);
				   
            	} else if (ac.shouldRedirectAfterLogin()) {
            		
            		shouldRedirect = true;
				   
			   } else if (action instanceof RubyAction) {
				   
				   RubyAction ra = (RubyAction) action;
				   
				   JRubyInterpreter ruby = JRubyInterpreter.getInstance();
				   
				   if (ruby.respondTo(ra.getRubyObject(), "shouldRedirect")) {
					   
					   shouldRedirect = (Boolean) ruby.call(ra.getRubyObject(), "shouldRedirect", Boolean.class, innerAction);
				   }
			   }
            
            if (!shouldRedirect) {
               
               Filter f = chain.getFilter(RedirectAfterLoginMarkerFilter.class);
               
               if (f != null) {
                  
                  RedirectAfterLoginMarkerFilter ramf = (RedirectAfterLoginMarkerFilter) f;
                  
                  shouldRedirect = ramf.shouldRedirect(innerAction);
               }
            }
				   
			   if (shouldRedirect) {
				   
			      HttpServletRequest req = ((SessionContext) session).getRequest();
			      HttpSession ses = ((SessionContext) session).getSession();
			      setCallbackUrl(ses, req);
			      
			   }
				
				if (actionImpl instanceof AjaxAction) {
					
					return AJAX_DENIED;
				}
				
				if( actionImpl instanceof Action ) {
					Input input = ((Action) actionImpl).getInput();
					String value = input.getHeader("isAjaxRequest");
					
					if( "true".equals( value ))
						return AJAX_DENIED;
				}
				
				return LOGIN;
			}
		}
		return chain.invoke();
	}
	
    /**
     * Sets a callback url for a redirection after the login.
     * This method is called by the authentication tag to set a url for the redirection.
     * You should not call this method.
     *
     * @param session The HttpSession where to put the URL.
     * @param req The HttpServletRequest from where to get the URL.
     */
	public static void setCallbackUrl(HttpSession session, HttpServletRequest req) {
		StringBuffer url = req.getRequestURL();
		String query = req.getQueryString();
		if(query != null) {
			url.append('?');
			url.append(query);
		}
		session.setAttribute(URL_KEY, url.toString());		
	}
    
    public void destroy() { }
	
}
		