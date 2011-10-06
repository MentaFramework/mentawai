/* Mentawai Web Framework http://mentawai.lohis.com.br/
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
package org.mentawai.util;

public class RuntimeException extends java.lang.RuntimeException {
	
	protected final Throwable rootCause;
	
	public RuntimeException() {
		
		super();
		
		this.rootCause = null;
		
	}
	
	public RuntimeException(String msg) {
		
		super(msg);
		
		this.rootCause = null;
		
	}
	
	public RuntimeException(Throwable t) {
		
		super(getMsg(t), t);
		
		Throwable root = getRootCause(t);
		
		setStackTrace(root.getStackTrace());
		
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
	
	public RuntimeException(String msg, Throwable t) {
		
		this(msg, t, false);
	}
	
	public RuntimeException(String msg, Throwable t, boolean appendRootMsg) {
		
		super(getMsg(msg, t, appendRootMsg), t);
		
		Throwable root = getRootCause(t);
		
		setStackTrace(root.getStackTrace());
		
		this.rootCause = root == this ? null : root;
	}
	
	private static String getMsg(String msg, Throwable t, boolean append) {
		
		if (!append) return msg;
		
		StringBuilder sb = new StringBuilder(64);
		
		sb.append(msg);
		
		Throwable root = getRootCause(t);
		
		String classname = root.getClass().getSimpleName();
		
		sb.append(" (").append(classname);
		
		String rootMsg = root.getMessage();
		
		if (rootMsg == null || rootMsg.length() == 0) {
			
			sb.append(")");
			
		} else {
			
			sb.append(": ").append(rootMsg).append(")");
		}
		
		return sb.toString();
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