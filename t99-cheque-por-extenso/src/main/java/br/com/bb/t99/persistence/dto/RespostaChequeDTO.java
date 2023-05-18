package br.com.bb.t99.persistence.dto;

// private class Detail {
//     private Float valor;
//     private String moeda;
//     private String resposta;
// }

// TODO: Criar objeto de resposta
public class RespostaChequeDTO {
    private String message;
    private Detail detail;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Detail getDetail() {
        return detail;
    }
    public void setDetail(Detail detail) {
        this.detail = detail;
    }
    
}
