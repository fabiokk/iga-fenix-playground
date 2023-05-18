package br.com.bb.t99.rest;

import br.com.bb.t99.persistence.dao.UsuarioDao;
import br.com.bb.t99.persistence.dto.UsuarioPorEstadoDTO;
import br.com.bb.t99.persistence.models.Usuario;
import br.com.bb.t99.services.UsuarioService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import br.com.bb.dev.tracer.TracedWithRequestID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/v1/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@TracedWithRequestID
public class UsuarioResource {

	@Inject
	UsuarioService service;

	@Inject
	UsuarioDao dao;

	@GET
	@Path("/{id}")
	@Operation(summary = "Busca Usuario por ID",
			description = "Retorna um usuario")
	@APIResponse(
			responseCode = "200",
			description = "Usuario",
			content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = Usuario.class))})
	public Response obtemUsuarioPorId(final @PathParam("id") long id) {
		return  Response.status(Response.Status.OK).entity(dao.buscarUsuario(id)).build();
	}

	@GET
	@Operation(summary = "Listar Usuarios ",
			description = "Retorna uma lista de usuario")
	@APIResponse(
			responseCode = "200",
			description = "Usuario",
			content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = Usuario.class, type = SchemaType.ARRAY))})
	public Response obtemUsuarios() throws Exception  {
		return Response.status(Response.Status.OK).entity(dao.buscaUsuarios()).build();
	}

	@POST
	@Operation(summary = "Incluir um usuario.",
			description = "Incluir um usuario")
	@APIResponse(
			responseCode = "201",
			description = "Usuario",
			content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = Usuario.class))})
	public Response inserirUsuario(Usuario usuario) {
		return  Response.status(Response.Status.CREATED).entity(service.inserirUsuario(usuario)).build();
	}

	@DELETE
	@Path("/{id}")
	@Operation(summary = "Remove um usuario",
			description = "Remove um usuario pelo ID informado")
	@APIResponse(
			responseCode = "200",
			description = "Usuario",
			content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = Usuario.class))})
	public Response excluirUsuario(final @PathParam("id") long id) {
		return  Response.status(Response.Status.OK).entity(dao.excluirUsuario(id)).build();
	}

	@PUT
	@Path("/{id}")
	@Operation(summary = "Atualizar um Usuario",
			description = "Atualização de um usuario")
	@APIResponse(
			responseCode = "200",
			description = "Usuario",
			content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = Usuario.class))})
	public Response atualizar(final @PathParam("id") long id, Usuario usuario) {
		return  Response.status(Response.Status.OK).entity(service.atualizarUsuario(id, usuario)).build();
	}

	@GET
	@Path("/porestado")
	@Operation(summary = "Busca quantidade de usuarios por estado(UF)",
			description = "Retorna uma lista de quantidade de usuarios por estado")
	@APIResponse(
			responseCode = "200",
			description = "UsuarioPorEstadoDTO",
			content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = UsuarioPorEstadoDTO.class))})
	public Response obteQtdPorEstado() {
		return  Response.status(Response.Status.OK).entity(dao.buscarUsuarioPorEstado()).build();
	}

}