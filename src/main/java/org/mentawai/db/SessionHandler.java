package org.mentawai.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mentacontainer.Factory;
import org.mentacontainer.Interceptor;

public class SessionHandler implements Factory, Interceptor<Session> {
	
	private final SessionFactory sessionFactory;
	
	public SessionHandler(SessionFactory sf) {
		
		this.sessionFactory = sf;
	}
	
	public Session getSession() {
		
		return sessionFactory.openSession();
	}
	
	public <T> T getInstance() {
		
		return (T) getSession();
			
	}
	
	public void release(Session session) {
		
		session.close();
	}
	
	public void onCleared(Session session) {
		
		release(session);
	}
	
	public void onCreated(Session session) {
		
	}
	
	public Class<? extends Object> getType() {
		
		return Session.class;
	}
	
}