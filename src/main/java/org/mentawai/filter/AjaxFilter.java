package org.mentawai.filter;

import org.mentawai.core.Action;
import org.mentawai.core.BaseAction;
import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;

public class AjaxFilter implements Filter {
	
	private static final String DEFAULT_RESULT_FOR_AJAX = Action.AJAX;

	private final String resultForAjax;
	
	public AjaxFilter(String resultForAjax) {
		this.resultForAjax = resultForAjax;
	}
	
	public AjaxFilter() {
		this(DEFAULT_RESULT_FOR_AJAX);
	}

	@Override
    public String filter(InvocationChain chain) throws Exception {
		String originalResult = chain.invoke();
		Input input = chain.getAction().getInput();
		if (BaseAction.isAjaxRequest(input)) {
			return resultForAjax;
		}
		return originalResult;
    }

	@Override
    public void destroy() {

	}
}