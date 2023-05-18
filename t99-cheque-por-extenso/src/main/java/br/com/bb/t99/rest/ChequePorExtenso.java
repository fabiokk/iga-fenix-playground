package br.com.bb.t99.rest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.bb.t99.persistence.dto.ChequeDTO;
import br.com.bb.t99.persistence.dto.Detail;
import br.com.bb.t99.persistence.dto.RespostaChequeDTO;

@Path("/cheque")
@RequestScoped
public class ChequePorExtenso {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response chequePorExtenso(ChequeDTO cheque) {
        
        if (cheque.getValor() <= 0 && cheque.getMoeda() != "real"){
            
            return Response.status(Response.Status.FORBIDDEN).entity("O número deve ser positivo! e A moeda precisa real ou dolar").build();

        }
        RespostaChequeDTO resposta = new RespostaChequeDTO();
        resposta.setMessage("cheque processado com sucesso!");
        
        Detail detail = new Detail();
        detail.setResposta("um centavo");
        
        resposta.setDetail(detail);
        return Response.status(Response.Status.CREATED).entity(resposta).build();
    }
}
