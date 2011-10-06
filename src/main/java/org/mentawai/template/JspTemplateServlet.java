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
package org.mentawai.template;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Heranca de TemplateServlet para trabalhar com JSP.
 * 
 * @author Davi Luan Carneiro
 */
public class JspTemplateServlet extends TemplateServlet {
	
	/**
	 * Seta o page na request. Faz isso atraves do metodo request.setAttribute
	 */
	protected void putPageInResponse(Page page, HttpServletRequest request, HttpServletResponse response, ServletContext application) throws Exception {
		request.setAttribute(TemplateServlet.PAGE_ATTR, page);		
	}

	/**
	 * Exibe a pagina no brownser do usuario, usando um dispatcher
	 */
	protected void showPage(Page page, HttpServletRequest request, HttpServletResponse response, ServletContext application) throws Exception {
		application.getRequestDispatcher("/" + page.getView()).forward(request, response);
	}
}
