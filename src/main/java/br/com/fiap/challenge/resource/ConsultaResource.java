package br.com.fiap.challenge.resource;

import br.com.fiap.challenge.business.ConsultaBusiness;
import br.com.fiap.challenge.business.LembreteBusiness;
import br.com.fiap.challenge.model.Consulta;
import br.com.fiap.challenge.model.ConsultaDTO;
import br.com.fiap.challenge.repository.ConsultaRepository;
import br.com.fiap.challenge.repository.EspecialidadeRepository;
import br.com.fiap.challenge.repository.PacienteRepository;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Path("/consultas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConsultaResource {

    private final ConsultaRepository consultaRepository = new ConsultaRepository();
    private final PacienteRepository pacienteRepository = new PacienteRepository();
    private final EspecialidadeRepository especialidadeRepository = new EspecialidadeRepository();
    private final ConsultaBusiness consultaBusiness = new ConsultaBusiness();
    private final LembreteBusiness lembreteBusiness = new LembreteBusiness();

    @OPTIONS
    @Path("/{any: .*}")
    public Response options() {
        return Response.ok().build();
    }

    @GET
    public Response listar() {
        try {
            List<Consulta> consultas = consultaRepository.findAll();
            return Response.ok(consultas).build();
        } catch (SQLException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/agendar")
    public Response agendar(ConsultaDTO dto) {
        try {
            if (dto == null || !dto.isValid()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Dados incompletos para agendamento.").build();
            }

            LocalDateTime dataHora = dto.toLocalDateTime();

            long idPaciente = pacienteRepository.findIdByCpf(dto.getCpfPaciente());
            long idEspecialidade = especialidadeRepository.findIdByNome(dto.getEspecialidade());
            long idHospital = 1L; // fixo para teste
            long idMedico = consultaRepository.selecionarMedicoDisponivel(idEspecialidade, dataHora);

            if (idMedico == 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Nenhum médico disponível para esta especialidade neste horário.").build();
            }

            // Monta objeto e insere no banco
            Consulta c = new Consulta();
            c.setData_hora_consulta(dataHora);
            c.setStatus_consulta(consultaBusiness.definirStatusConsulta(dataHora));
            c.setLink("https://meet.consulta/" + UUID.randomUUID());

            long idConsulta = consultaRepository.createReturningId(c, idPaciente, idMedico, idHospital);
            c.setId_consulta(idConsulta);

            // Gera lembrete automático
            lembreteBusiness.gerarLembreteAutomatico(c, 1L); // canal WhatsApp

            return Response.status(Response.Status.CREATED).entity(c).build();

        } catch (SQLException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response reagendar(@PathParam("id") long id, Consulta c) {
        try {
            c.setId_consulta(id);

            if (!consultaBusiness.validarDataConsulta(c.getData_hora_consulta())) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Data inválida (não pode ser no passado)").build();
            }

            boolean atualizado = consultaRepository.update(c);
            if (atualizado) {
                return Response.ok("Consulta reagendada com sucesso").build();
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
            boolean removido = consultaRepository.delete(id);
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
