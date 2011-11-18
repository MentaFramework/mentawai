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

import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mentabean.BeanConfig;
import org.mentabean.BeanManager;
import org.mentacontainer.Container;
import org.mentacontainer.Factory;
import org.mentacontainer.Scope;
import org.mentacontainer.impl.MentaContainer;
import org.mentacontainer.impl.WrapperFactory;
import org.mentawai.ajax.AjaxConsequence;
import org.mentawai.ajax.AjaxRenderer;
import org.mentawai.coc.ConsequenceProvider;
import org.mentawai.db.ConnectionHandler;
import org.mentawai.filter.AuthenticationFilter;
import org.mentawai.filter.AuthorizationFilter;
import org.mentawai.filter.DependencyFilter;
import org.mentawai.filter.ExceptionFilter;
import org.mentawai.filter.InjectionFilter;
import org.mentawai.filter.IoCFilter;
import org.mentawai.filter.OutjectionFilter;
import org.mentawai.filter.OutputFilter;
import org.mentawai.filter.PushIoCFilter;
import org.mentawai.filter.RedirectAfterLoginFilter;
import org.mentawai.formatter.DateFormatter;
import org.mentawai.formatter.FormatterManager;
import org.mentawai.i18n.LocaleManager;
import org.mentawai.ioc.Bean;
import org.mentawai.ioc.DefaultComponent;
import org.mentawai.ioc.Dependency;
import org.mentawai.ioc.ScopeComponent;
import org.mentawai.jruby.RubyActionConfig;
import org.mentawai.list.DBListData;
import org.mentawai.list.ListData;
import org.mentawai.list.ListManager;
import org.mentawai.spring.SpringActionConfig;
import org.mentawai.util.DebugServletFilter;
import org.mentawai.util.SystemUtils;

/**
 * The central abstract base manager which controls actions, filters, locales and data lists.
 * You can use this class to register actions and filters through the loadActions() method.
 * You can use this class to specify supported locales through the loadLocales() method.
 * You can use this class to manage the data list loading process.
 * You can use this class to initialize anything for your web application.
 *
 * @author Sergio Oliveira Jr.
 * @author Fernando Boaglio
 */
public abstract class ApplicationManager {

	// These are shortcuts

    public static final String SUCCESS = Action.SUCCESS;
    public static final String ERROR = Action.ERROR;
    public static final String SHOW = Action.SHOW;
    public static final String LIST = Action.LIST;
    public static final String INDEX = Action.INDEX;
    public static final String MAIN = Action.MAIN;
    public static final String LOGIN = AuthenticationFilter.LOGIN;
    public static final String ACCESSDENIED = AuthorizationFilter.ACCESSDENIED;
    public static final String TEST = Action.TEST;
    public static final String JSP = Action.JSP;
    public static final String CREATED = Action.CREATED;
    public static final String UPDATED = Action.UPDATED;
    public static final String REMOVED = Action.REMOVED;
    public static final String ALREADY = Action.ALREADY;
    public static final String BLOCKED = Action.BLOCKED;
    public static final String XML = Action.XML;
    public static final String JSON = Action.JSON;
    public static final String HTML = Action.HTML;
    public static final String ADD = Action.ADD;
    public static final String EDIT = Action.EDIT;
    public static final String NEXT = "next";
    public static final String BACK = "back";
    public static final String AJAX = Action.AJAX;
    public static final String STREAM = Action.STREAM;
    public static final String NOT_FOUND = Action.NOT_FOUND;

    public static final int ACTION = PushIoCFilter.ACTION;
    public static final int REQUEST = IoCFilter.REQUEST;

    public static final int INPUT = DependencyFilter.INPUT;
    public static final int  OUTPUT = DependencyFilter.OUTPUT;
    public static final int SESSION = DependencyFilter.SESSION;
    public static final int APPLICATION = DependencyFilter.APPLICATION;

    public static final String EXCEPTION = ExceptionFilter.EXCEPTION;

    public static final String REDIR = RedirectAfterLoginFilter.REDIR;
    
    public static Environment DEFAULT_ENVIRONMENT = Environment.DEV;

    public static final String MENTAWAI_VERSION = "2.3.1";
    public static final String MENTAWAI_BUILD = "20111117";
    public static String EXTENSION = "mtw";
    public static String CONTEXT_PATH = null;
    public static int PORT = 80;

    private static String REALPATH;

	private Map<String, ActionConfig> actions = new HashMap<String, ActionConfig>();
    private Map<String, Map<String, ActionConfig>> innerActions = new HashMap<String, Map<String, ActionConfig>>();
	private List<Filter> globalFilters = new LinkedList<Filter>();
    private List<Filter> globalFiltersLast = new LinkedList<Filter>();
	private Map<String, Consequence> globalConsequences = new HashMap<String, Consequence>();
    private Map<String, Bean> components = new HashMap<String, Bean>();
    private Set<Dependency> dependencies = new HashSet<Dependency>();

	private Map<Class<? extends Object>, List<Filter>> klassGlobalFilters = new HashMap<Class<? extends Object>, List<Filter>>();
    private Map<Class<? extends Object>, List<Filter>> klassGlobalFiltersLast = new HashMap<Class<? extends Object>, List<Filter>>();

    static ApplicationManager instance = null;

    private String reqCharEncoding = null;

    private static String viewDir = null;

    private final BeanManager beanManager = new BeanManager();

    private static ActionConfig defaultAction = null;

    private static Context appContext = null;

    protected static Container container = null;

    static boolean removeActionFromName = false;

    private boolean autowireEverything = true;

    private ApplicationManager parent;

