package br.com.fiap.challenge.resource;

import br.com.fiap.challenge.model.Consulta;
import br.com.fiap.challenge.repository.ConsultaRepository;
import br.com.fiap.challenge.business.ConsultaBusiness;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Path("/consultas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConsultaResource {

    private ConsultaRepository repository = new ConsultaRepository();
    private ConsultaBusiness business = new ConsultaBusiness();

    @GET
    public Response listar() {
        try {
            List<Consulta> consultas = repository.findAll();
            return Response.ok(consultas).build();
        } catch (SQLException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") long id) {
        try {
            Consulta c = repository.findById(id);
            if (c == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(c).build();
        } catch (SQLException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @POST
    public Response cadastrar(Consulta c,
                              @QueryParam("idPaciente") long idPaciente,
                              @QueryParam("idMedico") long idMedico,
                              @QueryParam("idHospital") long idHospital) {

        try {
            // Validação da data
            if (!business.validarDataConsulta(c.getData_hora_consulta())) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Data da consulta inválida (não pode ser no passado)").build();
            }

            // Define status automaticamente
            int status = business.definirStatusConsulta(c.getData_hora_consulta());
            c.setStatus_consulta(status);

            repository.create(c, idPaciente, idMedico, idHospital);

            return Response.status(Response.Status.CREATED).entity(c).build();

        } catch (SQLException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") long id, Consulta c) {
        try {
            c.setId_consulta(id);
            boolean atualizado = repository.update(c);
            if (atualizado) {
                return Response.ok(c).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (SQLException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") long id) {
        try {
            boolean removido = repository.delete(id);
            if (removido) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (SQLException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
