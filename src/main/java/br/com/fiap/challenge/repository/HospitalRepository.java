package br.com.fiap.challenge.repository;

import br.com.fiap.challenge.model.Endereco;
import br.com.fiap.challenge.model.Hospital;
import br.com.fiap.challenge.model.Telefone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HospitalRepository {

    private ConnectionFactory cf = new ConnectionFactory();

    public void create(Hospital h) throws SQLException {
        String sql = "INSERT INTO tbl_hospital (nome_hospital, endereco_hospital, telefone_hospital) VALUES (?,?,?)";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, h.getNome());
            st.setString(2, h.getEndereco().getRua() + ", " + h.getEndereco().getNumero() + " - " + h.getEndereco().getCep());
            st.setString(3, h.getTelefone().getNumeroTel());
            st.executeUpdate();
        }
    }

    public List<Hospital> findAll() throws SQLException {
        List<Hospital> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbl_hospital";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) lista.add(mapRowToHospital(rs));
        }
        return lista;
    }

    public Hospital findById(long id) throws SQLException {
        String sql = "SELECT * FROM tbl_hospital WHERE id_hospital=?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return mapRowToHospital(rs);
            }
        }
        return null;
    }

    public boolean update(Hospital h, long id) throws SQLException {
        String sql = "UPDATE tbl_hospital SET nome_hospital=?, endereco_hospital=?, telefone_hospital=? WHERE id_hospital=?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, h.getNome());
            st.setString(2, h.getEndereco().getRua() + ", " + h.getEndereco().getNumero() + " - " + h.getEndereco().getCep());
            st.setString(3, h.getTelefone().getNumeroTel());
            st.setLong(4, id);
            return st.executeUpdate() > 0;
        }
    }

    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM tbl_hospital WHERE id_hospital=?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, id);
            return st.executeUpdate() > 0;
        }
    }

    private Hospital mapRowToHospital(ResultSet rs) throws SQLException {
        String nome = rs.getString("nome_hospital");
        String endereco = rs.getString("endereco_hospital");
        String telefone = rs.getString("telefone_hospital");
        Endereco end = new Endereco(endereco, 0, ""); // simplificado
        Telefone tel = new Telefone(11, telefone);
        return new Hospital(nome, end, tel);
    }
}
