package org.mentawai.filter;


/**
 * Pagination POJO contem informacoes para fazer a paginacao sob demanda.
 * 
 * 
 * @author Caio Oliveira <caio@javacia.com.br>
 * @see PaginationFilter
 */
public class Pagination {

	private int initRecord;
	private int limitRecord;
	private String sortAttribute;
	private boolean desc;

	public Pagination(int initRecord, int limitRecord, String sortAttribute, boolean desc) {
		this.initRecord = initRecord;
		this.limitRecord = limitRecord;
		this.sortAttribute = sortAttribute;
		this.desc = desc;
	}

	public int getInitRecord() {
		return initRecord;
	}

	public int getLimitRecord() {
		return limitRecord;
	}

	public String getSortAttribute() {
		return sortAttribute;
	}

	public boolean isDesc() {
		return desc;
	}

	@Override
	public String toString() {
		return "Pagination [initRecord=" + initRecord + ", limitRecord=" + limitRecord + ", sortAttribute=" + sortAttribute + ", desc=" + desc + "]";
	}

}
