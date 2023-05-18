package br.com.bb.t99.persistence.dto;

public class Detail {

   
    private Float valor;
     private String moeda;
     private String resposta;
     
    public void setValor(Float valor) {
        this.valor = valor;
    }
    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }
    public Float getValor() {
        return valor;
    }
    public String getMoeda() {
        return moeda;
    }
    public String getResposta() {
        return resposta;
    }
    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
     
     

    
}
