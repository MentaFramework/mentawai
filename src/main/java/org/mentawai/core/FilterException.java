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
package org.mentawai.core;


/**
 * An exception that can happen when a filter is executed.
 *
 * @author Sergio Oliveira
 */
public class FilterException extends Exception {

	private static final long serialVersionUID = 2328808746264979914L;
	protected final Throwable rootCause;

	public FilterException() {
		super();

		this.rootCause = null;
	}

	public FilterException(Throwable e) {
		super(getMsg(e), e);

		Throwable root = getRootCause(e);

		this.setStackTrace(root.getStackTrace());

		this.rootCause = root == this ? null : root;
	}

	private static String getMsg(Throwable t) {

		Throwable root = getRootCause(t);

		String msg = root.getMessage();

		if (msg == null || msg.length() == 0) {

			msg = t.getMessage();

			if (msg == null || msg.length() == 0) return root.getClass().getName();
		}

		return msg;
	}


	public FilterException(String msg) {
		super(msg);

		this.rootCause = null;
	}

	public FilterException(String msg, Throwable e) {

		super(msg, e);

		Throwable root = getRootCause(e);

		this.setStackTrace(root.getStackTrace());

		this.rootCause = root == this ? null : root;
	}

	private static Throwable getRootCause(Throwable t) {

		Throwable root = t.getCause();

		if (root == null) return t;

		while(root.getCause() != null) {

			root = root.getCause();
		}

		return root;

	}

	@Override
	public Throwable getCause() {

		return rootCause;
	}

}
