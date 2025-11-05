package br.com.fiap.challenge.business;

import br.com.fiap.challenge.model.Consulta;

import java.time.LocalDateTime;

public class ConsultaBusiness {

    // Não permitir agendar em datas passadas
    public boolean validarDataConsulta(LocalDateTime dataConsulta) {
        return dataConsulta != null && dataConsulta.isAfter(LocalDateTime.now());
    }

    // Determina status automaticamente
    public int definirStatusConsulta(LocalDateTime dataConsulta) {
        if (dataConsulta.isAfter(LocalDateTime.now())) {
            return 0; // 0 - agendada
        } else {
            return 1; // 1 - realizada (se já passou)
        }
    }

    // Exemplo: Verifica se paciente pode reagendar
    public boolean podeReagendar(Consulta c) {
        // só pode reagendar se faltarem pelo menos 24h
        return c.getData_hora_consulta().isAfter(LocalDateTime.now().plusHours(24));
    }
}
