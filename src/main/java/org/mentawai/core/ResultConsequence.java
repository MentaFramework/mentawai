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
package org.mentawai.core;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Returns the result of the action as a String.
 * 
 * @author Sergio Oliveira Jr.
 */
public class ResultConsequence implements Consequence {
    
	public ResultConsequence() {
	    
		
    }
    
	public void execute(Action a, String result, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException {
		
		res.setContentType("text/plain");

		PrintWriter out = null;
		
		try {
			
			out = res.getWriter();
			
		} catch (IOException e) {
			
			throw new ConsequenceException(e);
			
		}
        
        out.print(result);
        
        out.close();
		
    }
}