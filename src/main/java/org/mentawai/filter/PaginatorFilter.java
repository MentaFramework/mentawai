package org.mentawai.filter;

import java.util.Collection;

import org.mentawai.core.Action;
import org.mentawai.core.Filter;
import org.mentawai.core.Input;
import org.mentawai.core.InvocationChain;
import org.mentawai.core.Output;

/**
 * <p>
 * This filter easily enables paging of data on top of your current structure.
 * The data that should be paged must be placed in the output stack with the
 * specified key (see constructor). Only arrays or <code>Collections</code>
 * can be paged, other data will be silently ignored.
 * </p>
 * <p>
 * The data with the specified key will be replaced by an instance of the
 * <code>Paginator</code> class which contains the paged data and some other
 * useful information.
 * </p>
 * <p>
 * Let's say you want paged search results: Add this filter to the
 * <code>ActionConfig</config> of your search action and don't forget to
 *   pass the page parameter to the query string:
 * </p>
 * <p>
 *   <code>http://example.com/search.mtw?q=keywords&p=0</code>
 * </p>
 *   The page parameter (which can be modified, see constructor) starts at 1.
 *   Use the other properties of the <code>Paginator</code> class to obtain
 *   information about the amount of all data, current page, maximum page and
 *   so on. With these information you can create links to the previous and
 *   next pages of your paged data.
 * </p>
 * <p>
 *  Mentawai provides the PaginatorTag, but this filter can be more useful for
 *  Ajax requests or any other situation when you don't want or cannot use the 
 *  PaginatorTag.
 *  </p>
 *
 * @author Sven Jacobs <sven.jacobs@web.de>
 * @see Paginator
 */
public class PaginatorFilter implements Filter {
	
	public final static String DEFAULT_PAGE_PARAM = "p";
	public final static int DEFAULT_ITEMS_PER_PAGE = 20;
	
	private int itemsPerPage;
	private String key;
	private String pageParam;

	/**
	 * Instantiate a PaginatorFilter with the specified key pointing to the data
	 * in the output stack which should be paged.
	 * 
	 * @param key Key of the data from the output stack to page
	 */
	public PaginatorFilter(String key) {
		this(key, DEFAULT_ITEMS_PER_PAGE, DEFAULT_PAGE_PARAM);
	}

	/**
	 * Instantiate a PaginatorFilter with the specified key and amount of items
	 * per page.
	 * 
	 * @param key Key of the data from the output stack to page
	 * @param itemsPerPage Amount of items per page
	 */
	public PaginatorFilter(String key, int itemsPerPage) {
		this(key, itemsPerPage, DEFAULT_PAGE_PARAM);
	}

	/**
	 * Instantiate a PaginatorFilter with the specified key, amount of items per
	 * page and name of the page parameter.
	 * 
	 * @param key Key of the data from the output stack to page
	 * @param itemsPerPage Amount of items per page
	 * @param pageParam Name of the page parameter
	 */
	public PaginatorFilter(String key, int itemsPerPage, String pageParam) {
		this.key = key;
		this.itemsPerPage = itemsPerPage;
		this.pageParam = pageParam;
	}

	/**
	 * The filter replaces the specified data with an instance of
	 * <code>Paginator</code> which contains the paged data and some other
	 * useful information.
	 * 
	 * Note: Only arrays and <code>Collections</code> can be paged.
	 * 
	 * @see Paginator
	 */
	public String filter(InvocationChain chain) throws Exception {
		
		Action action = chain.getAction();

		String result = chain.invoke();

		Output output = action.getOutput();
		
		Object value = output.getValue(key);

		if (value != null) {
			
			Object[] arr;

			if (value.getClass().isArray()) {
				
				arr = (Object[]) value;
				
			} else if (value instanceof Collection) {
				
				Collection<?> coll = (Collection<?>) value;
				
				arr = coll.toArray();
				
			} else {
				
				return result;
				
			}

			Input input = action.getInput();
			
			int page = input.getInt(pageParam);

			if (page <= 0) page = 1;

			int startIndex = (page - 1) * itemsPerPage;
			
			if (startIndex >= arr.length) {
				startIndex = 0;
			}

			int endIndex = startIndex + itemsPerPage;
			
			if (endIndex > arr.length) {
				endIndex = arr.length;
			}

			// endIndex is NOT inclusive
			Object[] newArr = copyOfRange(arr, startIndex, endIndex);
			
			// pages start with 1 not zero...
			
			int currentPage = (startIndex / itemsPerPage) + 1;
			
			int lastPage;
			
			if (arr.length % itemsPerPage == 0) {
				
				// 30 / 30 = 3 pages
				// 20 / 20 = 2 pages
				
				lastPage = arr.length / itemsPerPage;
				
			} else {
				
				// 33 / 30 = 4 pages
				// 28 / 30 = 3 pages
				
				lastPage = (arr.length / itemsPerPage) + 1;
			}
			
			Paginator p = new Paginator(newArr, currentPage, lastPage, itemsPerPage, arr.length);

			output.setValue(key, p);
		}

		return result;
	}
	
	public static Object[] copyOfRange(Object[] original, int from, int to) {
		
        int newLength = to - from;
        
        if (newLength < 0) throw new IllegalArgumentException(from + " > " + to);
        
        Object[] copy = new Object[newLength];
        
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        
        return copy;
    }
	
	public void destroy() {	}

}