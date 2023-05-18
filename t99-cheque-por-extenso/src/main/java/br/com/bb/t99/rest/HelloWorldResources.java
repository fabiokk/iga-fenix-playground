package br.com.bb.t99.rest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/hello")
@RequestScoped
public class HelloWorldResources {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response hello() {
        return Response.status(Response.Status.OK).entity("Hello!").build();
    }
}
