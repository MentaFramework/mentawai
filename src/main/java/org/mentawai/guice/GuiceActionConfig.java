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
package org.mentawai.guice;
 
 import org.mentawai.core.Action;
import org.mentawai.core.ActionConfig;
import org.mentawai.core.PojoAction;

import com.google.inject.Injector;
 
 /**
  * ActionConfig to tightly integrate Google Guice.
  * 
  * Use this ActionConfig if you want Guice to create your
  * Action instances and resolve all dependencies.
  * 
  * @author Sven Jacobs <sven.jacobs@web.de>
  * @version 0.1
  */
 public class GuiceActionConfig extends ActionConfig {
	 
   private Injector injector;
   
   public GuiceActionConfig( Class<? extends Object> klass, Injector injector ) {
     super( klass );
     this.injector = injector;
   }
   
   public GuiceActionConfig( String name, Class<? extends Object> klass, Injector injector ) {
     super( name, klass );
     this.injector = injector;
   }
   
   public GuiceActionConfig( String name, Class<? extends Object> klass, String innerAction, Injector injector ) {
	   super( name, klass, innerAction);
	   this.injector = injector;
   }
   
   @Override
   public Action getAction() {
	   
     Object instance = injector.getInstance( actionClass );
     
     if ( Action.class.isAssignableFrom( actionClass ) ) {
    	 
       return (Action) instance;
       
     } else {
    	 
       return new PojoAction( instance );
       
     }
   }
 }