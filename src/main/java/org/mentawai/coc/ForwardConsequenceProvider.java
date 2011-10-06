package org.mentawai.coc;

import org.mentawai.core.Consequence;
import org.mentawai.core.Forward;

/**
 * A ConsequenceProvider that provides ForwardConsequences based on the given
 * convetion: If the execute() method was invoked: /(the name of the
 * action)/(the result of the action's execution).jsp If a inner action was
 * invojed: /(the name of the action)/(the name of the inner action)/(the result
 * of the action's execution).jsp
 * 
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 * 
 */
public class ForwardConsequenceProvider implements ConsequenceProvider {

	private static final String SEP = "/";
	private static final String POSFIX = ".jsp";

	/**
	 * Create an consequence based on the convention described in the Class
	 * javadoc.
	 * 
	 * @param action
	 *            the name of the action
	 * @param result
	 *            the result of the actions execution.
	 * @param innerAction
	 *            the inner action called or null if the execute() method was
	 *            called.
	 * @return the Consequence created by convention.
	 * 
	 */
	public Consequence getConsequence(String action, Class<? extends Object> actionClass, String result,
			String innerAction) {
		String url = action
				+ (innerAction == null ? SEP : SEP + innerAction + SEP)
				+ result + POSFIX;

		// TODO cache?
		return new Forward(url);
	}

}
