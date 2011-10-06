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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Esta classe representa uma pagina. Seguindo o paradigma de blocos do
 * mentatemplates, uma pagina pode ser composta por varios blocos, os quais
 * tambem sao paginas. Uma pagina tambem pode derivar de outra, revelando assim
 * um conceito de heranca entre paginas. <br>
 * Alem disso, o atributo path pode ser um expressao regular. Isso traz grandes
 * possibilidades, e usaremos este mecanismo para dar suporte a convencoes.
 *
 * @author Davi Luan Carneiro
 */

public class Page {

	private String path, view;
	private Map<String, Object> blocks = new HashMap<String, Object>();
	private Class listener;

   /**
    * Atributo adicionado por Leandro Santana Pereira
   **/
	private Pattern pattern;


	/**
	 * Constroi um Page apenas usando a pagina jsp. Geralmente usado para os
	 * pages base, ou para pages que sao blocos de outros.
	 *
	 * @param view
	 *            Pagina jsp
	 */
	public Page(String view) {
		this.view = view;
	}

	/**
	 * @param path
	 *            O path que devera ser usado para o acesso a esse page
	 * @param view
	 *            Pagina jsp
	 */
	public Page(String path, String view) {
		this(view);
		this.path = path;
	}

	/**
	 * Utiliza o conceito de heranca, para construir um novo page. So nao herda
	 * o path, que e exclusivo de cada page.
	 * Atencao: alteracoes em tempo de execucao de classes pai nao irao alterar
	 * as classes filhas. A heranca tem o fim de facilitar a configuracao, eliminando
	 * a necessidade de codificacoes repetitivas.
     * Alterado por Leandro Santana Pereira
     * M�todo modificado na ultima linha, para corre��o de bug de um ou mais templates utilizando expressao regular.
	 *
	 * @param pageSuper
	 *        		Page pai
	 */


    public Page(Page pageSuper) {
        this(pageSuper.getView());
        blocks.putAll(pageSuper.getBlocks());
        this.listener = pageSuper.getListener();
        this.pattern = pageSuper.getPattern();
    }



	/**
	 * Muito usado para pages que herdam de um page base.
     * Alterado por Leandro Santana Pereira
     * M�todo modificado na ultima linha, para corre��o de bug de um ou mais templates utilizando expressao regular.
	 *
	 * @param path
	 *            O path que devera ser usado para o acesso a esse page
	 * @param pageSuper
	 *            Page pai
	 */
	public Page(String path, Page pageSuper) {
		this(pageSuper);
		this.path = path;
	}

	/**
	 * @param view
	 *            Pagina jsp
	 * @param listener
	 *            Classe do listener
	 */
	public Page(String view, Class listener) {
        this(view);
        this.listener = listener;
        // this.pattern = Pattern.compile(path); // Comentado, pois estava dando NullPointer (Ricardo Rufino)
    }


	/**
     * Alterado por Leandro Santana Pereira
     * M�todo modificado na ultima linha, para corre��o de bug de um ou mais templates utilizando expressao regular.
	 *
	 * @param path
	 *            O path que devera ser usado para o acesso a esse page
	 * @param view
	 *            Pagina jsp
	 * @param listener
	 *            Classe do listener
	 */
	public Page(String path, String view, Class listener) {
        this(path, view);
        this.listener = listener;
        this.pattern = Pattern.compile(path);
    }


	/**
	 * @param pageSuper
	 *            Page pai
	 * @param listener
	 *            Classe do listener
	 */
	public Page(Page pageSuper, Class listener) {
		this(pageSuper);
		this.listener = listener;
	}

	/**
     * Alterado por Leandro Santana Pereira
     * M�todo modificado na ultima linha, para corre��o de bug de um ou mais templates utilizando expressao regular.
	 *
	 * @param path
	 *            O path que devera ser usado para o acesso a esse page
	 * @param pageSuper
	 *            Page pai
	 * @param listener
	 *            Classe do listener
	 */
    public Page(String path, Page pageSuper, Class listener) {
        this(path, pageSuper);
        this.listener = listener;
        this.pattern = pageSuper.getPattern();
    }



	/**
	 * @return Todos os blocos desse page
	 */
	public Map<String, Object> getBlocks() {
		return blocks;
	}

	/**
	 * @param id
	 * @return O bloco desejado (que e uma instancia de Page)
	 */
	public Page getBlock(String id) {
		return (Page) blocks.get(id);
	}

	/**
	 * Definir um bloco de template para a página especificada.
	 * @param id
	 * @param page
	 *            Page que sera setado para o bloco especificado
	 */
	public void setBlock(String id, Page page) {
		blocks.put(id, page);
	}
	
	/**
	 * Definir um bloco de template para a página especificada.
	 * @param id
	 * @param view - pagina que será setada para o bloco especificado (internamente faz um new Page(view))
	 */
	public void setBlock(String id, String view) {
		setBlock(id, new Page(view));
	}

	/**
	 * @param id
	 * @return String que representa o StringBlock
	 */
	public String getStringBlock(String id) {
		return blocks.get(id) + "";
	}

	/**
	 * @param id
	 * @param stringBlock
	 *            StringBlock a ser setado
	 */
	public void setStringBlock(String id, String stringBlock) {
		blocks.put(id, stringBlock);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getView() {
		return view;
	}

	public Class getListener() {
		return listener;
	}

	public void setListener(Class listener) {
		this.listener = listener;
	}

	/**
	 * Metodo adicionado por Leandro Santana Pereira
     * retorna o padrao regex ja copilado da page
	**/
    public Pattern getPattern(){
        return this.pattern;
    }

	/**
	  * Metodo adicionado por Leandro Santana Pereira
	  * Verifica se o padr�o da p�gina (pattern) casa com o path passado como entrada.
	  * Se o pattern for nulo, retorno � false
	**/
    public boolean patternMatches(String path) {
        return (pattern == null) ? false : this.pattern.matcher(path).matches();
    }



}