    public enum Environment { TEST, DEV, INT, QA, PROD };

    private volatile Environment environment = null;

    public void setEnvironment(Environment env) {
    	this.environment = env;
    }

    public Environment getEnvironment() {

    	if (environment == null) {

    		String envString = SystemUtils.getString("env");

    		if (envString == null) envString = SystemUtils.getString("ENV");

    		if (envString == null) {
    			this.environment = DEFAULT_ENVIRONMENT;
    		} else {
    			this.environment = Environment.valueOf(envString.toUpperCase());
    		}
    	}
    	return environment;
    }

    public static void setRemoveActionFromName(boolean flag) {
    	ApplicationManager.removeActionFromName = flag;
    }

    private List<String> actionPackages = new LinkedList<String>();

    static void setApplication(Context appContext) {

       ApplicationManager.appContext = appContext;
    }

    public static Context getApplication() {

       return appContext;
    }

    public static ApplicationManager getInstance() {

        return instance;
    }

    public static void setRealPath(String realpath) {

        REALPATH = realpath;

    }

    public void setAutowireEverything(boolean flag) {
    	this.autowireEverything = flag;
    }

    public void setReqCharEncoding(String encoding) {
    	this.reqCharEncoding = encoding;
    }

    public String getReqCharEncoding() {
    	return reqCharEncoding;
    }

