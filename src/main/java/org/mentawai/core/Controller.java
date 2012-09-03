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
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.mentaregex.Regex;
import org.mentawai.coc.ConsequenceProvider;
import org.mentawai.coc.DefaultConsequenceProvider;
import org.mentawai.db.ConnectionHandler;
import org.mentawai.db.JPAHandler;
import org.mentawai.db.SessionHandler;
import org.mentawai.filter.GlobalFilterFreeMarkerFilter;
import org.mentawai.formatter.FormatterManager;
import org.mentawai.i18n.LocaleManager;
import org.mentawai.jruby.JRubyInterpreter;
import org.mentawai.jruby.RubyAction;
import org.mentawai.list.ListManager;
import org.mentawai.log.Debug;
import org.mentawai.log.Info;
import org.mentawai.util.DebugServletFilter;

/**
 * The Mentawai central controller. Mentawai actions are intercepted and
 * executed by this controller. The controller is also responsable for creating
 * and starting the ApplicationManager.
 * 
 * @author Sergio Oliveira
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 */
public class Controller extends HttpServlet {

	private static final long serialVersionUID = 5571457670768805678L;

	public static String DEFAULT_CHARSET = null;

	private static final char SEP = File.separatorChar;

	private static ApplicationManager appManager = null;

	private static String appMgrClassname = null;

	private static ServletContext application = null;

	private static ServletConfig config = null;

	protected static ApplicationContext appContext = null;

	private static ConsequenceProvider consequenceProvider = null;

	private static ConsequenceProvider defaultConsequenceProvider = new DefaultConsequenceProvider();

	private static File appManagerFile = null;

	private static long lastModified = 0;

	static boolean reloadAppManager = false;

	static boolean debugMode = false;

	static boolean autoView = false; // default is false since 1.14...

	static boolean statsMode = false;

	private static final String STICKY_KEY = "_stickyActions";

    private static final String[] APP_MGR_NAMES = { "ApplicationManager", "AppMgr", "AppManager" };
    
    // pretty
    
	private static final String INNER_ACTION_SEPARATOR_PARAM = "innerActionSeparator";

	private static final char DEFAULT_INNER_ACTION_SEPARATOR = '-';

	private static char innerActionSeparator;

	/**
	 * Initialize the Controller, creating and starting the ApplicationManager.
	 * 
	 * @param conf
	 *            the ServletConfig.
	 */
	public void init(ServletConfig conf) throws ServletException {

		super.init(conf);

		config = conf;

		application = conf.getServletContext();

		appContext = new ApplicationContext(application);

		ApplicationManager.setRealPath(application.getRealPath(""));

		// verifies if the "reload mode" is on
		String reload = config.getInitParameter("reloadAppManager");

		if (reload != null && reload.equalsIgnoreCase("true")) {

			reloadAppManager = true;

		}

		// verifies if the "debug mode" is on
		String debug = config.getInitParameter("debugMode");

		if (debug != null && debug.equalsIgnoreCase("true")) {
			debugMode = true;
		}

		// verifies if the "auto view" is on (default is on!)
		String auto = config.getInitParameter("autoView");

		if (auto != null && auto.equalsIgnoreCase("true")) {

			autoView = true;

		}

		// verifies if the "stats mode" is on
		// default is off -> Security issue!
		String stats = config.getInitParameter("stats");

		if (stats != null && stats.equalsIgnoreCase("true")) {

			statsMode = true;

		}

		// gets the AplicationManager class from ServletConfig
		appMgrClassname = config.getInitParameter("applicationManager");

		Debug.log("[Controller] ApplicationManager from web.xml = "+ appMgrClassname);

		if (appMgrClassname == null || appMgrClassname.trim().equals("")) {

			// try to find DEFAULT ApplicationManager...
			
			boolean found = false;

			Debug.log("[Controller] Nothing in web.xml finding classes '"+ APP_MGR_NAMES + "' in default package...");

			for (int i = 0; i < APP_MGR_NAMES.length; i++) {
				String classname = APP_MGR_NAMES[i];

				try {
					Class<? extends Object> klass = Class.forName(classname);

					if (ApplicationManager.class.isAssignableFrom(klass)) {
						appMgrClassname = new String(classname);
						found = true;
					}

				} catch (Exception e) {
					// class not found...
				}
			}

			// try to find ApplicationManager by checking all packages...
			if (!found) {
				Debug.log("[Controller] Nothing  found in default package, Scanning the classpath in search of ApplicationManager");

				try {

        		found = findAppMgr(new File(ApplicationManager.getRealPath() + SEP + "WEB-INF" + SEP + "classes"));

				} catch (Exception e) {
        		System.err.println("Erro looking for ApplicationManager.class!");
					e.printStackTrace();
				}
			}

			if (!found) {
		    	appMgrClassname = "ApplicationManager"; // default without package...
			}
		}

		Info.log("[Controller] Initializing ApplicationManager from class: "+ appMgrClassname);
		
		initApplicationManager();
		
		// pretty:
		
		String innerActionSeparatorParam = conf.getInitParameter(INNER_ACTION_SEPARATOR_PARAM);

		if (innerActionSeparatorParam != null) {

			validateLength(innerActionSeparatorParam);

			validateContent(innerActionSeparatorParam);

			Controller.innerActionSeparator = innerActionSeparatorParam.charAt(0);

		} else {

			Controller.innerActionSeparator = DEFAULT_INNER_ACTION_SEPARATOR;
		}

	}
	
