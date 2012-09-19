package org.mentawai.cript;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.log4j.Logger;

public class DesEncrypter {

	private static Logger logger = Logger.getLogger(DesEncrypter.class);
	
	private Cipher ecipher;
	private Cipher dcipher;

	// 8-byte Salt
	private byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };

	// Iteration count
	private final int iterationCount = 2;

	public DesEncrypter(String passPhrase) {
		try {
			// Create the key
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
			ecipher = Cipher.getInstance(key.getAlgorithm());
			dcipher = Cipher.getInstance(key.getAlgorithm());

			// Prepare the parameter to the ciphers
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

			// Create the ciphers
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
		} catch (java.security.InvalidAlgorithmParameterException e) {
			logger.debug("Error creating component DesEncrypter:", e);
		} catch (java.security.spec.InvalidKeySpecException e) {
			logger.debug("Error creating component DesEncrypter:", e);
		} catch (javax.crypto.NoSuchPaddingException e) {
			logger.debug("Error creating component DesEncrypter:", e);
		} catch (java.security.NoSuchAlgorithmException e) {
			logger.debug("Error creating component DesEncrypter:", e);
		} catch (java.security.InvalidKeyException e) {
			logger.debug("Error creating component DesEncrypter:", e);
		}
	}

	@SuppressWarnings("restriction")
	public String encrypt(String str) {
		try {
			// Encode the string into bytes using utf-8
			byte[] utf8 = str.getBytes("UTF8");

			// Encrypt
			byte[] enc = ecipher.doFinal(utf8);

			// Encode bytes to base64 to get a string
			return new sun.misc.BASE64Encoder().encode(enc);
		} catch (javax.crypto.BadPaddingException e) {
			logger.debug("Error encrypting string: " + str, e);
		} catch (IllegalBlockSizeException e) {
			logger.debug("Error encrypting string: " + str, e);
		} catch (UnsupportedEncodingException e) {
			logger.debug("Error encrypting string: " + str, e);
		}
		return null;
	}

	@SuppressWarnings("restriction")
	public String decrypt(String str) {
		
		try {
			// Decode base64 to get bytes
			byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

			// Decrypt
			byte[] utf8 = dcipher.doFinal(dec);

			// Decode using utf-8
			return new String(utf8, "UTF8");
			
		} catch (javax.crypto.BadPaddingException e) {
			logger.debug("Error decrypting string: " + str, e);
		} catch (IllegalBlockSizeException e) {
			logger.debug("Error decrypting string: " + str, e);
		} catch (UnsupportedEncodingException e) {
			logger.debug("Error decrypting string: " + str, e);
		} catch (java.io.IOException e) {
			logger.debug("Error decrypting string: " + str, e);
		}
		
		return null;
	}
		
}
