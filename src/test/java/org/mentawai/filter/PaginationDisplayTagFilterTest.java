package org.mentawai.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.junit.Test;
import org.mentawai.core.InvocationChain;
import org.mentawai.util.MockAction;


public class PaginationDisplayTagFilterTest {

	private static final String TABLE_ID = "table";

	@Test
	public void testFilter() throws Exception {

		PaginationDisplayTagFilter paginationFilter = new PaginationDisplayTagFilter(TABLE_ID, "nome");
		
		
		//Teste com todos os parametros.
		InvocationChain chain = createMockInvocationChain("1", "idade", "0");
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
		chain = createMockInvocationChain("", "", "1");
		paginationFilter.filter(chain);
	
		pagination = (Pagination)chain.getAction().getInput().getValue(paginationFilter.getKey());
		assertNotNull(pagination);
		
		assertEquals(0, pagination.getInitRecord());
		assertEquals(50, pagination.getLimitRecord());
		assertEquals("nome", pagination.getSortAttribute());
		assertTrue(pagination.isDesc());
		
		
		//Teste quinta pagina.
		chain = createMockInvocationChain("5", "", "1");
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
		
		action.getInput().setValue((new ParamEncoder(TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_PAGE)), numberPage);
		action.getInput().setValue((new ParamEncoder(TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_SORT)), sortAttribute);
		action.getInput().setValue((new ParamEncoder(TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_ORDER)), isDesc);
		
		when(chain.getAction()).thenReturn(action);
		
		return chain;
	}
	
}