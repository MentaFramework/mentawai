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

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mentawai.ajaxtag.responses.BaseAjaxtagResponse;
import org.mentawai.ajaxtag.responses.ErrorResponse;
import org.mentawai.core.Action;
import org.mentawai.core.Consequence;
import org.mentawai.core.ConsequenceException;
import org.mentawai.log.Error;
import org.mentawai.util.HttpUtils;

/**
 * <p>
 * A Consequence for using Ajaxtag taglib (ajaxtags.sf.net). This consequence
 * must be used as the way to send errors back to the client.
 * </p>
 * 
 * @author Marvin Froeder (m@rvin.info)
 * @since 1.9
 */
public class AjaxtagErrorConsequence implements Consequence {

	public AjaxtagErrorConsequence() {
	}

	/**
	 * <p>
	 * Send a non-200 HTTP response as an error responses when any problem
	 * happened.
	 * </p>
	 * <p>
	 * That response can be catch at JSP on ajaxtag errorFunction
	 * </p>
	 */
	public void execute(Action a, String result, HttpServletRequest req,
			HttpServletResponse res) throws ConsequenceException {

		HttpUtils.disableCache(res);

		BaseAjaxtagAction ba = (BaseAjaxtagAction) a;
		BaseAjaxtagResponse response = ba.getResponse();
		if (response == null) {
			sendError(res, null);
			return;
		}

		AjaxtagConstraints constraint = response.getConstraints();

		if (constraint == AjaxtagConstraints.ERROR) {
			ErrorResponse r = (ErrorResponse) response;
			sendError(res, r.getException());
		} else {
			sendError(res, null);
		}
	}

	/**
	 * Send a non-200 HTTP response.
	 * 
	 * That response can be catch at JSP on ajaxtag errorFunction
	 * 
	 * @param exception
	 *            the execution exception
	 */
	static void sendError(HttpServletResponse res, Exception exception)
			throws ConsequenceException {
		Error.log("An error has occuried on the ajaxtag renderization!",
				exception);

		String msg = "";
		if (exception != null)
			msg = exception.getMessage();

		try {
			res.sendError(SC_INTERNAL_SERVER_ERROR,
					"An error has occuried on the ajaxtag process: " + msg);
		} catch (IOException e1) {
			Error.log("Exception while writing to send an error: "
					+ e1.getMessage(), e1);
			throw new ConsequenceException(
					"Exception while writing to send an error: "
							+ e1.getMessage(), e1);
		}
	}
}
