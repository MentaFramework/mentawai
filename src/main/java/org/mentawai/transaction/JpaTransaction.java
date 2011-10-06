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

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * @author Marvin H Froeder (velo.br@gmail.com)
 */
public class JpaTransaction implements Transaction {

	private EntityManager session = null;

	private EntityTransaction transaction = null;

	private boolean active = false;

	public JpaTransaction() {
	}

	public JpaTransaction(EntityManager session) {
		this.session = session;
	}

	public JpaTransaction(EntityManager session, EntityTransaction transaction) {
		this(session);
		this.transaction = transaction;
	}

	public void setSession(EntityManager session) {
		this.session = session;
	}

	public void setTransaction(EntityTransaction transaction) {
		this.transaction = transaction;
	}

	public void begin() throws Exception {
		if (session == null)
			throw new IllegalStateException(
					"HibernateTransaction does not have a hibernate session!");

		// by Sergio: check if transaction was passed in constructor...
		if (transaction == null) {
			transaction = session.getTransaction();
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

	public EntityManager getSession() {
		return session;
	}

	public EntityTransaction getTransaction() {
		return transaction;
	}
}