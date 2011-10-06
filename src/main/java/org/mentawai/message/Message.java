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

import java.util.Locale;
import java.util.Map;

/**
 * Describes the behavior of a text message that can be displayed in web page.
 * A messsage has an ID and a message context from where to get the message as text.
 * Messages are localized and can have tokens.
 *
 * @author Sergio Oliveira
 */
public interface Message {

    /**
     * Gets the unique ID of this message.
     * A message context cannot have two messages with the same id.
     *
     * @return The unique id of this message.
     */
    public String getId();
    
    /**
     * Gets the message context for this message.
     *
     * @return The message context for this message.
     */
    public MessageContext getContext(); 
    
    /**
     * Gets the message text this message object represents.
     *
     * @param loc The locale of the message.
     * @return The message text.
     */
    public String getText(Locale loc);
    
    /**
     * Sets tokens or placeholders for this message.
     * A message can have tokens.
     * 
     * @param tokens The tokens for this message.
     */
    public void setTokens(Map<String, String> tokens);

    /**
     * Gets tokens or placeholders for this message.
     */
    public Map<String, String> getTokens();
  
    
    /**
     * Gets params for this message.
     */
    public String[] getParams();
    
    
}