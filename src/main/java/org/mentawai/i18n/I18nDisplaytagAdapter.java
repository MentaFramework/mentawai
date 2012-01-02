package org.mentawai.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.displaytag.localization.I18nResourceProvider;
import org.displaytag.localization.LocaleResolver;
import org.mentawai.tag.i18n.UseI18N;

/**
 * Mentawai implementation of a resource provider and locale resolver. 
 * 
 * @author Caio Oliveira <caio@javacia.com.br>
 */
public class I18nDisplaytagAdapter implements I18nResourceProvider,	LocaleResolver {

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		return LocaleManager.getLocale(request);
	}

	@Override
	public String getResource(String resourceKey, String defaultValue, Tag tag, PageContext pageContext) {
		
		if(resourceKey == null) return defaultValue;
		
		I18N[] props = findI18NInPageContext(pageContext);

		for (int i = props.length - 1; i >= 0; i--) {

			if (props[i] != null && props[i].hasKey(resourceKey)) {
				return props[i].get(resourceKey);
			}

		}
		
		return defaultValue;
	}

	protected I18N[] findI18NInPageContext(PageContext pageContext) {
        I18N [] props = (I18N []) pageContext.getAttribute("_i18n");
        
		if (props == null) {
		
			loadI18NInPageContext(pageContext);
			
			props = (I18N []) pageContext.getAttribute("_i18n");

		}
		
		return props;
	}

	protected void loadI18NInPageContext(PageContext pageContext) {
		UseI18N.loadI18N(pageContext, new String[0], null);
	}
	
}