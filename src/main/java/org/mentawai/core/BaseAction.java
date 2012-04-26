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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.mentawai.action.BaseLoginAction;
import org.mentawai.ajax.AjaxConsequence;
import org.mentawai.filter.TransactionFilter;
import org.mentawai.i18n.LocaleManager;
import org.mentawai.message.ClassMessageContext;
import org.mentawai.message.DefaultMessage;
import org.mentawai.message.FileMessageContext;
import org.mentawai.message.Message;
import org.mentawai.message.MessageContext;
import org.mentawai.message.MessageManager;
import org.mentawai.transaction.Transaction;
import org.mentawai.validation.Validator;

/**
 * The abstract base implementation of a Mentawai action.
 * It also privides access to input, output, session context and application context through protected data members.
 * 
 * @author Sergio Oliveira
 */
public abstract class BaseAction implements StickyAction {
    
    private static MessageContext FIXED_MSG_CONTEXT = null;
    
    protected MessageContext msgContext;
    
    protected Input input;
	protected Output output;
	protected Context session;
	protected Context application;
    protected Context cookies;
	protected Locale loc;
	
    /**
     * Creates a BaseAction.
     */
	public BaseAction() {
		
        if (LocaleManager.isUseMasterForEverything()) {
        	
        	// the master i18n file will be used for all actions...
        	
            msgContext = new FileMessageContext(LocaleManager.getMaster(), "");
            
            if (LocaleManager.isUsePrefixForActions()) {
            	
            	FileMessageContext fmc = (FileMessageContext) msgContext;
            	
            	String prefix = getClass().getSimpleName();
            	
            	fmc.setPrefix(prefix);
            }
            
        } else {
        	
            if (FIXED_MSG_CONTEXT != null) {
            	
            	// the static method setMessageContext was called to
            	// set the same message context for all actions...
            	
                msgContext = FIXED_MSG_CONTEXT;
                
            } else {
            	
            	// default is the action name...
            	
                msgContext = new ClassMessageContext(this.getClass());
                
            }
        }
    }
	
	public static void init(Action action) {
		
        action.setInput(new MapInput());
        action.setOutput(new MapOutput());
        action.setSession(new MapContext());
        
        Context application = ApplicationManager.getApplication();
        
        if (application != null) {
        	
        	action.setApplication(application);
        	
        } else {
        
        	action.setApplication(new MapContext());
        }
        
        action.setCookies(new MapContext());
        action.setLocale(Locale.getDefault());
		
	}
	
	public void init() {
		
		init(this);
	}
   
   public boolean isEmpty(String s) {
      
      if (s == null || s.trim().length() == 0) {
         
         return true;
      }
      
      return false;
   }
   
   public void commit() throws Exception {
	   
	   commit(TransactionFilter.TRANSACTION_KEY);
   }
   
   public void commit(String trans) throws Exception {

	   if (input.hasValue(trans)) { // check first not to trigger IoC
		   
		   Object obj = input.getValue(trans);
		   
		   if (obj instanceof Transaction) {
			   
			   Transaction t = (Transaction) obj;
			   
			   t.commit();
		   }
	   }
   }
   
   public void rollback() throws Exception {
	   
	   rollback(TransactionFilter.TRANSACTION_KEY);
   }
   
   public void rollback(String trans) throws Exception {

	   if (input.hasValue(trans)) { // check first not to trigger IoC
		   
		   Object obj = input.getValue(trans);
		   
		   if (obj instanceof Transaction) {
			   
			   Transaction t = (Transaction) obj;
			   
			   t.rollback();
		   }
	   }
   }
	
	public boolean isPost() {
		return isPost(this);
	}
	
	public boolean isGet() {
		return isGet(this);
	}
	
	public static boolean isPost(Action action) {
		Input input = action.getInput();
		String method = input.getProperty("method");
		boolean isPost = method != null && method.equalsIgnoreCase("post");
		return isPost;
	}
	
	public static boolean isGet(Action action) {
		Input input = action.getInput();
		String method = input.getProperty("method");
		boolean isGet = method != null && method.equalsIgnoreCase("get");
		return isGet;
	}
    
    /**
     * Sets a fixed message context for all actions derived from BaseAction.
     *
     * @param msgContext The message context to use for all actions.
     * @since 1.1.2
     */
    public static void setMessageContext(MessageContext msgContext) {
        FIXED_MSG_CONTEXT = msgContext;
    }
    
