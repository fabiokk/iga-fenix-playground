package br.com.bb.t99.persistence.dto;

public class UsuarioPorEstadoDTO {

	private String uf;
	private Long qtd;

	public UsuarioPorEstadoDTO(String uf, Long qtd) {
		this.uf = uf;
		this.qtd = qtd;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public Long getQtd() {
		return qtd;
	}

	public void setQtd(Long qtd) {
		this.qtd = qtd;
	}
}
