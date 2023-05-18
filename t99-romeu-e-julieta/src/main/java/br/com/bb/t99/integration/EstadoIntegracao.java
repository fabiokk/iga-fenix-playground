package br.com.bb.t99.integration;

import javax.enterprise.context.RequestScoped;
import java.util.Optional;

/**
 * Classe com escopo de request para armazenar o token do estado de integração, esse token esta criptografado e deve ser apenas
 * repassado do provimento da operação para o consumo de operação, ele possui informações de segurança.
 */
@RequestScoped
public class EstadoIntegracao {
	private String token = "";

	void setToken(String token) {
		this.token = Optional.ofNullable(token).orElse("");
	}

	public String getToken() {
		return this.token;
	}
}
