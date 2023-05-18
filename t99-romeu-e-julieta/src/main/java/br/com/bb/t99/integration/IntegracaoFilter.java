package br.com.bb.t99.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.jsonwebtoken.security.SignatureException;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static br.com.bb.t99.integration.InfoIntegracao.INTERFACE_CANAL;

/**
 * Filtro para recuperar os dados do info-integração e para repassar o token do estado de intregação.
 * Eles funcionam apenas nos endpoints que possuam a operação no path, ex: /op123456v1.
 * Os dados da info-integração serão armazenados na classe {@link InfoIntegracao} que possui o escopo de request.
 * Os dados do Estado-Integração serão repassados automaticamente da operação de provimento para as operações de consumo quando utilizado
 * o rest-client.
 * Outra função do filtro é a adição do codigo do canal no tracer da requisição
 */
@Provider
@IntegracaoIIB
@ApplicationScoped
public class IntegracaoFilter implements ContainerRequestFilter, ClientRequestFilter {

	static final Logger LOG = LoggerFactory.getLogger(IntegracaoFilter.class.getName());

	static final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(Include.NON_EMPTY)
			.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	static final JwtParser jwtDeserializer = Jwts.parserBuilder()
			.deserializeJsonWith(new JacksonDeserializer<>(objectMapper)).build();

	static final String HEADER_NAO_ENCONTRADO = "O header: {} não foi encontrado no contexto na chamada ao endpoint: {}.";
	static final String ERRO_HEADER = "Erro na utilização do header: %s na chamada ao endpoint: %s.";

	public static final String INFO_INTEGRACAO = "info-integracao";
	public static final String INFO_CNL = "infoCnl";
	public static final String AREA_NAO_AUTENTICADA = "areaNaoAutenticada";
	public static final String REGEX_OP_IIB = "/op[0-9]+v[0-9]{1,2}";
	public static final String ESTADO_INTEGRACAO = "ESTADO-INTEGRACAO";

	InfoIntegracao infoIntegracao;
	EstadoIntegracao estadoIntegracao;
	Tracer tracer;

	public IntegracaoFilter(InfoIntegracao infoIntegracao, EstadoIntegracao estadoIntegracao, Tracer tracer){
		this.infoIntegracao = infoIntegracao;
		this.estadoIntegracao = estadoIntegracao;
		this.tracer = tracer;
	}

	/**
	 * Filtro para receber o info-integração para os endpoints que provem uma operação.
	 * @param requestContext contexto de request dos endpoints providos pela aplicação
	 */
	@Override
	public void filter(ContainerRequestContext requestContext) {
		var path = requestContext.getUriInfo().getPath();

		if(!path.matches(REGEX_OP_IIB) ){
			return;
		}

		if(!requestContext.getHeaders().containsKey(INFO_INTEGRACAO)){
			LOG.debug(HEADER_NAO_ENCONTRADO,INFO_INTEGRACAO, path);
			return;
		}

		try {
			//Atribuindo o token estado integracao
			this.estadoIntegracao.setToken(requestContext.getHeaderString(ESTADO_INTEGRACAO));
			infoIntegracao.putAll(flatten(jwtDeserializer.parseClaimsJwt(requestContext.getHeaderString(INFO_INTEGRACAO)).getBody()));
			//Incluindo o canal da requisicao no tracer
			tracer.activeSpan().setTag(INTERFACE_CANAL, infoIntegracao.getCodigoInterfaceCanal());
		} catch (ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException jwtException){
			LOG.error(String.format(ERRO_HEADER, INFO_INTEGRACAO, path), jwtException);
		}
	}

	/**
	 * Filtro para repassar o estado de integração para o consumo de operacoes ibb
	 * @param clientRequestContext contexto de request dos endpoints do restclient
	 */
	@Override
	public void filter(ClientRequestContext clientRequestContext)  {
		//Se o path possuir o padrão /opXXXXvXX vai ser adicionar o token de estado de integração
		if(clientRequestContext.getUri().getPath().matches(REGEX_OP_IIB) && !estadoIntegracao.getToken().isBlank()){
			clientRequestContext.getHeaders().add(ESTADO_INTEGRACAO, estadoIntegracao.getToken());
		}
	}

	/**
	 * Metodo para fazer o "achatamento" das propriedades do conteudo do header em um map, exemplo, caso uma propriedade seja
	 * composta por outras esse metodo vai concatenar as chaves usando "." como separador ou "[]" como indicador de elementos em uma
	 * lista. No caso da interfaceCanal que esta dentro do infoCnl que por sua vez faz parte da areaNaoAutenticada, a chave da propriedade
	 * ficaria assim areaNaoAutenticada.infoCnl.interfaceCanal.
	 * @param mapToFlat
	 * @return Map com o nome da propriedade e o valor
	 */
	Map<String,Object> flatten(Map<String, Object> mapToFlat){
		return mapToFlat.entrySet().stream()
				.flatMap(this::flatten).collect(LinkedHashMap::new,
						(map, entry) -> map.put(entry.getKey(), entry.getValue()),
						LinkedHashMap::putAll);
	}

	Stream<Map.Entry<String,Object>> flatten(Map.Entry<String, Object> entry){
		if(entry==null){
			return Stream.empty();
		}

		Object value = entry.getValue();

		if(value instanceof Map<?,?>){
			Map<?,?> properties = (Map<?,?>) value;
			return properties.entrySet().stream()
					.flatMap(e-> flatten(new AbstractMap.SimpleEntry<>(entry.getKey()+"."+e.getKey(), e.getValue())));
		}

		if(value instanceof List<?>){
			List<?> list = (List<?>) value;
			return IntStream.range(0, list.size())
					.mapToObj(i -> new AbstractMap.SimpleEntry<String, Object>(entry.getKey() + "[" + i + "]", list.get(i)))
					.flatMap(this::flatten);
		}
		return Stream.of(entry);
	}

}
