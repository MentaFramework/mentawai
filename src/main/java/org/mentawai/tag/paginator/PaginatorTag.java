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
package org.mentawai.tag.paginator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.mentawai.tag.Out;
import org.mentawai.tag.util.ListContext;

/**
 * @author Sergio Oliveira
 */
public class PaginatorTag extends BodyTagSupport implements ListContext {
    
    public static final String DEFAULT_PAGENUM_PARAM = "page";
    
    public static final int DEFAULT_MAX_PER_PAGE = 10;                
    
    private int maxPerPage = DEFAULT_MAX_PER_PAGE;
    private String pageNumParam = DEFAULT_PAGENUM_PARAM;
    private int currPage = 0;
    private List<Object> list = null;
    private String value = null;
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public void setSize(int size) {
        this.maxPerPage = size;
    }
    
    public void setPageNumParam(String pageNumParam) {
        this.pageNumParam = pageNumParam;
    }
    
    public int getCurrPage() {
        return currPage;
    }
    
    public int getTotal() {
    	
    	return list != null ? list.size() : 0;
    }
    
    public int getFrom() {
    	
    	int currPage = getCurrPage();
    	
    	return ((currPage - 1) * maxPerPage) + 1;
    }
    
    public int getTo() {
    	
    	int currPage = getCurrPage();
    	
    	boolean hasNextPage = hasNextPage();
    	
    	if (hasNextPage) {
    		
    		return currPage * maxPerPage;
    		
    	} else {
    		
    		return list.size();
    	}
    }
    
    public int getLastPage() {
        
        int total = list.size();
        
        int last = total / maxPerPage;
        
        if (total % maxPerPage != 0) last++;
        
        return last;
    }
    
    public boolean hasNextPage() {
        int high = ((currPage - 1) * maxPerPage) + maxPerPage;
        if (high >= list.size()) return false;
        return true;
    }
    
    public boolean hasPreviousPage() {
        if (currPage - 1 == 0) return false;
        return true;
    }
    
    public List<Object> getList() {
    	
    	if (list.size() == 0) return list;
    	
        int low = (currPage - 1) * maxPerPage;
        int high = low + maxPerPage;
        
        if (low >= list.size()) throw new IllegalStateException("LOW = " + low + " and LIST = " + list.size());
        
        if (high > list.size()) high = list.size();
		
		int size = high - low;
		List<Object> retlist = new ArrayList<Object>(size);
		for(int i=0;i<size;i++) {
			retlist.add(list.get(low + i));
		}
        return retlist;
    }
    
    public int doStartTag() throws JspException {
        
        Tag parent = findAncestorWithClass(this, ListContext.class);
        
        if (parent != null) {
            
            ListContext ctx = (ListContext) parent;
            
            this.list = ctx.getList();
            
        } else {
            
            Object o = Out.getValue(value, pageContext, false);
            
            if (o instanceof List) {
                
                this.list = (List<Object>) o;
                
            } else if (o instanceof Collection) {
            	
            	Collection<Object> c = (Collection<Object>) o;
            	
            	this.list = new ArrayList<Object>(c.size());
            	
            	for(Object obj : c) {
            		
            		this.list.add(obj);
            	}
            }
        }
        
        Object o = pageContext.getRequest().getParameter(pageNumParam);
        
        if (o == null) { // allow to get it from session as well....

        	o = pageContext.findAttribute(pageNumParam);
        }
        
        
        String page = null;
        if (o != null) page = o.toString();
        
        if (page == null) currPage = 1;
        else currPage = Integer.parseInt(page);
        
        if (this.list == null/* || this.list.size() == 0*/) return SKIP_BODY;
        
        return EVAL_BODY_BUFFERED;
    }
    
    public int doAfterBody() throws JspException {
        BodyContent bc = getBodyContent();
        try {
             if (bc != null) bc.writeOut(bc.getEnclosingWriter());
        } catch(IOException e) {
            throw new JspException(e);
        } finally {
            if (bc != null) bc.clearBody();
            this.list = null; // necessary for GC and also to avoid carrying over to the next request!
        }
        return SKIP_BODY;
    }
}

    
    
    
    
    
    
    
