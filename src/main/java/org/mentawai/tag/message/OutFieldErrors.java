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
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.mentawai.message.Message;
import org.mentawai.message.MessageManager;
import org.mentawai.tag.util.ConditionalTag;
import org.mentawai.tag.util.ListContext;

/**
 * @author Sergio Oliveira
 */
public class OutFieldErrors extends ConditionalTag implements ListContext {

    private String var = "errors";

    /**
     * Identifies the validation errors should be exported to the page context as variables.
     * Ex: If there is a validation error in the 'username' will be created the following variables:
     * username_hasError and username_error
     */
    private boolean export;

    public void setVar(String var) {

        this.var = var;
    }

   public List<Object> getList() throws JspException {

        return (List<Object>) pageContext.getAttribute(var);
   }

   public boolean testCondition() throws JspException {

        Map<String, Message> map = MessageManager.getFieldErrors(action, false);

        if (map == null || map.size() == 0) return false;

        List<String> list = new ArrayList<String>(map.size());

        Iterator<Message> iter = map.values().iterator();

        while(iter.hasNext()) {

            Message msg = iter.next();

            list.add(msg.getText(loc));

        }

        pageContext.setAttribute(var, list);

        if(export)
        	exportVars();

        return true;
    }

    private void exportVars() {
    	 Map<String, Message> map = MessageManager.getFieldErrors(action, false);
    	 if(map!=null){
	    	 for (String key : map.keySet()) {

	             Message msg = map.get(key);

	             if (msg != null){
		             String text = msg.getText(loc);

		             if (text != null) {
		            	 pageContext.setAttribute(key + "_hasError", true);
		                 pageContext.setAttribute(key + "_error", text);
		             }
	             }
	    	 }
    	 }
    }

	public int doEndTag() throws JspException {

       pageContext.removeAttribute(var);

       return super.doEndTag();

   }

	public void setExport(boolean export) {
		this.export = export;
	}
}
