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
import org.mentawai.core.Filter;
import org.mentawai.core.InputWrapper;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.PojoAction;
import org.mentawai.i18n.LocaleManager;
import org.mentawai.message.ClassMessageContext;
import org.mentawai.message.DefaultMessage;
import org.mentawai.message.FileMessageContext;
import org.mentawai.message.Message;
import org.mentawai.message.MessageContext;
import org.mentawai.message.MessageManager;

public class ErrorsFilter extends InputWrapper implements Filter {

	private final String name;

	private boolean alwaysReturnError = false;

	public ErrorsFilter(String name) {

		this.name = name;
	}

	public ErrorsFilter(String name, boolean alwaysReturnError) {

		this(name);

		this.alwaysReturnError = alwaysReturnError;
	}

	private MessageContext getMessageContext(Action action) {

		if (LocaleManager.isUseMasterForEverything()) {

			// the master i18n file will be used for all actions...

			return new FileMessageContext(LocaleManager.getMaster(), "");

		} else {

			return new ClassMessageContext(action.getClass());

		}
	}

	private List<Message> getErrors(Action action) {

		return MessageManager.getErrors(action, true);

	}

	private void addError(Action action, String error_id) {

		List<Message> errors = getErrors(action);

		errors.add(new DefaultMessage(error_id, getMessageContext(action),
				BaseAction.getMessageTokens(action, null)));
	}

	public String filter(InvocationChain chain) throws Exception {

		Action action = chain.getAction();

		super.setInput(action.getInput());

		action.setInput(this);

		String result = chain.invoke();

		// now add the field errors normally...

		List<String> errors = (List<String>) super.getValue(name);

		if (errors == null)	return result;

		Iterator<String> iter = errors.iterator();

		while (iter.hasNext()) {

			String value = iter.next();

			addError(action, value);
		}

		// should we return ERROR or what the action returned?

		if (errors.size() > 0) {

			if (alwaysReturnError)
				return Action.ERROR;

			Object actionResult = action.getOutput().getValue(PojoAction.RESULT);

			if (actionResult != null && !(actionResult instanceof String)) {

				return Action.ERROR;
			}
		}

		return result;
	}

	@Override
	public Object getValue(String name) {

		if (name.equals(this.name)) {

			Object value = super.getValue(name);

			if (value != null) return value;

			List<String> errors = new LinkedList<String>();

			setValue(name, errors);

			return errors;

		} else {

			return super.getValue(name);
		}
	}

	public void destroy() {

	}

}