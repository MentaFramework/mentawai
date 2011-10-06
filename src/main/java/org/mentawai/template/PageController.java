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
 * Interface que permite um maior controle sobre os pages. Cada page pode ter o
 * seu PageController, os quais tem acesso ao page, request, response e application. O
 * TemplateManager pode ser obtido tambem, bastando chamar o metodo estatico getTemplateManager
 * de TemplateServlet.
 * Em caso de regexp, o atributo path do page sera o valor da URL, e nao a expressao regular
 * na qual a URL se enquadrou.
 *
 * @author Davi Luan Carneiro
 */
public interface PageController {

	/**
	 * Realiza alguma tarefa ANTES da renderizacao do page
	 */
	public void processPage(Page page, HttpServletRequest request,
			HttpServletResponse response, ServletContext application);

}
