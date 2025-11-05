package br.com.fiap.challenge.business;

import br.com.fiap.challenge.model.FormuContatoDTO;

public class ContatoBusiness {

    // Validações simples antes de salvar
    public boolean validarCampos(FormuContatoDTO dto) {
        return dto != null &&
                dto.getNome() != null && !dto.getNome().isBlank() &&
                dto.getEmail() != null && dto.getEmail().contains("@") &&
                dto.getMensagem() != null && !dto.getMensagem().isBlank();
    }
}
