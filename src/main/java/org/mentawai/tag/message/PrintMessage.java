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

import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.mentawai.message.Message;
import org.mentawai.message.MessageManager;
import org.mentawai.tag.util.PrintTag;

/**
 * @author Sergio Oliveira
 */
public class PrintMessage extends PrintTag {

	public String getStringToPrint() throws JspException {

		Tag parent = findAncestorWithClass(this, HasMessage.class);
        List<Message> msgs = MessageManager.getMessages(action, false);
		if (msgs == null || msgs.size() == 0) {
			if (parent != null) {
				HasMessage has = (HasMessage) parent;
				has.setHide(true);
			}
			return null;
		}
		if (parent != null) {
			HasMessage has = (HasMessage) parent;
			has.setHide(false);
		}
		StringBuffer sb = new StringBuffer();
		Iterator<Message> iter = msgs.iterator();
		boolean firstTime = true;
		while (iter.hasNext()) {
			Message msg = iter.next();
			if (!firstTime) {
				sb.append("<br/>");
			} else {
				firstTime = false;
			}			
			sb.append(msg.getText(loc));
		}
		return sb.toString();
	}
}