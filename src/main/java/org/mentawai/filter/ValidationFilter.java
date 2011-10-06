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
package org.mentawai.filter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.BaseAction;
import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;
import org.mentawai.i18n.LocaleManager;
import org.mentawai.message.ClassMessageContext;
import org.mentawai.message.FileMessageContext;
import org.mentawai.message.MessageContext;
import org.mentawai.validation.Validator;

/**
 * @author Sergio Oliveira
 */
public abstract class ValidationFilter implements Filter {
	
	public static final String DEFAULT_DIR = "/validation";
	
	private String resultForError = BaseAction.ERROR;
	private MessageContext msgContext;
	
	public ValidationFilter() {
        
        if (LocaleManager.isUseMasterForEverything()) {
            msgContext = new FileMessageContext(LocaleManager.getMaster(), "");
        } else {
            msgContext = new ClassMessageContext(this.getClass(), DEFAULT_DIR.replace('\\', '/'));
        }
	}

	
	private Validator getValidator(Action action, String innerAction) {
		
		Map<String, Object> rules = new LinkedHashMap<String, Object>();
		Map<String, Object> errors = new HashMap<String, Object>();
		
		Validator validator = new Validator(rules, errors);
		
		prepareValidator(validator, action, innerAction);
		
		return validator;
		
	}
	
    /**
     * Implement this abstract method to add rules to the fields you want to validate.
     */
	public abstract void prepareValidator(Validator validator, Action action, String innerAction);
	
    /**
     * By default, the filter returns the <i>BaseAction.ERROR</i> when a validation failure happens.
     * You can change that by calling this method.
     *
     * @param resultForError The result to return in case of a validation failure.
     */
	public void setResultForError(String resultForError) {
		this.resultForError = resultForError;
	}
	
    /**
     * Sets the MessageContext from where you want to get the error messsages.
     * You should only call this method if you want to change the default message context
     * for this filter which is <i>ClassMessageContext(this.getClass(), "/validation")</i>.
     *
     * @param msgContext The MessageContext to use instead of the default one.
     */
    public void setMessageContext(MessageContext msgContext) {
		this.msgContext = msgContext;
	}
    
    public static boolean isPost(Action action) {
    	
    	Input input = action.getInput();
    	
    	String method = input.getProperty("method");
    	
    	if (method != null && method.equalsIgnoreCase("post")) {
    		
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Sets the directory where to look for error messages.
     * You should only call this method if you want to change the default directory,
     * which is <i>/validation</i>.
     * Note that calling this method will force the change of the message context
     * of this filter to ClassMessageContext with the given directory.
     *
     * @param dir The directory where to look for error messages.
     */
	public void setDir(String dir) {
		this.msgContext = new ClassMessageContext(this.getClass(), dir);
	}
	
	public String filter(InvocationChain chain) throws Exception {
		
		Action action = chain.getAction();
		
		Validator validator = getValidator(action, chain.getInnerAction());
		
		boolean isOk = validator.validate(action, msgContext);
		
		if (!isOk) {
			
			return resultForError;
		}
		
		return chain.invoke();
	}
    
    public void destroy() { }
}
		