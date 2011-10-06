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
package org.mentawai.tag.message;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author Sergio Oliveira
 */
public class HasError extends BodyTagSupport {
    
    private boolean hide = false;
    
    public void setHide(boolean hide) {
        this.hide = hide;
    }
    
    public int doAfterBody() throws JspException {
        BodyContent bc = getBodyContent();
        
        if (hide) return SKIP_BODY;
        
        try {
             if (bc != null) bc.writeOut(bc.getEnclosingWriter());
        } catch(IOException e) {
            throw new JspException(e);
        } finally {
            if (bc != null) bc.clearBody();
        }
        
        return SKIP_BODY;
    }
    
	/*
    public int doEndTag() throws JspException {
        hide = false;
        return EVAL_PAGE;
    }
	*/
}
