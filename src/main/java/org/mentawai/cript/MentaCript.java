package org.mentawai.cript;


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
	
	private static final String DEFAULT_KEY_PASS = "_defaultKeyPass";
	
	private static DesEncrypter encrypter;

	
	static {
		encrypter = new DesEncrypter( DEFAULT_KEY_PASS );
	}
	
	public static void changePassPhrase(String newPassPhrase) {
		encrypter = new DesEncrypter( newPassPhrase );
	}
	
	public static String cript(Integer n){
		return n == null ? null : cript(n.toString());
	}
	
	public static String cript(String v){
		
		if(v == null) return null;
		
		return encrypter.encrypt(v);
	}
	
	public static String decript(String v) throws DecriptException {
		
		if(v == null) return null;
		if(v.isEmpty()) return v;
		
		String decrypted = encrypter.decrypt(v);
		if(decrypted == null || decrypted.isEmpty()) throw new DecriptException(v);
		
		return decrypted;
	}
	
	public static Integer decriptToInt(String v){
		
		if(v == null) return null;
		
		return new Integer( encrypter.decrypt(v) );
	}

}
