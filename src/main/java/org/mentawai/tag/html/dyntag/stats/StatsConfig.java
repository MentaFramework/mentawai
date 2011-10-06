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
package org.mentawai.tag.html.dyntag.stats;

import javax.servlet.jsp.JspException;

import org.mentawai.core.Controller;
import org.mentawai.tag.html.dyntag.BaseConfig;
import org.mentawai.tag.html.dyntag.stats.listener.StatsListener;

public class StatsConfig extends BaseConfig {

	private static final long serialVersionUID = 1L;

	/** Method to build tag */
	protected StringBuffer buildTag() {
		StringBuffer results = new StringBuffer();

		if (StatsListener.LIST_PATH_FILES == null) {

			StatsListener listener = new StatsListener();

			listener.contextInitialized(Controller.getApplication());

		}

		results.append(buldImportJsFile(StatsListener.LIST_PATH_FILES).toString());
		results.append(buldImportCssFile(StatsListener.LIST_PATH_FILES).toString());
		return results;
	}

	public String getStringToPrint() throws JspException {
		StringBuffer results = new StringBuffer(200);
		results.append(buildTag().toString());
		return results.toString();
	}

}
