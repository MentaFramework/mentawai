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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.mentawai.core.Action;
import org.mentawai.core.AfterConsequenceFilter;
import org.mentawai.core.Consequence;
import org.mentawai.core.FilterException;
import org.mentawai.core.InputWrapper;
import org.mentawai.core.InvocationChain;
import org.mentawai.transaction.JpaTransaction;
import org.mentawai.transaction.Transaction;

/**
 * Use this filter to place an opened and connected JPA entityManager in the
 * action input. There is no need to close the entityManager. The filter does it
 * automatically when the action ends. The action is injected using the <i>pull</i>.
 * 
 * @author Marvin H Froeder (velo.br@gmail.com)
 */
public class JpaFilter extends InputWrapper implements AfterConsequenceFilter {

	/** The default key name to use when placing the entityManager in the action input. */
	public static final String KEY = "jpa_entityManager";

	/**
	 * The default key name to use when placing a jpa transaction in the
	 * action input.
	 */
	public static final String TRANS_KEY = "jpa_transaction";

	private EntityManagerFactory factory;

	private String jpaEntityManagerKey = KEY;

	private String jpaTransactionKey = TRANS_KEY;

	private ThreadLocal<EntityManager> jpaEntityManagerThreadLocal = new ThreadLocal<EntityManager>();

	private ThreadLocal<Transaction> jpaTransactionThreadLocal = new ThreadLocal<Transaction>();

	private boolean transactional = false;

	private Set<String> resultsForRollback = new HashSet<String>();

	public JpaFilter(EntityManagerFactory factory) {
		this.factory = factory;
	}

	public JpaFilter(EntityManagerFactory factory, boolean transactional) {
		this.factory = factory;
		setTransactional(transactional);
	}

	public JpaFilter(String persistenceUnit) {
		factory = Persistence.createEntityManagerFactory(persistenceUnit);
	}

	public JpaFilter(String persistenceUnit, boolean transactional) {
		factory = Persistence.createEntityManagerFactory(persistenceUnit);
		setTransactional(transactional);
	}

	public JpaFilter(String persistenceUnit, boolean transactional,
			Map configOverrides) {
		factory = Persistence.createEntityManagerFactory(persistenceUnit,
				configOverrides);
		setTransactional(transactional);
	}

	public JpaFilter(String persistenceUnit, Map configOverrides) {
		factory = Persistence.createEntityManagerFactory(persistenceUnit,
				configOverrides);
	}

	public void setTransactional(boolean transactional) {
		this.transactional = transactional;

		addResultsForRollback(Action.ERROR, ExceptionFilter.EXCEPTION);
	}

	public void addResultsForRollback(String... results) {
		for (int i = 0; i < results.length; i++) {
			resultsForRollback.add(results[i]);
		}
	}

	public void setTransactional(boolean transactional, String transactionKey) {
		setTransactional(transactional);

		this.jpaTransactionKey = transactionKey;
	}

	public String toString() {

		StringBuilder sb = new StringBuilder(128);
		sb.append("JPAFilter: ").append("entityManagerKey=").append(
				jpaEntityManagerKey);
		sb.append(" transKey=").append(jpaTransactionKey);
		sb.append(" transactional=").append(transactional);

		return sb.toString();
	}

	/**
	 * Sets the action input key to use. The JPA entityManager will be placed in
	 * the action input with this key.
	 * 
	 * @param key
	 *            The action input key.
	 */
	public void setKey(String key) {
		this.jpaEntityManagerKey = key;
	}

	/**
	 * Sets the action to get an JPA EntityManager using the <i>pull</i> model.
	 */
	public String filter(InvocationChain chain) throws Exception {
		Action action = chain.getAction();

		super.setInput(action.getInput());

		action.setInput(this);

		String result = chain.invoke();

		// commit or rollback the transaction BEFORE consequence...

		if (transactional) {

			Transaction trans = jpaTransactionThreadLocal.get();
			
			jpaTransactionThreadLocal.remove();

			if (trans != null) {

				removeValue(jpaTransactionKey);

				boolean shouldRollback = result == null
						|| resultsForRollback.contains(result);

				if (!shouldRollback) {

					try {

						trans.commit();

					} catch (Exception e) {

						e.printStackTrace();

						throw new FilterException(
								"Unable to commit jpa transaction!", e);
					}

				} else {

					try {

						trans.rollback();

					} catch (Exception e) {

						e.printStackTrace();

						throw new FilterException(
								"Unable to rollback jpa transaction!", e);
					}
				}
			}
		}

		return result;
	}

	public void afterConsequence(Action action, Consequence c,
			boolean conseqExecuted, boolean actionExecuted, String result) {

		EntityManager entityManager = jpaEntityManagerThreadLocal.get();
		
		jpaEntityManagerThreadLocal.remove();

		if (entityManager != null) {
			removeValue(jpaEntityManagerKey);
			if (entityManager.isOpen()) {
			    entityManager.close();
			}
		}
	}

	public void destroy() {
		factory.close();
	}

	/**
	 * Verifies if the requested object is a JPA EntityManager, return a
	 * JPA EntityManager if it is and return the content of the action's input
	 * if not.
	 */
	public Object getValue(String key) {

		if (key.equals(jpaEntityManagerKey)) {
			EntityManager entityManager = jpaEntityManagerThreadLocal.get();
			if (entityManager == null) {

				entityManager = factory.createEntityManager();
				jpaEntityManagerThreadLocal.set(entityManager);

				if (transactional) {

					EntityTransaction tx = entityManager.getTransaction();

					Transaction trans = new JpaTransaction(entityManager, tx);

					jpaTransactionThreadLocal.set(trans);

					setValue(jpaTransactionKey, trans);

				}

				setValue(key, entityManager);

			}
			return entityManager;

		}

		return super.getValue(key);
	}
}