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
package org.mentawai.transaction;

import org.hibernate.Session;

/**
 * @author Rubem Azenha
 */
public class HibernateTransaction implements Transaction {
	
	private Session session = null;
	private org.hibernate.Transaction transaction = null;
    private boolean active = false;
    
    public HibernateTransaction() { }
    
	public HibernateTransaction(Session session) {
        this.session = session;
    }
    
    public HibernateTransaction(Session session, org.hibernate.Transaction transaction) {
        this(session);
        this.transaction = transaction;
        this.active = true;
    }
    
    public void setSession(Session session) {
        this.session = session;
    }
    
    public void setTransaction(org.hibernate.Transaction transaction) {
        this.transaction = transaction;
    }
	
	public void begin() throws Exception {
        if (session == null) throw new IllegalStateException("HibernateTransaction does not have a hibernate session!");
        
        // by Sergio: check if transaction was passed in constructor...
        if (transaction == null) {
    		transaction = session.beginTransaction();
        }
		active = true;
	}

	public void commit() throws Exception {
		if (!active) throw new IllegalStateException("Tried to commit but transaciton is not active!");
		transaction.commit();
	}

	public void rollback() throws Exception {
		if (!active) throw new IllegalStateException("Tried to rollback but transaciton is not active!");
		transaction.rollback();
	}

	public boolean isActive() {
		return active;
	}
	
	public void end() throws Exception {
		active = false;
	}

	public Session getSession() {
		return session;
	}

	public org.hibernate.Transaction getTransaction() {
		return transaction;
	}
}