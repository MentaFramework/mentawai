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

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mentawai.list.ListData;
import org.mentawai.list.ListManager;

/**
 * @author Sergio Oliveira
 */
public abstract class AbstractMessage implements Message {

	private static final String TOKEN_MARKER = "%";
   
   private static final String LIST_MARKER = "@";
   
   private static final String I18N_MARKER = "!";
   
   private static final String REGEX = "\\" + LIST_MARKER + "([^\\" + LIST_MARKER + "]+)\\" + LIST_MARKER;
   
   private static final String PARAM_REGEX = "\\{([0-9]+)\\}";
   
   private static final String I18N_REGEX = "\\" + I18N_MARKER + "([^\\" + I18N_MARKER + "]+)\\" + I18N_MARKER;
   
   private static final Pattern PATTERN = Pattern.compile(REGEX);
   
   private static final Pattern PARAM_PATTERN = Pattern.compile(PARAM_REGEX);
   
   private static final Pattern I18N_PATTERN = Pattern.compile(I18N_REGEX);
	
   protected String id;
   protected MessageContext context;
	protected Map<String, String> tokens = null;
	protected String tokenMarker = TOKEN_MARKER;
   protected String[] params = null;
    
    public AbstractMessage(String id, MessageContext context) {
        this.id = id;
        this.context = context;
    }
	
	public AbstractMessage(String id, MessageContext context, Map<String, String> tokens) {
		this(id, context);
		this.tokens = tokens;
	}
   
   public AbstractMessage(String id, MessageContext context, Map<String, String> tokens, String[] params) {
      this(id, context, tokens);
      this.params = params;
   }
   
    
    public AbstractMessage(int id, MessageContext context) {
        this.id = String.valueOf(id);
        this.context = context;
    }
	
	public AbstractMessage(int id, MessageContext context, Map<String, String> tokens) {
		this(id, context);
		this.tokens = tokens;
	}
   
   public AbstractMessage(int id, MessageContext context, Map<String, String> tokens, String[] params) {
      this(id, context, tokens);
      this.params = params;
   }

    
    public String getId() { return id; } 
    public Map<String, String> getTokens() {return tokens;}
    public MessageContext getContext() { return context; }
    public String[] getParams() {return params;}
    
	public void setTokenMarker(String tokenMarker) {
		this.tokenMarker = tokenMarker;
	}
    
    public void  setTokens(Map<String, String> tokens){
		this.tokens = tokens;
    }
    
    public String getText(Locale loc) {
       
		String s = context.getMessage(id, loc);
      
      if (s == null) return null;
      
      // first look for dyn parameters...
      
      if (params != null && params.length > 0) {
      
         Matcher m = PARAM_PATTERN.matcher(s);
         
         while(m.find()) {
            
            String index = m.group(1);
            
            int x = -1;
            
            try { x = Integer.parseInt(index); } catch(Exception e) { }
            
            if (x >= 0 && x < params.length) {
               
               StringBuilder sb = new StringBuilder(16);
               
               sb.append("\\{").append(x).append("\\}");
               
               String regex = sb.toString();
               
               s = s.replaceFirst(regex, params[x]);
               
            }
         }
      }
      
      // second look for @listname.item@...
      
      Matcher m = PATTERN.matcher(s);
      
      while(m.find()) {
         
         String token = m.group(1);
         
         StringTokenizer st = new StringTokenizer(token, ".");
         
         if (st.countTokens() != 2) continue;
         
         String listname = st.nextToken();
         
         String item = st.nextToken();
         
         ListData list = ListManager.getList(listname);
         
         if (list == null) continue;
         
         String value = list.getValue(item, loc);
         
         if (value == null) continue;
         
         StringBuilder sb = new StringBuilder(16);
         
         sb.append("\\").append(LIST_MARKER).append(listname).append("\\.").append(item).append("\\").append(LIST_MARKER);
         
         String regex = sb.toString();
         
         s = s.replaceFirst(regex, value);
      }
      
      // third look for !i18nkey!...
      
      m = I18N_PATTERN.matcher(s);
      
      while(m.find()) {
         
         String token = m.group(1);
         
         String value = context.getMessage(token, loc);
         
         if (value == null || value.equals(token)) continue;
         
         StringBuilder sb = new StringBuilder(16);
         
         sb.append("\\").append(I18N_MARKER).append(token).append("\\").append(I18N_MARKER);
         
         String regex = sb.toString();
         
         s = s.replaceFirst(regex, value);
      }

      
		if (tokens != null && !tokens.isEmpty()) {
         
			StringBuffer sb = new StringBuffer(s);
         
			Iterator<String> iter = tokens.keySet().iterator();
         
			while(iter.hasNext()) {
            
				String tokenKey = iter.next();
            
				String token = tokens.get(tokenKey);
            
				StringBuffer t = new StringBuffer(tokenKey.length() + 2);
				t.append(tokenMarker).append(tokenKey).append(tokenMarker);
            
				String marker = t.toString();
            
				int index = sb.indexOf(marker);
            
				if(index >= 0) {
               
					sb.replace(index, index + marker.length(), token);
               
				} else {
               
					continue;
				}
			}
         
			return sb.toString();
		}
		return s;
    }
    
    public String toString() {
    	return id;
    }
}

        
    