package org.mentawai.filter;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;

public class PaginationFilter implements Filter {

	public static final int LIMIT_PAGINATION = 50;
	public static final String KEY_PAGINATION = "pagination";
	
	public static final String PARAM_NUMBER_PAGE = "param-number-page";
	public static final String PARAM_SORT_ATTRIBUTE = "param-sort-attribute";
	public static final String PARAM_IS_DESC = "param-is-desc";

	private String defaultOrder;
	private int limitRecord;

	private String key;

	public PaginationFilter(String defaultOrder) {
		this(defaultOrder, LIMIT_PAGINATION);
	}

	public PaginationFilter(String defaultOrder, int limitRecord) {
		this(defaultOrder, limitRecord, KEY_PAGINATION);
	}
	
	public PaginationFilter(String defaultOrder, int limitRecord, String key) {
		this.defaultOrder = defaultOrder;
		this.limitRecord = limitRecord;
		this.key = key;
	}

	@Override
	public String filter(InvocationChain chain) throws Exception {
		final Action action = chain.getAction();
		final Input input = action.getInput();

		int numberPagina = getNumberPage(input);
		String sortAttribute = getSortAttribute(input);
		boolean desc = isDesc(input);

		int initRecord = 0;
		if (numberPagina > 0) {
			initRecord = limitRecord * (numberPagina - 1);
		}

		if (sortAttribute == null || "".equals(sortAttribute)) {
			sortAttribute = defaultOrder;
		}

		input.setValue(key, new Pagination(initRecord, limitRecord, sortAttribute, desc));

		return chain.invoke();
	}

	protected int getNumberPage(Input input) {
		return input.getInt(PARAM_NUMBER_PAGE);
	}

	protected String getSortAttribute(Input input) {
		return input.getString(PARAM_SORT_ATTRIBUTE);
	}

	protected boolean isDesc(Input input) {
		return input.getBoolean(PARAM_IS_DESC);
	}

	@Override
	public void destroy() {
	}

	public String getKey() {
		return key;
	}

}
