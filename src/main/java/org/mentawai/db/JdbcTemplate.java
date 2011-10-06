package org.mentawai.db;

import java.sql.Connection;

import org.mentawai.transaction.Transaction;

public interface JdbcTemplate<E> {
	
	
	public Object exec(E e, Connection conn, Transaction transaction) throws Exception;
	
	
}