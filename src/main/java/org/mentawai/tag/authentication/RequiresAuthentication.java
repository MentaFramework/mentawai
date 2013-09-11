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
package org.mentawai.tag.authentication;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.mentawai.action.BaseLoginAction;
import org.mentawai.action.SuccessAction;
import org.mentawai.core.Action;
import org.mentawai.core.ApplicationContext;
import org.mentawai.core.ApplicationManager;
import org.mentawai.core.Consequence;
import org.mentawai.core.ConsequenceException;
import org.mentawai.core.CookieContext;
import org.mentawai.core.Forward;
import org.mentawai.core.RequestInput;
import org.mentawai.core.ResponseOutput;
import org.mentawai.core.Redirect;
import org.mentawai.core.SessionContext;
import org.mentawai.filter.AuthenticationFilter;
import org.mentawai.i18n.LocaleManager;

/**
 * @author Sergio Oliveira
 */
public class RequiresAuthentication extends TagSupport {
    
    private static final String HEADER_KEY_PRAGMA = "Pragma";
    private static final String HEADER_KEY_CACHECONTROL = "Cache-Control";
    private static final String HEADER_VALUE_NOCACHE = "no-cache";    
    
    protected HttpSession session = null;
    protected HttpServletRequest req = null;
    protected HttpServletResponse res = null;
    protected ServletContext sc = null;
	protected Action action = null;
	protected Locale loc = null;
	protected ApplicationManager appManager = null;
	
	private boolean redir = false;
    private boolean cache = false;
    
    private String loginPage = null;
	
	public void setRedir(boolean redir) {
		this.redir = redir;
	}
    
    public void setCache(boolean cache) {
        this.cache = cache;
    }
    
    public void setLoginPage(String page) {
        this.loginPage = page;
    }
    
    public int doStartTag() throws JspException {
        this.session = pageContext.getSession();
        this.req = (HttpServletRequest) pageContext.getRequest();
        this.res = (HttpServletResponse) pageContext.getResponse();
        this.sc = pageContext.getServletContext();
		this.action = (Action) req.getAttribute(Forward.ACTION_REQUEST);
		this.loc = LocaleManager.decideLocale(req, res);
		this.appManager = ApplicationManager.getInstance();
        return super.doStartTag();
    }

    public int doEndTag() throws JspException {
        // cache policy... (default is no cache!)
        if (!cache) {
            res.setHeader(HEADER_KEY_PRAGMA, HEADER_VALUE_NOCACHE);
            res.setHeader(HEADER_KEY_CACHECONTROL, HEADER_VALUE_NOCACHE);
        }
        
		if (!BaseLoginAction.isLogged(session)) {
            
			if (redir) AuthenticationFilter.setCallbackUrl(session, req);
            
            Consequence redir = null;
            
            // check if loginPage attribute was passed...
            
            if (loginPage != null) {
                
                redir = new Redirect(loginPage);
                
            } else {
            
                redir = appManager.getGlobalConsequence(AuthenticationFilter.LOGIN);
                
                if (redir == null) throw new JspException("Cannot find global consequence for LOGIN! Don't know where to redirect unauthorized user!");
                
            }
            
            Action a = null;
            
            if (action != null) {
                
                a = action;
                
            } else {
            
                a = new SuccessAction();
                
                a.setInput(new RequestInput(req, res));
                a.setOutput(new ResponseOutput(res));
                a.setSession(new SessionContext(req, res, a));
                a.setApplication(new ApplicationContext(sc));
                a.setCookies(new CookieContext(req, res));
                a.setLocale(loc);
            
            }
            
            // Execute the consequence !!!
            
            try {
                
                redir.execute(a, AuthenticationFilter.LOGIN, req, res);
                
            } catch(ConsequenceException e) {
                
                e.printStackTrace();
                
                throw new JspException(e);
            }
                
			return SKIP_PAGE;
		}
        return EVAL_PAGE;
    }
}

    
