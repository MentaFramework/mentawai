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
package org.mentawai.message;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.Context;
import org.mentawai.core.Output;
import org.mentawai.filter.FlashScopeFilter;

/**
 * @author Sergio Oliveira
 */
public class MessageManager {
    
    public static String ERRORS = "_errors";
    public static String MESSAGES = "_messages";
    public static String FIELDERRORS = "_fieldErrors";
    
    public static List<Message> getErrors(Action action, boolean create) {
        if (action == null) return null;
        Output output = action.getOutput();
        List<Message> errors = (List<Message>) output.getValue(ERRORS);
        if (errors == null && create) {
            errors = new LinkedList<Message>();
            output.setValue(ERRORS, errors);
        }
        return errors;
    }
    
    public static Map<String, Message> getFieldErrors(Action action, boolean create) {
        if (action == null) return null;
        Output output = action.getOutput();
        Map<String, Message> fieldErrors = (Map<String, Message>) output.getValue(FIELDERRORS);
        if (fieldErrors == null && create) {
            fieldErrors = new LinkedHashMap<String, Message>();
            output.setValue(FIELDERRORS, fieldErrors);
        }
        return fieldErrors;
    }
    
    public static List<Message> getMessages(Action action, boolean create, boolean flash) {
        if (action == null) return null;
        Output output = action.getOutput();
        List<Message> messages = (List<Message>) output.getValue(MESSAGES);
        if (messages == null && create) {
            messages = new LinkedList<Message>();
            output.setValue(MESSAGES, messages);
            
            if (flash) {
            	
            	Context session = action.getSession();
            	
            	session.setAttribute(FlashScopeFilter.KEY + "." + MESSAGES, messages);
            }
        }
        return messages;
    }
    
    public static List<Message> getMessages(Action action, boolean create) {
    	
    	return getMessages(action, create, false);
    }
}
            
    
    
	