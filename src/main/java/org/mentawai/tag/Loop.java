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
package org.mentawai.tag;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.mentawai.tag.util.Context;
import org.mentawai.tag.util.ListContext;
import org.mentawai.tag.util.LoopTag;

/**
 * @author Sergio Oliveira
 */
public class Loop extends LoopTag implements Context {

    private static final String COUNTER_VAR = "counter";

    private List<Object> list = null;

    private int currIndex = -1;

    private String varname = null;

    private String counter = COUNTER_VAR;
    private int counterStart = 0;

    public void setVar(String var) {
        this.varname = var;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }
    
    public void setCounterStart(int start) {
    	this.counterStart = start;
    }

    private void setNext() {
        currIndex++;
        if (varname != null)
            pageContext.setAttribute(varname, list.get(currIndex),
                    PageContext.PAGE_SCOPE);
        if (counter != null)
            pageContext.setAttribute(counter, new Integer(currIndex + counterStart),
                    PageContext.PAGE_SCOPE);
    }

    private boolean hasMore() {
        if (list == null)
            return false;
        return (currIndex >= -1 && currIndex < list.size() - 1);
    }

    public Object getObject() {
        return list.get(currIndex);
    }

    public void init() throws JspException {
        // Pega uma lista de algum lugar para loopar...
        Tag parent = findAncestorWithClass(this, ListContext.class);
        if (parent != null) {
            ListContext ctx = (ListContext) parent;
            this.list = ctx.getList();
        } else {
            throw new JspException("Loop not enclosed by a ListContext !!! By example <mtw:list /> ");
        }
    }

    public boolean loopCondition() {
        if (hasMore()) {
            setNext();
            return true;
        }
        return false;
    }

    public void afterEachLoop() throws JspException {

        // no need to do anything...

    }

    /*
     * public int doEndTag() throws JspException { list = null; currIndex = -1;
     * pageContext.removeAttribute(varname); varname = VARNAME; return
     * super.doEndTag(); }
     */

    public int doEndTag() throws JspException {
        if (varname != null)
            pageContext.removeAttribute(varname, PageContext.PAGE_SCOPE);
        if (counter != null)
            pageContext.removeAttribute(counter, PageContext.PAGE_SCOPE);
        currIndex = -1;
        return EVAL_PAGE;
    }
}
