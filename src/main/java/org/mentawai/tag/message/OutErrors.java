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
package org.mentawai.tag.message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.mentawai.filter.FlashScopeFilter;
import org.mentawai.message.Message;
import org.mentawai.message.MessageManager;
import org.mentawai.tag.util.ConditionalTag;
import org.mentawai.tag.util.ListContext;

/**
 * @author Sergio Oliveira
 */
public class OutErrors extends ConditionalTag implements ListContext {

    private String var = "errors";

    public void setVar(String var) {

        this.var = var;
    }

    @SuppressWarnings("unchecked")
    public List<Object> getList() throws JspException {

        return (List<Object>) pageContext.getAttribute(var);
    }

    @SuppressWarnings("unchecked")
    public boolean testCondition() throws JspException {

        List<Message> errors = MessageManager.getErrors(action, false);

        if (errors == null || errors.size() == 0) {
        	
        	// before giving up check flash scope!
        	
        	Object o = FlashScopeFilter.getFlashValue(session, MessageManager.ERRORS);
        	
        	if (o instanceof List) {
        		errors = (List<Message>) o;
        	} else {
        		return false;
        	}
        }

        List<String> list = new ArrayList<String>(errors.size());
        Iterator<Message> iter = errors.iterator();
        while (iter.hasNext()) {
            Message msg = iter.next();
            list.add(msg.getText(loc));
        }

        if (list != null) {
            pageContext.setAttribute(var, list);
        }

        return true;
    }

    public int doEndTag() throws JspException {

        pageContext.removeAttribute(var);

        return super.doEndTag();

    }
}
