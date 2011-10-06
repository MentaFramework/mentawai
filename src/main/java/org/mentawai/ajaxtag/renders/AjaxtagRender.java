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

import static org.mentawai.ajaxtag.AjaxtagConstraints.FORM;
import static org.mentawai.ajaxtag.AjaxtagConstraints.HTML;
import static org.mentawai.ajaxtag.AjaxtagConstraints.LISTDATA;
import static org.mentawai.ajaxtag.AjaxtagConstraints.STRING;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.mentawai.ajax.AjaxRenderer;
import org.mentawai.ajaxtag.AjaxtagConstraints;
import org.mentawai.ajaxtag.responses.BaseAjaxtagResponse;
import org.mentawai.util.InjectionUtils;

/**
 * <p>
 * Base class for the all Ajaxtags render. This class is responseble to create a
 * comom access point to all renders.
 * </p>
 * <p>
 * That class also provides the instances of the common renders by the static
 * method getRender
 * </p>
 * 
 * @author Marvin Froeder (m@rvin.info)
 * @since 1.9
 */
public abstract class AjaxtagRender {
	/**
	 * Content-type for text/html.
	 */
	public static final String TEXT_HTML = AjaxRenderer.TEXT_HTML;

	/**
	 * Content-type for text/xml.
	 */
	public static final String TEXT_XML = AjaxRenderer.TEXT_XML;

	private static final Map<AjaxtagConstraints, AjaxtagRender> renders;
	static {
		HashMap<AjaxtagConstraints, AjaxtagRender> map = new HashMap<AjaxtagConstraints, AjaxtagRender>();
		map.put(HTML, new HtmlContentAjaxtagRender());
		map.put(STRING, new CalloutAjaxtagRender());
		map.put(FORM, new FormAjaxtagRender());
		map.put(LISTDATA, new ListDataAjaxtagRender());
		renders = Collections.unmodifiableMap(map);
	}

	public abstract String renderize(BaseAjaxtagResponse response,
			Locale locale) throws Exception;

	public String getContentType() {
		return TEXT_XML;
	}

	public String getProperty(Object bean, String nameProperty)
			throws Exception {
		if (bean == null)
			return "null";
		String value = InjectionUtils.getProperty(bean, nameProperty);
		if (value == null)
			return bean.toString();
		return value;
	}

	public static AjaxtagRender getRender(AjaxtagConstraints constraint) {
		return renders.get(constraint);
	}
}
