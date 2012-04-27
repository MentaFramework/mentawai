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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.mentacontainer.Container;
import org.mentawai.ajax.AjaxConsequence;
import org.mentawai.ajax.AjaxRenderer;
import org.mentawai.filter.AuthorizationFilter;
import org.mentawai.filter.PrettyURLParamFilter;

/**
 * An ActionConfig links together an action implementation, an action name or alias, action results and action consequences.
 * It makes it possible for an action implementation to be re-used in different situations with different names and consequences.
 * 
 * @author Sergio Oliveira
 */
public class ActionConfig {
	
	protected final Class<? extends Object> actionClass;
	private String name = null;
	private Map<String, Consequence> consequences = new HashMap<String, Consequence>();
    private Map<String, Map<String, Consequence>> innerConsequences = new HashMap<String, Map<String, Consequence>>();
    private List<Object[]> filters = new LinkedList<Object[]>();
    private List<Object[]> firstFilters = new LinkedList<Object[]>();
    private String innerAction = null;
    private String dirName = null;
    private Consequence catchAll = null;
    private boolean internal = false;
    private boolean bypassAuthentication = false;
    private boolean redirectAfterLogin = false;
    
	/**
	 * Creates an ActionConfig for the given action implementation.
     * This action config will use the name of the action to derive its name.
     * This can and should also be used with chain consequence!
     * 
     * org.myapp.blablabla.MyAction
     * 
     * The name will be:
     * 
     * /MyAction
     * 
	 * @param klass The action implementation to use
	 */
	public ActionConfig(Class<? extends Object> klass) {
		
		this.actionClass = klass;
        this.dirName = getDirFromClass(klass);
        this.name = getName(klass);
	}
    

	/**
	 * Creates an ActionConfig with the given name for the given action implementation.
	 * 
	 * @param name The name or alias of this ActionConfig
	 * @param klass The action implementation to use
	 */
	public ActionConfig(String name, Class<? extends Object> klass) {
      
      if (name.indexOf(".") > 0) {
         
         // use is probably defining action and inner action together...
         
         StringTokenizer st = new StringTokenizer(name, ".");
         
         if (st.countTokens() != 2) {
            
            throw new IllegalArgumentException("Bad action name: " + name);
         }
         
         this.name = cutSlash(st.nextToken());
         this.innerAction = st.nextToken();
         
      } else {
         
         this.name = cutSlash(name);
      }
		
		this.actionClass = klass;
      this.dirName = getDirFromClass(klass);
      
	}
    
	/**
	 * Creates an ActionConfig for the given action implementation.
     * This action config will use the name of the action to derive its name.
     * This can and should also be used with chain consequence!
	 *
     * Notice that this action config is specific to an inner action.
     * 
     * org.myapp.blablabla.MyAction
     * 
     * The name will be:
     * 
     * /MyAction
	 * 
	 * @param name The name or alias of this ActionConfig
	 * @param klass The action implementation to use
     * @param innerAction The inner action to use
	 */
	public ActionConfig(String name, Class<? extends Object> klass, String innerAction) {
		
		this.actionClass = klass;
		this.name = cutSlash(name);
        this.innerAction = innerAction;
        this.dirName = getDirFromClass(klass);
	}
	
	/**
	 * Creates an ActionConfig with the given name for the given inner action implementation.
     * Notice that this action config is specific to an inner action.
	 * 
	 * @param klass The action implementation to use
     * @param innerAction The inner action to use
	 */
	public ActionConfig(Class<? extends Object> klass, String innerAction) {
		
		this.actionClass = klass;
		this.name = getName(klass);
        this.innerAction = innerAction;
        this.dirName = getDirFromClass(klass);
	}
	
    /**
     * This method will imply a directory name from the action class name.
     * The action can then use this directory to look for JSPs in case of automatically
     * page discovery. 
     *
     * Ex: 
     * Action = examples.helloworld.HelloWorldAction
     * Directory = /helloworld
     * 
     * @param klass The action class name
     * @return a directory where to look for JSPs
     */
    protected String getDirFromClass(Class klass) {
        
        String classname = klass.getName();
        
        String[] s = classname.split("\\.");
        
        classname = s[s.length - 1];
        
        classname = classname.toLowerCase();
        
        int index = classname.indexOf("action");
        
        if (index > 0) {
            
            classname = classname.substring(0, index);
        }
        
        return classname;
    }
	
