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

import static org.mentawai.ajaxtag.AjaxtagConstraints.LISTDATA;

import org.mentawai.ajaxtag.AjaxtagConstraints;
import org.mentawai.list.ListData;

/**
 * Base class to other responses.
 * 
 * @author Marvin Froeder (m@rvin.info)
 * @since 1.9
 * @see AutocompleteResponse
 * @see SelectDropdownResponse
 */
public abstract class ListDataAjaxtagResponse extends
		BaseAjaxtagResponse<ListData> {

	public ListDataAjaxtagResponse() {
		super();
	}

	public ListDataAjaxtagResponse(ListData data) {
		super(data);
	}

	@Override
	public AjaxtagConstraints getConstraints() {
		return LISTDATA;
	}
}
