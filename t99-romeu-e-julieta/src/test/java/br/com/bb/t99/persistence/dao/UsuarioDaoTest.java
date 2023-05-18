package br.com.bb.t99.persistence.dao;

import br.com.bb.t99.exception.ErrosSistema;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioDaoTest {

	@PersistenceContext
	EntityManager em;

	@Test
	@Order(1)
	@Tag("database-test")
	public void verifica_busca_com_sucesso(){
		//Usando um entityManager real
		UsuarioDao dao = new UsuarioDao(em);
		var usuarios = dao.buscaUsuarios();

		assertNotNull(usuarios);
		assertEquals(6 , usuarios.size());
	}

	@Test
	@Order(2)
	public void verifica_busca_sem_resultado(){
		//Mock para lancar uma exception sem resultado.
		var emMock = Mockito.mock(EntityManager.class);
		var queryMock = Mockito.mock(TypedQuery.class);

		Mockito.when(queryMock.getResultList()).thenThrow(new NoResultException());
		Mockito.when(emMock.createNamedQuery(Mockito.anyString(), Mockito.any() )).thenReturn(queryMock);

		UsuarioDao dao = new UsuarioDao(emMock);

		var usuarios = dao.buscaUsuarios();

		assertNotNull(usuarios,"O resultado não deve ser nulo.");
		assertTrue(usuarios.isEmpty(), "A lista deveria estar vazia para teste sem resultado.");
	}

	@Test
	@Order(3)
	public void verifica_busca_com_exception(){
		//Mock para lancar um erro de sql, mas sem as informacoes de SQL
		var emMock = Mockito.mock(EntityManager.class);
		var queryMock = Mockito.mock(TypedQuery.class);

		Mockito.when(queryMock.getResultList()).thenThrow(new PersistenceException());
		Mockito.when(emMock.createNamedQuery(Mockito.anyString(), Mockito.any() )).thenReturn(queryMock);

		UsuarioDao dao = new UsuarioDao(emMock);

		try {
			dao.buscaUsuarios();
			fail("Era para ter lancado uma exception");
		} catch (ErrosSistema.ErroExecucaoSQL e){
			assertNotNull(e.getVars(), "Variaveis monitoradas vazias");
		}

	}

}
