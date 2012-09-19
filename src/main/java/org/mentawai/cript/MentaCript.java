package org.mentawai.cript;

import javax.servlet.http.HttpSession;

import org.mentawai.core.SessionContext;


/**
 * Class that cript end decript values.
 * 
 *  EX: MentaCript.cript("123") // returns DtdvnYls4L4=
 *      MentaCript.decript("DtdvnYls4L4=") // returns 123
 * 
 * @author Robert Willian Gil
 *
 */
public class MentaCript {

	public static final String PREFIX_CRIPT_TAG = "mtw_cript_";
	
	private static final String COMMON_KEY_PASS = "_defaultKeyPass";

	private static final String CRIPT_SESSION_KEY = "_CRIPT_SESSION_KEY";
	
	private DesEncrypter encrypter;

	private static MentaCript singletonInstance;
	
	private MentaCript() {
	}
	
	/**
	 * This return a common MentaCript instance, 
	 * if you want to send the hash to somebody in email or etc, 
	 * you may want to decode with the same pass phrase later.
	 * 
	 */
	public static MentaCript getCommonInstance() {
		
		if(singletonInstance == null) { 
			singletonInstance = new MentaCript();
			singletonInstance.setPassPhrase(COMMON_KEY_PASS);
		}
		
		return singletonInstance;
	}
	
	/**
	 * Return one instance per session with its own pass phrase.
	 * @param SessionContext
	 * @return MentaCript instance
	 */
	public static MentaCript getInstance(SessionContext sessionContext) {
		return getInstance(sessionContext.getSession());
	}
	
	/**
	 * Return one instance per session with its own pass phrase.
	 * @param HttpSession
	 * @return MentaCript instance
	 */
	public static MentaCript getInstance(HttpSession httpSession) {
		
		Object object = httpSession.getAttribute(CRIPT_SESSION_KEY);
		
		if(object != null) {
			return (MentaCript) object;
		} else {
			MentaCript mc = new MentaCript();
			mc.setPassPhrase(httpSession.getId());
			httpSession.setAttribute(CRIPT_SESSION_KEY, mc);
			return mc;
		}
	}
	
	public void setPassPhrase(String newPassPhrase) {
		encrypter = new DesEncrypter( newPassPhrase );
	}
	
	public String cript(Integer n){
		return n == null ? null : cript(n.toString());
	}
	
	public String cript(String v){
		
		if(v == null) return null;
		
		return encrypter.encrypt(v);
	}
	
	public String decript(String v) throws DecriptException {
		
		if(v == null) return null;
		if(v.isEmpty()) return v;
		
		v += "R"; // colocar mais um caracter que nao influencia, porem inibe o cara de colocar mais um na string original
		
		String decrypted = encrypter.decrypt(v);
		if(decrypted == null || decrypted.isEmpty()) throw new DecriptException(v);
		
		return decrypted;
	}
	
	public Integer decriptToInt(String v){
		
		if(v == null) return null;
		
		return new Integer( encrypter.decrypt(v) );
	}

	/**
	 * Used for taglib funcion
	 * @param v
	 * @param session
	 * @return cripted value
	 */
	public static String cript(String v, SessionContext session){
		return MentaCript.getInstance(session).cript(v);
	}
	
	/**
	 * Used for taglib funcion
	 * @param v
	 * @param session
	 * @return decripted value
	 * @throws DecriptException if not enable to decript
	 */
	public String decript(String v, SessionContext session) throws DecriptException {
		return MentaCript.getInstance(session).decript(v);
	}
}
