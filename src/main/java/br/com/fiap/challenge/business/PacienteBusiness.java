package br.com.fiap.challenge.business;

import br.com.fiap.challenge.model.Paciente;

public class PacienteBusiness {

    // Valida CPF (11 dígitos numéricos)
    public boolean validarCpf(String cpf) {
        return cpf != null && cpf.matches("\\d{11}");
    }

    // Valida telefone (mínimo 10, máximo 13 dígitos)
    public boolean validarTelefone(String telefone) {
        return telefone != null && telefone.matches("\\d{10,13}");
    }

    // Regra de exemplo: Paciente deve ter nome com pelo menos 3 caracteres
    public boolean validarNome(String nome) {
        return nome != null && nome.trim().length() >= 3;
    }

    // Combina as regras (pode ser usada antes de salvar)
    public boolean pacienteValido(Paciente p) {
        return validarCpf(p.getCpf()) &&
                validarNome(p.getNome()) &&
                p.getTelefone() != null;
    }
}
