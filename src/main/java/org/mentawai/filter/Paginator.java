package org.mentawai.filter;

/**
 * Paginator POJO which contains paged data and other useful information.
 * 
 * The class should only be instantiated and used by the
 * <code>PaginatorFilter</code>!
 * 
 * @author Sven Jacobs <sven.jacobs@web.de>
 * @see PaginatorFilter
 */
public class Paginator {
	
	private Object[] data;
	private int currentPage;
	private int lastPage;
	private int itemsPerPage;
	private int count;

	Paginator() { }

	/**
	 * Instantiate a Paginator with all required data.
	 * 
	 * @param data Paged data
	 * @param currentPage Index of the current page
	 * @param lastPage Index of the last page
	 * @param itemsPerPage Amount of items per page
	 * @param count Amount of all data (all pages)
	 */
	Paginator(Object[] data, int currentPage, int lastPage, int itemsPerPage, int count) {
		
		this.data = data;
		this.currentPage = currentPage;
		this.lastPage = lastPage;
		this.itemsPerPage = itemsPerPage;
		this.count = count;
	}
	
	public boolean hasNext() {
		
		return currentPage != lastPage;
	}
	
	public boolean hasPrevious() {
		
		return currentPage > 1;
	}
	
	public int getNextPage() {
		
		if (hasNext()) {
			
			return currentPage + 1;
			
		} else {
			
			return lastPage;
		}
	}
	
	public int getPreviousPage() {
		
		if (hasPrevious()) {
			
			return currentPage - 1;
			
		} else {
			
			return 1;
		}
	}

	/**
	 * Sets the paged data.
	 * 
	 * @param data Paged data
	 */
	public void setData(Object[] data) {
		this.data = data;
	}

	/**
	 * Returns the paged data.
	 * 
	 * @return Paged data
	 */
	public Object[] getData() {
		return data;
	}

	/**
	 * Set current page index.
	 * 
	 * @param currentPage Current page index
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * Gets current page index.
	 * 
	 * @return Current page index
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * Sets max page index.
	 * 
	 * @param maxPage Max page index
	 */
	public void setMaxPage(int maxPage) {
		this.lastPage = maxPage;
	}

	/**
	 * Gets max page index.
	 * 
	 * @return Max page index
	 */
	public int getMaxPage() {
		return lastPage;
	}

	/**
	 * Sets items per page.
	 * 
	 * <p>
	 * This value reflects the <code>itemsPerPage</code> property of the
	 * <code>PaginatorFilter</code> and not the actual amount of items of the
	 * current page.
	 * </p>
	 * 
	 * @param itemsPerPage Items per page
	 * @see PaginatorFilter
	 */
	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	/**
	 * Gets items per page.
	 * 
	 * <p>
	 * This value reflects the <code>itemsPerPage</code> property of the
	 * <code>PaginatorFilter</code> and not the actual amount of items of the
	 * current page.
	 * </p>
	 * 
	 * @return Items per page
	 * @see PaginatorFilter
	 */
	public int getItemsPerPage() {
		return itemsPerPage;
	}
	
	/**
	 * Return the number of items in this page.
	 * 
	 * This may be less than what this page can display,
	 * for example, you may be displaying 10 items per page
	 * but your collecion has only 15 items. So the last page
	 * will have 5 items only.
	 * 
	 * @return The number of items in this page
	 */
	public int getNumberOfItems() {
		
		if (data != null) return data.length;
		
		return 0;
	}

	/**
	 * Sets the count (amount of all data over all pages)
	 * 
	 * @param count Amount of all data
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * Gets the count (amount of all data over all pages)
	 * 
	 * @return Amount of all data
	 */
	public int getCount() {
		return count;
	}
}