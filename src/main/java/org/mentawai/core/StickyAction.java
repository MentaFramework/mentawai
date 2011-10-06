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

/**
 * An interface describing a sticky action, in other words,
 * an action that can adhere to the session and persist its state.
 * 
 * This is similar to what other frameworks call <i>continuations</i>, but
 * much simpler.
 * 
 * @author Sergio Oliveira
 */
public interface StickyAction extends Action {
    
	/**
	 * Adhere to the session, so the instance of this action will persist
	 * until disjoin is called.
	 */
    public void adhere();
    
    /**
     * Remove this action from session and discard its instance losing
     * all state (instance variables) associated with it.
     */
    public void disjoin();

    /**
     * This method will be called if the session has expired or if it has
     * been invalidated and there are sticky actions still sticked to the session.
     * 
     * This is very useful for clean up and will be called if your action calls
     * adhere and <i>forget</i> to call disjoin, leaving the actiion instance
     * forever in the session.
     */
    public void onRemoved();
    
}