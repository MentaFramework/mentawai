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

import static org.mentawai.ajaxtag.AjaxtagErrorConsequence.sendError;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mentawai.ajaxtag.renders.AjaxtagRender;
import org.mentawai.ajaxtag.responses.BaseAjaxtagResponse;
import org.mentawai.core.Action;
import org.mentawai.core.Consequence;
import org.mentawai.core.ConsequenceException;
import org.mentawai.util.HttpUtils;

/**
 * <p>
 * A Consequence for using Ajaxtag taglib (ajaxtags.sf.net). This consequence
 * must applied to a instance of BaseAjaxtagAction. The BaseAjaxtagAction
 * contains the response to the ajaxtag requisition and as all information
 * required to render that response.
 * </p>
 * 
 * @author Marvin Froeder (m@rvin.info)
 * @since 1.9
 */
public class AjaxtagConsequence implements Consequence {

	public AjaxtagConsequence() {
	}

	/**
	 * <p>
	 * Prints in the request's output the ajaxtag response.
	 * </p>
	 * <p>
	 * Also send a non-200 HTTP response as an error responses when any
	 * exception occurer on the renderization proccess. happened.
	 * </p>
	 */
	public void execute(Action a, String result, HttpServletRequest req,
			HttpServletResponse res) throws ConsequenceException {

		HttpUtils.disableCache(res);

		BaseAjaxtagResponse response = (BaseAjaxtagResponse) a.getOutput().getValue(BaseAjaxtagAction.AJAXTAG_RESPONSE);
		if (response == null) {
			return;
		}
		response.setRequest(req);
		response.setResponse(res);

		AjaxtagConstraints constraint = response.getConstraints();
		AjaxtagRender ajaxRender = AjaxtagRender.getRender(constraint);
		res.setContentType(ajaxRender.getContentType());

		try {
			String answer = ajaxRender.renderize(response, a.getLocale());
			PrintWriter writer = res.getWriter();
			writer.print(answer);
			writer.flush();
		} catch (IOException e) {
			throw new ConsequenceException("Exception while getting printer! "
					+ e.getMessage(), e);
		} catch (Exception e) {
			sendError(res, e);
		}
	}
}