	public static char getMethodSeparatorChar() {
		return innerActionSeparator;
	}
	
	public static String getExtension() {
		return ApplicationManager.EXTENSION;
	}

	private void validateContent(String innerActionSeparatorParam) throws ServletException {

		if (innerActionSeparatorParam.equals("/")) {

			throw new ServletException("The "
					+ INNER_ACTION_SEPARATOR_PARAM
					+ " context parameter cannot be the \'/\' char");
		}
	}

	private void validateLength(String innerActionSeparatorParam) throws ServletException {

		if (innerActionSeparatorParam.length() != 1) {

			throw new ServletException("The "
					+ INNER_ACTION_SEPARATOR_PARAM
					+ " context parameter must have only one char");
		}
	}

	static final String EXTENSION = "." + ApplicationManager.EXTENSION;

	private boolean isPrettyURL(HttpServletRequest req) {

		String uri = req.getRequestURI().toString();

		// cut the last '/'
		if (uri.endsWith("/") && uri.length() > 1) {

			uri = uri.substring(0, uri.length() - 1);
		}

		// ends with .mtw or have a "." is not pretty URL
		if (uri.endsWith(EXTENSION) || uri.indexOf(".") > 0) return false;

		return true;
	}

	private String getActionPlusInnerAction(HttpServletRequest req) {

		String context = req.getContextPath();

		String uri = req.getRequestURI().toString();

		// remove the context from the uri, if present

		if (context.length() > 0 && uri.indexOf(context) == 0) {

			uri = uri.substring(context.length());

		}

		// cut the first '/'
		if (uri.startsWith("/") && uri.length() > 1) {

			uri = uri.substring(1);

		}

		// cut the last '/'
		if (uri.endsWith("/") && uri.length() > 1) {

			uri = uri.substring(0, uri.length() - 1);
		}

		String[] s = uri.split("/");

		if (s.length >= 2) {

			return s[1];
		}

		return null;
	}
	

    public static String getBasePathForMaven() {
    	
        String mvn = SEP + "src" + SEP + "main" + SEP + "webapp";
        
        if (ApplicationManager.getRealPath().endsWith(mvn)) {

        	int index = ApplicationManager.getRealPath().indexOf(mvn);
        	
        	String base = ApplicationManager.getRealPath().substring(0, index);
        	
        	return base;
        }
        
        return null;
    }
    
	public static ServletConfig getConfig() {

		return config;

	}

	private boolean findAppMgr(File f) throws IOException {

		if (f.isDirectory()) {

			// check directory...

			String filename = f.toString();

			String[] s = Regex.match(filename, "/WEB#-INF#" + SEP + "classes#" + SEP + "(.+)/", '#');

			if (s != null && s.length == 1) {

				filename = s[0];

				filename = filename.replace(SEP, '.');

				for (int i = 0; i < APP_MGR_NAMES.length; i++) {

					// filename += ".ApplicationManager";

					String classname = filename + "." + APP_MGR_NAMES[i];

					try {
			    		Class<? extends Object>klass = Class.forName(classname);

						if (ApplicationManager.class.isAssignableFrom(klass)) {

							appMgrClassname = new String(classname);

							return true;
						}

					} catch (Exception e) {
						// class not found...
					}
				}

			}

			// recursive call...

			File[] childs = f.listFiles();
			for (File child : childs) {
				boolean found = findAppMgr(child);
				if (found) return true;
			}
		}

		return false;
	}

