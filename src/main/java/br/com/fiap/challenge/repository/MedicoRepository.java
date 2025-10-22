package br.com.fiap.challenge.repository;

import br.com.fiap.challenge.model.Especialidade;
import br.com.fiap.challenge.model.MedicoResp;
import br.com.fiap.challenge.model.Telefone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicoRepository {

    private ConnectionFactory cf = new ConnectionFactory();

    // CREATE
    public void create(MedicoResp m) throws SQLException {
        String sql = "INSERT INTO tbl_medico (nome_medico, crm_medico, telefone_medico, id_especialidade) VALUES (?,?,?,?)";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql, new String[]{"id_medico"})) {
            st.setString(1, m.getNome());
            st.setString(2, m.getCrm());
            st.setString(3, m.getTelefone().getNumeroTel());
            st.setLong(4, m.getEspecialidade().getId_especialidade());
            st.executeUpdate();
        }
    }

    public List<MedicoResp> findAll() throws SQLException {
        List<MedicoResp> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbl_medico";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) lista.add(mapRowToMedico(rs));
        }
        return lista;
    }

    public MedicoResp findById(long id) throws SQLException {
        String sql = "SELECT * FROM tbl_medico WHERE id_medico=?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return mapRowToMedico(rs);
            }
        }
        return null;
    }

    public boolean update(MedicoResp m, long id) throws SQLException {
        String sql = "UPDATE tbl_medico SET nome_medico=?, telefone_medico=?, especialidade_medico=? WHERE id_medico=?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, m.getNome());
            st.setString(2, m.getTelefone().getNumeroTel());
            st.setString(3, m.getEspecialidade().getNome());
            st.setLong(4, id);
            return st.executeUpdate() > 0;
        }
    }

    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM tbl_medico WHERE id_medico=?";
        try (Connection con = cf.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, id);
            return st.executeUpdate() > 0;
        }
    }

    private MedicoResp mapRowToMedico(ResultSet rs) throws SQLException {
        String nome = rs.getString("nome_medico");
        String crm = rs.getString("crm_medico");
        Telefone tel = new Telefone(11, rs.getString("telefone_medico"));
        Especialidade esp = new Especialidade(0, rs.getString("especialidade_medico"), "");
        return new MedicoResp(nome, crm, tel, esp);
    }
}
