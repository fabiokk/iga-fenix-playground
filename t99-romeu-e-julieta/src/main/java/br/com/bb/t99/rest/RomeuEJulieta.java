package br.com.bb.t99.rest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class RomeuEJulieta {

    @GET
    @Path("/romeuejulieta/{numero}")
    public Response romeuejulieta(@PathParam("numero") Integer numero) {
        
        Status status = Response.Status.OK;
        String mensagem; 
        
        if (numero <= 0){
            status = Response.Status.FORBIDDEN;
            mensagem = "O número deve ser positivo!";
        }else if (numero % 3 == 0 && numero % 5 == 0) {
            mensagem = "romeu e julieta";
        }else if (numero % 3 == 0) {
            mensagem = "queijo";
        }else if (numero % 5 == 0) {
            mensagem = "goiabada";
        }else{
            mensagem = numero.toString();
        }
        
        return Response.status(status).entity(mensagem).build();
    }
    
}
