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
package org.mentawai.ioc;

import java.util.List;
import java.util.Map;

/**
 * DefaultComponent with a scope attribute.
 * 
 * @author Sergio Oliveira
 */
public class ScopeComponent extends DefaultComponent {
    
    private final int scope;
    

    /**
     * Creates a new ScopeComponent for the given class with the given scope.
     *
     * @param klass The class used to create new instances.
     * @param scope The scope.
     */
    public ScopeComponent(Class<? extends Object> klass, int scope) {
        super(klass);
        this.scope = scope;
    }
    
    /**
     * Creates a new ScopeComponent for the given class with the given init (constructor) values in the given scope.
     * A constructor with the given init values will be called to instantiate the class.
     *
     * @param klass The class used to create new instances.
     * @param initValues The values for the constructor.
     * @param scope The scope 
     */
    public ScopeComponent(Class<? extends Object> klass, List<Object> initValues, int scope) {
        super(klass, initValues);
        this.scope = scope;
    }
    
    /**
     * Creates a new ScopeComponent for the given class with the given property map in the given scope.
     * The properties will be injected in the instance with reflection.
     *
     * @param klass The class used to create new instances.
     * @param props The properties that need to be injected in each instance.
     * @param scope The scope
     */
    public ScopeComponent(Class<? extends Object> klass, Map<String, Object> props, int scope) {
        super(klass, props);
        this.scope = scope;
    }

    /**
     * Creates a new ScopeComponent for the given class with the given property map and the given init (constructor) values in the given scope.
     * The properties will be injected in the instance with reflection.
     * A constructor with the given init values will be called to instantiate the class.
     *
     * @param klass The class used to create new instances.
     * @param initValues The values for the constructor.
     * @param props The properties that need to be injected in each instance.
     * @param scope The scope
     */   
    public ScopeComponent(Class<? extends Object> klass, List<Object> initValues, Map<String, Object> props, int scope) {
        super(klass, initValues, props);
        this.scope = scope;
    }
    
    public int getScope() {
        
        return scope;
        
    }
    
}
    