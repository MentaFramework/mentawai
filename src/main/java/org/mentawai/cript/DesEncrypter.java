package org.mentawai.cript;

import static org.mentalog.Log.Debug;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.logging.Level;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.thoughtworks.xstream.core.util.Base64Encoder;

public class DesEncrypter {

	// 8-byte Salt
	private byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };

	// Iteration count
	private final int iterationCount = 2;

	private SecretKey key;
	private PBEParameterSpec paramSpec;
	
	public DesEncrypter(String passPhrase) {
		
		// Create the key
		try {
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray());
			key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
			// Prepare the parameter to the ciphers
			paramSpec = new PBEParameterSpec(salt, iterationCount);

		} catch (InvalidKeySpecException e) {
			Debug.log("Error creating component DesEncrypter:", e);
		} catch (NoSuchAlgorithmException e) {
			Debug.log("Error creating component DesEncrypter:", e);
		}
				
	}

	public String encrypt(String str) {
		try {
			
			// Create the ciphers
			Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

			
			// Encode the string into bytes using utf-8
			byte[] utf8 = str.getBytes("UTF8");

			// Encrypt
			byte[] enc = ecipher.doFinal(utf8);

			// Encode bytes to base64 to get a string
			return new Base64Encoder().encode(enc);
			
		} catch (javax.crypto.BadPaddingException e) {
			Debug.log(Level.SEVERE, "Error decrypting string: " + str, e);
		} catch (IllegalBlockSizeException e) {
			Debug.log(Level.FINE, "Error decrypting string: " + str, e);
		} catch (UnsupportedEncodingException e) {
			Debug.log(Level.SEVERE, "Error decrypting string: " + str, e);
		} catch (NoSuchAlgorithmException e) {
			Debug.log(Level.SEVERE, "Error decrypting string: " + str, e);
		} catch (NoSuchPaddingException e) {
			Debug.log(Level.SEVERE, "Error decrypting string: " + str, e);
		} catch (InvalidKeyException e) {
			Debug.log(Level.SEVERE, "Error decrypting string: " + str, e);
		} catch (InvalidAlgorithmParameterException e) {
			Debug.log(Level.SEVERE, "Error decrypting string: " + str, e);
		} catch (ArrayIndexOutOfBoundsException e){
			Debug.log(Level.SEVERE, "Error decrypting string: " + str, e);
		}
		
		return null;
	}

	public String decrypt(String str) {
		
		try {
			
			Cipher dcipher = Cipher.getInstance(key.getAlgorithm());
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			// Decode base64 to get bytes
			byte[] dec = new Base64Encoder().decode(str);

			// Decrypt
			byte[] utf8 = dcipher.doFinal(dec);

			// Decode using utf-8
			return new String(utf8, "UTF8");
			
		} catch (javax.crypto.BadPaddingException e) {
			Debug.log(Level.SEVERE, "Error decrypting string: " + str, e);
		} catch (IllegalBlockSizeException e) {
			Debug.log(Level.FINE, "Error decrypting string: " + str, e);
		} catch (UnsupportedEncodingException e) {
			Debug.log(Level.SEVERE, "Error decrypting string: " + str, e);
		} catch (NoSuchAlgorithmException e) {
			Debug.log(Level.SEVERE, "Error decrypting string: " + str, e);
		} catch (NoSuchPaddingException e) {
			Debug.log(Level.SEVERE, "Error decrypting string: " + str, e);
		} catch (InvalidKeyException e) {
			Debug.log(Level.SEVERE, "Error decrypting string: " + str, e);
		} catch (InvalidAlgorithmParameterException e) {
			Debug.log(Level.SEVERE, "Error decrypting string: " + str, e);
		} catch (ArrayIndexOutOfBoundsException e){
			Debug.log(Level.SEVERE, "Error decrypting string: " + str, e);
		}
		
		return null;
	}
		
}