	public static void setConsequenceProvider(ConsequenceProvider provider) {
		Controller.consequenceProvider = provider;
	}
	
	public static ConsequenceProvider getConsequenceProvider() {
		return consequenceProvider;
	}

    private static File findAppManagerClass() {
    	
    	StringBuilder sb = new StringBuilder(ApplicationManager.getRealPath());
        sb.append(SEP).append("WEB-INF").append(SEP).append("classes").append(SEP);
        sb.append(appMgrClassname.replace('.', SEP)).append(".class");
        
        File f = new File(sb.toString());
        
        if (f.exists()) return f;
        
        
        // try to load using ClassLoager..
        
        try {
			Class<?> forName = Class.forName(appMgrClassname);
			String base = forName.getResource("/").getPath();
			sb.setLength(0);
			sb.append(base).append(appMgrClassname.replace('.', SEP)).append(".class");
			
			f = new File(sb.toString());
			if (f.exists()) return f;
			
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
        
		
		// try loagind using convenctions of maven...
        
        
        String base = getBasePathForMaven();
        
        if (base != null) {

            sb.setLength(0);
        	
        	sb.append(base).append(SEP).append("target").append(SEP).append("classes").append(SEP);
        	
        	sb.append(appMgrClassname.replace('.', SEP)).append(".class");
        	
        	File ff = new File(sb.toString());
        	
        	if (ff.exists()) return ff;
        }
        
        return f;
    }

	private static boolean isAppMgrModified() {

		// not thread-safe on purpouse...

		if (appManagerFile == null) {

        	/*

			StringBuffer sb = new StringBuffer(ApplicationManager.getRealPath());
			sb.append(SEP).append("WEB-INF").append(SEP).append("classes")
					.append(SEP);
			sb.append(appMgrClassname.replace('.', SEP)).append(".class");

            System.out.println("====> ::" + ApplicationManager.getRealPath());

			appManagerFile = new File(sb.toString());

            */
        	
        	appManagerFile = findAppManagerClass();

		}

		if (!appManagerFile.exists()) {
			//return true; // cannot find file... reload on every time !
			return false; // cannot find file... not reload.. (by Ricardo Rufino.)
		}

        // avoid unnecessary reload here...
        if (lastModified == 0) {
        	
        	lastModified = appManagerFile.lastModified();
        }

		if (appManagerFile.lastModified() != lastModified) {

			lastModified = appManagerFile.lastModified();

			return true;

		}

		return false;
	}

	/**
	 * Displays information about the MENTAWAI when the Application Manager is
	 * loaded
	 */
	private static void printInfoHeader() {
		System.out.println();
		System.out.println("==== [ Mentawai :: MVC WEB Framework ] ==========================================================");
		System.out.println("- Version : " + ApplicationManager.MENTAWAI_VERSION+ " (" + ApplicationManager.MENTAWAI_BUILD + ")");
		System.out.println("- ReloadAppManager: " + reloadAppManager);
		System.out.println("- DebugMode: " + debugMode);
		System.out.println("- AutoView: " + autoView);
		System.out.println("- ApplicationManager: " + appMgrClassname);
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("- Server: " + application.getServerInfo());
		if (getContextPath() != null) System.out.println("- ApplicationContext: " + getContextPath());
		System.out.println("- Environment: " + appManager.getEnvironment());
		System.out.println("==================================================================================================");
		System.out.println();
	}
	
	private static String getContextPath() {
		
		String s = ApplicationManager.getContextPath();
		
		if (s == null) return null;
		
		if (s.equals("")) return "ROOT";
		
		return s;
	}

	/**
	 * Creates the AplicationManager and starts it.
	 * 
	 * @throws ServletException
	 */
	private static void initApplicationManager() throws ServletException {
		
		try {

			Class<? extends Object> klass = Class.forName(appMgrClassname);

			appManager = (ApplicationManager) klass.newInstance();

            //ApplicationManager.instance = appManager; // went to ApplicationManager constructor...

			ApplicationManager.setApplication(appContext);

			appManager.init(appContext);
			
			// set some values...
			
			DebugServletFilter.addStaticInfo("Environment = " + appManager.getEnvironment().toString()); // for debug mode
			
			ConnectionHandler connHandler = appManager.createConnectionHandler();
			if (connHandler != null) {
				appManager.setConnectionHandler(connHandler);
			}
			
			SessionHandler sessionHandler = appManager.createSessionHandler();
			if (sessionHandler != null) {
				appManager.setSessionHandler(sessionHandler);
			}
			
			JPAHandler jpaHandler = appManager.createJPAHandler();
			if(jpaHandler != null) {
				appManager.setJPAHandler(jpaHandler);
			}
			
			appManager.setupDB();
			
			appManager.loadBeans();
			
			appManager.loadLocales();
			
			appManager.loadFilters();
			
			appManager.setupIoC();

			appManager.loadActions();

			// Try to automatic load the lists, if the user has created them
			// inside "/lists" directory (default dir).
			ListManager.init();

			// Then load any user-defined list, because there are other ways to
			// load lists besides the above.
			appManager.loadLists();

			// Load some pre-defined formatters here.
			FormatterManager.init();

			appManager.loadFormatters();

            appManager.onStarted(appContext);
            
            printInfoHeader();
            
            DebugServletFilter.addStaticInfo("ApplicationManager = " + appMgrClassname); // for debug mode
            DebugServletFilter.addStaticInfo("ReloadAppManager = " + reloadAppManager); // for debug mode
            DebugServletFilter.addStaticInfo("AutoView = " + autoView); // for debug mode
            DebugServletFilter.addStaticInfo("Version = " + ApplicationManager.MENTAWAI_VERSION+ " (" + ApplicationManager.MENTAWAI_BUILD + ")");
            if (getContextPath() != null) DebugServletFilter.addStaticInfo("ContextPath = " + getContextPath());
            DebugServletFilter.addStaticInfo("Server = " + application.getServerInfo());

		} catch (IOException e) {
			throw new ServletException(
					"Exception while loading lists in application manager: "
							+ e.getMessage(), e);
		} catch (Exception e) {
			throw new ServletException(
					"Exception while loading application manager "
							+ appMgrClassname + ": " + e.getMessage(), e);
		}
	}

	/**
	 * Destroy all filters defined in the ApplicationManager, call the destroy()
	 * method of ApplicationManager, then call super.destroy() to destroy this
	 * servlet (the Controller).
	 */
	public void destroy() {

		Info.log("[Controller]", " callling destroy() ...");

		if (appManager != null) {

        	appManager.destroy(appContext);

			Set<Filter> filters = appManager.getAllFilters();
			Iterator<Filter> iter = filters.iterator();
			while (iter.hasNext()) {
				Filter f = iter.next();
				f.destroy();
			}
		}

		super.destroy();

		if (JRubyInterpreter.isInitialized()) {
			JRubyInterpreter.getInstance().close();
		}

		LocaleManager.stopLocaleScan();

	}

	/**
	 * Returns the ServletContext of your web application.
	 * 
	 * @return The ServletContext of your web application.
	 */
	public static ServletContext getApplication() {
		return application;
	}

	/**
	 * Returns the URI from this request. URI = URI - context - extension. This
	 * method is used by getActionName and getInnerActionName. You may call this
	 * method in your own controller subclass. Ex: /myapp/UserAction.add.mtw
	 * will return UserAction.add
	 * 
	 * @param req
	 * @return The URI
	 */
	protected String getURI(HttpServletRequest req) {

		String context = req.getContextPath();

		String uri = req.getRequestURI().toString();

		// remove the context from the uri, if present

		if (context.length() > 0 && uri.indexOf(context) == 0) {

			uri = uri.substring(context.length());

		}

		// cut the extension... (.mtw or whatever was defined in web.xml)
		int index = uri.lastIndexOf(".");

		if (index > 0) {

			uri = uri.substring(0, index);

		}

		// cut the first '/'
		if (uri.startsWith("/") && uri.length() > 1) {

			uri = uri.substring(1, uri.length());

		}

		return uri;
	}

	/**
	 * From the http request, get the action name. You may override this if you
	 * want to extract the action name through some other way.
	 * 
	 * @param req
	 *            The http request
	 * @return The action name
	 */
	protected String getActionName(HttpServletRequest req) {

		if (isPrettyURL(req)) {

			String s = getActionPlusInnerAction(req);

			// separate the inner action from action...

			int index = s.indexOf(innerActionSeparator);

			if (index > 0) {

				return s.substring(0, index);
			}

			return s;
			
		} else {

			String uri = getURI(req);

			// If there is an Inner Action, cut it off from the action name

			int index = uri.lastIndexOf(".");

			if (index > 0 && (uri.length() - index) >= 2) {

				uri = uri.substring(0, index);

			}

			return uri;
		}
	}

	/**
	 * The action name may include an Inner Action. For example: for
	 * bookmanager.add.mtw the action name is "bookmanager" and the inneraction
	 * name is "add". If you want to extract the inner action through some other
	 * way you can override this method in your own controller.
	 * 
	 * @param req
	 * @return The inner action name or null if there is no inneraction.
	 */
	protected String getInnerActionName(HttpServletRequest req) {
		
		if (isPrettyURL(req)) {
			
			String s = getActionPlusInnerAction(req);

			// separate the inner action from action...

			int index = s.indexOf(innerActionSeparator);

			if (index > 0 && index + 1 < s.length()) {

				return s.substring(index + 1);
			}

			return null;
			
		} else {

    		String uri = getURI(req);
    
    		String innerAction = null;
    
    		int index = uri.lastIndexOf(".");
    
    		if (index > 0 && (uri.length() - index) >= 2) {
    
    			innerAction = uri.substring(index + 1, uri.length());
    
    		}
    
    		return innerAction;
		}
	}

	/**
	 * Subclasses of this controller may override this method to have a chance
	 * to prepare the action before it is executed. This method creates and
	 * injects in the action all contexts, input, output and locale.
	 * 
	 * @param action
	 *            The action to prepare for execution
	 * @param req
	 *            The http request (input will need that)
	 * @param res
	 *            The http response (output will need that)
	 * @since 1.2
	 */

	protected void prepareAction(Action action, HttpServletRequest req,
			HttpServletResponse res) {
		
		if (isPrettyURL(req)) {
			
			action.setInput(new PrettyURLRequestInput(req, res));
			action.setOutput(new ResponseOutput(res));
			action.setSession(new SessionContext(req, res));
			action.setApplication(appContext);
			action.setCookies(new CookieContext(req, res));
			action.setLocale(LocaleManager.decideLocale(req, res));
			
		} else {
		
    		action.setInput(new RequestInput(req, res));
    		action.setOutput(new ResponseOutput(res));
    		action.setSession(new SessionContext(req, res));
    		action.setApplication(appContext);
    		action.setCookies(new CookieContext(req, res));
    		action.setLocale(LocaleManager.decideLocale(req, res));
    		
		}
	}

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		if (appManager == null) {
			throw new ServletException("The Application manager is not loaded");
		}

		// notify client that response had processed by mentawai.
		res.setHeader("Mentawai-Version", ApplicationManager.MENTAWAI_VERSION);

		/*
		 * This is very useful during development time, however it should not be
		 * used in production, because it serializes all requests. Basicaly if
		 * you forget to turn this off in production your site will suck!
		 * 
         * 09/02/2008 UPDATE: This will work perfectly with JavaRebel.
         * More info about JavaRebel: http://www.javarebel.com/
		 */
		if (reloadAppManager) {

			synchronized (this) {

				if (isAppMgrModified()) {

					if(Debug.isEnabled()) Debug.log("Controller", "Re-loading modified application manager!");
					
					// Destroy before reload....
					if(appManager != null){
						destroy();
					}
					
					initApplicationManager();
				}
			}
		}

		/*
		 * Allow to change the req character encoding...
		 */
		if (appManager.getReqCharEncoding() != null) {
			try {
				req.setCharacterEncoding(appManager.getReqCharEncoding());
			} catch (Exception e) {
        		throw new ServletException("The encoding set by setReqCharEncoding is not supported: " + appManager.getReqCharEncoding());
			}
		}

		/*
		 * This is useful for ScriptApplicationManager that needs to check on
		 * every request if the script file has changed in disk.
		 */
		appManager.service(appContext, req, res);

		String actionName = getActionName(req);

		/**
		 * This is for ApplicationManager stats! =)
		 */
        if (statsMode && actionName.equalsIgnoreCase(ApplicationManagerViewer.STATS_PAGE_NAME)) {

			ApplicationManagerViewer applicationManagerViewer = new ApplicationManagerViewer();

			applicationManagerViewer.buildApplicationManagerStats(res);

			return;

		}

		String innerAction = getInnerActionName(req);

		ActionConfig ac = null;

		if (innerAction != null) {

			ac = appManager.getActionConfig(actionName, innerAction);

		}

		if (ac == null) {

			ac = appManager.getActionConfig(actionName);
		}

		if (ac == null) {

			if (ApplicationManager.getDefaultAction() != null) {

				ac = ApplicationManager.getDefaultAction();

			} else {

            throw new ServletException("Could not find action for actionName: "
								+ actionName
								+ (innerAction != null ? "." + innerAction : ""));

			}
		}

        // check if this action can be executed by this host...
        
        if (ac.isInternalOnly()) {
        	
        	String hostname = req.getRemoteHost();
        	
        	if (!hostname.equalsIgnoreCase("localhost") && !hostname.equals("127.0.0.1")) {
        		
        		throw new ServletException("Action is internal so you cannot call it from here: " + hostname);
        	}
        }

		Action action = null;

		// sticky logic! Re-use instance if action is sticky (similar to
		// continuations in other frameworks, but simpler!)

		Class<? extends Object> actionClass = ac.getActionClass();

		if (StickyAction.class.isAssignableFrom(actionClass)) {

			HttpSession session = req.getSession(true);

			StickyActionMap map = (StickyActionMap) session
					.getAttribute(STICKY_KEY);

			if (map != null) {

				action = map.get(actionClass);
			}
		}

		if (action == null) {

			action = ac.getAction(); // create an action instance here...

		}

		if (action == null) {

			throw new ServletException("Could not get an action instace: " + ac);

		}

		prepareAction(action, req, res);

		// BEGIN Code related to Debug Mode!

		StringBuffer sb = null;

		long now = 0;

		if (debugMode) {

			sb = new StringBuffer(2048);

            DebugServletFilter.debug(sb, actionName, innerAction, ac, req.getLocale(), action.getLocale());

			// save the debug for that action in its output...
			action.getOutput().setValue(DebugServletFilter.DEBUG_KEY, sb);

			now = System.currentTimeMillis();
		}

		// END

        List<Filter> filters = new ArrayList<Filter>(32);

		Consequence c = null;

		boolean conseqExecuted = false;

		boolean actionExecuted = false;

		StringBuilder returnedResult = new StringBuilder(32);

		try {

			c = invokeAction(ac, action, innerAction, filters, returnedResult);

			actionExecuted = true;

			// BEGIN Code related to Debug Mode

			if (debugMode) {

				DebugServletFilter.debug(sb, c);

				if (c instanceof Redirect) {

					// don't lose the debug... use the session...
					// DebugServletFilter will check and remove from session...

					action.getSession().setAttribute(
							DebugServletFilter.DEBUG_KEY, sb);
				}

				long time = System.currentTimeMillis() - now;

				DebugServletFilter.debug(sb, time);

			}

			// END
			
			// Put Action result in response (by Ricardo Rufino)
			// notify client that response had processed by mentawai.
			res.setHeader("Mentawai-Result", returnedResult.toString());
			
			c.execute(action, returnedResult.toString(), req, res);
			

			conseqExecuted = true;

		} catch (Exception e) {

			e.printStackTrace();

			Throwable cause = getRootCause(e);

			throw new ServletException("Exception while invoking action "
                    + actionName + ": " + e.getMessage() + " / " + e.getClass().getName() + " / " + cause.getMessage() + " / " + cause.getClass().getName(), cause);

		} finally {
			
			// remember to clear thread local.
			
			if (action instanceof PojoAction) {
				PojoAction pojoAction = (PojoAction) action;
				pojoAction.removeAction();
			}
			
			if (action instanceof SingleInstanceBaseAction) {
				SingleInstanceBaseAction siba = (SingleInstanceBaseAction) action;
				siba.removeAll();
			}
			
			if (action instanceof RubyAction) {
				RubyAction ra = (RubyAction) action;
				ra.removeAction();
			}

			/*
			 * Here we check all filters that were executed together with the
			 * action. If they are AfterConsequenceFilters, we need to call the
			 * afterConsequence method.
			 */

            for(int i = filters.size() - 1; i >= 0; i--) {

                Filter f = filters.get(i);

				if (f instanceof AfterConsequenceFilter) {

					AfterConsequenceFilter acf = (AfterConsequenceFilter) f;

					try {

						String s = returnedResult.toString();

                        acf.afterConsequence(action, c, conseqExecuted, actionExecuted, s.length() > 0 ? s : null);

					} catch (Exception e) {

						throw new ServletException(
								"Exception while executing the AfterConsequence filters: "
										+ e.getMessage(), e);
					}

				}
                
                if (f instanceof InputWrapper) {
                    
                    InputWrapper iw = (InputWrapper) f;
                    
                    iw.removeInput();
                }
			}
		}
	}

