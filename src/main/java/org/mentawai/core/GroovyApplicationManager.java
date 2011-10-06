package org.mentawai.core;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.groovy.control.CompilationFailedException;
import org.mentawai.formatter.FormatterManager;
import org.mentawai.ioc.Bean;
import org.mentawai.ioc.DefaultComponent;
import org.mentawai.ioc.ScopeComponent;
import org.mentawai.list.ListManager;

/**
 * @author Rubem Azenha
 */
public class GroovyApplicationManager extends ScriptApplicationManager {

    private static final String SCRIPT_LOCATION = "ApplicationManager.groovy";

    private ApplicationManager delegate = null;

    public GroovyApplicationManager() {
        createAppManager(getFilename(SCRIPT_LOCATION));
    }

    protected String getScriptName() {
        return SCRIPT_LOCATION;
    }
    
    public ApplicationManager getAppManager() {
    	return delegate;
    }

    public void runScript(String scriptFile, Context application) {

        createAppManager(scriptFile);

        delegate.init(application);
        
        delegate.setupDB();

        delegate.loadBeans();
        
        delegate.loadLocales();
        
        delegate.loadFilters();
        
        delegate.setupIoC();

        delegate.loadActions();
        
        // Then load any user-defined list, because there are other ways to
        // load lists besides the above.
        try {
            delegate.loadLists();
            ListManager.init();
        } catch (Exception e) {
            throw new RuntimeException("Error while initing ListManager: "
                    + e.getMessage(), e);
        }

        // Load some pre-defined formatters here.
        FormatterManager.init();

        delegate.loadFormatters();
        
        delegate.onStarted(application);

    }

    private void createAppManager(String scriptFile) {
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        
        File file = new File(scriptFile);

        Class klass = null;
        try {
            klass = groovyClassLoader.parseClass(file);
        } catch (CompilationFailedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            delegate = (ApplicationManager) klass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public ActionConfig action(Class klass, String innerAction) {
        return delegate.action(klass, innerAction);
    }

    public ActionConfig action(Class klass) {
        return delegate.action(klass);
    }

    public ActionConfig action(String name, Class klass, String innerAction) {
        return delegate.action(name, klass, innerAction);
    }

    public ActionConfig action(String name, Class klass) {
        return delegate.action(name, klass);
    }

    public ActionConfig add(ActionConfig ac) {
        return delegate.add(ac);
    }

    public Bean add(String name, Bean comp) {
        return delegate.add(name, comp);
    }

    public ActionConfig addActionConfig(ActionConfig ac) {
        return delegate.addActionConfig(ac);
    }

    public Bean addComponent(String name, Bean comp) {
        return delegate.addComponent(name, comp);
    }

    public void addGlobalConsequence(String result, Consequence c) {
        delegate.addGlobalConsequence(result, c);
    }

    public void addGlobalFilter(Filter filter, boolean last) {
        delegate.addGlobalFilter(filter, last);
    }

    public void addGlobalFilter(Filter filter) {
        delegate.addGlobalFilter(filter);
    }

    public void addGlobalFilter(List filters, boolean last) {
        delegate.addGlobalFilter(filters, last);
    }

    public void addGlobalFilter(List filters) {
        delegate.addGlobalFilter(filters);
    }

    public void destroy(Context application) {
        delegate.destroy(application);
    }

    public void filter(Filter filter, boolean last) {
        delegate.filter(filter, last);
    }

    public void filter(Filter filter) {
        delegate.filter(filter);
    }

    public void filter(List filters, boolean last) {
        delegate.filter(filters, last);
    }

    public void filter(List filters) {
        delegate.filter(filters);
    }

    public void filterLast(Filter filter) {
        delegate.filterLast(filter);
    }

    public void filterLast(List filters) {
        delegate.filterLast(filters);
    }

    public ActionConfig getActionConfig(String name, String innerAction) {
        return delegate.getActionConfig(name, innerAction);
    }

    public ActionConfig getActionConfig(String name) {
        return delegate.getActionConfig(name);
    }

    public Bean getComponent(String name) {
        return delegate.getComponent(name);
    }

    public Consequence getGlobalConsequence(String result) {
        return delegate.getGlobalConsequence(result);
    }

    public List<Filter> getGlobalFilters() {
        return delegate.getGlobalFilters();
    }
    
    public List<Filter> getGlobalFilters(Class<? extends Object> klass, boolean last) {
    	return delegate.getGlobalFilters(klass, last);
    }

    public List<Filter> getGlobalFilters(boolean last) {
        return delegate.getGlobalFilters(last);
    }

    public void init(Context application) {
        delegate.init(application);
    }

    public ScopeComponent ioc(String name, Class klass, int scope) {
        return delegate.ioc(name, klass, scope);
    }

    public DefaultComponent ioc(String name, Class<? extends Object> klass) {
        return delegate.ioc(name, klass);
    }

    public Bean ioc(String name, Bean comp) {
        return delegate.ioc(name, comp);
    }

    public void loadActions() {
        delegate.loadActions();
    }
    
    public void setupDB() {
    	delegate.setupDB();
    }
    
    public void loadFilters() {
    	delegate.loadFilters();
    }
    
    public void setupIoC() {
    	delegate.setupIoC();
    }
    
    public void loadBeans() {
    	
    	delegate.loadBeans();
    }

    public void loadLists() throws Exception {
        delegate.loadLists();
    }
    
    public void onStarted(Context context) {
    	delegate.onStarted(context);
    }

    public void loadLocales() {
        delegate.loadLocales();
    }

    public void on(String result, Consequence c) {
        delegate.on(result, c);
    }

    public void setDebugMode(boolean debugMode, boolean commented) {
        delegate.setDebugMode(debugMode, commented);
    }

    public void setDebugMode(boolean debugMode) {
        delegate.setDebugMode(debugMode);
    }

    public void setReloadMode(boolean reloadMode) {
        delegate.setReloadMode(reloadMode);
    }

    public ActionConfig spring(String path, String action) {
        return delegate.spring(path, action);
    }
}