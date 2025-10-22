package br.com.fiap.challenge.repository;

import br.com.fiap.challenge.model.CanalComunicacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CanalComunicacaoRepository {

    private ConnectionFactory cf = new ConnectionFactory();

    public void create(CanalComunicacao c) throws SQLException {
        String sql = "INSERT INTO tbl_canal_comunicacao (nome, tipo) VALUES (?, ?)";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, c.getNome());
            st.setString(2, c.getTipo());
            st.executeUpdate();
        }
    }

    public CanalComunicacao findById(long id) throws SQLException {
        String sql = "SELECT * FROM tbl_canal_comunicacao WHERE id_canal_comunicacao = ?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new CanalComunicacao(
                            rs.getLong("id_canal_comunicacao"),
                            rs.getString("nome"),
                            rs.getString("tipo")
                    );
                }
            }
        }
        return null;
    }

    public List<CanalComunicacao> findAll() throws SQLException {
        String sql = "SELECT * FROM tbl_canal_comunicacao";
        List<CanalComunicacao> lista = new ArrayList<>();
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                lista.add(new CanalComunicacao(
                        rs.getLong("id_canal_comunicacao"),
                        rs.getString("nome"),
                        rs.getString("tipo")
                ));
            }
        }
        return lista;
    }

    public boolean update(CanalComunicacao c) throws SQLException {
        String sql = "UPDATE tbl_canal_comunicacao SET nome = ?, tipo = ? WHERE id_canal_comunicacao = ?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, c.getNome());
            st.setString(2, c.getTipo());
            st.setLong(3, c.getId_canal_comu());
            return st.executeUpdate() > 0;
        }
    }

    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM tbl_canal_comunicacao WHERE id_canal_comunicacao = ?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, id);
            return st.executeUpdate() > 0;
        }
    }
}