	private Throwable getRootCause(Throwable t) {

		Throwable curr = t;

		while (curr.getCause() != null) {

			curr = curr.getCause();

		}

		return curr;
	}

	/**
	 * Invoke an action and return the consequence generated by this invocation.
	 * This method also return all filters that were executed together with the
	 * action inside the filters list parameter.
	 * 
	 * @param ac
	 *            The ActionConfig which contains the consequences for this
	 *            action.
	 * @param action
	 *            The action to invoke.
	 * @param innerAction
	 *            The inner action to execute or null to execute the regular
	 *            action (execute() method).
	 * @param filters
	 *            The filters that were applied to the action. (You should pass
	 *            an empty list here!)
	 * @return A consequence generated by this invocation.
	 * @throws ActionException
	 *             if there was an exception executing the action.
	 * @throws FilterException
	 *             if there was an exception executing a filter for the action.
	 */
	public static Consequence invokeAction(ActionConfig ac, Action action,
            String innerAction, List<Filter> filters, StringBuilder returnedResult) throws Exception {

		InvocationChain chain = createInvocationChain(ac, action, innerAction);

		// copy all filters executed together with that action to the filters
		// parameter...

		if (filters == null || filters.size() != 0) {

			throw new IllegalArgumentException(
					"filters parameter should be non-null and a zero-sized list!");
		}

		Iterator<Filter> iter = chain.getFilters().iterator();

		while (iter.hasNext()) {

			filters.add(iter.next());

		}

		// execute chain!

		String result = chain.invoke();

		returnedResult.append(result);

		// BEGIN Code related to Debug Mode

		if (debugMode) {

			DebugServletFilter.debugInputOutput(action);

			StringBuffer sb = DebugServletFilter.getDebug(action);

			DebugServletFilter.debug(sb, result, true);
		}

		// END

		// If there is an inner action, try to get a consequence for the inner
		// action

		Consequence c = null;

		if (innerAction != null) {

			c = ac.getConsequence(result, innerAction);

		}

		// If not fount, try to get a consequene specific for that action

		if (c == null) {

			c = ac.getConsequence(result);

		}

		// If not found, try to get a global consequence

		if (c == null) {

			c = appManager.getGlobalConsequence(result);
		}

        if (c == null) {
        	
        	c = ac.getCatchAll();
        }

		if (consequenceProvider != null) {

			// new consequenceProvider for Controller...

			if (c == null) {

        		c = consequenceProvider.getConsequence(ac.getName(), ac.getActionClass(), result, innerAction);

				// add the consequence dynamically...

				if (c != null) {

					if (innerAction != null && ac.getInnerAction() == null) {

						ac.addConsequence(result, innerAction, c);

					} else {

						ac.addConsequence(result, c);
					}
				}
			}

		} else {

			// use the default consequence provider...

			if (c == null && autoView) {

	            //c = ac.getAutoConsequence(result, innerAction); // moved to OldAutoViewConsequenceProvider

	        	c = defaultConsequenceProvider.getConsequence(ac.getName(), ac.getActionClass(), result, innerAction);
			}

		}

		if (c == null) {

			throw new ActionException("Action has no consequence for result: "
                    + ac.getName() + " / innerAction = " + (innerAction != null ? innerAction : "NULL") + " - " + result);
		}

		return c;
	}

