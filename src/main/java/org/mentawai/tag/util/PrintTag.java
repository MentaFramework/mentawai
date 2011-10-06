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
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.mentawai.action.BaseLoginAction;
import org.mentawai.core.Action;
import org.mentawai.core.Forward;
import org.mentawai.i18n.LocaleManager;
import org.mentawai.tag.Out;

/**
 * A very easy abstract class to create custom tags that print something to the screen.
 *
 * @author Sergio Oliveira
 */
public abstract class PrintTag extends BodyTagSupport {
    
	protected ServletContext application = null;
    protected HttpSession session = null;
    protected HttpServletRequest req = null;
    protected HttpServletResponse res = null;
	protected Action action = null;
	protected Locale loc = null;
    
    protected int maxToPrint = -1;
    
    public void setMax(int maxToPrint) {
        
        this.maxToPrint = maxToPrint;
    }
    
    protected boolean noHTML = false;
    
    public void setNoHTML(boolean flag) {
    	this.noHTML = flag;
    }
    
    public Object getValue(String value) {
    	return  getValue(value, false);
    }
    
    public Object getValue(String value, boolean tryBoolean) {
    	return Out.getValue(value, pageContext, tryBoolean);
    }
    
    public Object getSessionObj() {
    	
    	return BaseLoginAction.getSessionObj(session);
    }
    
    /**
     * Override this method to return what you want to print in the screen.
     * 
     * @return The string to print.
     */
    public abstract String getStringToPrint() throws JspException;
    
    protected String getBody() {
    	
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
    
    
	public static Boolean getBooleanValue(Object bean, String value) {
		try {
			StringBuffer sb = new StringBuffer(50);
			sb.append("is");
			sb.append(value.substring(0, 1).toUpperCase());
			if (value.length() > 1) sb.append(value.substring(1));
			Method m = bean.getClass().getMethod(sb.toString(), new Class[0]);
			if (m != null) {
                m.setAccessible(true);
				Object obj = m.invoke(bean, new Object[0]);
                if (obj instanceof Boolean) {
                    return (Boolean) obj;
                }
			}
		}
		catch (Exception e) {
			//e.printStackTrace();
		}
        
		try {
			StringBuffer sb = new StringBuffer(50);
			sb.append("has");
			sb.append(value.substring(0, 1).toUpperCase());
			if (value.length() > 1) sb.append(value.substring(1));
			Method m = bean.getClass().getMethod(sb.toString(), new Class[0]);
			if (m != null) {
				Object obj = m.invoke(bean, new Object[0]);
                if (obj instanceof Boolean) {
                    return (Boolean) obj;
                }                
			}
		}
		catch (Exception e) {
			//e.printStackTrace();
		}        
        
		return null;
	}    
    
    /**
     * Finds a value through reflection.
     *
     * @param bean The object in where to look for the value.
     * @param name The name of the attribute.
     * @param tryBoolean Should I try isXXX and hasXXX ?
     * @return The value found by reflection or null.
     * @since 1.1.1
     */
	public static Object getValue(Object bean, String name, boolean tryBoolean) {
		try {
			StringBuffer sb = new StringBuffer(50);
			sb.append("get");
			sb.append(name.substring(0, 1).toUpperCase());
			if (name.length() > 1) sb.append(name.substring(1));
			Method m = bean.getClass().getMethod(sb.toString(), new Class[0]);
			if (m != null) {
                m.setAccessible(true);
				return m.invoke(bean, new Object[0]);
			}
		}
		catch (Exception e) {
			//e.printStackTrace();
		}
		
		if (name.equals("size")) {
			try {
				StringBuffer sb = new StringBuffer(50);
				sb.append(name);
				Method m = bean.getClass().getMethod(sb.toString(), new Class[0]);
				if (m != null) {
	                m.setAccessible(true);
					return m.invoke(bean, new Object[0]);
				}
			}
			catch (Exception e) {
				//e.printStackTrace();
			}
		}
		
        if (tryBoolean) {
		    return getBooleanValue(bean, name);
        }
        return null;
	}    
    
    /**
     * Finds a value for the corresponding expression.
     * This is useful to look for expressions like user.name.firstName.
     * It works pretty much like a JSP Expression Language.
     *
     * @param expression The expression to look for.
     * @param pageContext The pageContext of the tag.
     * @param tryBoolean Should I try isXXX and has XXX ?
     * @return The value corresponding to the expression.
     * @since 1.1.1
     */
    public static Object getValue(String expression, PageContext pageContext, boolean tryBoolean) {
        StringTokenizer st = new StringTokenizer(expression, ".");
        if (st.countTokens() == 1) {
            //return findValue(st.nextToken(), pageContext);
            return pageContext.findAttribute(st.nextToken());
        } else if (st.countTokens() > 1) {
            String first = st.nextToken();
            //Object value = findValue(first, pageContext);
            Object value = pageContext.findAttribute(first);
            if (value == null) return null;
            while(st.hasMoreTokens()) {
                String next = st.nextToken();
                if (value instanceof Map && !next.equals("size")) {
                    Map map = (Map) value;
                    value = map.get(next);
                } else {
                    value = getValue(value, next, tryBoolean);
                }
                if (value == null) return null;
            }
            return value;
        } else {
        	return null; // expression was blank ???
        }
    }
    
    /**
     * Finds a value for the corresponding expression.
     * This is useful to look for expressions like user.name.firstName.
     * It works pretty much like a JSP Expression Language, but it searches a java Object.
     *
     * @param expression The expression to look for.
     * @param bean The bean where to search.
     * @param tryBoolean Should I try isXXX and has XXX ?
     * @return The value corresponding to the expression.
     * @since 1.3
     */
    public static Object getValue(String expression, Object bean, boolean tryBoolean) {
        StringTokenizer st = new StringTokenizer(expression, ".");
        if (st.countTokens() == 1) {
            //return findValue(st.nextToken(), pageContext);
            return getValue(bean, st.nextToken(), tryBoolean);
        } else {
            String first = st.nextToken();
            //Object value = findValue(first, pageContext);
            Object value = getValue(bean, first, false);
            if (value == null) return null;
            while(st.hasMoreTokens()) {
                String next = st.nextToken();
                if (value instanceof Map) {
                    Map map = (Map) value;
                    value = map.get(next);
                } else {
                    value = getValue(value, next, (tryBoolean && !st.hasMoreTokens()));
                }
                if (value == null) return null;
            }
            return value;
        }
    }        
    
    public int doStartTag() throws JspException {
    	this.application = pageContext.getServletContext();
        this.session = pageContext.getSession();
        this.req = (HttpServletRequest) pageContext.getRequest();
        this.res = (HttpServletResponse) pageContext.getResponse();
		this.action = (Action) req.getAttribute(Forward.ACTION_REQUEST);
		this.loc = LocaleManager.getLocale(req);
        return super.doStartTag();
    }

    public int doEndTag() throws JspException {
        String s = getStringToPrint();
        if (s != null) {
        	
        	if (noHTML) {
        	
        		s = s.replaceAll("\\<.*?\\>","");
        		
        	}
            
            if (maxToPrint > 0 && s.length() > maxToPrint) {
                
                s = s.substring(0, maxToPrint) + "...";
                
            }
            
            try {
                pageContext.getOut().print(s);
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        return EVAL_PAGE;
    }
}

    