package org.mentawai.filter;

import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.mentawai.core.Input;

public class PaginationDisplayTagFilter extends PaginationFilter {

	private String tableId;
	
	public PaginationDisplayTagFilter(String tableId, String defaultOrder) {
		super(defaultOrder);
		this.tableId = tableId;
	}

	public PaginationDisplayTagFilter(String tableId, String defaultOrder, int limitRecord) {
		super(defaultOrder, limitRecord);
		this.tableId = tableId;
	}

	public PaginationDisplayTagFilter(String tableId, String defaultOrder, int limitRecord, String key) {
		super(defaultOrder, limitRecord, key);
		this.tableId = tableId;
	}

	protected int getNumberPage(Input input) {
		String page = (new ParamEncoder(tableId).encodeParameterName(TableTagParameters.PARAMETER_PAGE));
		return input.getInt(page);	
	}

	protected String getSortAttribute(Input input) {
		String sort = (new ParamEncoder(tableId).encodeParameterName(TableTagParameters.PARAMETER_SORT));
		return input.getString(sort);
	}

	protected boolean isDesc(Input input) {
		String order = (new ParamEncoder(tableId).encodeParameterName(TableTagParameters.PARAMETER_ORDER));
		return input.getInt(order) == 1;
	}

	public String getTableId() {
		return tableId;
	}

}