    private static boolean hasGlobalFilterFreeMarkerFilter(List<Filter> filters, String innerAction) {

		Iterator<Filter> iter = filters.iterator();

		while (iter.hasNext()) {

			Filter f = iter.next();

          if (GlobalFilterFreeMarkerFilter.class.isAssignableFrom(f.getClass())) {

				GlobalFilterFreeMarkerFilter gffmf = (GlobalFilterFreeMarkerFilter) f;

				return gffmf.isGlobalFilterFree(innerAction);
			}
		}

		return false;
	}

	private static InvocationChain createInvocationChain(ActionConfig ac,
			Action action, String innerAction) {

		InvocationChain chain = new InvocationChain(ac.getName(), action, ac);

		Object pojo = chain.getPojo();

		Object actionImpl = pojo != null ? pojo : action;

		// first place the "firstFilters" for the action...

		List<Filter> firstFilters = ac.getFirstFilters(innerAction);

		if (firstFilters != null) {

			chain.addFilters(firstFilters);
		}

		// place the global filters that are NOT LAST...

		boolean isGlobalFilterFree = false;

		if (actionImpl instanceof GlobalFilterFree) {

			GlobalFilterFree gff = (GlobalFilterFree) actionImpl;

			isGlobalFilterFree = gff.isGlobalFilterFree(innerAction);

		}

		if (!isGlobalFilterFree) {

			List<Filter> globals = appManager.getGlobalFilters(false);

			if (globals != null) {

				chain.addFilters(globals);
			}
		}

		// first place action class specific filters...
        List<Filter> klassFilters = appManager.getGlobalFilters(ac.getActionClass(), false);
        if (klassFilters != null) chain.addFilters(klassFilters);

		// place the action specific filters...

		List<Filter> filters = ac.getFilters(innerAction);

		if (filters != null) {

            isGlobalFilterFree = hasGlobalFilterFreeMarkerFilter(filters, innerAction);

			if (isGlobalFilterFree) {

				// remove previously added global filters...

				chain.clearFilters();

				// add again klass specfic filters! (they are not global!)

               if (klassFilters != null) chain.addFilters(klassFilters);
			}

			chain.addFilters(filters);
		}

		// now add the last klass specfic filters... (before globals)

        List<Filter> klassFiltersLast = appManager.getGlobalFilters(ac.getActionClass(), true);
        if (klassFiltersLast != null) chain.addFilters(klassFiltersLast);


		// place the global filters that are LAST

		if (!isGlobalFilterFree) {

			List<Filter> globals = appManager.getGlobalFilters(true);

			if (globals != null) {

				chain.addFilters(globals);

			}

		}

		if (innerAction != null) {

			chain.setInnerAction(innerAction);
		}

		return chain;
	}

