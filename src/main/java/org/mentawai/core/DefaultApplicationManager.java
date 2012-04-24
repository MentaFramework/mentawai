package org.mentawai.core;

import java.util.List;

import org.mentawai.db.ConnectionHandler;
import org.mentawai.db.mysql.MySQLBoneCPConnectionHandler;
import org.mentawai.filter.AuthenticationFilter;
import org.mentawai.filter.ExceptionFilter;
import org.mentawai.filter.MentaContainerFilter;
import org.mentawai.filter.RedirectAfterLoginFilter;
import org.mentawai.filter.TransactionFilter;
import org.mentawai.filter.ValidationFilter;
import org.mentawai.mail.Email;
import org.mentawai.transaction.JdbcTransaction;

public class DefaultApplicationManager extends ApplicationManager {

	private Props props = null;
	
	private ConnectionHandler connHandler;
	
	public ConnectionHandler getConnHandler() {
		
		return connHandler;
	}
	
	@Override
	public Props getProps() {
		if (props == null) {
			props = super.getProps();
		}
		return props;
	}
	
	
	
	public void init(Context application, Props props) {
		
	}
	
	@Override
	public final void init(Context application) {
		
		this.props = getProps();
		
		////////////////////////////////////////////
		// TURN ON/OFF DEBUG MODE
		////////////////////////////////////////////
		if (props.has("debug_mode")) {
			setDebugMode(props.getBoolean("debug_mode"));
		}
		
		///////////////////////////////////////////////////
		// TURN ON/OFF APP MANAGER AUTO-REDEPLOY FEATURE
        // OBS: Requires http://www.javarebel.com to work
		///////////////////////////////////////////////////
		if (props.has("auto_reload")) {
			setReloadable(props.getBoolean("auto_reload"));
		}
		
		//////////////////////////////////////////
		// FOR SENDING EMAIL
		//////////////////////////////////////////
		if (!props.getBoolean("email.send_email")) {
			
			Email.setSendEmail(false);
			
		} else {
		
			Email.setDefaultHostName(props.getString("email.host"));
			
			Email.setDefaultSslConnection( props.getBoolean("email.ssl") );
			
			Email.setDefaultPort( props.getInt("email.port") );

			if (props.getBoolean("email.use_authentication")) {
				
				Email.setDefaultAuthentication(props.getString("email.user"), props.getString("email.pass")); 
			}
			
			Email.setDefaultFrom(props.getString("email.from_email"), props.getString("email.from_name"));
		}
		
		init(application, props);
	}

	@Override
	public final void setupDB() {
		
		if (props.has("jdbc.url")) {
			String driver = props.getString("jdbc.driver");
			String url = props.getString("jdbc.url");
			String user = props.getString("jdbc.user");
			String pass = props.getString("jdbc.pass");
		
			this.connHandler = new MySQLBoneCPConnectionHandler(driver, url, user, pass);
		
			initDB(connHandler);
		}
	}
	
	public void initDB(ConnectionHandler connHandler) {
		
	}
	
	public ConnectionHandler getConnectionHandler() {
		return connHandler;
	}
	
	@Override
	public final void loadBeans() {
		initMentaBeans();
	}
	
	public void initMentaBeans() {
		
	}
	
    @Override
    public final void loadLists() {
    	
    	if (props.has("localizedLists")) {
    		
    		List<String> list = props.getList("localizedLists");
    		String[] array = new String[list.size()];
    		array = list.toArray(array);
    		
    		addLocalizedLists(connHandler, array);
    		
    	}
    	
    	if (props.has("lists")) {

    		List<String> list = props.getList("lists");
    		String[] array = new String[list.size()];
    		array = list.toArray(array);
    		
    		addLists(connHandler, array);
    	}
    	
    	initLists(connHandler);
	}
    
    public void initLists(ConnectionHandler connHandler) {
    	
    }
	
	@Override
	public final void loadLocales() {
		
		if (props.has("locales")) {
			
			List<String> list = props.getList("locales");
			
			for(String loc : list) {
				addLocale(loc);
			}
		}

		initLocales();
	}
	
	public void initLocales() {
		
	}
	
	@Override
	public final void loadFilters() {
		
		/////////////////////////////////////////////
		// GLOBAL FILTERS
		/////////////////////////////////////////////
		
		filter(new ExceptionFilter());

		filter(new MentaContainerFilter());
		
		//////////////////////////////////////////////////////////
		// AUTHENTICATION: ALL ACTIONS THAT DO NOT IMPLEMENT
		// THE AuthenticationFree INTERFACE WILL REQUIRE
		// AUTHENTICATION
		//////////////////////////////////////////////////////////
		if (props.getBoolean("authentication.on")) {
			filter(new AuthenticationFilter());	
		}
		
		filter(new RedirectAfterLoginFilter());
		on(REDIR, redir());

		filter(new ValidationFilter()); 
		
		filter(new TransactionFilter("transaction"));
		
		initGlobalFilters();
		initGlobalConsequences();
	}
	
	public void initGlobalFilters() {
		
	}
	
	public void initGlobalConsequences() {
		
	}
	
	@Override
	public final void setupIoC() {

		////////////////////////////////////////////////////////
		// INVERSION OF CONTROL: SET UP YOUR REPOSITORIES OR 
		// ANY OTHER OBJECT IMPLEMENTATION YOU WANT TO MAKE 
		// AVAILABLE THROUGH IOC (INVERSION OF CONTROL)
		////////////////////////////////////////////////////////
		
		if (connHandler != null) {
			ioc("conn", connHandler);
			ioc("beanManager", getBeanManager()); // always return the same instance...
			if (props.has("mentabean.dialect")) {
				ioc("beanSession", props.getClass("mentabean.dialect"));
			}
			ioc("transaction", JdbcTransaction.class);
		}
		
		initIoC();
	}
	
	public void initIoC() {
		
	}
	
	@Override
	public final void loadActions() {
		initActions();
	}
	
	public void initActions() {
		
	}
}