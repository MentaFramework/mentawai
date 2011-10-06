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
package org.mentawai.rule;

import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.Input;

/**
 * @author Helio Frota(heliofrota@gmail.com)
 */
public class DejavuRule implements Rule {
    
	public DejavuRule() { }
	
	public boolean check(String field, Action action) {
        if(field == null){
        	return true;
        }
        else {
        	Input input = action.getInput();
        	if(input.getValue(field) == null){
        		return true;
        	}
        	String dejavuValue = input.getValue(field).toString();
        	if (action.getSession().getAttribute("dejavu") == null){
				action.getSession().setAttribute("dejavu", dejavuValue);
        	}
			else{
				if (action.getSession().getAttribute("dejavu").equals(dejavuValue)){
					return false;
				}
				else{
					action.getSession().setAttribute("dejavu", dejavuValue);
				}
			}
		}
		return true;
    }
	
	public Map<String, String> getTokens() {
		return null;
	}
	
}