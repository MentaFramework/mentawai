package org.mentawai.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


/**
 * JPA Utilities
 * 
 * Java Persistence API
 * 
 * JSR 220 - http://jcp.org/aboutJava/communityprocess/final/jsr220/index.html
 * 
 * @author Fernando Boaglio
 */
public class JPAHelper {

	private static EntityManagerFactory entityManagerFactory;
	
	private static String persistenceUnit;
	

	/**
	 * Set Persistence Unit value for ApplicationManager
	 * @param value
	 */
	public static void setPersistenceUnit(String value) {
		persistenceUnit = value;
	}
	
	/**
	 * get transaction start
	 * @return EntityManager
	 */
	public static EntityManager beginTransaction() {
		setupEntityManagerFactory();
		EntityManager em = entityManagerFactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		return em;
	}
	
	/**
	 * get transaction end
	 * @param em
	 */
	public static void commitTransaction(EntityManager em) {
		EntityTransaction tx = em.getTransaction();
		tx.commit();
		em.close();
	}

	/**
	 * Get EM for SELECTs
	 * @return EntityManager
	 */
	public static EntityManager getEntityManagerForQueries() {
		setupEntityManagerFactory();
		return entityManagerFactory.createEntityManager();
	}

	/**
	 * Close EM for SELECTs
	 */
	public static void closeEntityManagerForQueries(EntityManager em) {
		em.close();
	}
	
	/**
	 * Check if exists a defined persistence unit
	 * @return boolean
	 */
	public static boolean existsJPAPersistenceUnit() {
		if (persistenceUnit==null) return false;
		return true;
	}
	
	/**
	 * Creates EntityManager factory
	 */
	private static void  setupEntityManagerFactory() {
		
		// if there is no persistence unit defined...
		if ("".equals(persistenceUnit)) return;
		
		// check if factory is up and running...
		if (entityManagerFactory!=null && entityManagerFactory.isOpen()) return; 
		
		// creates EM factory
		entityManagerFactory =  Persistence.createEntityManagerFactory(persistenceUnit);
	}

}