	/**
	 * Adds a consequence for the given result.
	 * An action must have a consequence for each of its possible results.
	 * 
	 * @param result A possible result of this ActionConfig
	 * @param c The consequence for this result
     * @return this action config for method chaining. Ex: addConsequence().addConsequence();
	 */
	public ActionConfig addConsequence(String result, Consequence c) {
		consequences.put(result, c);
        return this;
	}
	
	public ActionConfig internalOnly() {
		this.internal = true;
		return this;
	}
	
	public boolean isInternalOnly() {
		return internal;
	}
	
	/**
	 * Indicate that this action should NOT be authenticated. (For registration, login, etc.)
	 * 
	 * @return this action config
	 * @since 2.4.0
	 */
	public ActionConfig bypassAuthentication() {
		this.bypassAuthentication = true;
		return this;
	}
	
	public boolean shouldBypassAuthentication() {
		return bypassAuthentication;
	}
	
	public ActionConfig prettyURLParams(String ... params) {
		filter(new PrettyURLParamFilter(params));
		return this;
	}
	
	/**
	 * Indicate that this action redirects after login.
	 * 
	 * @return this action config
	 * @since 2.4.0
	 */
	public ActionConfig redirectAfterLogin() {
		this.redirectAfterLogin = true;
		return this;
	}
	
	public boolean shouldRedirectAfterLogin() {
		return redirectAfterLogin;
	}
    
    /**
     * Shorter version of addConsequence.
     * 
     * @param result
     * @param c
     * @return this action config
     * @since 1.2
     */
    public ActionConfig on(String result, Consequence c) {
       return addConsequence(result, c); 
    }
    
    /**
     * Shorter verions of addConsequence that will assume a forward.
     * 
     * @param result
     * @param jsp
     * @return  this action config
     * @since 1.9
     */
    public ActionConfig on(String result, String jsp) {
    	
    	return addConsequence(result, new Forward(jsp));
    }
    
    private String cutSlash(String name) {
        if (name.startsWith("/") && name.length() > 1) {
            return name.substring(1, name.length());
        }
        return name;
    }
    
	/**
	 * Adds a consequence for the given result of the given inner action.
	 * An inner action can have a consequence for each of its possible results.
     * If you don't define consequences for an inner action, 
     * the consequences of the main action (execute() method) is used instead.
	 * 
	 * @param result A possible result of this ActionConfig
     * @param innerAction The inner action that can return this result.
	 * @param c The consequence for this result
     * @return this action config for method chaining Ex: addConsequence().addConsequence();
     * @throws IllegalStateException If this method is called for a action config specific to an inner action
	 */
	public ActionConfig addConsequence(String result, String innerAction, Consequence c) {
        if (this.innerAction != null) throw new IllegalStateException("Calling addConsequence(result,innerAction,c) is illegal for inner action configs!");
        Map<String, Consequence> map = innerConsequences.get(innerAction);
        if (map == null) {
            map = new HashMap<String, Consequence>();
            innerConsequences.put(innerAction, map);
        }
        map.put(result, c);
        return this;
	}
    
    /**
     * Shorter version of addConsequence.
     * @param result
     * @param innerAction
     * @param c
     * @return this action config
     * @since 1.2
     */
    public ActionConfig on(String result, String innerAction, Consequence c) {
        return addConsequence(result, innerAction, c);
    }
    
    /**
     * Adds a filter for the action.
     *
     * @param filter The filter to add for this action.
     * @return this action config for method chaining Ex: addConsequence().addFilter();
     */
    public ActionConfig addFilter(Filter filter) {
        return addFilter(filter, (String) null);
    }
    
    public ActionConfig authorize(Enum<?> ... es) {
    	filter(new AuthorizationFilter(es));
    	return this;
    }
    
    public ActionConfig authorize(String ... groups) {
    	filter(new AuthorizationFilter(groups));
    	return this;
    }
    
    /**
     * Shorter version of addFilter.
     * 
     * @param filter
     * @return this action config
     * @since 1.2
     */
    public ActionConfig filter(Filter filter) {
        return addFilter(filter);
    }
    
    public Consequence getCatchAll() {
    	
    	return catchAll;
    }
    
    public ActionConfig catchAll(Consequence c) {
    	this.catchAll = c;
    	return this;
    }
    
    public ActionConfig all(Consequence c) {
    	return catchAll(c);
    }
    
    /**
     * Adds a filter for this inner action.
     *
     * @param filter The filter to add for this inner action.
     * @param innerAction the inner action
     * @return this action config for method chaining Ex: addConsequence().addFilter();
     * @since 1.1.1
     */
    public ActionConfig addFilter(Filter filter, String innerAction) {
        Object [] array = new Object[2];
        array[0] = innerAction;
        array[1] = filter;
        filters.add(array);
        return this;
    }
    
