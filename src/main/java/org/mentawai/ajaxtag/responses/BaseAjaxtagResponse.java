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
package org.mentawai.ajaxtag.responses;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.mentawai.ajaxtag.AjaxtagConstraints;

/**
 * Base class who holds the aschycronous response between the Action and the
 * Consequence.
 * 
 * @author Marvin Froeder (m@rvin.info)
 * @since 1.9
 * @param <E>
 *            the data type. Many be anything and must be parsed by the
 *            AjaxtagRender.
 */
public abstract class BaseAjaxtagResponse<E> {
	protected HttpServletRequest request;

	protected HttpServletResponse response;

	private E data;

	public BaseAjaxtagResponse() {
	}

	public BaseAjaxtagResponse(E data) {
		this.data = data;
	}

	public abstract AjaxtagConstraints getConstraints();

	public E getData() throws Exception {
		return data;
	}

	public void setData(E data) {
		this.data = data;
	}

	public String getName() {
		return getConstraints().name();
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	protected HttpServletResponseWrapper getWrapper(){
		return new IncludeWrapper(response);
	}
}
