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
package org.mentawai.filter;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.mentawai.core.Action;
import org.mentawai.core.AfterConsequenceFilter;
import org.mentawai.core.Consequence;
import org.mentawai.core.FilterException;
import org.mentawai.core.InputWrapper;
import org.mentawai.core.InvocationChain;
import org.mentawai.transaction.HibernateTransaction;
import org.mentawai.transaction.Transaction;
import org.w3c.dom.Document;

/**
 * Use this filter to place an opened and connected Hibernate session in the
 * action input. There is no need to close the session. The filter does it
 * automatically when the action ends. The action is injected using the
 * <i>pull</i>.
 * 
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 */
public class HibernateFilter extends InputWrapper implements
        AfterConsequenceFilter {

    /** The default key name to use when placing the session in the action input. */
    public static final String KEY = "session";
    
    /** The default key name to use when placing a hibernate transaction in the action input. */
    public static final String TRANS_KEY = "transaction";

    private SessionFactory sessionFactory;

    private String hibernateSessionKey = KEY;
    
    private String hibernateTransactionKey = TRANS_KEY;

    private ThreadLocal<Session> hibernateSessionThreadLocal = new ThreadLocal<Session>();
    
    private ThreadLocal<Transaction> hibernateTransactionThreadLocal = new ThreadLocal<Transaction>();
    
    private boolean transactional = false;
    
    private Set<String> resultsForRollback = new HashSet<String>();

    public HibernateFilter() {
        Configuration configuration = new Configuration().configure();
        init(configuration);
    }
    
    public HibernateFilter(boolean transactional) {
    	this();
    	setTransactional(transactional);
    }
    
    public HibernateFilter(Document document) {
        Configuration configuration = new Configuration().configure(document);
        init(configuration);
    }
    
    public HibernateFilter(boolean transactional, Document document) {
    	this(document);
    	setTransactional(transactional);
    }

    public HibernateFilter(File file) {
        Configuration configuration = new Configuration().configure(file);
        init(configuration);
    }
    
    public HibernateFilter(boolean transactional, File file) {
    	this(file);
    	setTransactional(transactional);
    }

    public HibernateFilter(URL url) {
        Configuration configuration = new Configuration().configure(url);
        init(configuration);
    }
    
    public HibernateFilter(boolean transactional, URL url) {
    	this(url);
    	setTransactional(transactional);
    }

    public HibernateFilter(String string) {
        Configuration configuration = new Configuration().configure(string);
        init(configuration);
    }
    
    public HibernateFilter(boolean transactional, String string) {
    	this(string);
    	setTransactional(transactional);
    }

    public HibernateFilter(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public HibernateFilter(boolean transactional, SessionFactory sessionFactory) {
    	this(sessionFactory);
    	setTransactional(transactional);
    }
    
    public void setTransactional(boolean transactional) {
    	
    	this.transactional = transactional;
    	
    	addResultsForRollback(Action.ERROR, ExceptionFilter.EXCEPTION);
    }
    
    public void addResultsForRollback(String...results) {
    	
    	for(int i=0;i<results.length;i++) {
    		
    		resultsForRollback.add(results[i]);
    	}
    }
    
    public void setTransactional(boolean transactional, String transactionKey) {
    	
    	setTransactional(transactional);
    	
    	this.hibernateTransactionKey = transactionKey;
    }
    
    private void init(Configuration configuration) {
        this.sessionFactory = configuration.buildSessionFactory();

    }
    
    public String toString() {
    	
    	StringBuilder sb = new StringBuilder(128);
    	sb.append("HibernateFilter: ").append("sessionKey=").append(hibernateSessionKey);
    	sb.append(" transKey=").append(hibernateTransactionKey);
    	sb.append(" transactional=").append(transactional);
    	
    	return sb.toString();
    }

    /**
     * Sets the action input key to use. The Hibernate session will be placed in
     * the action input with this key.
     * 
     * @param key
     *            The action input key.
     */
    public void setKey(String key) {
        this.hibernateSessionKey = key;
    }

    /**
     * Sets the action to get an Hibernate Session using the <i>pull</i> model.
     */
    public String filter(InvocationChain chain) throws Exception {
        Action action = chain.getAction();

        super.setInput(action.getInput());

        action.setInput(this);

        String result = chain.invoke();
        
        // commit or rollback the transaction BEFORE consequence...
        
    	if (transactional) {
    		
    		Transaction trans = hibernateTransactionThreadLocal.get();
    		
    		if (trans != null) {
    			
    			hibernateTransactionThreadLocal.set(null);
    			
    			removeValue(hibernateTransactionKey);
    			
    			boolean shouldRollback = result == null || resultsForRollback.contains(result);
    			
	    		if (!shouldRollback) {
	    			
	    			try {
	    				
	    				trans.commit();
	    				
	    			} catch(Exception e) {
	    				
	    				e.printStackTrace();
	    				
	    				// try to rollback
	    				try {
	    					trans.rollback();
	    				} catch(Exception e2) { 
	    					throw new FilterException("Unable to rollback transaction after a commit error !", e);
	    				}
	    				
	    				throw new FilterException("Unable to commit hibernate transaction!", e);
	    			}
	    			
	    		} else {
	    			
	    			try {
	    			
	    				trans.rollback();
	    				
	    			} catch(Exception e) {
	    				
	    				e.printStackTrace();
	    				
	    				throw new FilterException("Unable to rollback hibernate transaction!", e);
	    			}
	    		}
    		}
    	}
    	
    	return result;
    }

    public void afterConsequence(Action action, Consequence c,
            boolean conseqExecuted, boolean actionExecuted, String result) {

        Session session = hibernateSessionThreadLocal.get();

        if (session != null) {
            hibernateSessionThreadLocal.set(null);
            removeValue(hibernateSessionKey);
            session.close();
        }
    }

    public void destroy() {
        sessionFactory.close();
    }

    /**
     * Verifies if the requested object is a Hibernate Session, return a
     * Hibernate Session if it is and return the content of the action's input
     * if not.
     */
    public Object getValue(String key) {

        if (key.equals(hibernateSessionKey)) {
            Session session = hibernateSessionThreadLocal.get();
            if (session == null) {
            	
                session = openSessionHibernate();
                
                hibernateSessionThreadLocal.set(session);
                
                if (transactional) {
                	
                	org.hibernate.Transaction tx = session.beginTransaction();
                	
                	Transaction trans = new HibernateTransaction(session, tx);
                	
                	hibernateTransactionThreadLocal.set(trans);
                	
                	setValue(hibernateTransactionKey, trans);
                	
                }
                
                setValue(key, session);
                
            }
            return session;

        }

        return super.getValue(key);
    }
    
    /**
	 * Create database connection and open a <tt>Session</tt> on it.
	 * 
	 * @param SessionFactory
	 * @param Action
	 * @return Session
	 */
	protected Session openSessionHibernate() {
		return sessionFactory.openSession();
	}
	
}