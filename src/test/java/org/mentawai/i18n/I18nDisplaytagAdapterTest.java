package org.mentawai.i18n;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.junit.Test;
import org.mentawai.action.BaseLoginAction;

public class I18nDisplaytagAdapterTest {

	@Test
	public void testResolveLocale() {

		LocaleManager.DEFAULT_LOCALE = new Locale("pt","BR");
		
		I18nDisplaytagAdapter displaytagAdapter = new I18nDisplaytagAdapter();
		
		assertEquals(Locale.ENGLISH, displaytagAdapter.resolveLocale(createMockHttpServletRequest(Locale.ENGLISH)));
		
		assertEquals(new Locale("pt","BR"), displaytagAdapter.resolveLocale(createMockHttpServletRequest(null)));
	}

	@Test
	public void testGetResource() {
		I18nDisplaytagAdapter displaytagAdapter = new I18nDisplaytagAdapter();
		
		PageContext pageContext = createMockPageContext(true);
				
		assertEquals("Nome", displaytagAdapter.getResource("nome", "defaultValue", null, pageContext));
		
		assertEquals("defaultValue", displaytagAdapter.getResource("nome1", "defaultValue", null, pageContext));
		
		assertEquals("Sobrenome", displaytagAdapter.getResource("sobrenome", "defaultValue", null, pageContext));

		assertEquals("defaultValue", displaytagAdapter.getResource("sobrenome1", "defaultValue", null, pageContext));
	}

	protected HttpServletRequest createMockHttpServletRequest(Locale locale) {
		HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

		HttpSession httpSession = mock(HttpSession.class);

		//Adiciona na http a session.
		when(httpServletRequest.getSession()).thenReturn(httpSession);

		//Adiciona na sessao o locale.
		when(httpSession.getAttribute(BaseLoginAction.LOCALE_KEY)).thenReturn(locale);
		
		return httpServletRequest;
	}

	protected PageContext createMockPageContext(boolean isNull) {
		PageContext pageContext = mock(PageContext.class);

		I18N [] i18ns =  new I18N[2];
			
		Properties props = new Properties();
		props.setProperty("nome", "Nome");
		i18ns[0] = new I18N(props );
		
		Properties propsTwo = new Properties();
		propsTwo.setProperty("sobrenome", "Sobrenome");
		i18ns[1] = new I18N(propsTwo);
				
		when(pageContext.getAttribute("_i18n")).thenReturn(i18ns);

		return pageContext;
	}

}
