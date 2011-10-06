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
package org.mentawai.tag.paginator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.mentawai.tag.util.PrintTag;

public class ResultFrom extends PrintTag {
	
	public String getStringToPrint() throws JspException {
		
        Tag parent = findAncestorWithClass(this, PaginatorTag.class);
        
        if (parent != null) {
            
            PaginatorTag paginator = (PaginatorTag) parent;
            
            return String.valueOf(paginator.getFrom());
            
        } else {
            
            throw new JspException("ResultFrom not enclosed by a PaginatorTag !!!");
            
        }
	}
}