    /**
     * Add a list of filters that will be executed before the global filters.
     * 
     * @param filters
     * @return this
     * @since 1.9
     */
    public ActionConfig filterFirst(List filters) {
    	
    	return addFilterFirst(filters);
    }
    
    /**
     * Add a list of filters that will be executed before the global filters.
     * 
     * @param filters
     * @param innerAction
     * @return this
     * @since 1.9
     */
    public ActionConfig filterFirst(List filters, String innerAction) {
    	
    	return addFilterFirst(filters, innerAction);
    	
    }
    
    /**
     * Add a list of filters that will be executed before the global filters.
     * 
     * @param filter
     * @return this
     * @since 1.9
     */
    public ActionConfig filterFirst(Filter filter) {
    	
    	return addFilterFirst(filter);
    }
    
    /**
     * Add a list of filters that will be executed before the global filters.
     * 
     * @param filter
     * @param innerAction
     * @return this
     * @since 1.9
     */
    public ActionConfig filterFirst(Filter filter, String innerAction) {
    	
    	return addFilterFirst(filter, innerAction);
    }
    
    /**
     * Add a list of filters that will be executed before the global filters.
     * 
     * @param filters
     * @return this
     * @since 1.9
     */
    public ActionConfig addFilterFirst(List filters) {
    	
    	return addFilterFirst(filters, null);
    }
    
    /**
     * Adds a list of filters that will be executed before the global filters.
     * 
     * @param filters
     * @param innerAction
     * @return this
     * @since 1.9
     */
    public ActionConfig addFilterFirst(List filters, String innerAction) {
        Iterator iter = filters.iterator();
        while(iter.hasNext()) {
            Filter f = (Filter) iter.next();
            addFilterFirst(f, innerAction);
        }
        return this;
    }
    
    /**
     * Adds a filter that will be executed before the global filters.
     * 
     * @param filter
     * @return this
     * @since 1.9
     */
    public ActionConfig addFilterFirst(Filter filter) {
    	
    	return addFilterFirst(filter, null);
    }
    
    /**
     * Adds a filter that will be executed before the global filters.
     * 
     * @param filter
     * @param innerAction
     * @return this
     * @since 1.9
     */
    public ActionConfig addFilterFirst(Filter filter, String innerAction) {
    	Object [] array = new Object[2];
    	array[0] = innerAction;
    	array[1] = filter;
    	
    	firstFilters.add(array);
    	
    	return this;
    }
    
    private static String getName(Class<? extends Object> klass) {
        
        String[] tokens = klass.getName().split("\\.");
        
        String name = tokens[tokens.length - 1];
        
        if (ApplicationManager.removeActionFromName && name.endsWith("Action")) {
        	
        	int index = name.lastIndexOf("Action");
        	
        	name = name.substring(0, index);
        }
        
        return name;
        
    }
    
    /**
     * Shorter version of addFilter.
     * 
     * @param filter
     * @param innerAction
     * @return this action config
     * @since 1.2
     */
    public ActionConfig filter(Filter filter, String innerAction) {
        return addFilter(filter, innerAction);
    }
    
    /**
     * Adds a list of filter for the action.
     *
     * @param filters A list of filters.
     * @return this action config for method chaining Ex: addConsequence().addFilter();
     */
    public ActionConfig addFilter(List filters) {
        return addFilter(filters, null);
    }
    
    /**
     * Shorter version of addFilter.
     * 
     * @param filters
     * @return this action config
     * @since 1.2
     */
    public ActionConfig filter(List filters) {
        return addFilter(filters);
    }
    
    /**
     * Shorter version of a forward on success.
     * 
     * @param page
     * @return this
     * @since 1.3
     */
    public ActionConfig fwdOk(String page) {
        
        return addConsequence(Action.SUCCESS, new Forward(page));
    }
    
    /**
     * Shorter version of a forward on error.
     * 
     * @param page
     * @return this
     * @since 1.3
     */
    public ActionConfig fwdError(String page) {
        
        return addConsequence(Action.ERROR, new Forward(page));
    }
    
    /**
     * Shorter version of a AjaxConsequence success.
     * 
     * @param renderer
     * @return this
     * @since 1.10.1
     */
    public ActionConfig ajaxOk(AjaxRenderer renderer){
    
    	return addConsequence(Action.SUCCESS, new AjaxConsequence(renderer));
    }

