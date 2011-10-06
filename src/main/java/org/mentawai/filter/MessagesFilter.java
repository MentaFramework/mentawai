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
package org.mentawai.filter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mentawai.core.Action;
import org.mentawai.core.BaseAction;
import org.mentawai.core.Context;
import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InputWrapper;
import org.mentawai.core.InvocationChain;
import org.mentawai.i18n.LocaleManager;
import org.mentawai.message.ClassMessageContext;
import org.mentawai.message.DefaultMessage;
import org.mentawai.message.FileMessageContext;
import org.mentawai.message.Message;
import org.mentawai.message.MessageContext;
import org.mentawai.message.MessageManager;

public class MessagesFilter extends InputWrapper implements Filter {

	private final String name;

	public MessagesFilter(String name) {

		this.name = name;
	}

	private MessageContext getMessageContext(Action action) {

		if (LocaleManager.isUseMasterForEverything()) {

			// the master i18n file will be used for all actions...

			return new FileMessageContext(LocaleManager.getMaster(), "");

		} else {

			return new ClassMessageContext(action.getClass());

		}
	}

	private List<Message> getMessages(Action action) {

		return MessageManager.getMessages(action, true);

	}

	private void addMessage(Action action, String msg_id) {

		List<Message> messages = getMessages(action);

		messages.add(new DefaultMessage(msg_id, getMessageContext(action),
				BaseAction.getMessageTokens(action, null)));
	}

	public String filter(InvocationChain chain) throws Exception {

		Action action = chain.getAction();

		super.setInput(action.getInput());

		action.setInput(this);

		String result = chain.invoke();

		// now add the field errors normally...
		
		List<String> messages = (List<String>) super.getValue(name);
		
		if (messages == null) return result;

		Iterator<String> iter = messages.iterator();

		while (iter.hasNext()) {

			String value = iter.next();

			addMessage(action, value);
		}

		return result;
	}
	
	@Override
	public Object getValue(String name) {
		
		if (name.equals(this.name)) {
			
			Object value = super.getValue(name);
			
			if (value != null) return value;
			
			List<String> messages = new LinkedList<String>();
			
			setValue(name, messages);
			
			return messages;
			
		} else {
			
			return super.getValue(name);
		}
	}


	public void destroy() {

	}

}