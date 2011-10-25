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
package org.mentawai.tag.i18n;

import java.io.File;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.mentawai.core.ApplicationManager;
import org.mentawai.i18n.I18N;
import org.mentawai.i18n.I18NMap;
import org.mentawai.i18n.LocaleManager;
import org.mentawai.template.TemplateServlet;

/**
 * @author Sergio Oliveira
 */
public class UseI18N extends TagSupport {

    private static final String SEP = File.separator;
    
    private String files = null;

    private String prefix = null;
    
    private boolean optional = false;
    
    public void setOptional(boolean optional) {
    	this.optional = optional;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    private String[] parseFiles() {
        if (files == null)
            return new String[0];
        StringTokenizer st = new StringTokenizer(files, ",");
        String[] array = new String[st.countTokens()];
        int index = 0;
        while (st.hasMoreTokens()) {
            array[index++] = st.nextToken().trim();
        }
        return array;
    }
    
    public static void loadI18N(PageContext pageContext, String[] files, String prefix) {
    	
        // if you are not using the Mentawai controller you need this...
        if (ApplicationManager.getRealPath() == null) {
            ApplicationManager.setRealPath(pageContext.getServletContext()
                    .getRealPath(""));
        }

        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse res = (HttpServletResponse) pageContext.getResponse();
        
        LocaleManager.decideLocale(req, res); // force the locale to be chosen from param or cookie...
        
        Locale loc = LocaleManager.getLocale(req, false); // notice the
        // false...
        Locale supportedLoc = LocaleManager.getLocale(req, true);

        StringBuffer sb = new StringBuffer(128);

        I18N[] props = new I18N[files.length + 2];

        int index = 0;

        sb.append(SEP).append(LocaleManager.getMaster()).append('_').append(
                loc.toString());

        props[index] = I18NMap.getI18N(sb.toString());

        // try just the language...
        if (props[index] == null) {

            sb.delete(0, sb.length());

            sb.append(SEP).append(LocaleManager.getMaster()).append('_')
                    .append(loc.getLanguage());

            props[index] = I18NMap.getI18N(sb.toString());
        }

        // try supported locale...
        if (props[index] == null) {

            sb.delete(0, sb.length());

            sb.append(SEP).append(LocaleManager.getMaster()).append('_')
                    .append(supportedLoc);

            props[index] = I18NMap.getI18N(sb.toString());
        }

        // try supported locale language...
        if (props[index] == null) {

            sb.delete(0, sb.length());

            sb.append(SEP).append(LocaleManager.getMaster()).append('_')
                    .append(supportedLoc.getLanguage());

            props[index] = I18NMap.getI18N(sb.toString());
        }
        
        String jspFile = req.getServletPath();

        // verify if use templates
        String templateView = (String) req
                .getAttribute(TemplateServlet.CURRENT_VIEW_ATTR);
        if (templateView != null) {
            jspFile = templateView;
        }

        int x = jspFile.lastIndexOf(".");
        jspFile = jspFile.substring(0, x);
        StringBuffer temp = new StringBuffer(jspFile);
        temp.append("_");

        sb.delete(0, sb.length());

        sb.append(SEP).append(LocaleManager.getDir()).append(temp.toString()).append(
                loc.toString());

        props[++index] = I18NMap.getI18N(sb.toString());

        // try just the language..
        if (props[index] == null) {

            sb.delete(0, sb.length());

            sb.append(SEP).append(LocaleManager.getDir()).append(temp.toString()).append(
                    loc.getLanguage());

            props[index] = I18NMap.getI18N(sb.toString());
        }

        // try supported locale...
        if (props[index] == null) {

            sb.delete(0, sb.length());

            sb.append(SEP).append(LocaleManager.getDir()).append(temp.toString()).append(
                    supportedLoc);

            props[index] = I18NMap.getI18N(sb.toString());
        }

        // try supported locale language..
        if (props[index] == null) {

            sb.delete(0, sb.length());

            sb.append(SEP).append(LocaleManager.getDir()).append(temp.toString()).append(
                    supportedLoc.getLanguage());

            props[index] = I18NMap.getI18N(sb.toString());
        }
        
        for (int i = 0; i < files.length; i++) {

            sb.delete(0, sb.length());

            sb.append(SEP).append(LocaleManager.getDir()).append(SEP).append(files[i])
                    .append('_').append(loc.toString());

            I18N prop = I18NMap.getI18N(sb.toString());

            if (prop == null) {

                sb.delete(0, sb.length());

                sb.append(SEP).append(LocaleManager.getDir()).append(SEP).append(files[i])
                        .append('_').append(loc.getLanguage());

                prop = I18NMap.getI18N(sb.toString());
            }
            if (prop == null) {

                sb.delete(0, sb.length());

                sb.append(SEP).append(LocaleManager.getDir()).append(SEP).append(files[i])
                        .append('_').append(supportedLoc);

                prop = I18NMap.getI18N(sb.toString());
            }
            if (prop == null) {

                sb.delete(0, sb.length());

                sb.append(SEP).append(LocaleManager.getDir()).append(SEP).append(files[i])
                        .append('_').append(supportedLoc.getLanguage());

                prop = I18NMap.getI18N(sb.toString());
            }
            
            if (prop != null) {
            
            	props[++index] = prop;
            	
            }
        }

        if (props != null) { // have to check if the attr is null before add
            // it to pageContext because of a WS problem
            pageContext.setAttribute("_i18n", props);
        }
        if (loc != null) {
            pageContext.setAttribute("_locale", loc);
        }
        if (prefix != null) {
            pageContext.setAttribute("_prefix", prefix);
        }

    }

    public int doEndTag() throws JspException {
    	
    	I18N[] props = (I18N[]) pageContext.getAttribute("_i18n");
    	
    	if (props != null && optional) return EVAL_PAGE;
    	
    	loadI18N(pageContext, parseFiles(), prefix);

        return EVAL_PAGE;
    }

}
