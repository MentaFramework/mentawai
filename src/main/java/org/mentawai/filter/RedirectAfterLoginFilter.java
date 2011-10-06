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

import org.mentawai.core.Action;
import org.mentawai.core.BaseAction;
import org.mentawai.core.Context;
import org.mentawai.core.Filter;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.Output;
import org.mentawai.core.Redirect;

/**
 * A filter that implements the redirect after login mechanism.
 * Apply this filter to your Login action if you want it to perform a redict 
 * to the first page the user tried to access.
 *
 * @author Sergio Oliveira
 */
public class RedirectAfterLoginFilter implements Filter {
	
    /**
     * The result indicating that a redict after login should be done.
     */
	public static final String REDIR = "redir";
	
    /**
     * Creates a RedirectAfterLoginFilter.
     */
	public RedirectAfterLoginFilter() { }
	
	public String filter(InvocationChain chain) throws Exception {
		Action action = chain.getAction();
		Context session = action.getSession();
		String callback = (String) session.getAttribute(AuthenticationFilter.URL_KEY);
		String result = chain.invoke();
		if (callback != null && !result.equals(BaseAction.ERROR)) {
			Output output = action.getOutput();
			output.setValue(Redirect.REDIRURL_PARAM, callback);
			return REDIR;
		}
		return result;
	}
    
    public void destroy() { }
}
		