    /**
     * Checks if any error has been thrown into the action. Or in the action that called this action in the case of a Chain.
     * @since 1.16
     */
    protected boolean hasError(){
    	return (getErrors().size() > 0 || getFieldErrors().size() > 0);
    }
    
    private List<Message> getMessages() {
        return MessageManager.getMessages(this, true);
    }

    private List<Message> getMessages(boolean flash) {
        return MessageManager.getMessages(this, true, flash);
    }

    
    private List<Message> getErrors() {
        return MessageManager.getErrors(this, true);
    }
    
    private Map<String, Message> getFieldErrors() {
        return MessageManager.getFieldErrors(this, true);
    }
    
    public static Map<String, String> getMessageTokens(Action action, String field) {
    	
    	Input input = action.getInput();
    	
    	Output output = action.getOutput();
    	
        Map<String, String> map = new HashMap<String, String>();
        
        Iterator<String> iter = input.keys();
        
        while(iter.hasNext()) {
            
            String key = iter.next();
            
            String value = input.getString(key);
            
            map.put(key, value);
            
        }
        
        if (field != null) {
            
            map.put(Validator.FIELD_TOKEN, input.getString(field));
            
        }
        
        iter = output.keys();
        
        while(iter.hasNext()) {
            
            String key = (String) iter.next();
            
            Object value = output.getValue(key);
            
            if (value != null) {
                
                map.put(key, value.toString());
                
            }
        }
        
        return map;
    }
    
    /**
     * This method will generate dynamic tokens for the success/error messages (dynamic messages).
     * 
     * It will basicaly place the the Input values and the Output values as tokens.
     * 
     * The output values precedence over the input values, in other words, output values override input values.
     * 
     * @param field The field to which the tokens refer to (you can pass null if the tokens do not refer to any field in particular
     * @return A map with the tokens to use for the dynamic message
     */
    protected Map<String, String> getMessageTokens(String field) {
    	
    	return getMessageTokens(this, field);
        
    }
    
    
	/**
	 * Adds a message to the action with the given number.
	 * 
	 * @param msg_id The id of the message to show.
     * @since 1.1.1
	 */
	public void addMessage(String msg_id) {
		addMessage(msg_id, true);
	}
	
	public void addMessage(String msg_id, boolean flash) {
        List<Message> messages = getMessages(flash);
		messages.add(new DefaultMessage(msg_id, msgContext, getMessageTokens(null)));
	}
	
	public void addMessage(String msg_id, Map<String, String> tokens) {
        addMessage(msg_id, tokens, true);
	}
	
	public void addMessage(String msg_id, Map<String, String> tokens, boolean flash) {
        List<Message> messages = getMessages(flash);
		messages.add(new DefaultMessage(msg_id, msgContext, tokens));
	}
    
	/**
	 * Adds a message to the action with the given number.
	 * 
	 * @param msg_id The id of the message to show.
	 */    
    public void addMessage(int msg_id) {
        addMessage(msg_id, true);
    }
    
    public void addMessage(int msg_id, boolean flash) {
        addMessage(String.valueOf(msg_id), flash);
    }

	
	/**
	 * Adds an error to the action with the given number.
	 * 
	 * @param error_id The id of the error to show.
     * @since 1.1.1
	 */
	public void addError(String error_id) {
        List<Message> errors = getErrors();
		errors.add(new DefaultMessage(error_id, msgContext, getMessageTokens(null)));
	}
	
	public void addError(String error_id, Map<String, String> tokens) {
        List<Message> errors = getErrors();
		errors.add(new DefaultMessage(error_id, msgContext, tokens));
	}
    
	/**
	 * Adds an error to the action with the given number.
	 * 
	 * @param error_id The id of the error to show.
	 */
	public void addError(int error_id) {
        addError(String.valueOf(error_id));
	}    
    
	/**
	 * Adds an error to the action with the given number for the given field.
	 * 
     * @param field The name of the field containing this error message.
	 * @param error_id The id of the error to show.
     * @since 1.1.1
	 */
	public void addError(String field, String error_id) {
        Map<String, Message> fieldErrors = getFieldErrors();
		fieldErrors.put(field, new DefaultMessage(error_id, msgContext, getMessageTokens(field)));
    }
	
	public String getError(String field) {
		Map<String, Message> fieldErrors = getFieldErrors();
		
		Message m = fieldErrors.get(field);
		
		if (m != null) {
			
			return m.getId();
		}
		
		return null;
	}
	
