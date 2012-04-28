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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mentawai.i18n.LocaleManager;

public class PrettyURLController extends Controller {

	private static final long serialVersionUID = 4167310974749699926L;

	private static final String INNER_ACTION_SEPARATOR_PARAM = "innerActionSeparator";

	private static final char DEFAULT_INNER_ACTION_SEPARATOR = '-';

	private static char innerActionSeparator;

	@Override
	public void init(ServletConfig conf) throws ServletException {

		super.init(conf);

		String innerActionSeparatorParam = conf.getInitParameter(INNER_ACTION_SEPARATOR_PARAM);

		if (innerActionSeparatorParam != null) {

			validateLength(innerActionSeparatorParam);

			validateContent(innerActionSeparatorParam);

			PrettyURLController.innerActionSeparator = innerActionSeparatorParam.charAt(0);

		} else {

			PrettyURLController.innerActionSeparator = DEFAULT_INNER_ACTION_SEPARATOR;
		}
	}
	
	public static char getMethodSeparatorChar() {
		return innerActionSeparator;
	}
	
	public static String getExtension() {
		return ApplicationManager.EXTENSION;
	}

	private void validateContent(String innerActionSeparatorParam) throws ServletException {

		if (innerActionSeparatorParam.equals("/")) {

			throw new ServletException("The "
					+ INNER_ACTION_SEPARATOR_PARAM
					+ " context parameter cannot be the \'/\' char");
		}
	}

	private void validateLength(String innerActionSeparatorParam) throws ServletException {

		if (innerActionSeparatorParam.length() != 1) {

			throw new ServletException("The "
					+ INNER_ACTION_SEPARATOR_PARAM
					+ " context parameter must have only one char");
		}
	}

	static final String EXTENSION = "." + ApplicationManager.EXTENSION;

	private boolean isPrettyURL(HttpServletRequest req) {

		String uri = req.getRequestURI().toString();

		// cut the last '/'
		if (uri.endsWith("/") && uri.length() > 1) {

			uri = uri.substring(0, uri.length() - 1);
		}

		// ends with .mtw or have a "." is not pretty URL
		if (uri.endsWith(EXTENSION) || uri.indexOf(".")>0) return false;

		return true;
	}

	private String getActionPlusInnerAction(HttpServletRequest req) {

		String context = req.getContextPath();

		String uri = req.getRequestURI().toString();

		// remove the context from the uri, if present

		if (context.length() > 0 && uri.indexOf(context) == 0) {

			uri = uri.substring(context.length());

		}

		// cut the first '/'
		if (uri.startsWith("/") && uri.length() > 1) {

			uri = uri.substring(1);

		}

		// cut the last '/'
		if (uri.endsWith("/") && uri.length() > 1) {

			uri = uri.substring(0, uri.length() - 1);
		}

		String[] s = uri.split("/");

		if (s.length >= 2) {

			return s[1];
		}

		return null;
	}

	@Override
	protected String getActionName(HttpServletRequest req) {

		if (!isPrettyURL(req)) {

			return super.getActionName(req);
		}

		String s = getActionPlusInnerAction(req);

		// separate the inner action from action...

		int index = s.indexOf(innerActionSeparator);

		if (index > 0) {

			return s.substring(0, index);
		}

		return s;
	}

	@Override
	protected String getInnerActionName(HttpServletRequest req) {

		if (!isPrettyURL(req)) {

			return super.getInnerActionName(req);
		}

		String s = getActionPlusInnerAction(req);

		// separate the inner action from action...

		int index = s.indexOf(innerActionSeparator);

		if (index > 0 && index + 1 < s.length()) {

			return s.substring(index + 1);
		}

		return null;
	}

	@Override
	protected void prepareAction(Action action, HttpServletRequest req,
			HttpServletResponse res) {

		if (!isPrettyURL(req)) {

			super.prepareAction(action, req, res);

			return;
		}

		action.setInput(new PrettyURLRequestInput(req, res));
		action.setOutput(new ResponseOutput(res));
		action.setSession(new SessionContext(req, res));
		action.setApplication(appContext);
		action.setCookies(new CookieContext(req, res));
		action.setLocale(LocaleManager.decideLocale(req, res));
	}
}
