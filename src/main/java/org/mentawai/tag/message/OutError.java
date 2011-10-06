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

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.mentawai.message.Message;
import org.mentawai.message.MessageManager;
import org.mentawai.tag.util.ConditionalTag;
import org.mentawai.tag.util.Context;

/**
 * @author Sergio Oliveira
 */
public class OutError extends ConditionalTag implements Context {

    private String field = null;

    private String var = "error";

    public void setField(String field) {
        this.field = field;
    }

    public void setVar(String var) {

        this.var = var;
    }

    public Object getObject() throws JspException {

        return pageContext.getAttribute(var);
    }

    public boolean testCondition() throws JspException {

        if (field != null) {

            Map<String, Message> map = MessageManager.getFieldErrors(action, false);

            if (map == null)
                return false;

            Message msg = map.get(field);

            if (msg == null)
                return false;

            String text = msg.getText(loc);
            if (text != null) {
                pageContext.setAttribute(var, text);
            }

            return true;

        } else {

            List<Message> errors = MessageManager.getErrors(action, false);

            if (errors == null || errors.size() == 0)
                return false;

            Message msg = errors.get(0);

            String text = msg.getText(loc);
            if (text != null) {
                pageContext.setAttribute(var, text);
            }

            return true;
        }
    }

    public int doEndTag() throws JspException {

        pageContext.removeAttribute(var);

        return super.doEndTag();

    }

}
