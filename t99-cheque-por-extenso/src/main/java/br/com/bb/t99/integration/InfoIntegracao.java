package br.com.bb.t99.integration;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;

/**
 * Classe com escopo de request para armazenar os dados obtidos do header com as informações de integração.
 */
@RequestScoped
public class InfoIntegracao {

	public static final int INTERFACE_CANAL_PLATAFORMA = 55;
	public static final int INTERFACE_CANAL_COBOL= 3;
	public static final int INTERFACE_CANAL_APF_WEB= 12;
	public static final int INTERFACE_CANAL_APF_MOBILE = 20;
	public static final int INTERFACE_CANAL_HBK = 67;
	public static final int INTERFACE_CANAL_API = 68;
	public static final int INTERFACE_API_IMPL = 29;

	static final String INTERFACE_CANAL = "areaNaoAutenticada.infoCnl.interfaceCanal";
	static final String IMPL_CANAL = "areaNaoAutenticada.infoCnl.implementacaoInterfaceCanal";
	static final String TICKET_CNL = "areaNaoAutenticada.infoCnl.ticketCanal";

	Map<String, Object> propriedades = new HashMap<>();

	public int getCodigoInterfaceCanal(){
		return getAsInt(INTERFACE_CANAL);
	}

	public int getImplementacaoInterfaceCanal(){
		return getAsInt(IMPL_CANAL);
	}

	public String getTicketCanal(){
		return getAsString(TICKET_CNL);
	}

	void putAll(Map<String, Object> map){
		propriedades.putAll(map);
	}

	public Object get(String nomeProp){
		return propriedades.get(nomeProp);
	}

	public String getAsString(String nomeProp){
		return String.valueOf(this.get(nomeProp));
	}

	public int getAsInt(String nomeProp){
		return Integer.parseInt(String.valueOf(propriedades.getOrDefault(nomeProp,0)));
	}

	@Override
	public String toString() {
		return InfoIntegracao.class.getSimpleName() + "[" +
				propriedades.entrySet().stream()
						.map(e -> e.getKey() + "=" + e.getValue())
						.collect(Collectors.joining(",")) + "]";
	}
}
