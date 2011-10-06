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
package org.mentawai.ajaxtag.renders;

import java.util.Locale;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.mentawai.ajaxtag.responses.BaseAjaxtagResponse;
import org.mentawai.ajaxtag.responses.CalloutResponse;

/**
 * Render String responses
 * 
 * @author Marvin Froeder (m@rvin.info)
 * @since 1.9
 */
class CalloutAjaxtagRender extends AjaxtagRender {

	public String renderize(BaseAjaxtagResponse response, Locale locale)
			throws Exception {
		CalloutResponse r = (CalloutResponse) response;
		String msg = r.getData();

		AjaxXmlBuilder xmlBuilder = new AjaxXmlBuilder();
		xmlBuilder.addItemAsCData("Callout Header", msg);

		return xmlBuilder.toString();
	}

}
