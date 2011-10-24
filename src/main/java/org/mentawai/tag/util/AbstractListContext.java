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
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.mentawai.core.Action;
import org.mentawai.core.Forward;
import org.mentawai.i18n.LocaleManager;

/**
 * @author Sergio Oliveira
 */
public abstract class AbstractListContext extends BodyTagSupport implements
        ListContext {

    protected ServletContext application = null;

    protected HttpSession session = null;

    protected HttpServletRequest req = null;

    protected HttpServletResponse res = null;

    protected Locale loc = null;

    protected Action action = null;

    protected String var = null;

    public void setVar(String var) {
        this.var = var;
    }

    private String getVar() {
        if (var == null)
            return getName();
        return var;
    }

    protected abstract String getName();

    public int doStartTag() throws JspException {
        this.application = pageContext.getServletContext();
        this.session = pageContext.getSession();
        this.req = (HttpServletRequest) pageContext.getRequest();
        this.res = (HttpServletResponse) pageContext.getResponse();
        this.loc = LocaleManager.decideLocale(req, res);
        this.action = (Action) req.getAttribute(Forward.ACTION_REQUEST);

        List<Object> list = getList();
        if (list != null) {
            pageContext.setAttribute(getVar(), list, PageContext.PAGE_SCOPE);
        }

        return super.doStartTag();
    }

    public int doAfterBody() throws JspException {
        BodyContent bc = getBodyContent();
        try {
            if (bc != null)
                bc.writeOut(bc.getEnclosingWriter());
        } catch (IOException e) {
            throw new JspException(e);
        } finally {
            if (bc != null)
                bc.clearBody();
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        pageContext.removeAttribute(getVar(), PageContext.PAGE_SCOPE);
        return super.doEndTag();
    }

}
