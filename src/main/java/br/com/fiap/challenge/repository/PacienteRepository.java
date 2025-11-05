package br.com.fiap.challenge.repository;

import br.com.fiap.challenge.model.Paciente;
import br.com.fiap.challenge.model.Telefone;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PacienteRepository {

    private ConnectionFactory cf = new ConnectionFactory();

    public void create(Paciente p) throws SQLException {
        String sql = "INSERT INTO tbl_paciente (cpf, nome_completo, data_nascimento, telefone_paciente) VALUES (?, ?, ?, ?)";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, p.getCpf());
            st.setString(2, p.getNome());
            st.setDate(3, Date.valueOf(p.getDataNascimento()));
            st.setString(4, p.getTelefone().getNumeroTel());

            st.executeUpdate();
        }
    }

    public long findIdByCpf(String cpf) throws SQLException {
        String sql = "SELECT id_paciente FROM tbl_paciente WHERE cpf = ?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, cpf);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id_paciente");
                }
            }
        }
        return 0;
    }

    public Paciente findById(long id) throws SQLException {
        String sql = "SELECT * FROM tbl_paciente WHERE id_paciente = ?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return mapRowToPaciente(rs);
                }
            }
        }
        return null;
    }

    public List<Paciente> findAll() throws SQLException {
        String sql = "SELECT * FROM tbl_paciente";
        List<Paciente> lista = new ArrayList<>();
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRowToPaciente(rs));
            }
        }
        return lista;
    }

    public boolean update(Paciente p) throws SQLException {
        String sql = "UPDATE tbl_paciente SET nome_completo = ?, data_nascimento = ?, telefone_paciente = ? WHERE id_paciente = ?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, p.getNome());
            st.setDate(2, Date.valueOf(p.getDataNascimento()));
            st.setString(3, p.getTelefone().getNumeroTel());
            st.setLong(4, p.getId_paciente());

            return st.executeUpdate() > 0;
        }
    }

    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM tbl_paciente WHERE id_paciente = ?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setLong(1, id);
            return st.executeUpdate() > 0;
        }
    }

    private Paciente mapRowToPaciente(ResultSet rs) throws SQLException {
        long id = rs.getLong("id_paciente");
        String nome = rs.getString("nome_completo");
        String cpf = rs.getString("cpf");
        LocalDate dataNascimento = rs.getDate("data_nascimento").toLocalDate();
        String telStr = rs.getString("telefone_paciente");

        // Se desejar extrair DDD do número, faça substring.
        Telefone tel = new Telefone(11, telStr);

        return new Paciente(id, nome, cpf, dataNascimento, tel);
    }
}
