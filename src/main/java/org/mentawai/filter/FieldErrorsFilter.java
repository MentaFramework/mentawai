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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

public class FieldErrorsFilter extends InputWrapper implements Filter {

	private final String name;

	private boolean alwaysReturnError = false;

	public FieldErrorsFilter(String name) {

		this.name = name;
	}

	public FieldErrorsFilter(String name, boolean alwaysReturnError) {

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

	private Map<String, Message> getFieldErrors(Action action) {

		return MessageManager.getFieldErrors(action, true);

	}

	private void addError(Action action, String field, String error_id) {

		Map<String, Message> fieldErrors = getFieldErrors(action);

		fieldErrors.put(field, new DefaultMessage(error_id,
				getMessageContext(action), BaseAction.getMessageTokens(action,
						field)));
	}

	public String filter(InvocationChain chain) throws Exception {

		Action action = chain.getAction();

		super.setInput(action.getInput());

		action.setInput(this);

		String result = chain.invoke();
		
		// now add the field errors normally...
		
		Map<String, String> fieldErrors = (Map<String, String>) super.getValue(name);
		
		if (fieldErrors == null) return result;

		Iterator<String> iter = fieldErrors.keySet().iterator();

		while (iter.hasNext()) {

			String key = iter.next();

			String value = fieldErrors.get(key);

			addError(action, key, value);
		}

		// should we return ERROR or what the action returned?

		if (fieldErrors.size() > 0) {

			if (alwaysReturnError) return Action.ERROR;

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

			Map<String, String> fieldErrors = new HashMap<String, String>();

			setValue(name, fieldErrors);

			return fieldErrors;

		} else {

			return super.getValue(name);
		}
	}
	

	public void destroy() {

	}

}