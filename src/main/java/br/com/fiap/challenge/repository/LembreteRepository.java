package br.com.fiap.challenge.repository;

import br.com.fiap.challenge.model.Lembrete;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LembreteRepository {

    private ConnectionFactory cf = new ConnectionFactory();

    public void create(Lembrete l, long idConsulta, long idCanal) throws SQLException {
        String sql = "INSERT INTO tbl_lembrete (id_consulta, data_hora_lembrete, canal_comunicacao, mensagem, status_lembrete) VALUES (?,?,?,?,?)";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, idConsulta);
            st.setTimestamp(2, Timestamp.valueOf(l.getDataEnvio()));
            st.setLong(3, idCanal);
            st.setString(4, l.getMensagem());
            st.setInt(5, 0);
            st.executeUpdate();
        }
    }

    public List<Lembrete> findByConsulta(long idConsulta) throws SQLException {
        String sql = "SELECT * FROM tbl_lembrete WHERE id_consulta=?";
        List<Lembrete> lista = new ArrayList<>();
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, idConsulta);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    // montar Lembrete
                }
            }
        }
        return lista;
    }

    public boolean updateStatus(long idLembrete, int status) throws SQLException {
        String sql = "UPDATE tbl_lembrete SET status_lembrete=? WHERE id_lembrete=?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, status);
            st.setLong(2, idLembrete);
            return st.executeUpdate() > 0;
        }
    }

    public boolean delete(long idLembrete) throws SQLException {
        String sql = "DELETE FROM tbl_lembrete WHERE id_lembrete=?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, idLembrete);
            return st.executeUpdate() > 0;
        }
    }
}
