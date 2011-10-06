package org.mentawai.db;

import java.sql.Connection;

import org.mentacontainer.Container;
import org.mentacontainer.Scope;
import org.mentawai.core.Action;
import org.mentawai.core.ApplicationManager;
import org.mentawai.core.BaseAction;
import org.mentawai.transaction.JdbcTransaction;
import org.mentawai.transaction.Transaction;

public class JdbcContext {
	
	public static String CONN_KEY = "conn";
	
	public static String TRANSACTION_KEY = "transaction";
	
	public static <T> Object exec(Class<T> klass, JdbcTemplate<T> template) {
		
		Container c = ApplicationManager.getContainer();
		
		if (c == null) throw new RuntimeException("Cannot find container in application manager!");
		
		return exec(c, klass, template);
	}
	
	public static <T> Object exec(Container container, Class<T> klass, JdbcTemplate<T> template) {
		
		Connection conn = null;
		
		Transaction t = null;
		
		boolean wasConnAlreadyPresent = false;
		
		try {
			
			// check if we are getting connection from pool...
		
    		if (container.check(CONN_KEY)) {
    			
    			conn = container.get(CONN_KEY);
    			
    			wasConnAlreadyPresent = true;
    			
    		} else {
    			
    			conn = container.get(CONN_KEY);
    			
    			wasConnAlreadyPresent = false;
    		}
    		
    		// handle transaction...
    		
    		if (!wasConnAlreadyPresent) {
    			
    			// new connection so you must create new transaction...
    			
        		if (!container.check(TRANSACTION_KEY)) {
        			
        			t = container.get(TRANSACTION_KEY);
        			
        			if (t == null) {
        				
        				t = new JdbcTransaction(conn);
        			}
        			
        		} else {
        			
        			throw new RuntimeException("Did not find connection but found transaction in the container!");
        		
        		}
        		
    		} else {
    			
    			t = container.get(TRANSACTION_KEY);
    			
    			if (t == null) {
    				
    				t = new JdbcTransaction(conn);
    			}

    		}
    		
    		T action = klass.newInstance();
    		
    		if (action instanceof Action) {
    			
    			Action a = (Action) action;
    			
    			BaseAction.init(a);
    			
    			a.getInput().setValue(CONN_KEY, conn);
    			
    			a.getInput().setValue(TRANSACTION_KEY, t);
    			
    		}
    		
    		container.inject(action);
    		
    		t.begin();
    		
    		Object result = template.exec(action, conn, t);
    		
    		t.commit();
    		
    		return result;
    		
		} catch(Exception e) {
			
			try { 
				
				if (t != null && t.isActive()) t.rollback(); 
				
			} catch(Exception exp) { 
			
				System.err.println("Could not rollback transaction!");
				
				exp.printStackTrace();
			}
			
			throw new RuntimeException(e);
			
		} finally {
			
			try {
				
				if (!wasConnAlreadyPresent && t != null && t.isActive()) t.end();
				
			} catch(Exception e) {
				
				System.err.println("Cannot end transaction!");
				
				e.printStackTrace();
			}
			
			
			if (!wasConnAlreadyPresent) container.clear(Scope.THREAD);
				
		}
	}
}