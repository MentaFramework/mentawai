package org.mentawai.bean;

import java.util.List;

import org.mentawai.core.Input;


public abstract class BeanBase {
	
	public static String SESSION_KEY = "beanSession";
	
	private transient BeanSession session;
	
	public void setBeanSession(BeanSession session) {
		this.session = session;
	}
	
	public void setBeanSession(Input input) {
		this.session = (BeanSession) input.getValue(SESSION_KEY);
	}
	
	private void checkIfSessionNull() {
		
		if (session == null) throw new IllegalStateException("Session is null!");
	}
	
	public boolean save(BeanSession session) {
		
		try {
		
			return session.update(this) == 1;
			
		} catch(Exception e) {
			
			throw new RuntimeException(e);
		}
	}
	
	public boolean save() {
		
		checkIfSessionNull();
		
		return save(session);
	}
	
	public boolean update(BeanSession session, boolean dynUpdate) {
		
		try {
		
			return session.update(this, dynUpdate) == 1;
			
		} catch(Exception e) {
			
			throw new RuntimeException(e);
		}			
	}
	
	public boolean update(boolean dynUpdate) {
		
		checkIfSessionNull();
		
		return update(session, dynUpdate);
	}
	
	public boolean update(BeanSession session) {
		
		try {
		
			return save(session);
			
		} catch(Exception e) {
			
			throw new RuntimeException(e);
		}
	}
	
	public boolean update() {
		
		checkIfSessionNull();
		
		return update(session);
	}
	
	public void insert(BeanSession session) {
		
		try {
			session.insert(this);
			
		} catch(Exception e) {
			
			throw new RuntimeException(e);
		}
	}
	
	public void insert() {
		
		checkIfSessionNull();
		
		insert(session);
	}
	
	public boolean delete(BeanSession session) {
		
		try {
		
			return session.delete(this);
			
		} catch(Exception e) {
			
			throw new RuntimeException(e);
		}
	}
	
	public boolean delete() {
		
		checkIfSessionNull();
		
		return delete(session);
	}
	
	public boolean load(BeanSession session) {
		
		try {
		
			return session.load(this);
		
		} catch(Exception e) {
			
			throw new RuntimeException(e);
		}		
	}
	
	public boolean load() {
		
		checkIfSessionNull();
		
		return load(session);
	}
	
	public boolean reload(BeanSession session) {
		
		return load(session);
	}
	
	public boolean reload() {
		
		checkIfSessionNull();
		
		return reload(session);
	}
	
	public int countJoin(BeanSession session, Class<? extends Object> klass) {
		
		try {
		
			return session.countJoin(this, klass);
			
		} catch(Exception e) {
			
			throw new RuntimeException(e);
		}
	}
	
	public int countJoin(Class<? extends Object> klass) {
		
		checkIfSessionNull();
		
		return countJoin(session, klass);
	}
	
	public <E> List<E> loadJoin(BeanSession session, Class<? extends E> klass) {
		
		try {
		
			return session.loadJoin(this, klass);
			
		} catch(Exception e) {
			
			throw new RuntimeException(e);
		}
	}
	
	public <E> List<E> loadJoin(Class<? extends E> klass) {
		
		checkIfSessionNull();
		
		return loadJoin(session, klass);
	}
	
	public List<Integer> loadJoinIds(BeanSession session, Class<? extends Object> klass) {
		
		try {
		
			return session.loadJoinIds(this, klass);
			
		} catch(Exception e) {
			
			throw new RuntimeException(e);
		}
	}
	
	public List<Integer> loadJoinIds(Class<? extends Object> klass) {
		
		checkIfSessionNull();
		
		return loadJoinIds(session, klass);
	}
	
	public boolean add(BeanSession session, Object bean) {
		
		try {
		
			return session.add(this, bean);
			
		} catch(Exception e) {
			
			throw new RuntimeException(e);
		}
	}
	
	public boolean add(Object bean) {
		
		checkIfSessionNull();
		
		return add(session, bean);
	}
	
	public boolean remove(BeanSession session, Object bean) {
		
		try {
		
			return session.remove(this, bean);
			
		} catch(Exception e) {
			
			throw new RuntimeException(e);
		}
	}
	
	public boolean remove(Object bean) {
		
		checkIfSessionNull();
		
		return remove(session, bean);
	}
}