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
package org.mentawai.ajaxtag;

import org.mentawai.ajax.AjaxAction;
import org.mentawai.ajaxtag.responses.AutocompleteResponse;
import org.mentawai.ajaxtag.responses.BaseAjaxtagResponse;
import org.mentawai.ajaxtag.responses.CalloutResponse;
import org.mentawai.ajaxtag.responses.HTMLContentReplaceResponse;
import org.mentawai.ajaxtag.responses.PortletAreaResponse;
import org.mentawai.ajaxtag.responses.SelectDropdownResponse;
import org.mentawai.ajaxtag.responses.TabPanelResponse;
import org.mentawai.ajaxtag.responses.UpdateFieldResponse;
import org.mentawai.core.BaseAction;
import org.mentawai.core.Output;

/**
 * This actions holds an aschycronous response to ajaxtags.
 * 
 * @see AutocompleteResponse
 * @see CalloutResponse
 * @see HTMLContentReplaceResponse
 * @see PortletAreaResponse
 * @see SelectDropdownResponse
 * @see TabPanelResponse
 * @see UpdateFieldResponse
 * 
 * @author Marvin Froeder (m@rvin.info)
 * @since 1.9
 */
public abstract class BaseAjaxtagAction extends BaseAction implements AjaxAction {

	public static final String AJAXTAG_RESPONSE = "ajaxtag_response";
	private Output output;

	public BaseAjaxtagAction() {
	}

	/**
	 * @return an Instance of an AjaxtagLibResponse. This instance will be used
	 *         by the Ajaxtag render in order to ofer an ajax response.
	 */
	public final BaseAjaxtagResponse getResponse() {
		return (BaseAjaxtagResponse) super.output.getValue(AJAXTAG_RESPONSE);
	}

	public final void setResponse(BaseAjaxtagResponse response) {
		super.output.setValue(AJAXTAG_RESPONSE, response);
	}

}
