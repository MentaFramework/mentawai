package org.mentawai.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mentawai.ioc.Bean;
import org.mentawai.ioc.Dependency;

/**
 * @author Marvin H. Froeder
 */
public abstract class MultiApplicationManager extends ApplicationManager {

	private final List<ApplicationManager> managers;
	
	private boolean initialized = false;

	/**
	 * Default constructor, capable to call the registerManagers() method.
	 * 
	 * @throws Exception
	 *             about the ApplicationManager instanciation
	 */
	public MultiApplicationManager() {
		super();
		this.managers = new ArrayList<ApplicationManager>();
		registerManagers();
		initialized = true;
	}

	/**
	 * Abstract method to alow the registration the ApplicationManagers.
	 * 
	 * @throws Exception
	 *             about the ApplicationManager instanciation
	 */
	public abstract void registerManagers();

	/**
	 * Call this method to register an ApplicationManager.
	 * 
	 * @param manager The application manager to register.
	 */
	public void register(Class<? extends ApplicationManager> manager) {
		
		if (initialized) throw new IllegalStateException("MultiApplicationManager is already initialized! Call register from registerManagers() method!");
		
		try {
			ApplicationManager newInstance = manager.newInstance();
			newInstance.setParent(this);
			this.managers.add(newInstance);
		} catch (Exception e) {
			throw new RuntimeException("Unable to instanciate the class: "
					+ manager.getSimpleName() + ".  Read the next stack for details.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mentawai.core.ApplicationManager#init(org.mentawai.core.Context)
	 */
	@Override
	public final void init(Context application) {
		super.init(application);
		for (ApplicationManager manager : this.managers) {
			manager.init(application);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mentawai.core.ApplicationManager#loadActions()
	 */
	@Override
	public final void loadActions() {
		super.loadActions();
		for (ApplicationManager manager : this.managers) {
			manager.loadActions();
		}
	}
	
	@Override
	public final void loadBeans() {
		super.loadBeans();
		for (ApplicationManager manager : this.managers) {
			manager.loadBeans();
		}
	}
	
	@Override
	public final void setupIoC() {
		super.setupIoC();
		for (ApplicationManager manager : this.managers) {
			manager.setupIoC();
		}
	}
	
	@Override
	public final void loadFilters() {
		super.loadFilters();
		for (ApplicationManager manager : this.managers) {
			manager.loadFilters();
		}
	}
	
	@Override
	public final void setupDB() {
		super.setupDB();
		for (ApplicationManager manager : this.managers) {
			manager.setupDB();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mentawai.core.ApplicationManager#loadLocales()
	 */
	@Override
	public final void loadLocales() {
		super.loadLocales();
		for (ApplicationManager manager : this.managers) {
			manager.loadLocales();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mentawai.core.ApplicationManager#loadFormatters()
	 */
	@Override
	public final void loadFormatters() {
		super.loadFormatters();
		for (ApplicationManager manager : this.managers) {
			manager.loadFormatters();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mentawai.core.ApplicationManager#loadLists()
	 */
	@Override
	public final void loadLists() throws Exception {
		super.loadLists();
		for (ApplicationManager manager : this.managers) {
			manager.loadLists();
		}
	}
	
	@Override
	public final void onStarted(Context context) {
		super.onStarted(context);
		for (ApplicationManager manager : this.managers) {
			manager.onStarted(context);
		}
	}
	
	@Override
	public final Bean getComponent(String name) {
		
		Bean c = super.getComponent(name);
		
		if (c != null) return c;
		
		for (ApplicationManager manager : this.managers) {
			
			c = manager.getComponent(name);
			
			if (c != null) return c;
		}
		
		return null;
	}
	
	@Override
	public final Iterator<Dependency> getDependencies() {
		
		List<Dependency> uniqueList = new LinkedList<Dependency>();
		
		Iterator<Dependency> iter = super.getDependencies();
		
		while(iter.hasNext()) {
			
			uniqueList.add(iter.next());
		}
		
		for (ApplicationManager manager : this.managers) {
			
			iter = manager.getDependencies();
			
			while(iter.hasNext()) {
				
				uniqueList.add(iter.next());
			}
		}
		
		return uniqueList.iterator();
	}
}
