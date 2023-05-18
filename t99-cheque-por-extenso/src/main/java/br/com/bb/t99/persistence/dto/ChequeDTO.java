package br.com.bb.t99.persistence.dto;

public class ChequeDTO {

    private Float valor;
    private String moeda;

    
    public ChequeDTO() {
    }
    public ChequeDTO(Float valor, String moeda) {
        this.valor = valor;
        this.moeda = moeda;
    }
    public Float getValor() {
        return valor;
    }
    public void setValor(Float valor) {
        this.valor = valor;
    }
    public String getMoeda() {
        return moeda;
    }
    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }
    
    
}
