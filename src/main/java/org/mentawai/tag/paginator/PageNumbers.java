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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.mentawai.tag.util.Context;
import org.mentawai.tag.util.LoopTag;

/**
 * @author Sergio Oliveira
 */
public class PageNumbers extends LoopTag implements Context {
    
    private int pagesToShow = 5;
    
    private String var = "pageNum";
    
    public void setPagesToShow(int pagesToShow) {
        
        this.pagesToShow = pagesToShow;
        
    }
    
    public void setVar(String var) {
        
        this.var = var;
    }
    
    public Object getObject() throws JspException {
        
        return pageContext.getAttribute(var);
        
    }
    
    private int startPageNum;
    
    private int endPageNum;
    
    private int currPage;
    
    private PaginatorTag paginator = null;
    
    public int getCurrPage() {
        
        return currPage;
        
    }
    
    public void init() throws JspException {
        
        Tag parent = findAncestorWithClass(this, PaginatorTag.class);
        
        if (parent != null) {
            
            paginator = (PaginatorTag) parent;
            
        } else {
            
            throw new JspException("PageNumbers not enclosed by a PaginatorTag !!!");
            
        }
        
        int currPageNum = paginator.getCurrPage();
        
        startPageNum = currPageNum - pagesToShow;
        
        if (startPageNum < 1) startPageNum = 1;
        
        endPageNum = currPageNum + pagesToShow;
        
        int lastPage = paginator.getLastPage();
        
        if (endPageNum > lastPage) endPageNum = lastPage;
        
        this.currPage = startPageNum;
    }

    public boolean loopCondition() throws JspException {
        
        if (currPage > endPageNum) return false;
        
        if (startPageNum == endPageNum) return false; // only one page?
        
        pageContext.setAttribute(var, new Integer(currPage));
        
        return true;
    }
    
    public void afterEachLoop() throws JspException {
        
        currPage++;
    }
    
    public int doEndTag() throws JspException {
        
        pageContext.removeAttribute(var);
        
        return super.doEndTag();
        
    }
}
