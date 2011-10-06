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
package org.mentawai.tag;

import java.security.MessageDigest;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Helio Frota (heliofrota@gmail.com)
 */
public class Dejavu extends TagSupport {
    
	private static final long serialVersionUID = 1L;
	
	private String sessionId;

    public int doStartTag() throws JspException {
        try{
            DejavuCript dc = new DejavuCript(pageContext.getSession().getId());
            pageContext.getOut().print("<input type='hidden' name='dejavu' " +
        			"value='"+ dc +"'>");
        }
        catch(Exception e){
            throw new JspException(e);
        }
        return SKIP_BODY;
    }

    public String getSessionId(){
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    class DejavuCript {
    	
    	private String dejavu;
    	
    	public DejavuCript(String session_id) throws Exception {
    		long systime = System.currentTimeMillis();
            byte[] time = new Long(systime).toString().getBytes();
            byte[] id = session_id.getBytes();
            try{
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(id);
                md.update(time);
                dejavu = toHex(md.digest());
            }
            catch(Exception e){
                throw new Exception(e);
            }
    	}
    	
    	private String toHex(byte[] bytes)  {
            StringBuffer sb = new StringBuffer();
            for(int i = 0;i < bytes.length;i++){
                sb.append(Integer.toHexString(bytes[i] & 0x00ff));
            }    
            return sb.toString();
        }
    	
    	public String toString() {
            return dejavu;
        }

    	public String getDejavu() {
    		return dejavu;
    	}

    	public void setDejavu(String dejavu) {
    		this.dejavu = dejavu;
    	}

    }
    
}