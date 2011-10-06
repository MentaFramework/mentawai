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
 * Use this action config if you want your action to be a single instance for all requests,
 * in other words, you don't want to create a new instance of your action for every request.
 * If you use this action config, it is your responsibility to make your action thread-safe.
 * If you are not sure that your action is thread-safe, than you should not use this action config.
 * Sometimes our actions are so simple (Ex: org.mentawai.action.SuccessAction) that it doesn't make sense to create a new instance for every request.
 * Although this is not a requirement, it is strongly recommended that you use SingleInstanceActionConfigs to configure SingleInstanceBaseActions.
 *
 * @author Sergio Oliveira
 */
public class SingleInstanceActionConfig extends ActionConfig {
	
    private Action instance;
    
	public SingleInstanceActionConfig(Action instance) {
        super(instance.getClass());
        this.instance = instance;
	}
    

	public SingleInstanceActionConfig(String name, Action instance) {
        super(name, instance.getClass());
        this.instance = instance;
	}
    
	public SingleInstanceActionConfig(String name, Action instance, String innerAction) {
        super(name, instance.getClass(), innerAction);
        this.instance = instance;
	}    
	
    /**
     * Returns an action instance to be used with this request.
     * This method will return the same instance for every call.
     * It is your responsibility to make your action thread-safe.
     * 
     * @return The action instance to use for the request.
     */
    public Action getAction() {
        return instance;
    }
}

		
	
	
	