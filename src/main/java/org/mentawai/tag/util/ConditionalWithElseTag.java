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
package org.mentawai.tag.util;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.mentaregex.Regex;
import org.mentawai.action.BaseLoginAction;
import org.mentawai.core.Action;
import org.mentawai.core.Forward;
import org.mentawai.i18n.LocaleManager;
import org.mentawai.tag.Out;

/**
 * @author Sergio Oliveira
 */
public abstract class ConditionalWithElseTag extends BodyTagSupport {
	
	private static final String ELSE_REGEX = "\\s*\\[else\\]\\s*";
	
	private String else_regex = ELSE_REGEX;

	private boolean negate = false;

	protected ServletContext application = null;
	protected HttpSession session = null;
	protected HttpServletRequest req = null;
	protected HttpServletResponse res = null;
	protected Action action = null;
	protected Locale loc = null;    
	
	public void setElseMarker(String elseMarker) {
		this.else_regex = "\\s*\\[" + elseMarker + "\\]\\s*";
	}

	public void setNegate(boolean negate) {
		this.negate = negate;
	}

	public abstract boolean testCondition() throws JspException;
	
    public Object getValue(String value) {
    	return  getValue(value, false);
    }
    
    public Object getValue(String value, boolean tryBoolean) {
    	return Out.getValue(value, pageContext, tryBoolean);
    }
    
    public Object getSessionObj() {
    	
    	return BaseLoginAction.getSessionObj(session);
    }

	public int doStartTag() throws JspException {

		this.application = pageContext.getServletContext();
		this.session = pageContext.getSession();
		this.req = (HttpServletRequest) pageContext.getRequest();
		this.res = (HttpServletResponse) pageContext.getResponse();
		this.action = (Action) req.getAttribute(Forward.ACTION_REQUEST);
		this.loc = LocaleManager.decideLocale(req, res);
		
		return EVAL_BODY_BUFFERED;
	}
	
    private String getBody() {
    	
    	BodyContent bc = getBodyContent();
    	
    	if (bc != null) {
    		
    		String s = bc.getString();
    		
    		if (s != null) {
    			
    			s = s.trim();
    			
    			if (!s.equals("")) return s;
    		}
    	}
    	
    	return null;
    }
    
	public int doAfterBody() throws JspException {
		
		String body = getBody();
		
		String finalBody = null;
		
		if (body != null && Regex.matches(body, "/" + else_regex + "/")) {
			
			// else if provided... split body...
			
			String[] blocks = body.split(else_regex);
			
			if (blocks.length != 2) throw new JspException("Invalid body! [ELSE] found more than once!");
			
			if (testCondition()) {
				
				if (!negate) finalBody = blocks[0];
				else finalBody = blocks[1];

			} else {

				if (!negate) finalBody = blocks[1];
				else finalBody = blocks[0];
			
			}
			
		} else {
			
			if (testCondition()) {

				if (!negate) {
					
					finalBody = body;
					
				} else {
					
					finalBody = "";
					
				}
				
			} else {

				if (!negate) {
			
					finalBody = "";
					
				} else {
					
					finalBody = body;
					
				}
			}
		}
		
		BodyContent bc = getBodyContent();
		
		try {
			if (bc != null) {
			
				if (finalBody != null) {
					
					bc.getEnclosingWriter().print(finalBody);
					
				} else {
				
					bc.writeOut(bc.getEnclosingWriter());
				}
			}
		}
		catch (IOException e) {
			throw new JspException(e);
		}
		finally {
			if (bc != null)
				bc.clearBody();
		}

		return SKIP_BODY;
	}
}
