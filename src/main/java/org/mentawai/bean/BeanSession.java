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
package org.mentawai.bean;

import java.util.List;

import org.mentawai.transaction.Transaction;

/**
 * Describe a simple ORM interface that can perform CRUD for Beans, Joins for 
 * relationships and load a list of beans according to properties.
 * 
 * @author Sergio Oliveira Jr.
 */
public interface BeanSession {
	
	/**
	 * Load the bean from the database,
	 * injecting all its properties through reflection.
	 * Note that the bean passed MUST have its primary key set.
	 * 
	 * @param bean The bean we want to load from the DB.
	 * @return true if the bean was found in the database, false otherwise 
	 * @throws Exception if bean was found but could not be loaded for some error condition
	 */
	public boolean load(Object bean) throws Exception;
	
	/**
	 * Update the bean in the database. Only the fields that have
	 * been modified (dirty) will be updated.
	 * 
	 * It will return 1 if an update did happen or 0 if the bean could not
	 * be found in the database or if there was nothing to update in the bean.
	 * 
	 * The bean MUST have its primary key set, otherwise it is impossible to update
	 * the bean in the database, and an exception will be thrown.
	 * 
	 * @param bean The bean to be updated
	 * @return 1 if update was successful, 0 if the update did not happen
	 * @throws Exception
	 */
	public int update(Object bean) throws Exception;
	
	public int update(Object bean, boolean dynUpdate) throws Exception;
	
	/**
	 * Insert the bean in the database.
	 * 
	 * Depending on the type of PK, the generation of the PK can and
	 * should be taken care by the DB itself. The generated PK should
	 * be inserted in the bean by reflection.
	 * 
	 * The default, database-independent implementation of this method
	 * must only insert all fields in the database not worrying about
	 * PK generation issues.
	 * 
	 * @param bean The bean to insert
	 * @throws Exception
	 */
	public void insert(Object bean) throws Exception;
	
	/**
	 * Add a dependency based on a relationship OneToMany/ManyToMany.
	 * 
	 * Returns true if the dependency was really added or false if it
	 * was already there.
	 * 
	 * @param bean1
	 * @param bean2
	 * @throws Exception
	 */
	public boolean add(Object bean1, Object bean2) throws Exception;
	
	/**
	 * Remove a dependency based on a relationship OneToMany/ManyToMany.
	 * 
	 * Returns true if the dependency was really removed or false if it
	 * was not there.
	 * 
	 * @param bean1
	 * @param bean2
	 * @return true if the dependency was really removed or false if it was not there.
	 * @throws Exception
	 */
	public boolean remove(Object bean1, Object bean2) throws Exception;
	
	/**
	 * Delete the bean from the database.
	 * 
	 * The PK of the bean MUST be set.
	 * 
	 * @param bean
	 * @return true if it was deleted or false if it did not exist
	 * @throws Exception
	 */
	public boolean delete(Object bean) throws Exception;
	
	/**
	 * Create a transaction for this session.
	 * 
	 * Please check org.mentawai.transaction package.
	 * 
	 * @return The org.mentawai.transaction.Transaction
	 * @throws Exception
	 */
	public Transaction beginTransaction() throws Exception;

	public List<Integer> loadJoinIds(Object bean, Class<? extends Object> klass) throws Exception;
	
	public <E> List<E> loadJoin(Object bean, Class<? extends E> klass) throws Exception;
	
	public int countJoin(Object bean, Class<? extends Object> klass) throws Exception;
	
	public <E> List<E> loadList(E bean) throws Exception;
	
	public <E> List<E> loadList(E bean, String orderBy) throws Exception;
	
	public <E> List<E> loadList(E bean, int limit) throws Exception;
	
	public <E> List<E> loadList(E bean, String orderBy, int limit) throws Exception;
	
	public <E> E loadUnique(E bean) throws Exception;
	
}