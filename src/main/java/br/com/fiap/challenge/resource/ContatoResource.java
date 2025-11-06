package br.com.fiap.challenge.resource;

import br.com.fiap.challenge.model.Contato;
import br.com.fiap.challenge.model.FormuContatoDTO;
import br.com.fiap.challenge.repository.ContatoRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Path("/contato")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContatoResource {

    private final ContatoRepository repository = new ContatoRepository();

    @POST
    public Response enviarContato(FormuContatoDTO dto) {
        if (dto == null || !dto.isValid()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Dados inválidos.").build();
        }

        try {
            Contato c = new Contato();
            c.setNome(dto.getNome());
            c.setEmail(dto.getEmail());
            c.setMensagem(dto.getMensagem());
            c.setFormulario_origem(dto.getFormulario_origem());
            c.setNota(dto.getNota());
            c.setDataenvio(LocalDateTime.now());
            c.setStatus_contato(0);

            repository.salvar(c);
            return Response.status(Response.Status.CREATED)
                    .entity("Contato registrado com sucesso!").build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("Erro ao salvar o contato: " + e.getMessage()).build();
        }
    }

    @GET
    public Response listarTodos() {
        try {
            List<Contato> lista = repository.buscarTodas();
            return Response.ok(lista).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().entity("Erro ao buscar contatos.").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") long id) {
        try {
            Contato c = repository.buscarPorId(id);
            if (c == null)
                return Response.status(Response.Status.NOT_FOUND).entity("Contato não encontrado.").build();
            return Response.ok(c).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().entity("Erro ao buscar contato.").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizarContato(@PathParam("id") long id, Contato c) {
        try {
            c.setId_contato(id);
            boolean atualizado = repository.update(c);
            if (atualizado)
                return Response.ok("Contato atualizado com sucesso!").build();
            else
                return Response.status(Response.Status.NOT_FOUND).entity("Contato não encontrado.").build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().entity("Erro ao atualizar contato.").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarContato(@PathParam("id") long id) {
        try {
            boolean deletado = repository.deletarPorId(id);
            if (deletado)
                return Response.noContent().build();
            else
                return Response.status(Response.Status.NOT_FOUND).entity("Contato não encontrado.").build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().entity("Erro ao deletar contato.").build();
        }
    }
}
