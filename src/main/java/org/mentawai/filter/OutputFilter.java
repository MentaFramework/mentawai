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

import java.lang.reflect.Method;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.InputWrapper;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.Output;
import org.mentawai.core.PojoAction;

public class OutputFilter extends InputWrapper implements Filter {

	private String name = null;
	
	private ThreadLocal<Action> action = new ThreadLocal<Action>();

	/**
	 * Creates a OutputFilter.
	 * 
	 * @deprecated Use OutjectionFilter instead!
	 */
	public OutputFilter() {
		
	}

	public OutputFilter(String name) {

		// new filter !!! (inject the output)

		this.name = name;
	}
	
	public boolean isNewVersion() {
		
		// temporary until we remove the deprecated old version...
		
		return name != null;
	}

	private String filterNew(InvocationChain chain) throws Exception {
		
        Action action = chain.getAction();

        super.setInput(action.getInput());

        action.setInput(this);
        
        this.action.set(action);
        
		return chain.invoke();
	}
	
	@Override
	public Object getValue(String name) {
		
		if (name.equals(this.name)) {
			
			Object value = super.getValue(name);
			
			if (value != null) return value;
			
			Action action = this.action.get();
			
			if (action == null) throw new IllegalStateException("Action cannot be null here!");
			
			Output output = action.getOutput();
			
			setValue(name, output);
			
			return output;
			
		} else {
			
			return super.getValue(name);
		}
	}

	private String adjustName(String name) {
		StringBuffer sb = new StringBuffer(name.length() - 3);
		sb.append(name.substring(3, 4).toLowerCase());
		sb.append(name.substring(4, name.length()));
		return sb.toString();
	}

	public String filter(InvocationChain chain) throws Exception {

		if (name != null) {

			// new filter! (the old one is deprecated, should use
			// OutjectionFilter instead!)

			return filterNew(chain);
		}

		String result = chain.invoke();

		Action action = chain.getAction();
		Output output = action.getOutput();

		boolean isModelDriven = false;

		boolean isPojoAction = false;

		if (action instanceof ModelDriven) {

			isModelDriven = true;

		} else if (action instanceof PojoAction) {

			isPojoAction = true;
		}

		Method[] methods = null;
		if (isModelDriven) {
			ModelDriven md = (ModelDriven) action;
			methods = md.getModel().getClass().getMethods();
		} else if (isPojoAction) {

			PojoAction pa = (PojoAction) action;
			methods = pa.getPojo().getClass().getMethods();

		} else {
			methods = action.getClass().getDeclaredMethods();
		}

		for (int i = 0; i < methods.length; i++) {
			String name = methods[i].getName();
			if (name.length() > 3 && name.startsWith("get")
					&& methods[i].getParameterTypes().length == 0) {

				if (name.equals("getClass"))
					continue;

				try {
					methods[i].setAccessible(true);

					Object value = null;

					if (isModelDriven) {

						ModelDriven md = (ModelDriven) action;

						value = methods[i].invoke(md.getModel(),
								(Object[]) null);

					} else if (isPojoAction) {

						PojoAction pa = (PojoAction) action;

						value = methods[i]
								.invoke(pa.getPojo(), (Object[]) null);

					} else {

						value = methods[i].invoke(action, new Object[0]);

					}

					output.setValue(adjustName(name), value);

				} catch (Exception e) {
					System.err.println("Error calling method in OutputFilter: "
							+ name);
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public void destroy() {
	}
}
