package org.mentawai.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mentawai.core.InvocationChain;
import org.mentawai.util.MockAction;


public class PaginationFilterTest {

	@Test
	public void testFilter() throws Exception {

		PaginationFilter paginationFilter = new PaginationFilter("nome");
		
		
		//Teste com todos os parametros.
		InvocationChain chain = createMockInvocationChain("1", "idade", "false");
		paginationFilter.filter(chain);
	
		Pagination pagination = (Pagination)chain.getAction().getInput().getValue(paginationFilter.getKey());
		assertNotNull(pagination);
		
		assertEquals(0, pagination.getInitRecord());
		assertEquals(50, pagination.getLimitRecord());
		assertEquals("idade", pagination.getSortAttribute());
		assertFalse(pagination.isDesc());

		
		//Teste com segunda pagina.
		chain = createMockInvocationChain("2", "cpf", "");
		paginationFilter.filter(chain);
	
		pagination = (Pagination)chain.getAction().getInput().getValue(paginationFilter.getKey());
		assertNotNull(pagination);
		
		assertEquals(50, pagination.getInitRecord());
		assertEquals(50, pagination.getLimitRecord());
		assertEquals("cpf", pagination.getSortAttribute());
		assertFalse(pagination.isDesc());

		
		//Teste com os parametros igual a nulo.
		chain = createMockInvocationChain("", "", "");
		paginationFilter.filter(chain);
	
		pagination = (Pagination)chain.getAction().getInput().getValue(paginationFilter.getKey());
		assertNotNull(pagination);
		
		assertEquals(0, pagination.getInitRecord());
		assertEquals(50, pagination.getLimitRecord());
		assertEquals("nome", pagination.getSortAttribute());
		assertFalse(pagination.isDesc());
		
		
		//Teste com ordenacao desc.
		chain = createMockInvocationChain("", "", "true");
		paginationFilter.filter(chain);
	
		pagination = (Pagination)chain.getAction().getInput().getValue(paginationFilter.getKey());
		assertNotNull(pagination);
		
		assertEquals(0, pagination.getInitRecord());
		assertEquals(50, pagination.getLimitRecord());
		assertEquals("nome", pagination.getSortAttribute());
		assertTrue(pagination.isDesc());
		
		
		//Teste quinta pagina.
		chain = createMockInvocationChain("5", "", "true");
		paginationFilter.filter(chain);
	
		pagination = (Pagination)chain.getAction().getInput().getValue(paginationFilter.getKey());
		assertNotNull(pagination);
		
		assertEquals(200, pagination.getInitRecord());
		assertEquals(50, pagination.getLimitRecord());
		assertEquals("nome", pagination.getSortAttribute());
		assertTrue(pagination.isDesc());
		
	}

	protected InvocationChain createMockInvocationChain(String numberPage, String sortAttribute, String isDesc) {
		InvocationChain chain = mock(InvocationChain.class);
		
		MockAction action = new MockAction();
		
		action.getInput().setValue(PaginationFilter.PARAM_NUMBER_PAGE, numberPage);
		action.getInput().setValue(PaginationFilter.PARAM_SORT_ATTRIBUTE, sortAttribute);
		action.getInput().setValue(PaginationFilter.PARAM_IS_DESC, isDesc);
		
		when(chain.getAction()).thenReturn(action);
		
		return chain;
	}
	
}
