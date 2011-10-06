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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe abstrata, a qual deve ser estendida e implementado o metodo
 * configurePages. Possibilita configuracao programaticamente, porem tambem e
 * possivel extende-la para que leia as configuracoes de um arquivo xml,
 * properties, banco de dados, ou qualquer coisa.
 *
 * @author Davi Luan Carneiro
 */
public abstract class TemplateManager {

	private Map<String, Page> pages = new HashMap<String, Page>();

	/**
	 * Metodo a ser implementado, onde as configuracoes devem ser feitas
	 */
	public abstract void configurePages();

	/**
	 * Adiciona um novo page ao TemplateManager. Pode lancar duas
	 * TemplateExceptions, caso o page nao possua path, ou ja exista um page com
	 * o mesmo path.
	 *
	 * @param page
	 *            Page a ser adicionado
	 */
	public void add(Page page) {
		if (page.getPath() == null) {
			throw new TemplateException("Can't add a page without path");
		}

		String path = cutSlash(page.getPath());

		if (findPageForPath(path, false) != null) {

			throw new TemplateException("Already exists a page with the path " + page.getPath());

		}
		pages.put(path, page);
	}

	public Collection<Page> getPages() {
		return pages.values();
	}

	/**
	 * Primeiro procura pela String exata. Se nao encontrar, considera todos os paths como expressoes
	 * regulares e procura novamente.
	 *
	 * @param path
	 *            Path a ser procurado
	 * @return Page que possui o path informado
	 */
	public Page getPageForPath(String path) {

		path = cutSlash(path);

		Page page = findPageForPath(path, false);

		if (page == null) {

			page = findPageForPath(path, true);

		}

		return page;
	}

	/**
	* Alterado por Leandro Santana Pereira
 	* Metodo alterado de Iterator por foreach para obter-se ganho de desempenho e a
 	* substituicao do metodo path.matches(page.getPath() pelo metodo page.patternMatches(path).
 	**/
     private Page findPageForPath(String path, boolean regexp) {
         if (!regexp) {
             return pages.get(path);
         } else {
             for (Page page : pages.values()) {
                 if (page.patternMatches(path)) {
                     return page;
                 }
             }
             return null;
         }
     }


	/**
	 * Limpa a lista de pages e chama novamente o configurePages
	 */
	public void reconfigurePages() {
		pages.clear();
		configurePages();
	}

    private static String cutSlash(String name) {
        if (name.startsWith("/") && name.length() > 1) {
            return name.substring(1, name.length());
        }
        return name;
    }


}
