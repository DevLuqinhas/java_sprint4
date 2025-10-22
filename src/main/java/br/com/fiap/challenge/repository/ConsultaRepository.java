package br.com.fiap.challenge.repository;

import br.com.fiap.challenge.model.Consulta;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultaRepository {

    private ConnectionFactory cf = new ConnectionFactory();

    public void create(Consulta c, long idPaciente, long idMedico, long idHospital) throws SQLException {
        String sql = "INSERT INTO tbl_consulta (id_paciente, id_medico, id_hospital, data_hora_consulta, status_consulta, link_consulta) VALUES (?,?,?,?,?,?)";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setLong(1, idPaciente);
            st.setLong(2, idMedico);
            st.setLong(3, idHospital);
            st.setTimestamp(4, Timestamp.valueOf(c.getData_hora_consulta()));
            st.setInt(5, c.getStatus_consulta());
            st.setString(6, c.getLink());

            st.executeUpdate();
        }
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
        String sql = "SELECT * FROM tbl_consulta";
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

    private Consulta mapRowToConsulta(ResultSet rs) throws SQLException {
        long id = rs.getLong("id_consulta");
        LocalDateTime dataHora = rs.getTimestamp("data_hora_consulta").toLocalDateTime();
        int status = rs.getInt("status_consulta");
        String link = rs.getString("link_consulta");

        return new Consulta(id, dataHora, null, status, null, link, null, null);
    }
}
