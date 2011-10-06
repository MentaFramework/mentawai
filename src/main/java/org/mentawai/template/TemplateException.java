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

/**
 * Exception para o EasyTemplates. Eh uma excecao unchecked, isto e,
 * estende de RuntimeException. Isto porque nenhuma exception sera tratada:
 * todas elas serao erros de configuracao ou de execucao, devendo parar o
 * processo.
 *
 * @author Davi Luan Carneiro
 */
public class TemplateException extends org.mentawai.util.RuntimeException {

	public TemplateException(String message) {
		super(message);
	}

	public TemplateException(Throwable e) {
		super(e);
	}
}