    private String findHostName() {

        try {

            return InetAddress.getLocalHost().getHostName();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

    public static String getContextPath() {
    	return CONTEXT_PATH;
    }

    public static void setDefaultAction(ActionConfig ac) {

       ApplicationManager.defaultAction = ac;
    }

    public static ActionConfig getDefaultAction() {

       return defaultAction;
    }

    public Props getProperties() {

    	if (REALPATH == null) throw new IllegalStateException("Realpath is not set for this application!");

    	String hostname = findHostName();

      File fileWithHostname = null;

    	if (hostname != null) {

    	   fileWithHostname = new File(REALPATH + File.separator + "WEB-INF" + File.separator + "appManager-" + hostname + ".properties");

      }

    	File file = new File(REALPATH + File.separator + "WEB-INF" + File.separator + "appManager.properties");

    	if (fileWithHostname != null && fileWithHostname.exists()) {

    		Props props = new Props();

    		try {

    			props.load(new FileInputStream(fileWithHostname));

    		} catch(Exception e) {

    			throw new RuntimeException(e);
    		}

    		return props;

    	} else if (file.exists()) {

    		Props props = new Props();

    		try {

    			props.load(new FileInputStream(file));

    		} catch(Exception e) {

    			throw new RuntimeException(e);
    		}

    		return props;

    	} else {

    		throw new RuntimeException("Cannot find appManager.properties or appManager-HOSTNAME.properties inside WEB-INF!");
    	}

    }

    public Props getProps() {
    	return getProps(getEnvironment().toString().toLowerCase());
    }

    public Props getProps(String env) {

    	if (REALPATH == null) throw new IllegalStateException("Realpath is not set for this application!");

    	File def = new File(REALPATH + File.separator + "WEB-INF" + File.separator + "conf" + File.separator + "default" + File.separator + "appManager.properties");

    	File file = null;

    	if (env != null) file = new File(REALPATH + File.separator + "WEB-INF" + File.separator + "conf" + File.separator + env + File.separator + "appManager.properties");

    	if (!def.exists() && (file == null || !file.exists())) {

    		throw new RuntimeException("Cannot find appManager.properties inside WEB-INF/conf!");
    	}

    	File local = null;

    	if (env != null) local = new File(REALPATH + File.separator + "WEB-INF" + File.separator + "conf" + File.separator + env + File.separator + "appManager-LOCAL.properties");

    		Props props = new Props();

    	if (def.exists()) {

    		try {

    			props.loadDefault(new FileInputStream(def));

    		} catch(Exception e) {

    			throw new RuntimeException(e);
    		}
    	}

    	if (file != null && file.exists()) {

    		try {

    			props.load(new FileInputStream(file));

    		} catch(Exception e) {

    			throw new RuntimeException(e);
    		}
    	}

    	if (local != null && local.exists()) {

    		try {

    			props.loadLocal(new FileInputStream(local));

    		} catch(Exception e) {

    			throw new RuntimeException(e);
    	}

    }

    	return props;
    }


    public StreamConsequence stream(String contentType) {

    	return new StreamConsequence(contentType);
    }

    public StringConsequence string(String key) {
    	return new StringConsequence(key);
    }

    public StringConsequence string() {
    	return new StringConsequence();
    }

    public void addActionPackage(String actionPackage) {

    	actionPackages.add(actionPackage);
    }

    public void removeActionPackage(String actionPackage) {

    	actionPackages.remove(actionPackage);
    }

    public static void setViewDir(String viewDir) {

    	ApplicationManager.viewDir = viewDir;
    }

    public static String getViewDir() {

    	return viewDir;
    }

    /**
     * Returns this web application's real path.
     * For example: c:\program files\tomcat\webapps\myapplication
     *
     * @return The real path
     */
    public static String getRealPath() {
        return REALPATH;
    }

    /**
     * Default constructor
     */
    public ApplicationManager(){
    	super();
    	instance = this;
    	container = new MentaContainer();
    }

    public void setWebappPath(String s) {
    	REALPATH = s;
    }

    public static Container getContainer() {

    	return container;
    }

    /**
     * Reset this application manager. All configuration (actions, filters, etc) is discarded.
     */
    public void reset() {
        actions.clear();
        innerActions.clear();
        globalFilters.clear();
        globalFiltersLast.clear();
        globalConsequences.clear();
    }

    void service(Context appContext, HttpServletRequest req, HttpServletResponse res) {

    }

	/**
	 * Register an ActionConfig for the Mentawai controller.
     *
     * Note: Starting from version 1.2, this method is returning the action config it receives.
	 *
	 * @param ac The ActionConfig to register
     * @return The ActionConfig it receives to register
     * @throws IllegalStateException if you try to add an action config with no name (internal action config)
	 */
	public ActionConfig addActionConfig(ActionConfig ac) {
        if (ac.getName() == null) throw new IllegalStateException("Cannot add an action config without a name!");
        String innerAction = ac.getInnerAction();
        if (innerAction == null) {
		    actions.put(ac.getName(), ac);
        } else {
            Map<String, ActionConfig> map = innerActions.get(ac.getName());
            if (map == null) {
                map = new HashMap<String, ActionConfig>();
                innerActions.put(ac.getName(), map);
            }
            map.put(innerAction, ac);
        }

        return ac;
	}

	public BeanConfig addBeanConfig(BeanConfig bc) {

		beanManager.addBeanConfig(bc);

		return bc;

	}

	public BeanConfig getBeanConfig(Class<? extends Object> beanClass) {

		return beanManager.getBeanConfig(beanClass);
	}

	public BeanManager getBeanManager() {
		return beanManager;
	}

	public BeanConfig bean(Class<? extends Object> beanClass, String tableName) {

		return addBeanConfig(new BeanConfig(beanClass, tableName));
	}

	/**
	 * Remove an action config from this application manager.
	 *
	 * @param ac The action config to remove
	 * @return true if removed, false if not found
	 * @since 1.8
	 */
	public boolean removeActionConfig(ActionConfig ac) {

		String name = ac.getName();

		if (name == null) throw new IllegalStateException("Cannot remove an action config without a name!");

		String innerAction = ac.getInnerAction();

		if (innerAction == null) {

			return actions.remove(name) != null;

		} else {

			Map<String, ActionConfig> map = innerActions.get(name);

            if (map != null) {

            	return map.remove(innerAction) != null;
            }

            return false;
		}
	}

    /**
     * Shorter version of addActionConfig.
     *
     * @param ac
     * @since 1.2
     * @return The ActionConfig it receives
     */
    public ActionConfig add(ActionConfig ac) {
        return addActionConfig(ac);
    }

    /**
     * Override this method to do any initialization for your web application.
     */
    public void init() {

    	init(ApplicationManager.getApplication());

    }

    /**
     * Override this method to do any initialization for your web application.
     *
     * @param application The application context of your web application.
     * @since 1.1
     */
    public void init(Context application) {

    }

    /**
     * Called by the controller when the application is exiting.
     *
     * OBS: This is called by the Controller servlet's destroy method.
     *
     * @param application
     * @since 1.4
     */
    public void destroy(Context application) {


    }

    public void setupDB() { }

    /**
     * Override this method to register your Filters.
     */
    public void loadFilters() { }

    /**
     * Override this method to register your IoC Components.
     */
    public void setupIoC() { }

/**
     * Override this method to register your mentabeans.
     */
    public void loadBeans() { }

	/**
	 * Override this method to register actions and filters in this application manager.
	 */
	public void loadActions() { }

	/**
	 * Override this method to specify the supported locales for your application.
	 */
	public void loadLocales() {	}

	/**
	 * Override this method to control the data list loading process.
	 */
	public void loadLists() throws Exception {

	}

    /**
     * Override this method to define formatters that can be used by mtw:out tag
     */
    public void loadFormatters() {

    }

    public void onStarted(Context context) {

    }

    public void addLocale(String loc) {
    	LocaleManager.add(loc);
    }

    public void addLocale(Locale loc) {
    	LocaleManager.add(loc);
    }

	/**
	 * Gets the ActionConfig with the given name or alias.
	 *
	 * @param name The name of the ActionConfig
	 * @return The ActionConfig associated with the given name
	 */
	public ActionConfig getActionConfig(String name) {

		ActionConfig ac = actions.get(name);

		if (ac == null) {

			ac = loadActionConfig(name);
		}

		return ac;
	}


	protected Map<String, ActionConfig> getActions() {
		return actions;
	}

	/**
	 * Gets the Inner ActionConfig with the given name and inner action.
	 *
	 * @param name The name of the ActionConfig
     * @param innerAction (Optional) The inner action of the ActionConfig.
	 * @return The Inner ActionConfig associated with the given name and inner action.
	 */
	public ActionConfig getActionConfig(String name, String innerAction) {

		if(innerAction == null)
			return getActionConfig(name);

		ActionConfig ac = null;

        Map<String, ActionConfig> map = innerActions.get(name);

        if (map != null) {

        	ac = map.get(innerAction);
        }

        if (ac == null) {

        	ac = loadActionConfig(name);
        }

        return ac;
	}

	private ActionConfig loadActionConfig(String name) {

		StringBuilder sb = new StringBuilder(32);

		Iterator<String> iter = actionPackages.iterator();

		while(iter.hasNext()) {

			String actionPackage = iter.next();

			sb.setLength(0);

			sb.append(actionPackage).append('.').append(name);

			// check if action is in the classpath...

			Class<? extends Object> actionClass = null;

			try {

				actionClass = Class.forName(sb.toString());

			} catch(Exception e) {

				continue;
			}

			ActionConfig ac = new ActionConfig(actionClass);

			addActionConfig(ac);

			return ac;
		}

		return null;
	}

	/**
	 * Register a filter for all actions in this application manager.
	 * The filters registered with this method will be executed <i>before</i>
     * the specific action filters.
     *
	 * @param filter The filter to register as a global filter.
	 */
	public void addGlobalFilter(Filter filter) {
		addGlobalFilter(filter, false);
	}

    /**
     * Shorter version of addGlobalFilter.
     *
     * @param filter
     * @since 1.2
     */
    public void filter(Filter filter) {
        addGlobalFilter(filter);
    }

	/**
	 * Register a list of filters for all actions in this application manager.
	 * The filters registered with this method will be executed <i>before</i>
     * the specific action filters.
     *
	 * @param filters A list of filters.
     * @since 1.1.1
	 */
	public void addGlobalFilter(List filters) {
		addGlobalFilter(filters, false);
	}

    /**
     * Shorter version of addGlobalFilter.
     *
     * @param filters
     * @since 1.2
     */
    public void filter(List filters) {
        addGlobalFilter(filters);
    }

	/**
	 * Register a filter for all actions in this application manager.
	 *
	 * @param filter The filter to register as a global filter.
     * @param last true if you want this filter to be executed <i>after</i> the specific action filters.
     * @since 1.1.1
	 */
	public void addGlobalFilter(Filter filter, boolean last) {
        if (last) {

            globalFiltersLast.add(filter);

        } else if (filter.getClass().equals(InjectionFilter.class)
                || filter.getClass().equals(OutputFilter.class)
                || filter.getClass().equals(OutjectionFilter.class)) {

        	if (filter instanceof OutputFilter) {

        		OutputFilter outputFilter = (OutputFilter) filter;

        		if (outputFilter.isNewVersion()) {

        			globalFilters.add(filter);

        		} else {

        			globalFiltersLast.add(filter);
        		}

        	} else {

        		// force those filters to be the last in the chain, because if they are global that what makes sense...

        		globalFiltersLast.add(filter);
        	}

        } else {

		    globalFilters.add(filter);
        }
	}

	public void addGlobalFilter(Class<? extends Object> klass, Filter filter, boolean last) {

        if (last) {

        	List<Filter> list = klassGlobalFiltersLast.get(klass);

        	if (list != null) {

        		list.add(filter);

        	} else {

        		list = new LinkedList<Filter>();

        		list.add(filter);

        		klassGlobalFiltersLast.put(klass, list);
        	}

        } else {

        	List<Filter> list = klassGlobalFilters.get(klass);

        	if (list != null) {

        		list.add(filter);

        	} else {

        		list = new LinkedList<Filter>();

        		list.add(filter);

        		klassGlobalFilters.put(klass, list);
        	}
        }
	}

	public void filter(Class<? extends Object> klass, Filter filter) {

		addGlobalFilter(klass, filter, false);
	}

	public void filterLast(Class<? extends Object> klass, Filter filter) {

		addGlobalFilter(klass, filter, true);
	}

    /**
     * Shorter version of addGlobalFilter.
     *
     * @param filter
     * @param last
     * @since 1.2
     */
    public void filter(Filter filter, boolean last) {
        addGlobalFilter(filter, last);
    }

    /**
     * Shorter version of addFlobalFilter.
     *
     * @param filter
     * @since 1.3
     */
    public void filterLast(Filter filter) {
    	addGlobalFilter(filter, true);
    }

	/**
	 * Register a list of filters for all actions in this application manager.
	 *
	 * @param filters A list of filters.
     * @param last true if you want these filters to be executed <i>after</i> the specific action filters.
     * @since 1.1.1
	 */
	public void addGlobalFilter(List filters, boolean last) {
        Iterator iter = filters.iterator();
        while(iter.hasNext()) {
            Filter f = (Filter) iter.next();
            addGlobalFilter(f, last);
        }
	}

    /**
     * Shorter version of addGlobalFilter.
     *
     * @param filters
     * @param last
     * @since 1.2
     */
    public void filter(List filters, boolean last) {
        addGlobalFilter(filters, last);
    }

    /**
     * Shorter version of addGlobalFilter.
     *
     * @param filters
     * @since 1.3
     */
    public void filterLast(List filters) {
    	addGlobalFilter(filters, true);
    }

	/**
	 * Register a consequence for all actions in this application manager.
	 * A global consequence has precedence over action consequences.
	 *
	 * @param result The result for what a global consequence will be registered
	 * @param c The consequence to register as a global consequence
	 */
	public void addGlobalConsequence(String result, Consequence c) {
		globalConsequences.put(result, c);
	}

    /**
     * Shorter version of addGlobalConsequence.
     *
     * @param result
     * @param c
     * @since 1.2
     */
    public void on(String result, Consequence c) {
        addGlobalConsequence(result, c);
    }

    /**
     * Shorter verions of addGlobalConsequence that will assume a forward.
     *
     * @param result
     * @param jsp
     * @since 1.9
     */
    public void on(String result, String jsp) {

    	addGlobalConsequence(result, new Forward(jsp));

    }

	/**
	 * Gets the global filters registered in this application manager.
	 *
     * @param last true if you want the global filters registered to be executed <i>after</i> the specific action filters.
	 * @return A java.util.List with all the filters registered in this application manager.
     * @since 1.1.1
	 */
	public List<Filter> getGlobalFilters(boolean last) {
        if (last) return globalFiltersLast;
        return globalFilters;
	}

	public List<Filter> getGlobalFilters(Class<? extends Object> klass, boolean last) {

		if (last) {

			return klassGlobalFiltersLast.get(klass);

		} else {

			return klassGlobalFilters.get(klass);
		}

	}

	/**
	 * Gets all the global filters registered in this application manager.
     * Note that it will sum up in a list the filters executed <i>before</i> and <i>after</i> the specific action filters.
	 *
	 * @return A java.util.List with all the filters registered in this application manager.
	 */
	public List<Filter> getGlobalFilters() {
        List<Filter> list = new LinkedList<Filter>();
        list.addAll(getGlobalFilters(false));
        list.addAll(getGlobalFilters(true));
        return list;
	}

	/**
	 * Gets a global consequence associated with the result.
	 *
	 * @param result The result for what to get a global consequence.
	 * @return A global consequence for the result.
	 */
	public Consequence getGlobalConsequence(String result) {
		return globalConsequences.get(result);
	}

    /*
     * This is useful for filter destroying in the Controller.
     */
    Set<Filter> getAllFilters() {
        Set<Filter> filters = new HashSet<Filter>();
        filters.addAll(globalFilters);
        filters.addAll(globalFiltersLast);

        Iterator<ActionConfig> iterAc = actions.values().iterator();
		while(iterAc.hasNext()) {
            ActionConfig ac = (ActionConfig) iterAc.next();
            filters.addAll(ac.getFilters());
        }

		Iterator<Map<String, ActionConfig>> iter = innerActions.values().iterator();

        while(iter.hasNext()) {

        	Map<String, ActionConfig> map = iter.next();

            Iterator iter2 = map.values().iterator();

            while(iter2.hasNext()) {

                ActionConfig ac = (ActionConfig) iter2.next();

                filters.addAll(ac.getFilters());

            }
        }

        return filters;
    }

    /**
     * Convenient method that provides a less verbose way to create a forward.
     *
     * This is shorter than new Forward("/foo.jsp").
     *
     * @param page
     * @return a new forward consequence
     * @since 1.2
     */
    public static Consequence fwd(String page) {

        return new Forward(page);

    }

    /**
     * Convenient method that provides a less verbose way to create a redirect.
     *
     * This is shorter than new Redirect("/foo.jsp").
     *
     * @param page
     * @return a new redirect consequence
     * @since 1.2
     */
    public static Consequence redir(String page) {

        return new Redirect(page);

    }

    public static Consequence exception(String msg) {

    	return new ExceptionConsequence(msg);
    }

 public static Consequence result() {

    	return new ResultConsequence();
    }

    /**
     * Redir to an action.
     *
     * @param ac
     * @return Consequence
     * @since 1.16
     */
    public static Consequence redir(ActionConfig ac) {

    	return new Redirect(ac);
    }


    /**
     * Convenient method that provides a less verbose way to create a redirect to a Action.
     *
     * This is shorter than <code>new Redirect("FoobarAction.mtw"); </code> <br/>
     *
     * <b>Notice</b> that the action is generated when the pattern above, if you are using a PrettyURLController will not work
     * and is indicated use normal redirect: {@link #redir(String, boolean)}
     *
     * @param klass
     * @return a new redirect consequence
     * @since 1.15.1
     */
    public static Consequence redir(Class<?> klass) {
        return redir(klass, null, false);
    }

    /**
     * Convenient method that provides a less verbose way to create a redirect to a Action.
     *
     * This is shorter than <code>new Redirect("FoobarAction.mtw"); </code> <br/>
     *
     * <b>Notice</b> that the action is generated when the pattern above, if you are using a PrettyURLController will not work
     * and is indicated use normal redirect: {@link #redir(String, boolean)}
     *
     * @param klass
     * @param appendOutput is true if this redirect is to use dynamic parameters.
     * @return a new redirect consequence
     * @since 1.15.1
     */
    public static Consequence redir(Class<?> klass, boolean appendOutput) {
        return redir(klass, null, appendOutput);
    }

    /**
     * Convenient method that provides a less verbose way to create a redirect to a Action.
     *
     * This is shorter than <code>new Redirect("FoobarAction.saveFoo.mtw"); </code> <br/>
     *
     * <b>Notice</b> that the action is generated when the pattern above, if you are using a PrettyURLController will not work
     * and is indicated use normal redirect: {@link #redir(String, boolean)}
     *
     * @param klass
     * @param innerAction
     * @return a new redirect consequence
     * @since 1.15.1
     */
    public static Consequence redir(Class<?> klass, String innerAction) {
    	return redir(klass, innerAction, false);
    }


    /**
     * Convenient method that provides a less verbose way to create a redirect to a Action.
     *
     * This is shorter than <code>new Redirect("FoobarAction.saveFoo.mtw"); </code> <br/>
     *
     * <b>Notice</b> that the action is generated when the pattern above, if you are using a PrettyURLController will not work
     * and is indicated use normal redirect: {@link #redir(String, boolean)}
     *
     * @param klass
     * @param innerAction
     * @param appendOutput
     * @return a new redirect consequence
     * @since 1.15.1
     */
    public static Consequence redir(Class<?> klass, String innerAction, boolean appendOutput) {
    	StringBuilder page = new StringBuilder(klass.getSimpleName().length() + 4);

    	page.append(klass.getSimpleName());

    	if(innerAction != null) page.append(".").append(innerAction);

    	page.append(".").append(EXTENSION);

        return new Redirect(page.toString(), appendOutput);
    }


    /**
     * Convenient method that provides a less verbose way to create a redirect.
     *
     * This is shorter than new Redirect("/foo.jsp", true).
     *
     * @param page
     * @param flag
     * @return a new redirect consequence
     * @since 1.3
     */
    public static Consequence redir(String page, boolean flag) {

        return new Redirect(page, flag);

    }

    /**
     * Redir to an action.
     *
     * @param ac
     * @param flag
     * @return Consequence
     * @since 1.16
     */
    public static Consequence redir(ActionConfig ac, boolean flag) {

    	return new Redirect(ac, flag);
    }

    /**
     * Convenient method that provides a less verbose way to create a redirect.
     *
     * This is shorter than new Redirect().
     *
     * @return a new redirect consequence
     * @since 1.3
     */
    public static Consequence redir() {

        return new Redirect();

    }

    /**
     * Convenient method that provides a less verbose way to create a redirect.
     *
     * This is shorter than new Redirect().
     *
     * @param flag
     * @return a new redirect consequence
     * @since 1.4
     */
    public static Consequence redir(boolean flag) {

        return new Redirect(flag);

    }

    /**
     * Convenient method that provides a less verbose way to create a chain.
     *
     * This is shorter than new Chain("/foo.jsp").
     *
     * @param ac The action config to chain
     * @return a new chain consequence
     * @since 1.2
     */
    public static Consequence chain(ActionConfig ac) {

        return new Chain(ac);

    }

    public static Consequence ajax(AjaxRenderer renderer) {

       return new AjaxConsequence(renderer);
    }

    /**
     * Convenient method that provides a less verbose way to create a chain.
     *
     * @param ac
     * @param innerAction
     * @return a new chain consequence
     * @since 1.12
     */
    public static Consequence chain(ActionConfig ac, String innerAction) {

       return new Chain(ac, innerAction);
    }

    /**
     * Convenient method for setting a chain.
     *
     * @param klass
     * @return a new chain consequence
     * @since 1.11
     */
    public static Consequence chain(Class<? extends Object> klass) {

       return new Chain(klass);

    }

    /**
     * Convenient method for setting a chain.
     *
     * @param klass
     * @param innerAction
     * @return a new chain consequence
     * @since 1.11
     */
    public static Consequence chain(Class<? extends Object> klass, String innerAction) {

       return new Chain(klass, innerAction);
    }


    /**
     * Convenient method that provides a less verbose way to create a ClassActionConfig.
     * <br/>
     * <b>Note:</b> This will also add the action to this ApplicationManager, in other words,
     * no need to call add or addActionConfig !!!
     *
     * @param klass
     * @return a new ActionConfig
     * @since 1.3
     */
    public ActionConfig action(Class<? extends Object> klass) {

        return addActionConfig(new ActionConfig(klass));
    }

    public ActionConfig ruby(String klass) {
    	return addActionConfig(new RubyActionConfig(klass));
    }

    public ActionConfig ruby(String name, String klass) {
    	return addActionConfig(new RubyActionConfig(name, klass));
    }

    public ActionConfig ruby(String name, String klass, String innerAction) {
    	return addActionConfig(new RubyActionConfig(name, klass, innerAction));
    }

    /**
     * Convenient method that provides a less verbose way to create a SpringActionConfig.
     * <br/>
     * <b>Note:</b> This will also add the action to this ApplicationManager, in other words,
     * no need to call add or addActionConfig !!!
     *
     * @param path
     * @param action
     * @return a new SpringActionConfig
     * @since 1.3
     */
    public ActionConfig spring(String path, String action) {

        return addActionConfig(new SpringActionConfig(path, action));
    }


    /**
	 * Convenient method that provides a less verbose way to create a
	 * SpringActionConfig. <br/>
	 * <b>Note:</b> This will also add the action to this ApplicationManager, in
	 * other words, no need to call add or addActionConfig !!!
	 *
	 * @param path
	 * @param action
	 * @param innerAction
	 * @return a new SpringActionConfig
	 * @since 1.0
	 */
	public ActionConfig spring(String path, String action, String innerAction) {
		return addActionConfig(new SpringActionConfig(path, action, innerAction));
	}

    /**
     * Convenient method that provides a less verbose way to create an action config.
     * <br/>
     * <b>Note:</b> This will also add the action to this ApplicationManager, in other words,
     * no need to call add or addActionConfig !!!
     *
     * @param name
     * @param klass
     * @return a new action config
     * @since 1.2
     */
    public ActionConfig action(String name, Class<? extends Object> klass) {

        return addActionConfig(new ActionConfig(name, klass));

    }

    public ActionConfig internal(Class<? extends Object> klass, String innerAction) {

    	ActionConfig ac = action(klass, innerAction);

    	ac.catchAll(result()).internalOnly();

    	return ac;
    }

    /**
     * Convenient method that provides a less verbose way to create an action config.
     * <br/>
     * <b>Note:</b> This will also add the action to this ApplicationManager, in other words,
     * no need to call add or addActionConfig !!!
     *
     * @param name
     * @param klass
     * @param innerAction
     * @return a new action config
     * @since 1.2
     */
    public ActionConfig action(String name, Class<? extends Object> klass, String innerAction) {

        return addActionConfig(new ActionConfig(name, klass, innerAction));

    }

    /**
     * Convenient method that provides a less verbose way to create a ClassActionConfig.
     * <br/>
     * <b>Note:</b> This will also add the action to this ApplicationManager, in other words,
     * no need to call add or addActionConfig !!!
     *
     * @param klass
     * @param innerAction
     * @return a new ClassActionConfig
     * @since 1.3
     */
    public ActionConfig action(Class<? extends Object> klass, String innerAction) {

        return addActionConfig(new ActionConfig(klass, innerAction));

    }

    /**
     * Turn on/off application manager auto-reload feature.
     * In order for this to work you must me using JRebel
     * to force a class reload. More info here: http://www.jrebel.com
     *
     * @param reloadable
     */
    public void setReloadable(boolean reloadable) {
    	Controller.reloadAppManager = reloadable;
    }

    /**
     * Turn on/off the debug mode here.
     * This can also be done in the web.xml file.
     *
     * @param debugMode
     */
    public void setDebugMode(boolean debugMode) {

        Controller.debugMode = debugMode;

    }

    /**
     * Turn on/off the debug mode here.
     * This can also be done in the web.xml file.
     *
     * @param debugMode
     * @param commented
     */
    public void setDebugMode(boolean debugMode, boolean commented) {

        Controller.debugMode = debugMode;

        DebugServletFilter.COMMENTED = commented;
    }

    /**
     * Turn on/off the statsMode mode here.
     *
     * @param statsMode
     */
    public void setStatsMode(boolean statsMode) {

        Controller.statsMode = statsMode;

    }


    /**
     * Turn on/off the reload mode of application manager.
     * This can also be done in the web.xml file.
     *
     * @param reloadMode
     */
    public void setReloadMode(boolean reloadMode) {

        Controller.reloadAppManager = reloadMode;

    }

    public Dependency autowire(String target, Class<? extends Object> klass, String source) {

    	return addDependency(klass, target, source);
    }

    public Dependency autowire(String target, Class<? extends Object> klass) {

    	return addDependency(klass, target, target);
    }

    public Dependency autowire(String sourceFromContainer) {

    	Dependency d;

    	container.autowire(sourceFromContainer);

    	Class<? extends Object> klass = container.getType(sourceFromContainer);

    	dependencies.add(d = new Dependency(klass, sourceFromContainer, sourceFromContainer));

    	return d;
    }

    public Dependency addDependency(Class<? extends Object> klass, String target, String source) {

    	Dependency d;

    	container.autowire(source, target);

    	dependencies.add(d = new Dependency(klass, target, source));

    	return d;
    }

    public Dependency addDependency(Class<? extends Object> klass, String target) {

    	return addDependency(klass, target, target);
    }


    public Dependency di(String target, Class<? extends Object> klass) {

    	return addDependency(klass, target, target);
    }

    public Dependency di(String target, Class<? extends Object> klass, String source) {

    	return addDependency(klass, target, source);
    }

    /**
     * @deprecated Use autowire instead.
     */
    public Dependency autoWiring(String target, Class<? extends Object> klass) {
    	return di(target, klass);
    }

    public Dependency aw(String target, Class<? extends Object> klass, String source) {

    	return di(target, klass, source);
    }

    public Dependency aw(String target, Class<? extends Object> klass) {
    	return di(target, klass);
    }

    /**
     * @deprecated Use autowire instead.
     */
    public Dependency autoWiring(String target, Class<? extends Object> klass, String source) {

    	return di(target, klass, source);
    }

    public Iterator<Dependency> getDependencies() {

    	return dependencies.iterator();
    }

    public Map<String, Bean> getComponents() {
		return components;
	}

    public Set<String> getComponentNames() {
		return components.keySet();
	}

    /**
     * Turn on/off auto view discovery. Default is on!
     * This can also be done in the web.xml file.
     *
     * Auto view gives the controller the ability to generate
     * forward consequences automatically for the results it cannot
     * find a consequence, so that you don't need to define consequences
     * for your actions in the configuration.
     *
     * @param autoView
     */
    public void setAutoView(boolean autoView) {

        Controller.autoView = autoView;

    }

    /**
     * Add an IOC component to this application manager.
     *
     * @param name
     * @param comp
     * @return The component just added
     */
    public Bean addComponent(String name, Bean comp) {

    	if (comp == null) return null;

    	/*
    	 * MentaContainerFilter will clean the thread scope.
    	 *
    	 * Since this are actions, it does not hurt to use a THREAD scope
    	 * instead of NONE.
    	 */
    	Scope scope = Scope.THREAD;

    	if (comp instanceof ScopeComponent) {

    		ScopeComponent sc = (ScopeComponent) comp;

    		if (sc.getScope() == APPLICATION) scope = Scope.SINGLETON;
    	}

    	container.ioc(name, comp.getType(), scope);

    	if (autowireEverything) {
    		container.autowire(name);
    	}

        components.put(name, comp);

        return comp;

    }

    /**
     * Add an IOC component to this application manager.
     *
     * @param name
     * @param comp
     * @return The component just added
     */
    public Bean add(String name, Bean comp) {

        addComponent(name, comp);

        return comp;
    }

    /**
     * Gets an IOC component from this application manager.
     *
     * @param name
     * @return The ioc component
     */
    public Bean getComponent(String name) {

        return components.get(name);
    }

    /**
     * Add an IOC component to this application manager.
     *
     * @param name
     * @param comp
     * @return The component just added
     */
    public Bean ioc(String name, Bean comp) {

        addComponent(name, comp);

        return comp;
    }

    public void ioc(String name, Factory c) {

    	ioc(name, c, Scope.THREAD);
    }

    public void ioc(String name, Object singleInstance) {
    	WrapperFactory f = new WrapperFactory(singleInstance);
    	ioc(name, f);
    }

    public void ioc(String name, Factory c, Scope s) {

    	if (c != null) {

    		container.ioc(name, c, s);

    		if (autowireEverything) {
    			container.autowire(name);
    		}
    	}

    }

    /**
     * Add an IOC component to this application manager.
     * @param name
     * @param klass
     * @return The component just added
     */
    public DefaultComponent ioc(String name, Class<? extends Object> klass) {

        DefaultComponent c;

        addComponent(name, c = new DefaultComponent(klass));

        return c;
    }

    /**
     * Add an IOC component to this application manager.
     *
     * @param name
     * @param klass
     * @param scope
     * @return The component just added
     */
    public ScopeComponent ioc(String name, Class klass, int scope) {

        ScopeComponent c;

        addComponent(name, c = new ScopeComponent(klass, scope));

        return c;
    }

    /**
     * Look inside a package and load all actions classes that implement the interface Configurable.
     *
     * Call the method configure to configure these actions.
     *
     * This is useful if you prefer to place the configuration for an action inside the action itself.
     *
     * OBS: By doing this you are spreading your configuration all over your code, so this is not recommended
     * for serious projects. This boils down to a matter of taste, so Mentawai provides this as well.
     *
     * @param packageName
     */
    public void configureActions(String packageName) {

        try {

            List<Class> classes = getActionClasses(packageName);

            Iterator<Class> iter = classes.iterator();

            while(iter.hasNext()) {

                Class klass = iter.next();

                if (Configurable.class.isAssignableFrom(klass)) {

                    try {

                        Configurable config = (Configurable) klass.newInstance();

                        config.configure(this);

                    } catch(Exception e) {

                        System.err.println("Could not load class: " + klass);

                        e.printStackTrace();
                    }

                }
            }

        } catch(ClassNotFoundException e) {

            System.err.println("Could not load package: " + packageName);

            e.printStackTrace();

        }
    }

    private static List<Class> getActionClasses(String packageName) throws ClassNotFoundException {

        List<Class> classes = new LinkedList<Class>();

        // Get a File object for the package
        File directory = null;

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        String path = packageName.replace('.', '/');

        URL resource = loader.getResource(path);

        if (resource == null) {

            throw new ClassNotFoundException("No resource for " + path);

        }

        try {

            String filename = URLDecoder.decode(resource.getFile(), "UTF-8");

            if (File.separatorChar == '\\' && filename.startsWith("/")) {

                filename = filename.substring(1, filename.length());
            }

            directory = new File(filename);

        } catch(Exception e) {

            throw new ClassNotFoundException("Problem getting filename for " + resource.getFile());
        }

        if (directory.exists()) {

            // Get the list of the files contained in the package

            String[] files = directory.list();

            for (int i = 0; i < files.length; i++) {

                // we are only interested in .class files

                if (files[i].endsWith(".class")) {

                    String classname = packageName + '.' + files[i].replaceAll(".class","");

                    Class teste = Class.forName(classname);

                    //Get a Class different of interface, anonymous class an equals BaseAction by Mentawai
                    if (!teste.isInterface() &&
                            (teste.getSuperclass().equals(BaseAction.class) || Action.class.isAssignableFrom(teste))) {

                        classes.add(teste);

                    }
                }
            }
        } else {

            throw new ClassNotFoundException(packageName + " does not appear to be a valid package");

        }

        return classes;

    }

	/**
     * This method override the ApplicationManager attributes.
	 * @param The parent ApplicationManager
	 * @return
	 */
	ApplicationManager setParent(ApplicationManager parent) {
    	this.actions = parent.actions;
    	this.components = parent.components;
    	this.globalConsequences = parent.globalConsequences;
    	this.globalFilters = parent.globalFilters;
    	this.globalFiltersLast = parent.globalFiltersLast;
    	this.innerActions = parent.innerActions;
    	this.parent = parent;

    	return this; // FIXME: Na Documentação DIZ: The parent ApplicationManager, mas ele retorna this. Está certo ??? (by Ricardo Rufino)
	}

	/**
	 * Retorna o ApplicationManager que registou esse ApplicationManager. <br/>
	 * @return Uma instância de {@link MultiApplicationManager} ou NULL se este for o ApplicationManager de mais auto nível.
	 */
	public ApplicationManager getParent() {
		return parent;
	}

   /**
    * Sets the consequence provider that will be used by the controller.
    *
    * @param consequenceProvider
    * @since 1.11
    */
   public void setConsequenceProvider(ConsequenceProvider consequenceProvider) {

      Controller.setConsequenceProvider(consequenceProvider);
   }

   /**
    * Get the consequence provider that will be used by the controller.
    *
    * @since 2.1.2
    */
	public ConsequenceProvider getConsequenceProvider() {
		return Controller.getConsequenceProvider();
	}

   /**
    * Adds the list to the ListManager, so there is no need to use ListManager.addList
    *
    * @param list
    * @param connHandler
    * @since 1.11
    */
   public void addList(DBListData list, ConnectionHandler connHandler) {

      ListManager.addList(list, connHandler);
   }

   public void addLocalizedLists(ConnectionHandler connHandler, String ...lists) {

	   for(String list: lists) {

		   // assume the list table in the database will begin with a capital letter...

		   String tableName = list.substring(0, 1).toUpperCase() + list.substring(1);

		   addList(new DBListData(list, "id", "value", "locale",tableName, "id"), connHandler);

	   }
   }

   public void addLists(ConnectionHandler connHandler, String ... lists) {
	   for(String list : lists) {
		   addList(new DBListData(list), connHandler);
	   }
   }

   /**
    * Adds the list to this ListManager, so there is no need to use ListManager.addList
    *
    * @param listData
    * @since 1.11
    */
   public void addList(ListData listData) {

      ListManager.addList(listData);
   }


   /**
    * Set default mask for the whole application
    * @param mask
    * @since 1.12
    */
   public void setDateMaskForEverything(String mask)  {

	   if (mask==null) return;

       FormatterManager.addFormatter("dateFormatter", new DateFormatter(mask));
       FormatterManager.setFixedDateFormatter(new DateFormatter(mask));
       LocaleManager.setDefaultDateMask(mask);
   }

}
