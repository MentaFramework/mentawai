package org.mentawai.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.mentacontainer.Factory;
import org.mentacontainer.Interceptor;
import org.mentawai.log.Debug;
import org.mentawai.log.Info;

/**
 * Handler for JPA Stuff.
 * 
 * @author Robert W. Gil
 */
public class JPAHandler implements Factory, Interceptor<EntityManager> {

	private EntityManagerFactory entityManagerFactory;
	private final boolean transactional;
	
	public Class<? extends Object> getType() {
		return EntityManager.class;
	}
	
	/**
	 * 
	 * @param entityManagerFactory the entity manager factory
	 * @param transactional if true, the transaction always be created and commited
	 */
	public JPAHandler(EntityManagerFactory entityManagerFactory, boolean transactional) {
		this.entityManagerFactory = entityManagerFactory;
		this.transactional = transactional;
		Info.log(String.format("Creating JPAHandler with transactional feature %s", transactional ? "on" : "off"));
	}
	
	/**
	 * 
	 * @param persisnteceUnitName persisten unit name
	 * @param transactional if true, the transaction always be created and commited
	 */
	public JPAHandler(String persisnteceUnitName, boolean transactional) {
		this(Persistence.createEntityManagerFactory( persisnteceUnitName ), transactional);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getInstance() {
		return (T) entityManagerFactory.createEntityManager();
	}

	public void onCreated(EntityManager entityManager) {
		if(transactional) { 
			entityManager.getTransaction().begin();
			if (Debug.isEnabled()) Debug.log("Entity manager transaction begun!");
		}
	}

	public void onCleared(EntityManager entityManager) {
		
		if(transactional && entityManager.getTransaction().isActive()) {
			try {
				entityManager.getTransaction().commit();
				if (Debug.isEnabled()) Debug.log("EntityManager transaction commited successfully");
			} catch (Exception e) {
				entityManager.getTransaction().rollback();
				org.mentawai.log.Error.log("EntityManager transaction was rolled back");
				e.printStackTrace();
			}
			
		}
		entityManager.close();
	}
	
}