	public void addError(String field, String error_id, Map<String, String> tokens) {
        Map<String, Message> fieldErrors = getFieldErrors();
		fieldErrors.put(field, new DefaultMessage(error_id, msgContext, tokens));
	}
    
	/**
	 * Adds an error to the action with the given number for the given field.
	 * 
     * @param field The name of the field containing this error message.
	 * @param error_id The id of the error to show.
	 */
	public void addError(String field, int error_id) {
        addError(field, String.valueOf(error_id));
    }    
	
	/**
	 * Adds a message to the action with the given number and context.
	 * 
	 * @param msg_id The id of the message to show.
	 * @param msgContext The MessageContext where the message is.
     * @since 1.1.1
	 */
	public void addMessage(String msg_id, MessageContext msgContext) {
		addMessage(msg_id, msgContext, true);
	}
	
	public void addMessage(String msg_id, MessageContext msgContext, boolean flash) {
        List<Message> messages = getMessages(flash);
		messages.add(new DefaultMessage(msg_id, msgContext, getMessageTokens(null)));
	}

    
	/**
	 * Adds a message to the action with the given number and context.
	 * 
	 * @param msg_id The id of the message to show.
	 * @param msgContext The MessageContext where the message is.
	 */
	public void addMessage(int msg_id, MessageContext msgContext) {
        addMessage(msg_id, msgContext, true);
	}
	
	public void addMessage(int msg_id, MessageContext msgContext, boolean flash) {
        addMessage(String.valueOf(msg_id), msgContext, flash);
	}    


	/**
	 * Adds an error to the action with the given number and context.
	 * 
	 * @param error_id The id of the error to show.
	 * @param msgContext The MessageContext where the error is.
     * @since 1.1.1
	 */
	public void addError(String error_id, MessageContext msgContext) {
        List<Message> errors = getErrors();
		errors.add(new DefaultMessage(error_id, msgContext, getMessageTokens(null)));
	}

	/**
	 * Adds an error to the action with the given number and context.
	 * 
	 * @param error_id The id of the error to show.
	 * @param msgContext The MessageContext where the error is.
	 */
	public void addError(int error_id, MessageContext msgContext) {
        addError(String.valueOf(error_id), msgContext);
	}
    
	/**
	 * Adds an error to the action with the given number and context for the given field.
	 * 
     * @param field The name of the field containing this error message.
	 * @param error_id The id of the error to show.
	 * @param msgContext The MessageContext where the error is.
     * @since 1.1.1
	 */
	public void addError(String field, String error_id, MessageContext msgContext) {
        Map<String, Message> fieldErrors = getFieldErrors();
		fieldErrors.put(field, new DefaultMessage(error_id, msgContext, getMessageTokens(field)));
	}    

	/**
	 * Adds an error to the action with the given number and context for the given field.
	 * 
     * @param field The name of the field containing this error message.
	 * @param error_id The id of the error to show.
	 * @param msgContext The MessageContext where the error is.
	 */
	public void addError(String field, int error_id, MessageContext msgContext) {
        addError(field, String.valueOf(error_id), msgContext);
	}    
	
	/**
	 * Adds an message to the action.
	 * 
	 * @param msg The message object.
	 */
	public void addMessage(Message msg) {
		addMessage(msg, true);
	}

	public void addMessage(Message msg, boolean flash) {
        List<Message> messages = getMessages(flash);
		messages.add(msg);
	}

	
	/**
	 * Adds an error to the action.
	 * 
	 * @param error The error object.
	 */
	public void addError(Message error) {
        List<Message> errors = getErrors();
		errors.add(error);
	}
    
    /**
     * Adds an error to the action for the given field.
     *
     * @param field The name of the field containing this error message.
     * @param error The error object.
     * @since 1.1
     */
    public void addError(String field, Message error) {
        Map<String, Message> fieldErrors = getFieldErrors();
        fieldErrors.put(field, error);
    }
    
	public void setInput(Input input) {
		this.input = input;
	}
	
	public void setOutput(Output output) {
		this.output = output;
	}
	
	public void setSession(Context context) {
		this.session = context;
	}
	
	public void setApplication(Context context) {
		this.application = context;
	}
    
    public void setCookies(Context context) {
        this.cookies = context;
    }
	
	public void setLocale(Locale loc) {
		this.loc = loc;
	}
    
	public Input getInput() {
		return input;
	}
	
	public Output getOutput() {
		return output;		
	}
	
	public Context getSession() {
		return session;
	}
	
	public Context getApplication() {
		return application;
	}
    
