package br.com.fiap.challenge.repository;

import br.com.fiap.challenge.model.Contato;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContatoRepository {

    private ConnectionFactory cf = new ConnectionFactory();

    // CREATE
    public void salvar(Contato contato) throws SQLException {
        String sql = "INSERT INTO tbl_contato (nome, email, mensagem, data_envio, status_contato) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, contato.getNome());
            st.setString(2, contato.getEmail());
            st.setString(3, contato.getMensagem());
            st.setTimestamp(4, Timestamp.valueOf(contato.getDataenvio()));
            st.setInt(5, contato.getStatus_contato());
            st.executeUpdate();
        }
    }

    // READ
    public List<Contato> buscarTodas() throws SQLException {
        List<Contato> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbl_contato";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Contato buscarPorId(long id) throws SQLException {
        String sql = "SELECT * FROM tbl_contato WHERE id_contato = ?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    // UPDATE
    public boolean update(Contato contato) throws SQLException {
        String sql = "UPDATE tbl_contato SET nome=?, email=?, mensagem=?, data_envio=?, status_contato=? WHERE id_contato=?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, contato.getNome());
            st.setString(2, contato.getEmail());
            st.setString(3, contato.getMensagem());
            st.setTimestamp(4, Timestamp.valueOf(contato.getDataenvio()));
            st.setInt(5, contato.getStatus_contato());
            st.setLong(6, contato.getId_contato());
            return st.executeUpdate() > 0;
        }
    }

    // DELETE
    public boolean deletarPorId(long id) throws SQLException {
        String sql = "DELETE FROM tbl_contato WHERE id_contato = ?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, id);
            return st.executeUpdate() > 0;
        }
    }

    private Contato mapear(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("data_envio");
        return new Contato(
                rs.getLong("id_contato"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("mensagem"),
                (ts != null ? ts.toLocalDateTime() : null),
                rs.getInt("status_contato")
        );
    }
}
