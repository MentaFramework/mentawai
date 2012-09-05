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
import java.util.Set;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.FilterException;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;
import org.mentawai.transaction.Transaction;

import static org.mentalog.Log.*;

/**
 * <p>
 * Mentawai filter for transaction managment. This filters starts a transaction before the action's execution and commits or rollbacks the transaction after the action's execution,
 * depending on the the action's execution result.
 * </p>
 * <p>
 * The default value for commiting the transaction is SUCCESS but this can be configurated, passing a list of results that allow the commit of the transactionon.
 * </p>
 * <p>
 * Also, if a exception is throwed by the action's execution, the transaction is rollbacked.
 * </p>
 * <p>
 * The transaction filter requires that in the moment that the action's executed is filtered the action's input have an object of a org.mentawai.transaction.Transation
 * implementation class. This object may be injected by the IoCFilter. See <a href="http://www.mentaframework.org/transfilter.jsp">the site documentation</a> for details.
 * </p>
 * <p>
 * The default expected key of the transaction is "transaction", but that can be changed.
 * </p>
 * 
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 * @author Sergio Oliveira Jr. (sergio.oliveira.jr@gmail.com)
 */
public class TransactionFilter implements Filter {

	private final static String NAME = "TransactionFilter";

	public static String TRANSACTION_KEY = "transaction";

	private final static boolean ONLY_POST = false;

	private final String transactionKey;

	private Set<String> resultsForRollback = new HashSet<String>();

	private Set<String> resultsForCommit = new HashSet<String>();

	private final boolean onlyPost;

	/**
	 * Creates a new TransactionFilter using the default key for the transaction and the default result for the transaction commit.
	 * 
	 */
	public TransactionFilter() {
		this(TRANSACTION_KEY, ONLY_POST);
	}

	public TransactionFilter(boolean onlyPost) {
		this(TRANSACTION_KEY, onlyPost);
	}

	/**
	 * Creates a new TransactionFilter using the given key for the transaction and the default result for the transaction commit.
	 * 
	 * @param transaction_key
	 */
	public TransactionFilter(String transaction_key) {
		this(transaction_key, ONLY_POST);
	}

	public TransactionFilter(String transaction_key, boolean onlyPost) {
		this.transactionKey = transaction_key;
		this.onlyPost = onlyPost;
		resultsForCommit.add(Action.SUCCESS);
		resultsForRollback.add(Action.ERROR);
	}

	public TransactionFilter commitOn(String... results) {

		for (String result : results) {

			if (resultsForRollback.contains(result)) {
				resultsForRollback.remove(result);
			}
			resultsForCommit.add(result);
		}
		return this;
	}

	public TransactionFilter rollbackOn(String... results) {

		for (String result : results) {

			if (resultsForCommit.contains(result)) {
				resultsForCommit.remove(result);
			}
			resultsForRollback.add(result);
		}
		return this;
	}

	/**
	 * Filters the action, begining a transaction before the action's execution and commiting or rollbacking the trasaction after the action's exection depending on the result.
	 * 
	 */
	public String filter(InvocationChain chain) throws Exception {
		Action action = chain.getAction();
		Input input = action.getInput();

		if (onlyPost) {

			// only execute for POST...

			String method = input.getProperty("method");

			boolean isPost = method != null && method.equalsIgnoreCase("post");

			if (!isPost) return chain.invoke();
		}

		Transaction transaction = (Transaction) input.getValue(transactionKey);

		// special case: two actions with transaction filter being called in a chain:
		/*
		 * if (transaction != null && (transaction.wasCommited() || transaction.wasRolledBack())) {
		 * 
		 * input.removeValue(transactionKey);
		 * 
		 * transaction = (Transaction) input.getValue(transactionKey); }
		 * 
		 * // by Sergio: You can now freely re-use transaction (since 1.16)
		 */

		if (transaction == null) {

			Debug.log(NAME, "Transaction was NULL inside TransactionFilter!!!");

			throw new FilterException("Cannot find transaction in action's " + "input with the given key: " + transactionKey);
		}

		try {

			Debug.log(NAME, "Beginning transaction...");

			transaction.begin();

			Debug.log(NAME, "Transaction was begun! Will invoke action...");

			String result = chain.invoke();

			boolean shouldRollback = resultsForRollback.contains(result);

			boolean shouldCommit = resultsForCommit.contains(result);

			if (shouldCommit || (!shouldCommit && !shouldRollback)) {

				Debug.log(NAME, "Result was ok! Will commit the transaction...", "Result =", result);

				transaction.commit();

				Debug.log(NAME, "Transaction was committed!");

			} else {

				Debug.log(NAME, "Result was not ok! Will rollback the transaction...", "Result =", result);

				transaction.rollback();

				Debug.log(NAME, "Transaction was rolled back!");
			}

			return result;

		} catch (Exception e) {

			e.printStackTrace();

			// rollbacks the transcion if any error occours.

			Debug.log(NAME, "An exception was thrown while executing action! Will try to rollback...", "msg =", e.getMessage());

			if (transaction.isActive()) {
				transaction.rollback();
				Debug.log(NAME, "Transaction was rolled back!");
			}

			throw e;
		}
	}

	/**
	 * @return the results that will make the transaction be commited after the action's execution.
	 * 
	 */
	public Set<String> getResultsForCommit() {
		return resultsForCommit;
	}

	public Set<String> getResultsForRollback() {
		return resultsForRollback;
	}

	/**
	 * Do nothing.
	 */
	public void destroy() {

	}

}