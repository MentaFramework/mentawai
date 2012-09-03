package org.mentawai.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.mentacontainer.Factory;
import org.mentacontainer.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Handler for JPA Stuff.
 * 
 * @author Robert W. Gil
 */
public class JPAHandler implements Factory, Interceptor<EntityManager> {

	private static final Logger logger = LoggerFactory.getLogger(JPAHandler.class);
	
	private EntityManagerFactory entityManagerFactory;
	private final boolean transactional;
	
	public Class<? extends Object> getType() {
		return EntityManager.class;
	}
	
	/**
	 * 
	 * @param entityManagerFactory
	 * @param transactional if true, the transaction always be created and commited
	 */
	public JPAHandler(EntityManagerFactory entityManagerFactory, boolean transactional) {
		this.entityManagerFactory = entityManagerFactory;
		this.transactional = transactional;
		
		logger.info(String.format("Creating JPAHandler with transactional feature %s", transactional ? "on" : "off"));
	}
	
	public JPAHandler(String persisnteceUnitName, boolean transactional) {
		this(Persistence.createEntityManagerFactory( persisnteceUnitName ), transactional);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getInstance() {
		return (T) entityManagerFactory.createEntityManager();
	}

	public void onCreated(EntityManager entityManager) {
		
		logger.debug("EntityManager created");
		
		if(transactional) { 
			entityManager.getTransaction().begin();
			logger.debug("EntityManager transaction begun");
		}
		
	}

	public void onCleared(EntityManager entityManager) {
		
		if(transactional && entityManager.getTransaction().isActive()) {
			try {
				entityManager.getTransaction().commit();
				logger.debug("EntityManager transaction commited successfully");
			} catch (Exception e) {
				entityManager.getTransaction().rollback();
				logger.error("EntityManager transaction was rolled back");
			}
			
		}
		
		entityManager.close();
		logger.debug("EntityManager closed");
	}
	
}
