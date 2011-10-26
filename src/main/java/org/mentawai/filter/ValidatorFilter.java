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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.mentawai.core.Action;
import org.mentawai.core.BaseAction;
import org.mentawai.core.Filter;
import org.mentawai.core.InvocationChain;
import org.mentawai.i18n.LocaleManager;
import org.mentawai.jruby.JRubyInterpreter;
import org.mentawai.jruby.RubyAction;
import org.mentawai.message.ClassMessageContext;
import org.mentawai.message.FileMessageContext;
import org.mentawai.message.MessageContext;
import org.mentawai.util.FindMethod;
import org.mentawai.validation.Validatable;
import org.mentawai.validation.ValidationInterceptor;
import org.mentawai.validation.Validator;

/**
 * A filter to validate the values of an action input. This filter has the same
 * behaviour of a ValidationFilter except that it will get the validation
 * information from the action, if it implements the Validatable interface.
 * 
 * @author Sergio Oliveira
 */
public class ValidatorFilter implements Filter {

   private static final String DEFAULT_DIR = "/validation";

   private String resultForError = BaseAction.ERROR;

   private Map<ActionUniqueId, MessageContext> msgContexts = new Hashtable<ActionUniqueId, MessageContext>();

   private String dir = DEFAULT_DIR;

   private static String DIR = DEFAULT_DIR;

   /**
    * Creates a Validator filter.
    */
   public ValidatorFilter() {

   }

   public ValidatorFilter(String resultForError) {

      this();

      this.resultForError = resultForError;
   }

   public static String getDir() {

      return DIR;
   }

   private MessageContext getMessageContext(Class<? extends Object> klass) {
	   
	      FileMessageContext masterMsgContext = null;
	      
	      if (LocaleManager.isUseMasterForEverything()) {
	    	  
	          masterMsgContext = new FileMessageContext(LocaleManager.getMaster(), "");
	          
	          if (LocaleManager.isUsePrefixForActions()) {
	         	 
	         	String prefix = klass.getSimpleName();
	         	
	         	masterMsgContext.setPrefix(prefix);
	          }
	          
	          return masterMsgContext;
	       }

      return new ClassMessageContext(klass, dir);
   }

   /**
    * By default, the filter returns the <i>BaseAction.ERROR</i> when a
    * validation failure happens. You can change that by calling this method.
    * 
    * @param resultForError
    *           The result to return in case of a validation failure.
    */
   public void setResultForError(String resultForError) {
      this.resultForError = resultForError;
   }

   /**
    * Sets the directory where to look for error messages. You should only call
    * this method if you want to change the default directory, which is
    * <i>/validation</i>. Note that calling this method will force the change
    * of the message context of this filter to ClassMessageContext with the
    * given directory.
    * 
    * @param dir
    *           The directory where to look for error messages.
    */
   public void setDir(String dir) {

      this.dir = dir.replace('\\', '/');

      DIR = dir;

      msgContexts.clear();

   }

   public String filter(InvocationChain chain) throws Exception {

      Action action = chain.getAction();
      
      Object pojo = chain.getPojo();
      
      Object actionImpl = pojo != null ? pojo : action;
      
      Validator validator = null;
      
      ActionUniqueId actionId = null;
      
      if (actionImpl instanceof ValidationInterceptor) {
    	  
    	  ValidationInterceptor vi = (ValidationInterceptor) actionImpl;
    	  
    	  boolean goAhead = vi.beforeValidation(chain.getInnerAction());
    	  
    	  if (!goAhead) {
    		  return chain.invoke();
    	  }
      }
      
      if (actionImpl instanceof Validatable) {

         actionId = new ActionUniqueId(actionImpl.getClass().getName(), chain.getInnerAction());

         Validatable v = (Validatable) actionImpl;

         validator = new Validator(new LinkedHashMap<String, Object>(), new HashMap<String, Object>());
         
         v.prepareValidator(validator, chain.getInnerAction());
         
      } else if (action instanceof RubyAction) {
    	  
    	  RubyAction ra = (RubyAction) action;
    	  
    	  JRubyInterpreter ruby = JRubyInterpreter.getInstance();
    	  
    	  if (ruby.respondTo(ra.getRubyObject(), "prepareValidator")) {
    		  
    		  validator = new Validator(new LinkedHashMap<String, Object>(), new HashMap<String, Object>());
    		  
    		  actionId = new ActionUniqueId(ra.getActionName(), chain.getInnerAction());
    		  
    		  ruby.call(ra.getRubyObject(), "prepareValidator", validator, chain.getInnerAction());
    		  
    	  } else {
    		  
    		  return chain.invoke();
    	  }
         
      } else {
    	  
    	  return chain.invoke();
    	  
      }
      
     MessageContext msgContext = msgContexts.get(actionId);

     if (msgContext == null) {

        msgContext = getMessageContext(actionImpl.getClass());

        msgContexts.put(actionId, msgContext);
     }

     boolean isOk = validator.validate(action, msgContext);
     
     if (actionImpl instanceof ValidationInterceptor) {
    	 
    	 ValidationInterceptor vi = (ValidationInterceptor) actionImpl;
    	 
    	 vi.afterValidation(chain.getInnerAction(), isOk);
     }

     if (!isOk) {
        return resultForError;
     }

     return chain.invoke();
   }

   public void destroy() {
   }

   static class ActionUniqueId {

      String classname;

      String innerAction = null;

      public ActionUniqueId(String classname, String innerAction) {
         this.classname = classname;
         this.innerAction = innerAction;
      }

      public int hashCode() {

         return 34544 * classname.hashCode() + 194 * (innerAction == null ? 0 : innerAction.hashCode());

      }

      public boolean equals(Object obj) {

         if (obj instanceof ActionUniqueId) {

            ActionUniqueId a = (ActionUniqueId) obj;

            if (a.classname.equals(this.classname)) {

               if (a.innerAction == null && this.innerAction == null)
                  return true;

               if (a.innerAction != null && this.innerAction != null && a.innerAction.equals(this.innerAction))
                  return true;

            }

         }

         return false;
      }
   }
}