    static void adhere(StickyAction action, Class<? extends BaseAction> actionClass) {

		Context session = action.getSession();

		StickyActionMap map = (StickyActionMap) session
				.getAttribute(STICKY_KEY);

		if (map == null) {

			map = new StickyActionMap(new HashMap<Object, StickyAction>());

			session.setAttribute(STICKY_KEY, map);
		}

		map.put(actionClass, action);

	}

    static void disjoin(StickyAction action, Class<? extends BaseAction> actionClass) {

		Context session = action.getSession();

		StickyActionMap map = (StickyActionMap) session
				.getAttribute(STICKY_KEY);

		if (map != null) {

			map.remove(actionClass);
		}
	}

	private static class StickyActionMap implements HttpSessionBindingListener, Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final Map<Object, StickyAction> map;

		public StickyActionMap(Map<Object, StickyAction> map) {

			this.map = map;

		}

		public void put(Object key, StickyAction value) {

			map.put(key, value);
		}

		public StickyAction get(Object key) {

			return map.get(key);
		}

		public StickyAction remove(Object key) {

			return map.remove(key);
		}

		public void valueBound(HttpSessionBindingEvent evt) {

		}

		public void valueUnbound(HttpSessionBindingEvent evt) {

			Iterator<StickyAction> iter = map.values().iterator();

			while (iter.hasNext()) {

				StickyAction sticky = iter.next();

				sticky.onRemoved();

			}
		}
	}

	public static void setAppManager(ApplicationManager applicationManager) {
		synchronized (applicationManager) {
			appManager = applicationManager;

		}
	}

}