    /**
     * Shorter version of a AjaxConsequence error.
     * 
     * @param renderer
     * @return this
     * @since 1.10.1
     */
    public ActionConfig ajaxError(AjaxRenderer renderer){
    
    	return addConsequence(Action.ERROR, new AjaxConsequence(renderer));
    }    
    
    /**
     * Shorter version of a redir on success.
     * 
     * @param page
     * @return this
     * @since 1.3
     */
    public ActionConfig redirOk(String page) {
        
        return addConsequence(Action.SUCCESS, new Redirect(page));
    }
    
    /**
     * Shorter version of a redir on error.
     * 
     * @param page
     * @return this
     * @since 1.3
     */
    public ActionConfig redirError(String page) {
        
        return addConsequence(Action.ERROR, new Redirect(page));
    }
    
    /**
     * Shorter version of a redir on success.
     * 
     * @return this
     * @since 1.3
     */
    public ActionConfig redirOk() {
        
        return addConsequence(Action.SUCCESS, new Redirect());
    }
    
    /**
     * Shorter version of a redir on error.
     * 
     * @return this
     * @since 1.3
     */
    public ActionConfig redirError() {
        
        return addConsequence(Action.ERROR, new Redirect());
    }
    
    /**
     * Shorter version of a redir on success.
     * 
     * @param page
     * @param flag
     * @return this
     * @since 1.3
     */
    public ActionConfig redirOk(String page, boolean flag) {
        
        return addConsequence(Action.SUCCESS, new Redirect(page, flag));
    }
    
    /**
     * Shorter version of a redir on error.
     * 
     * @param page
     * @param flag
     * @return this
     * @since 1.3
     */
    public ActionConfig redirError(String page, boolean flag) {
        
        return addConsequence(Action.ERROR, new Redirect(page, flag));
    }    
    
    
    /**
     * Shorter version of chain on success
     * @param actionConfig
     * @return this
     * @since 2.0.1
     */
	public ActionConfig chainOk(ActionConfig actionConfig) {
		return addConsequence(Action.SUCCESS, new Chain(actionConfig));
	}

	/**
     * Shorter version of chain on error
     * @param actionConfig
     * @return this
     * @since 2.0.1
     */
	public ActionConfig chainError(ActionConfig actionConfig) {
		return addConsequence(Action.ERROR, new Chain(actionConfig));
	}
    
    
    /**
     * Adds a list of filter for the inner action.
     *
     * @param filters A list of filters
     * @param innerAction the inner action
     * @return this action config for method chaining Ex: addConsequence().addFilter();
     * @since 1.1.1
     */
    public ActionConfig addFilter(List filters, String innerAction) {
        Iterator iter = filters.iterator();
        while(iter.hasNext()) {
            Filter f = (Filter) iter.next();
            addFilter(f, innerAction);
        }
        return this;
    }

    /**
     * Adds a filter to a list of inner actions.
     * 
     * @param filter The filter to add
     * @param args The list of inner actions
     * @return this
     * @since 1.11
     */
    public ActionConfig addFilter(Filter filter, String ... args) {
    	
    	if (args != null) {
    		
    		for(int i=0;i<args.length;i++) {
    			
    			addFilter(filter, args[i]);
    		}
    	}
    	
    	return this;
    }
    
    /**
     * Shorter version.
     * 
     * @param filter
     * @param args
     * @return this action config
     * @since 1.11
     */
    public ActionConfig filter(Filter filter, String ... args) {
    	
    	return addFilter(filter, args);
    }
    
    /**
     * Shorter version of addFilter.
     * 
     * @param filters
     * @param innerAction
     * @return this action config
     * @since 1.2
     */
    public ActionConfig filter(List filters, String innerAction) {
        return addFilter(filters, innerAction);
    }
    
    /**
     * Returns the filters for this action.
     *
     * @return The filters for this action.
     */
    public List<Filter> getFilters() {
        List<Filter> list = new ArrayList<Filter>(filters.size() + firstFilters.size());
        
        Iterator<Object[]> iter = firstFilters.iterator();
        while(iter.hasNext()) {
            Object [] array = iter.next();
            list.add((Filter) array[1]);
        }
        
        iter = filters.iterator();
        while(iter.hasNext()) {
            Object [] array = iter.next();
            list.add((Filter) array[1]);
        }
        return list;
    }
    
