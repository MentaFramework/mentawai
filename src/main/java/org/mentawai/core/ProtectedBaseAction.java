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

import java.util.Locale;

/**
 * This class exists solely for the purpose of hidding the protected variables of BaseAction.
 * 
 * Some people do not like to work with protected variables, so this class is here so that they can
 * call getSession() instead of using the session protected variable for example.
 * 
 * For the sake of offering another option, this class implements the methods: session(), application(), cookies(), etc.
 * Those methods just called the corresponding getSession(), getApplication(), getCookies(), etc.
 * 
 * @author Sergio Oliveira
 *
 */
public abstract class ProtectedBaseAction extends BaseAction {
    
	// make all protected variables private!
	
	private Input input;
	private Output output;
	private Context session;
	private Context application;
	private Context cookies;
	private Locale loc;
	
    /**
     * Creates a ContextBaseAction.
     */
	public ProtectedBaseAction() {
		
		super();
		
    }
}

	