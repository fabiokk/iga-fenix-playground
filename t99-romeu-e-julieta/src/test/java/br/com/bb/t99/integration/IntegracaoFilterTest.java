package br.com.bb.t99.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static br.com.bb.t99.integration.InfoIntegracao.INTERFACE_CANAL;
import static br.com.bb.t99.integration.InfoIntegracao.IMPL_CANAL;
import static br.com.bb.t99.integration.InfoIntegracao.TICKET_CNL;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntegracaoFilterTest {

	@Test
	void verificar_regex_path_operacao(){
		assertFalse("".matches(IntegracaoFilter.REGEX_OP_IIB), "regex invalido para o valor \"\"");
		assertFalse("op1v1".matches(IntegracaoFilter.REGEX_OP_IIB),"regex invalido para o valor op1v1");
		assertFalse("opv1".matches(IntegracaoFilter.REGEX_OP_IIB), "regex invalido para o valor opv1");
		assertFalse("/testop1v1".matches(IntegracaoFilter.REGEX_OP_IIB),"regex invalido para o valor /testop1v1");
		assertFalse("/opv1".matches(IntegracaoFilter.REGEX_OP_IIB),"regex invalido para o valor /opv1");
		assertFalse("/optes1v1".matches(IntegracaoFilter.REGEX_OP_IIB),"regex invalido para o valor /optes1v1");
		assertFalse("/op123v123".matches(IntegracaoFilter.REGEX_OP_IIB),"regex invalido para o valor /op123v123");
		assertTrue("/op123v12".matches(IntegracaoFilter.REGEX_OP_IIB),"regex valido para o valor /op123v12");
		assertTrue("/op123v1".matches(IntegracaoFilter.REGEX_OP_IIB),"regex valido para o valor /op123v1");
	}

	@Test
	void execucao_filtro_de_recebimento_path_incorreto() {
		ContainerRequestContext ctxMock = Mockito.mock(ContainerRequestContext.class);
		UriInfo uriInfoCtx = Mockito.mock(UriInfo.class);
		MultivaluedMap<String,String> headersMock = Mockito.mock(MultivaluedMap.class);

		String path = "/optestev1";

		Mockito.when(uriInfoCtx.getPath()).thenReturn(path);
		Mockito.when(headersMock.containsKey(IntegracaoFilter.INFO_INTEGRACAO)).thenReturn(false);
		Mockito.when(ctxMock.getUriInfo()).thenReturn(uriInfoCtx);
		Mockito.when(ctxMock.getHeaders()).thenReturn(headersMock);

		IntegracaoFilter filtro = new IntegracaoFilter(new InfoIntegracao(), new EstadoIntegracao(), gerarTracerMock());
		filtro.filter(ctxMock);
		Mockito.verify(headersMock, Mockito.never()).containsKey(IntegracaoFilter.INFO_INTEGRACAO);
	}

	@Test
	void execucao_filtro_de_recebimento_sem_header_info_integracao() {
		String path = "/op123v1";
		ContainerRequestContext ctxMock = Mockito.mock(ContainerRequestContext.class);
		UriInfo uriInfoCtx = Mockito.mock(UriInfo.class);
		MultivaluedMap<String,String> headersMock = Mockito.mock(MultivaluedMap.class);

		Mockito.when(uriInfoCtx.getPath()).thenReturn(path);
		Mockito.when(headersMock.containsKey(IntegracaoFilter.INFO_INTEGRACAO)).thenReturn(false);
		Mockito.when(ctxMock.getUriInfo()).thenReturn(uriInfoCtx);
		Mockito.when(ctxMock.getHeaders()).thenReturn(headersMock);

		IntegracaoFilter filtro = new IntegracaoFilter(new InfoIntegracao(), new EstadoIntegracao(), gerarTracerMock());
		filtro.filter(ctxMock);
		Mockito.verify(headersMock, Mockito.times(1)).containsKey(IntegracaoFilter.INFO_INTEGRACAO);
	}

	@Test
	void execucao_filtro_de_recebimento_com_exception_no_parse_do_jwt() throws URISyntaxException {
		ContainerRequestContext ctxMock = Mockito.mock(ContainerRequestContext.class);
		UriInfo uriInfoCtx = Mockito.mock(UriInfo.class);
		MultivaluedMap<String,String> headersMock = Mockito.mock(MultivaluedMap.class);
		String path = "/op12345v1";

		Mockito.when(uriInfoCtx.getPath()).thenReturn(path);
		Mockito.when(ctxMock.getUriInfo()).thenReturn(uriInfoCtx);
		Mockito.when(headersMock.containsKey(IntegracaoFilter.INFO_INTEGRACAO)).thenReturn(true);
		Mockito.when(ctxMock.getHeaderString(IntegracaoFilter.ESTADO_INTEGRACAO)).thenReturn("TOKEN-SECRETO-ESTADO-INTEGRACAO");
		Mockito.when(ctxMock.getHeaderString(IntegracaoFilter.INFO_INTEGRACAO)).thenReturn("TOKEN-JWT-INVALIDO");

		Mockito.when(ctxMock.getHeaders()).thenReturn(headersMock);

		var tracerMock = gerarTracerMock();
		IntegracaoFilter filtro = new IntegracaoFilter(new InfoIntegracao(), new EstadoIntegracao(), tracerMock);

		filtro.filter(ctxMock);

		Mockito.verify(tracerMock, Mockito.never()).activeSpan();
	}


	@Test
	void execucao_filtro_de_recebimento_com_sucesso() throws URISyntaxException {
		// para gerar exemplos de token use o site jwt.io
		var infoIntegracaoEsperado = new InfoIntegracao();

		infoIntegracaoEsperado.putAll(Map.of(
				INTERFACE_CANAL,10,
				IMPL_CANAL,20,
				TICKET_CNL, "meuTicketMock"));

		var tokenJwtMock = criarToken(infoIntegracaoEsperado);

		var tokenEstadoIntegracao = "TOKEN-SECRETO-ESTADO-INTEGRACAO";
		ContainerRequestContext ctxMock = Mockito.mock(ContainerRequestContext.class);
		UriInfo uriInfoCtx = Mockito.mock(UriInfo.class);
		MultivaluedMap<String,String> headersMock = Mockito.mock(MultivaluedMap.class);

		String path = "/op12345v1";

		Mockito.when(uriInfoCtx.getPath()).thenReturn(path);
		Mockito.when(ctxMock.getUriInfo()).thenReturn(uriInfoCtx);
		//Criacao dos headers Mock
		Mockito.when(headersMock.containsKey(IntegracaoFilter.INFO_INTEGRACAO)).thenReturn(true);
		Mockito.when(ctxMock.getHeaderString(IntegracaoFilter.ESTADO_INTEGRACAO)).thenReturn(tokenEstadoIntegracao);
		Mockito.when(ctxMock.getHeaderString(IntegracaoFilter.INFO_INTEGRACAO)).thenReturn(tokenJwtMock);

		Mockito.when(ctxMock.getHeaders()).thenReturn(headersMock);

		var infoIntegracao = new InfoIntegracao();
		var estadoIntegracao = new EstadoIntegracao();
		var tracerMock = gerarTracerMock();
		IntegracaoFilter filtro = new IntegracaoFilter(infoIntegracao, estadoIntegracao, tracerMock);

		filtro.filter(ctxMock);

		//Verifica se chamou o metodo pelo menos uma vez
		Mockito.verify(tracerMock, Mockito.atMostOnce()).activeSpan();
		assertEquals(tokenEstadoIntegracao, estadoIntegracao.getToken());
		assertEquals(infoIntegracao.getCodigoInterfaceCanal(), infoIntegracao.getCodigoInterfaceCanal(), "O campo interfaceCanal esta incorreto");
		assertEquals(infoIntegracao.getImplementacaoInterfaceCanal(), infoIntegracao.getImplementacaoInterfaceCanal(),
				"O campo implementacaoInterfaceCanal esta incorreto");
		assertEquals(infoIntegracao.getTicketCanal(), infoIntegracao.getTicketCanal(), "O campo ticket esta incorreto");
		assertEquals(infoIntegracao.toString(), infoIntegracao.toString());
	}

	@Test
	void execucao_filtro_de_envio_com_sucesso() throws URISyntaxException {
		ClientRequestContext ctxMock = Mockito.mock(ClientRequestContext.class);
		var tokenEstadoIntegracao = "TOKEN_SECRETO";
		String path = "/op123v1";
		MultivaluedMap<String,Object> headersMock = Mockito.mock(MultivaluedMap.class);

		URI uri = new URI(path);
		Mockito.when(ctxMock.getUri()).thenReturn(uri);
		Mockito.when(ctxMock.getHeaders()).thenReturn(headersMock);
		Mockito.doNothing().when(headersMock).add(Mockito.anyString(), Mockito.any());

		var infoIntegracao = new InfoIntegracao();
		var estadoIntegracao = new EstadoIntegracao();
		estadoIntegracao.setToken(tokenEstadoIntegracao);
		var tracerMock = gerarTracerMock();
		IntegracaoFilter filtro = new IntegracaoFilter(infoIntegracao, estadoIntegracao, tracerMock);

		filtro.filter(ctxMock);

		Mockito.verify(headersMock, Mockito.atMostOnce()).add(IntegracaoFilter.ESTADO_INTEGRACAO, tokenEstadoIntegracao);
	}

	@Test
	void execucao_filtro_de_envio_com_token_estado_integracao_vazio() throws URISyntaxException {
		ClientRequestContext ctxMock = Mockito.mock(ClientRequestContext.class);
		String path = "/op123v1";

		URI uri = new URI(path);
		Mockito.when(ctxMock.getUri()).thenReturn(uri);

		var infoIntegracao = new InfoIntegracao();
		var estadoIntegracao = new EstadoIntegracao();
		var tracerMock = gerarTracerMock();

		IntegracaoFilter filtro = new IntegracaoFilter(infoIntegracao, estadoIntegracao, tracerMock);

		filtro.filter(ctxMock);

		Mockito.verify(ctxMock, Mockito.never()).getHeaders();
	}

	@Test
	void execucao_filtro_de_envio_com_path_incorreto() throws URISyntaxException {
		ClientRequestContext ctxMock = Mockito.mock(ClientRequestContext.class);
		String path = "/optestev1";

		URI uri = new URI(path);
		Mockito.when(ctxMock.getUri()).thenReturn(uri);

		var infoIntegracao = new InfoIntegracao();
		var estadoIntegracao = new EstadoIntegracao();
		var tracerMock = gerarTracerMock();
		IntegracaoFilter filtro = new IntegracaoFilter(infoIntegracao, estadoIntegracao, tracerMock);

		filtro.filter(ctxMock);

		Mockito.verify(ctxMock, Mockito.never()).getHeaders();
	}

	@Test
	void execucao_flat_map(){
		var infoIntegracao = new InfoIntegracao();
		var estadoIntegracao = new EstadoIntegracao();
		var tracerMock = gerarTracerMock();
		IntegracaoFilter filtro = new IntegracaoFilter(infoIntegracao, estadoIntegracao, tracerMock);

		var map = new HashMap<String, Integer>();
		map.put("Item1",1);
		map.put("Item2", null);
		var resultado = filtro.flatten(Map.of("Nivel1", Map.of(
				"Nivel2-1", "valor1",
				"Nivel2-2", List.of("Item1","Item2","Item3"),
				"Nivel3", map
		)));

		assertTrue(resultado.containsKey("Nivel1.Nivel2-1"));
		assertEquals("valor1",resultado.get("Nivel1.Nivel2-1"));

		assertTrue(resultado.containsKey("Nivel1.Nivel2-2[1]"));
		assertEquals("Item2",resultado.get("Nivel1.Nivel2-2[1]"));

		Map.Entry<String, Object> m = null;
		assertTrue(filtro.flatten(m).findAny().isEmpty(), "Stream vazia quando o parametro for nulo");
	}

	/**
	 * Metodo para criar um token JWT a partir de um InfoIntegracao
	 * @param infoIntegracao
	 * @return string
	 */
	String criarToken(InfoIntegracao infoIntegracao){
		return JWT.create()
				.withClaim(IntegracaoFilter.AREA_NAO_AUTENTICADA, Map.of(IntegracaoFilter.INFO_CNL, Map.of(
						InfoIntegracao.INTERFACE_CANAL, infoIntegracao.getCodigoInterfaceCanal(),
						InfoIntegracao.IMPL_CANAL, infoIntegracao.getImplementacaoInterfaceCanal(),
						InfoIntegracao.TICKET_CNL, infoIntegracao.getTicketCanal())))
				.sign(Algorithm.none());
	}

	/**
	 * Metodo para criar um mock do tracer
	 * @return Tracer mockado
	 */
	Tracer gerarTracerMock(){
		var mockSpan = Mockito.mock(Span.class);
		var tracerMock = Mockito.mock(Tracer.class);

		Mockito.when(mockSpan.setTag(Mockito.anyString(), Mockito.anyString())).thenReturn(mockSpan);
		Mockito.when(tracerMock.activeSpan()).thenReturn(mockSpan);
		return tracerMock;
	}

}
