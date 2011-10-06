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
package org.mentawai.util;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.mentawai.core.Action;
import org.mentawai.core.ActionConfig;
import org.mentawai.core.ApplicationManager;
import org.mentawai.core.Consequence;
import org.mentawai.core.Input;
import org.mentawai.core.Output;
import org.mentawai.i18n.LocaleManager;

/**
 * A Servlet filter for showing the execution of the action chain.
 *
 * @author Sergio Oliveira
 */
public class DebugServletFilter implements Filter {

    public static String DEBUG_KEY = "_debug";

    public static boolean COMMENTED = false;

    private static String htmlRedFont="<font color=\"red\">";
    private static String htmlBlueFont="<font color=\"blue\">";
    private static String htmlCloseFont="</font>";
    private static String htmlOpenItalic="<i>";
    private static String htmlCloseItalic="</i>";
    private static String htmlOpenBold="<b>";
    private static String htmlCloseBold="</b>";
    
    private static Set<String> moreStaticInfo = new LinkedHashSet<String>();
    
    public static void addStaticInfo(String s) {
    	moreStaticInfo.add(s);
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        PrintWriter writer = res.getWriter();
        CharResponseWrapper wrapper = new CharResponseWrapper((HttpServletResponse) res);

        chain.doFilter(req, wrapper);

        writer.println(wrapper.toString());

        Object debug = req.getAttribute(DEBUG_KEY);

        if (debug == null) {

            HttpSession session = ((HttpServletRequest) req).getSession();

            if (session != null) {

                debug = session.getAttribute(DEBUG_KEY);

                session.removeAttribute(DEBUG_KEY);
            }
        }

        if (debug != null) {
            if (COMMENTED) writer.println("\n\n<!--  MENTAWAI DEBUG: Begin \n\n");
            else writer.println("<pre>\n\n");

            writer.println("- - - - - - - - - - - - - - - -  Mentawai DEBUG - - - - - - - - - - - - - - - - \n\n");
            writer.println(debug.toString());
            writer.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - \n\n");
            if (COMMENTED)
              writer.println("\n\n" +
            		  htmlRedFont+"Mentawai Web Framework - Version:"+
                      ApplicationManager.MENTAWAI_VERSION+" Build:"+
                      ApplicationManager.MENTAWAI_BUILD);
            else
             writer.println(htmlRedFont+"<b>Mentawai Web Framework</b> - Version:<b>"+
                     ApplicationManager.MENTAWAI_VERSION+" </b> Build:<b>"+
                     ApplicationManager.MENTAWAI_BUILD +"</b>"+htmlCloseFont);

            if (COMMENTED) writer.println("\n\n MENTA DEBUG : End -->\n");
            else writer.println("</pre>\n");
        }
    }

    public static void debug(Action a, org.mentawai.core.Filter f) {

    	debug(getDebug(a), f);
    }

    public static void debug(StringBuffer sb, org.mentawai.core.Filter f) {

    	if (sb == null) return;

    	sb.append("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - \n\n");
    	sb.append("\n"+htmlBlueFont+"Filter = ").append(f.toString()).append(htmlCloseFont+"\n\n\n");
    }

    public static void debug(Action a, String value, boolean flag) {

    	debug(getDebug(a), value, flag);
    }

    public static void debug(StringBuffer sb, String value, boolean flag) {

    	if (sb == null) return;

    	clearHtmlTags(COMMENTED);

    	sb.append("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - \n\n");
    	if (flag) {

    		sb.append("\n"+htmlRedFont+"Result = ").append(value).append(htmlCloseFont+"\n\n");

    	} else {

    		sb.append("\n"+htmlOpenBold+htmlRedFont+"Action = ").append(value != null ? value : "execute").append("()"+htmlCloseFont+htmlCloseBold+"\n\n");
    	}
    }

    public static void debug(Action a, long time) {

    	debug(getDebug(a), time);
    }

    public static void debug(StringBuffer sb, long time) {

    	if (sb == null) return;

    	sb.append("Total Time = ").append(time).append("ms\n");
    }

    public static void debug(Action a, Consequence c) {

    	debug(getDebug(a), c);
    }

    public static void debug(StringBuffer sb, Consequence c) {

    	if (sb == null) return;

    	clearHtmlTags(COMMENTED);

    	sb.append(htmlRedFont+"Consequence = ").append(c.toString()).append(htmlCloseFont+"\n\n");
    }

    public static void debug(Action a, String actionName, String innerAction, ActionConfig ac, Locale pageLocale, Locale actionLocale) {

    	debug(getDebug(a), actionName, innerAction, ac, pageLocale, actionLocale);
    }

