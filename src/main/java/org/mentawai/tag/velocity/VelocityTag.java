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
package org.mentawai.tag.velocity;

import java.io.Reader;
import java.util.Properties;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

/**
 * @author Sergio Oliveira
 */
public class VelocityTag extends BodyTagSupport {
    
    private static VelocityEngine ve = null;

    public VelocityTag() {
        if (ve == null) {
            try {
                ve = new VelocityEngine();
                Properties p = new Properties();
                p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
                ve.init(p);
            } catch(Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public int doEndTag() throws JspException {
        
        BodyContent bc = getBodyContent();
        if (bc == null) return EVAL_PAGE;

        try {
            JspWriter writer = pageContext.getOut();
            Reader reader = bc.getReader();
            Context ctx = new JSPContext(pageContext);
            ve.evaluate(ctx, writer, "VelocityTag", reader);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JspException(e);
        }

        return EVAL_PAGE;
    }
}
