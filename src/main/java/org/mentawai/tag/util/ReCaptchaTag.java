package org.mentawai.tag.util;

import javax.servlet.jsp.JspException;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;

import org.mentawai.util.ReCaptchaUtils;

public class ReCaptchaTag extends PrintTag {
	
	public String getStringToPrint() throws JspException {
		
		ReCaptchaUtils.checkKeys();
		
		ReCaptcha c = ReCaptchaFactory.newReCaptcha(ReCaptchaUtils.PUBLIC_KEY, ReCaptchaUtils.PRIVATE_KEY, false);
		
		return c.createRecaptchaHtml(null, null);
	}
}