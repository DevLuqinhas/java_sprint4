package br.com.fiap.challenge.repository;

import br.com.fiap.challenge.model.Especialidade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadeRepository {

    private ConnectionFactory cf = new ConnectionFactory();

    public void create(Especialidade e) throws SQLException {
        String sql = "INSERT INTO tbl_especialidade (nome, descricao) VALUES (?, ?)";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, e.getNome());
            st.setString(2, e.getDescricao());
            st.executeUpdate();
        }
    }

    public Especialidade findById(long id) throws SQLException {
        String sql = "SELECT * FROM tbl_especialidade WHERE id_especialidade = ?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new Especialidade(
                            rs.getLong("id_especialidade"),
                            rs.getString("nome"),
                            rs.getString("descricao")
                    );
                }
            }
        }
        return null;
    }

    public List<Especialidade> findAll() throws SQLException {
        String sql = "SELECT * FROM tbl_especialidade";
        List<Especialidade> lista = new ArrayList<>();
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                lista.add(new Especialidade(
                        rs.getLong("id_especialidade"),
                        rs.getString("nome"),
                        rs.getString("descricao")
                ));
            }
        }
        return lista;
    }

    public boolean update(Especialidade e) throws SQLException {
        String sql = "UPDATE tbl_especialidade SET nome = ?, descricao = ? WHERE id_especialidade = ?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, e.getNome());
            st.setString(2, e.getDescricao());
            st.setLong(3, e.getId_especialidade());
            return st.executeUpdate() > 0;
        }
    }

    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM tbl_especialidade WHERE id_especialidade = ?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, id);
            return st.executeUpdate() > 0;
        }
    }
}