    public static void debug(StringBuffer sb, String actionName, String innerAction, ActionConfig ac, Locale pageLocale, Locale actionLocale) {

    	if (sb == null) return;

    	clearHtmlTags(COMMENTED);
    	
    	sb.append(htmlRedFont);

        sb.append("ActionName = ").append(actionName).append('\n');
        sb.append("InnerAction = ").append(innerAction != null ? innerAction : "no inner action").append('\n');
        sb.append("ActionClass = ").append(ac.getActionClass().getName()).append('\n');
        
        sb.append("Browser Locale = ").append(pageLocale).append('\n');
        sb.append("Action Locale = ").append(actionLocale).append('\n');
        sb.append("Default Locale = ").append(LocaleManager.getDefaultLocale()).append('\n');
        
        for(String info : moreStaticInfo) {
        	
        	sb.append(info).append('\n');
        }
        
        sb.append(htmlCloseFont).append("\n\n");
    }

    public static StringBuffer getDebug(Action action) {

        return (StringBuffer) action.getOutput().getValue(DEBUG_KEY);

    }

    public static StringBuffer getDebug(Output output) {

    	return (StringBuffer) output.getValue(DEBUG_KEY);
    }

    public static void debugInputOutput(Action action) {

        StringBuffer sb = getDebug(action);

        if (sb == null) return;

        sb.append("Input:\n\n");

        printInput(sb, action.getInput());

        sb.append("\n");

        sb.append("Output:\n\n");

        printOutput(sb, action.getOutput());

        sb.append("\n");

    }

    private static void printInput(StringBuffer sb, Input input) {

    	if (sb == null) return;

    	clearHtmlTags(COMMENTED);

        Iterator<String> iter = input.keys();

        if (!iter.hasNext()) {

            sb.append("\t"+htmlOpenItalic+"empty"+htmlCloseItalic+"\n");

            return;

        }

        while(iter.hasNext()) {


            String name = iter.next();
            Object value = input.getValue(name);
            String s = null;
            if (value instanceof Collection) {
			 	int size = ((Collection) value).size();
			 	s = value != null ? " Collection of "+size+" elements: "+ value.toString() : "null";
			}
            else if (value instanceof Output) {
            	
            	s = "<i>Action Output</i>";
            	
            }  else if (value != null && value.getClass().getName().equals("org.apache.commons.fileupload.FileItem")) {
            	
            	FileItem fi = (FileItem) value;
            	
            	String filename = fi.getName();
            	
            	if (filename != null && !filename.trim().equals("")) {
            	
            		s = "File Upload: " + filename + " (" + fi.getSize() + " bytes)";
            		
            	} else {
            		
            		s = "";
            	}
            	
            } else {
            	
             s = value != null ? value.toString() : "null";
             
            }
            
            if (s.length() > 100) s = s.substring(0, 100)+" ...";
            
            s = s.replace('\n', ' ');
            
            sb.append('\t').append(name).append(" = ").append(s).append("\n");

        }
    }

    private static void printOutput(StringBuffer sb, Output output) {

    	if (sb == null) return;

    	clearHtmlTags(COMMENTED);

    	Iterator<String> iter = output.keys();
        boolean isEmpty = true;

        while(iter.hasNext()) {

            String name = iter.next();

            if (name.equals(DebugServletFilter.DEBUG_KEY)) continue;

            Object value = output.getValue(name);

            String s = null;
            if (value instanceof Collection) {
			 	int size = ((Collection) value).size();
			 	s = value != null ? " Collection of "+size+" elements: "+ value.toString() : "null";
			} else if (value != null && value.getClass().getName().equals("org.apache.commons.fileupload.FileItem")) {
            	
            	FileItem fi = (FileItem) value;
            	
            	String filename = fi.getName();
            	
            	if (filename != null && !filename.trim().equals("")) {
            	
            		s = "File Upload: " + filename + " (" + fi.getSize() + " bytes)";
            		
            	} else {
            		
            		s = "";
            	}
            	
            } else {
             s = value != null ? value.toString() : "null";
            }

            if (s.length() > 100) s = s.substring(0, 100)+" ...";

            s = s.replace('\n', ' ');

            sb.append('\t').append(name).append(" = ").append(s).append("\n");

            isEmpty = false;
        }

        if (isEmpty) sb.append("\t"+htmlOpenItalic+"empty"+htmlCloseItalic+"\n");
    }

    /**
     * Clean HTML tags for Debug
     */
    private static void clearHtmlTags(boolean flag) {

    	if (flag) {

	      // no HTML tags inside HTML comment!
	      htmlRedFont=" ";
	      htmlBlueFont=" ";
	      htmlCloseFont=" ";
	      htmlOpenItalic=" ";
	      htmlCloseItalic=" ";
	      htmlOpenBold=" ";
	      htmlCloseBold=" ";

    	} else {

    		htmlRedFont="<font color=\"red\">";
    	    htmlBlueFont="<font color=\"blue\">";
    	    htmlCloseFont="</font>";
    	    htmlOpenItalic="<i>";
    	    htmlCloseItalic="</i>";
    	    htmlOpenBold="<b>";
    	    htmlCloseBold="</b>";
    	}
    }

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void destroy() { }

}