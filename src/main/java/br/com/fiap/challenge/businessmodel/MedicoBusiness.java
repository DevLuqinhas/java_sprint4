package br.com.fiap.challenge.businessmodel;

import br.com.fiap.challenge.model.MedicoResp;

public class MedicoBusiness {

    // Valida CRM (não vazio e no máximo 15 caracteres)
    public boolean validarCrm(String crm) {
        return crm != null && crm.matches("\\w{4,15}");
    }


    // Verifica se o médico pode atender uma especialidade
    public boolean medicoAtendeEspecialidade(MedicoResp medico, String nomeEspecialidade) {
        return medico.getEspecialidade().getNome().equalsIgnoreCase(nomeEspecialidade);
    }
}
