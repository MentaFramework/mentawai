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
package org.mentawai.ajax;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mentawai.ajax.renderer.ResultRenderer;
import org.mentawai.core.Action;
import org.mentawai.core.Consequence;
import org.mentawai.core.ConsequenceException;
import org.mentawai.core.Output;
import org.mentawai.util.DebugServletFilter;
import org.mentawai.util.HttpUtils;

public class AjaxConsequence implements Consequence {

	public static String OBJECT = "ajax_object";

	public static String DEFAULT_CHARSET = "UTF-8";

	private String keyForObject = OBJECT;
	
	private final AjaxRenderer ajaxRenderer;

	private boolean pretty;

	private boolean disableCache;

	public AjaxConsequence(AjaxRenderer renderer) {
		this(renderer, true);
	}

	public AjaxConsequence(AjaxRenderer renderer, boolean pretty) {
		this(renderer, pretty, true);
	}
	
	public AjaxConsequence(AjaxRenderer renderer, boolean pretty, boolean disableCache) {
		this.ajaxRenderer = renderer;
		this.pretty = pretty;
		this.disableCache = disableCache;
	}
	
	public AjaxConsequence keyForObject(String key) {
		
		this.keyForObject = key;
		
		return this;
	}
	
	public AjaxConsequence source(String key) {
		
		return keyForObject(key);
	}

	/**
	 * Prints in the request's output the ajax response. The ajax response is supplied by the AjaxRenderer.
	 */
	public void execute(Action a, String result, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException {

		Output output = a.getOutput();
		
		Object obj = null;
		
		if (keyForObject == null || !output.has(keyForObject)) {
			
			if (ajaxRenderer.getClass().equals(ResultRenderer.class)) {
				
				// render the result...
				
				obj = result;
				
			} else {

				// remove debug... For ajax it does not make sense anyways to have debug mode...

				output.removeValue(DebugServletFilter.DEBUG_KEY);
				
				// render the whole output !!!
			
				obj = output;
			}

		} else {

			obj = a.getOutput().getValue(keyForObject);
		}

		try {

			StringBuilder sb = new StringBuilder(64);

			sb.append(ajaxRenderer.getContentType()).append("; charset=").append(ajaxRenderer.getCharset());

			res.setContentType(sb.toString());

			String s = ajaxRenderer.encode(obj, a.getLocale(), pretty);

			if(disableCache){
				HttpUtils.disableCache(res);
			}
			
			PrintWriter printWriter = res.getWriter();
			printWriter.print(s);
			printWriter.flush();

		} catch (IOException e) {
			throw new ConsequenceException("Exception while writing the renderized document: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new ConsequenceException("Exception while renderizing with render " + ajaxRenderer.getClass() + ": " + e.getMessage(), e);
		}

	}
}