    /**
     * Returns the filters for this inner action.
     *
     * @param innerAction the inner action.
     * @return The filters for this action.
     * @since 1.1.1
     */
    public List<Filter> getFilters(String innerAction) {
        List<Filter> list = new ArrayList<Filter>(filters.size());
        Iterator<Object[]> iter = filters.iterator();
        while(iter.hasNext()) {
            Object [] array = iter.next();
            if (array[0] == null || array[0].equals(innerAction)) {
                list.add((Filter) array[1]);
            }
        }
        return list;
    }    
    
    /**
     * Returns the filters for this inner action, that will be executed before the global filters.
     *
     * @param innerAction the inner action.
     * @return The filters for this action.
     * @since 1.9
     */
    public List<Filter> getFirstFilters(String innerAction) {
        List<Filter> list = new ArrayList<Filter>(firstFilters.size());
        Iterator<Object[]> iter = firstFilters.iterator();
        while(iter.hasNext()) {
            Object [] array = iter.next();
            if (array[0] == null || array[0].equals(innerAction)) {
                list.add((Filter) array[1]);
            }
        }
        return list;
    }    
	
	/**
	 * Gets the name or alias of this ActionConfig.
	 * 
	 * @return The name or alias of this ActionConfig.
	 */
	public String getName() { return name; }
	
    /**
     * Gets the inner action that this action config represents.
     *
     * @return The inner action name that his action represents.
     */
    public String getInnerAction() {
        return innerAction;
    }
    
    void setInnerAction(String innerAction) {
        this.innerAction = innerAction;
    }
	
	/**
	 * Gets the consequence for the given result.
	 * 
	 * @param result The result for what to get the consequence
	 * @return The consequence associated with the result.
	 */
	public Consequence getConsequence(String result) {
		return consequences.get(result);
	}
    
	/**
	 * Gets the consequence for the given result of the given inner action.
	 * 
	 * @param result The result for what to get the consequence
     * @param innerAction The innerAction from where to get the consequence.
	 * @return The consequence associated with the result and the inner action.
	 */
	public Consequence getConsequence(String result, String innerAction) {
        Map map = innerConsequences.get(innerAction);
        if (map != null) {
            return (Consequence) map.get(result);
        }
		return null;
	}
    
    public Consequence getAutoConsequence(String result, String innerAction) {
    	
    	if (result == null) return null;
        
        StringBuffer sb = new StringBuffer(128);
        
        sb.append("/").append(dirName).append("/");
        
        if (innerAction != null) {
            
            sb.append(innerAction).append(".");
        }
        
        if (result.equals(Action.SUCCESS)) {
            
            sb.append("ok.jsp");
            
        } else {
            
            sb.append(result).append(".jsp");
        }
        
        Consequence c = new Forward(sb.toString());
        
        if (innerAction != null) {
            
            addConsequence(result, innerAction, c);
            
        } else {
            
            addConsequence(result, c);
        }
        
        return c;
    }

    /**
     * Returns an action instance to be used with this request.
     * Mentawai creates a new action instance for each request.
     * You can extend ActionConfig and override this class to integrate Mentawai 
     * with other IoC containers, that may want to create the action themselves.
     * 
     * @return The action instance to use for the request.
     */
    public Action getAction() {
    	
    	Container container = ApplicationManager.getContainer();
    	
    	if (Action.class.isAssignableFrom(actionClass)) {
    		
    		// first try to get action from container...

    		try {
    			
    			Action a = container.construct(actionClass);
    			
    			if (a != null) return a;
    			
    		} catch(Exception e) {
    			// ignore and try below...
    		}
    		
	        try {
	        	
	            return (Action) actionClass.newInstance();
	            
	        } catch(Exception e) {
	        	
	            e.printStackTrace();
	        }
	        
    	} else {
    		
    		try {
    			
    			// first try to get action from container...
    			
    			Object pojo = null;

        		try {
        			
        			pojo = container.construct(actionClass);
        			
        		} catch(Exception e) {
        			// ignore and try below...
        		}
        		
        		if (pojo == null) {
    		
        			pojo = actionClass.newInstance();
        		}
	        	
	        	PojoAction pojoAction = new PojoAction(pojo);
	        	
	            return pojoAction;
            
    		} catch(Exception e) {
    			
    			e.printStackTrace();
    		}
    	}

    	return null;
    }
    
    /**
     * Returns the action class for this action config.
     * 
     * @return The action class for this action config.
     * @since 1.2.1
     */
    public Class<? extends Object> getActionClass() {
        return actionClass;
    }
    
    /**
     * Returns the name of this ActionConfig.
     * Ex: /HelloWorld, /customers/add, etc.
     *
     * @return The name of this action.
     */
    public String toString() {
        return name;
    }
}

		
	
	
	