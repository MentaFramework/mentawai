package org.mentawai.filter;

import org.mentawai.core.Action;
import org.mentawai.core.SessionContext.Holder;


/**
 * 
 * This listener can change attributes that will be handled by session
 * 
 * @author Robert Willian Gil
 *
 */
public interface SessionListener {

	/**
	 * Called before set any attribute to the session
	 * @param name of attribute
	 * @param value of the attribute
	 * @param action that is being processed
	 */
	void beforeSetAttribute(String name, Holder<Object> value, Action action);

	
	/**
	 * Called before remove any attribute from session
	 * @param name of attribute
	 * @param value of the attribute
	 * @param action that is being processed
	 */
	void beforeRemoveAttribute(String name, Action action);


	void beforeResetSession(Action action);

	
}
