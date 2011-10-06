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
package org.mentawai.transaction;

/**
 * Defines the behaviour of a transaction.
 *
 * @author Sergio Oliveira
 */
public interface Transaction {
    
    /**
     * Begins the transaction.
     */
    public void begin() throws Exception;
    
    /**
     * Commits the transaction.
     */
    public void commit() throws Exception;
    
    /**
     * Rollbacks the transaction.
     */
    public void rollback() throws Exception;
    
    /**
     * Is the transaction still active, in other words,
     * is it still not commited and not rolledback ?
     *
     * @return true if the transaction was not commited and was not rolledback
     */
    public boolean isActive();
    
    public void end() throws Exception;
}