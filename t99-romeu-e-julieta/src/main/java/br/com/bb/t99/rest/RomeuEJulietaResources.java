package br.com.bb.t99.rest;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/romeuejulieta")
@RequestScoped
@Tag(name = "Dojo", description = "Time Fênix")
public class RomeuEJulietaResources {

    @GET
    @Path("/{number}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response romeuejulieta(@PathParam("number") int number) {
        if (number <= 0) {
            return Response.status(Response.Status.FORBIDDEN).entity("O número deve ser positivo").build();
        }
        if (number % 15 == 0) {
            return Response.status(Response.Status.OK).entity("romeu e julieta").build();
        }
        if (number % 3 == 0) {
            return Response.status(Response.Status.OK).entity("queijo").build();
        }
        if (number % 5 == 0) {
            return Response.status(Response.Status.OK).entity("goiabada").build();
        }

        return Response.status(Response.Status.OK).entity(String.valueOf(number)).build();
    }
}
