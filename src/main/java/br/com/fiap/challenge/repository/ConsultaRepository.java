package br.com.fiap.challenge.repository;

import br.com.fiap.challenge.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultaRepository {

    private ConnectionFactory cf = new ConnectionFactory();

    public long createReturningId(Consulta c, long idPaciente, long idMedico, long idHospital) throws SQLException {
        String sql = "INSERT INTO tbl_consulta (id_paciente, id_medico, id_hospital, data_hora_consulta, status_consulta, link_consulta) VALUES (?,?,?,?,?,?)";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql, new String[]{"id_consulta"})) {

            st.setLong(1, idPaciente);
            st.setLong(2, idMedico);
            st.setLong(3, idHospital);
            st.setTimestamp(4, Timestamp.valueOf(c.getData_hora_consulta()));
            st.setInt(5, c.getStatus_consulta());
            st.setString(6, c.getLink());

            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return 0;
    }

    public Consulta findById(long id) throws SQLException {
        String sql = """
        SELECT 
            c.id_consulta,
            c.data_hora_consulta,
            c.status_consulta,
            c.link_consulta,
            p.nome_completo AS nome_paciente,
            m.nome_medico,
            e.nome AS especialidade_nome,
            h.nome_hospital
        FROM tbl_consulta c
        JOIN tbl_paciente p ON p.id_paciente = c.id_paciente
        JOIN tbl_medico m ON m.id_medico = c.id_medico
        JOIN tbl_especialidade e ON e.id_especialidade = m.id_especialidade
        JOIN tbl_hospital h ON h.id_hospital = c.id_hospital
        WHERE c.id_consulta = ?
    """;

        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setLong(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return mapRowToConsulta(rs);
                }
            }
        }

        return null;
    }

    public List<Consulta> findAll() throws SQLException {
        String sql = """
        SELECT 
            c.id_consulta,
            c.data_hora_consulta,
            c.status_consulta,
            c.link_consulta,
            p.nome_completo AS nome_paciente,
            m.nome_medico,
            e.nome AS especialidade_nome,
            h.nome_hospital
        FROM tbl_consulta c
        JOIN tbl_paciente p ON p.id_paciente = c.id_paciente
        JOIN tbl_medico m ON m.id_medico = c.id_medico
        JOIN tbl_especialidade e ON e.id_especialidade = m.id_especialidade
        JOIN tbl_hospital h ON h.id_hospital = c.id_hospital
        ORDER BY c.data_hora_consulta ASC
    """;

        List<Consulta> lista = new ArrayList<>();

        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRowToConsulta(rs));
            }
        }

        return lista;
    }

    public boolean update(Consulta c) throws SQLException {
        String sql = "UPDATE tbl_consulta SET data_hora_consulta = ?, status_consulta = ?, link_consulta = ? WHERE id_consulta = ?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setTimestamp(1, Timestamp.valueOf(c.getData_hora_consulta()));
            st.setInt(2, c.getStatus_consulta());
            st.setString(3, c.getLink());
            st.setLong(4, c.getId_consulta());

            return st.executeUpdate() > 0;
        }
    }

    public boolean delete(long idConsulta) throws SQLException {
        String deleteLembretes = "DELETE FROM tbl_lembrete WHERE id_consulta = ?";
        String deleteConsulta = "DELETE FROM tbl_consulta WHERE id_consulta = ?";

        try (Connection con = cf.getConnection()) {
            try (PreparedStatement stLembrete = con.prepareStatement(deleteLembretes);
                 PreparedStatement stConsulta = con.prepareStatement(deleteConsulta)) {

                con.setAutoCommit(false);

                // 1️⃣ Apaga lembretes relacionados
                stLembrete.setLong(1, idConsulta);
                stLembrete.executeUpdate();

                // 2️⃣ Apaga consulta
                stConsulta.setLong(1, idConsulta);
                int linhasAfetadas = stConsulta.executeUpdate();

                con.commit();
                return linhasAfetadas > 0;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public long selecionarMedicoDisponivel(long idEspecialidade, LocalDateTime dataHora) throws SQLException {
        String sql = "SELECT id_medico FROM tbl_medico WHERE id_especialidade = ? FETCH FIRST 1 ROWS ONLY";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setLong(1, idEspecialidade);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id_medico");
                }
            }
        }
        return 0; // Nenhum médico encontrado
    }

    private Consulta mapRowToConsulta(ResultSet rs) throws SQLException {
        Consulta c = new Consulta();

        c.setId_consulta(rs.getLong("id_consulta"));

        Timestamp ts = rs.getTimestamp("data_hora_consulta");
        if (ts != null) {
            c.setData_hora_consulta(ts.toLocalDateTime());
        }

        c.setStatus_consulta(rs.getInt("status_consulta"));
        c.setLink(rs.getString("link_consulta"));

        // Paciente
        Paciente paciente = new Paciente();
        paciente.setNome(rs.getString("nome_paciente"));
        c.setPaciente(paciente);

        // Médico + Especialidade
        MedicoResp medico = new MedicoResp();
        medico.setNome(rs.getString("nome_medico"));

        Especialidade especialidade = new Especialidade();
        especialidade.setNome(rs.getString("especialidade_nome"));
        medico.setEspecialidade(especialidade);

        c.setMedico_resp(medico);

        // Hospital (opcional)
        Hospital hospital = new Hospital();
        hospital.setNome(rs.getString("nome_hospital"));
        c.setHospital(hospital);

        return c;
    }
}
