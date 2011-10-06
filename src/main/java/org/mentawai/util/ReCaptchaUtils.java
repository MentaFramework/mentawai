package org.mentawai.util;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.mentawai.core.Action;
import org.mentawai.core.Input;

public class ReCaptchaUtils {
	
	public static String PUBLIC_KEY = null;
	
	public static String PRIVATE_KEY = null;
	
	public static boolean check(Action action) {
		
		checkKeys();
		
		Input input = action.getInput();
		
		String remoteAddr = input.getProperty("remoteAddr");
		
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey(PRIVATE_KEY);

        String challenge = input.getString("recaptcha_challenge_field");
        String uresponse = input.getString("recaptcha_response_field");
        
        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);

        return reCaptchaResponse.isValid();
	}
	
	public static void checkKeys() {
		
		if (PUBLIC_KEY == null || PRIVATE_KEY == null) throw new IllegalStateException("Private and Public keys are not set!");
	}
}