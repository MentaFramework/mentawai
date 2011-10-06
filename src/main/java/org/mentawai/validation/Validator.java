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
package org.mentawai.validation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.Input;
import org.mentawai.message.DefaultMessage;
import org.mentawai.message.Message;
import org.mentawai.message.MessageContext;
import org.mentawai.message.MessageManager;
import org.mentawai.rule.IntegerRule;
import org.mentawai.rule.RequiredRule;
import org.mentawai.rule.Rule;

/**
 * @author Sergio Oliveira
 */
public class Validator {
    
    public static String FIELD_TOKEN = "field";
	
	private Map<String, Object> rules;
	private Map<String, Object> errors;
   private Map<String, Object> params;
	
	public Validator(Map<String, Object> rules, Map<String, Object> errors) {
		this.rules = rules;
		this.errors = errors;
	}
   
   public Validator(Map<String, Object> rules, Map<String, Object> errors, Map<String, Object> params) {
      this(rules, errors);
      this.params = params;
   }
   
	
	private String getErrorId(String field, Rule rule) {
		Map map = (Map) errors.get(field);
		String id = (String) map.get(rule);
		return id;
	}
   
   private String[] getParams(String field, Rule rule) {
      
      if (params == null) return null;
      
      Map<Rule, String[]> map = (Map<Rule, String[]>) params.get(field);
      
      if (map != null) {
         
         return map.get(rule);
      }
      
      return null;
   }
    
    protected Map<String, String> getTokens(Action action, Map<String, String> tokens, String field) {
        
        Map<String, String> map = new HashMap<String, String>();
        
        if (tokens != null) {
        
            Iterator<String> iter = tokens.keySet().iterator();
            
            while(iter.hasNext()) {
                
                String key = iter.next();
                
                String value = tokens.get(key);
                
                map.put(key, value);
                
            }
        }
        
        Input input = action.getInput();
        
        Iterator<String> iter = input.keys();
        
        while(iter.hasNext()) {
            
            String key = iter.next();
            
            String value = input.getString(key);
            
            map.put(key, value);
        }
        
        String value = input.getString(field);
        
        if (value != null) {
        
            map.put(FIELD_TOKEN, value);
            
        }
        
        return map;
        
        
    }
	
	public boolean validate(Action action, MessageContext msgContext) {

        boolean isOk = true;
		Iterator<String> iter = rules.keySet().iterator();
		while(iter.hasNext()) {
			String field = iter.next();
			List list = (List) rules.get(field);
			Iterator iter2 = list.iterator();
			while(iter2.hasNext()) {
				Rule rule = (Rule) iter2.next();
				String error_id = getErrorId(field, rule);
                if (!rule.check(field, action)) {
                   
                   isOk = false;
                   
                   Map<String, Message> map = MessageManager.getFieldErrors(action, true);
                    
                   Map<String, String> tokens = getTokens(action, rule.getTokens(), field);
                   
                   String[] params = getParams(field, rule);
                   
                   map.put(field, new DefaultMessage(error_id, msgContext, tokens, params));
                   
                   break;
				}
			}
		}
		return isOk;
	}
    
	public void add(String field, Rule rule, String error_id) {
		// add the rule...
		List<Rule> list = (List<Rule>) rules.get(field);
		if (list == null) {
			list = new LinkedList<Rule>();
			rules.put(field ,list);
		}
		list.add(rule);
		
		// add the error id...
		Map<Rule, String> map = (Map<Rule, String>) errors.get(field);
		if (map == null) {
			map = new HashMap<Rule, String>();
			errors.put(field, map);
		}
		map.put(rule, error_id);
	}
   
   public void requiredFields(String error_id, String ... fields) {
      
      if (fields == null || fields.length == 0) return;
      
      for(int i=0;i<fields.length;i++) {
         
         add(fields[i], RequiredRule.getInstance(), error_id);
      }
   }
   
   public void requiredLists(String error_id, String ... fields) {
      
      if (fields == null || fields.length == 0) return;
      
      for(int i=0;i<fields.length;i++) {
         
         add(fields[i], IntegerRule.getInstance(1), error_id);
      }
   }
   
   public void add(String field, Rule rule, String error_id, String ... params) {
      
      add(field, rule, error_id);
      
      if (params == null || params.length == 0) return;
      
      if (this.params == null) this.params = new HashMap<String, Object>();
      
      // add the params...
      Map<Rule, String[]> map = (Map<Rule, String[]>) this.params.get(field);
      
      if (map == null) {
         
         map = new HashMap<Rule, String[]>();
         
         this.params.put(field, map);
      }
      
      map.put(rule, params);
      
   }
    
    public void add(String field, Rule rule, int error_id) {
        add(field, rule, String.valueOf(error_id));
    }
    
    public void add(String field, Rule rule, int error_id, String ... params) {
       add(field, rule, String.valueOf(error_id), params);
   }
}
	