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

    public Consulta findById(long idConsulta) throws SQLException {
        String sql = "SELECT * FROM tbl_consulta WHERE id_consulta = ?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setLong(1, idConsulta);
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
            m.nome_medico,
            e.nome AS especialidade_nome,
            p.nome_completo,
            h.nome_hospital
        FROM tbl_consulta c
        LEFT JOIN tbl_medico m ON m.id_medico = c.id_medico
        LEFT JOIN tbl_especialidade e ON e.id_especialidade = m.id_especialidade
        LEFT JOIN tbl_paciente p ON p.id_paciente = c.id_paciente
        LEFT JOIN tbl_hospital h ON h.id_hospital = c.id_hospital
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
        String sql = "DELETE FROM tbl_consulta WHERE id_consulta = ?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setLong(1, idConsulta);
            return st.executeUpdate() > 0;
        }
    }

    public long selecionarMedicoDisponivel(long idEspecialidade, LocalDateTime dataConsulta) throws SQLException {
        String sql = """
            SELECT m.id_medico
            FROM tbl_medico m
            WHERE m.id_especialidade = ?
              AND m.id_medico NOT IN (
                    SELECT c.id_medico FROM tbl_consulta c WHERE c.data_hora_consulta = ?
              )
            AND ROWNUM = 1
            """;

        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, idEspecialidade);
            st.setTimestamp(2, Timestamp.valueOf(dataConsulta));

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id_medico");
                }
            }
        }
        return 0;
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

        var paciente = new Paciente();
        paciente.setNome(rs.getString("nome_completo"));
        c.setPaciente(paciente);

        var medico = new MedicoResp();
        medico.setNome(rs.getString("nome_medico"));

        var especialidade = new Especialidade();
        especialidade.setNome(rs.getString("especialidade_nome"));
        medico.setEspecialidade(especialidade);

        c.setMedico_resp(medico);

        var hospital = new Hospital();
        hospital.setNome(rs.getString("nome_hospital"));
        c.setHospital(hospital);

        return c;
    }
}