    public Context getCookies() {
        return cookies;
    }
	
	public Locale getLocale() {
		return loc;
	}
	
	public <E> E getSessionObj() {
		
		return (E) BaseLoginAction.getSessionObj(session);
	}
    
    public <E> E getUserSession() {
        
        return (E) BaseLoginAction.getUserSession(session);
        
    }
    
    public Locale getUserLocale() {
       
       return BaseLoginAction.getUserLocale(session);
    }
    
    public boolean isLogged() {
        
        return BaseLoginAction.isLogged(session);
    }
    
    public void adhere() {
        
        Controller.adhere(this, this.getClass());
    }
    
    public void disjoin() {
        
        Controller.disjoin(this, this.getClass());
    }
    
    public void onRemoved() {
    	
    	// subclasses can override this to trap this callback for sticky actions...
    }
    
    public String execute() throws Exception {
    	
    	return SUCCESS;
    }
    
    /**
     * Shortcut for output.setValue(AjaxConsequence.KEY, value)
     * 
     * @param value
     * @since 1.13
     */  
    public void ajax(Object value){
    	output.setValue(AjaxConsequence.OBJECT, value);
    }
    
    public void stream(Object value) {
    	output.setValue(StreamConsequence.STREAM, value);
    }
    
    public void stream(Object value, int length) {
    	output.setValue(StreamConsequence.CONTENT_LENGTH, length);
    	stream(value);
    }
    
    public void stream(Object value, String filename) {
    	output.setValue(StreamConsequence.FILENAME, filename);
    	stream(value);
    }
    
    /**
     * Shortcut for output.setValue(Redirect.REDIRURL_PARAM, "url"); <br/>
     * Use redir() consequence for ActionConfig<br/><br/>
     * 
     * Example:<br/>
     * In Action:<br/>
     * redir("some.jsp?someparam=somevalue");<br/><br/>
     * 
     * In ApplicationManager:<br/>
     * action("someAction.mtw").redir();
     * 
     * @param url
     */
    public void redir(String url) {
    	output.setValue(Redirect.REDIRURL_PARAM, url);
    }
    
    /**
     * 
     * @return String the ContextPath
     */
    public String getContextPath(){
    	return input.getProperty("contextPath");
    }
    
    // This is for Ruby Actions...
	public Input input() { return getInput(); }
	
	public Output output() { return getOutput(); }
	
	public Context session() { return getSession(); }
	
	public Context application() { return getApplication(); }
	
	public Context cookies() { return getCookies(); }
	
	public Locale loc() { return getLocale(); }
	
	public static Object findValue(String key, Action action) {
		
		Object value = null;
		
		Input input = action.getInput();
		
		if ((value = input.getValue(key)) != null) return value;
		
		Output output = action.getOutput();
		
		if ((value = output.getValue(key)) != null) return value;
		
		Context session = action.getSession();
		
		if ((value = session.getAttribute(key)) != null) return value;
		
		Context application = action.getApplication();
		
		if ((value = application.getAttribute(key)) != null) return value;
		
		return null;
	}
	
	public Object findValue(String key) {
		
		return findValue(key, this);
	}
	
	public void setSessionObj(Object user) {
		setUserSession(user);
	}
	
	public void setSessionUser(Object user) {
		setUserSession(user);
	}
	
	public void setSessionGroup(Enum<?> e) {
		setSessionGroup(e.toString());
	}
	
	public void setSessionGroups(Enum<?> ... es) {
		String[] s = new String[es.length];
		int index = 0;
		for(Enum<?> e : es) {
			s[index++] = e.toString();
		}
		setSessionGroups(s);
	}
	
	public void setSessionGroup(String group) {
		setSessionGroups(group);
	}
	
	public void setSessionGroups(String ... groups) {

		BaseLoginAction.setUserGroups(session, groups);
	}
	
	public Locale getSessionLocale() {
		
		return getUserLocale();
	}
	
	public void setSessionLocale(Locale loc) {
		BaseLoginAction.setSessionLocale(loc, session);
	}
	
	public void setSessionLocale(String locale) {
		
		BaseLoginAction.setUserLocale(locale, session);
	}
	
	public void setUserSession(Object user) {
		BaseLoginAction.setUserSession(user, session);
	}
	
	public void replaceSessionObj(Object newUser) {
		replaceUserSession(newUser);
	}
	
	public void replaceUserSession(Object newUser) {
		BaseLoginAction.replaceUserSession(newUser, session);
	}

}

	