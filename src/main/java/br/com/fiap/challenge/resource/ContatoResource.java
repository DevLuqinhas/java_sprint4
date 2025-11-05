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

    // ✅ Enviar (criar) um novo contato
    @POST
    public Response enviarContato(FormuContatoDTO dto) {
        try {
            Contato c = new Contato();
            c.setNome(dto.getNome());
            c.setEmail(dto.getEmail());
            c.setMensagem(dto.getMensagem());
            c.setDataenvio(LocalDateTime.now());
            c.setStatus_contato(0); // 0 = recebido

            repository.salvar(c);
            return Response.status(Response.Status.CREATED).entity(c).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao salvar o contato no banco de dados.")
                    .build();
        }
    }

    // ✅ Buscar todos os contatos
    @GET
    public Response listarTodos() {
        try {
            List<Contato> lista = repository.buscarTodas();
            return Response.ok(lista).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar contatos.")
                    .build();
        }
    }

    // ✅ Buscar contato por ID
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
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar o contato.")
                    .build();
        }
    }

    // ✅ Atualizar contato (ex: mudar status, corrigir dados)
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
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar o contato.")
                    .build();
        }
    }

    // ✅ Excluir contato
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
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao deletar o contato.")
                    .build();
        }
    }
}
