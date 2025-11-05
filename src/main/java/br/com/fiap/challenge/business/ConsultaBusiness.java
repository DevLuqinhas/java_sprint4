package br.com.fiap.challenge.business;

import br.com.fiap.challenge.model.Consulta;
import br.com.fiap.challenge.model.Lembrete;
import br.com.fiap.challenge.model.CanalComunicacao;
import br.com.fiap.challenge.repository.LembreteRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class ConsultaBusiness {

    private final LembreteRepository lembreteRepository = new LembreteRepository();

    // ✅ Valida se a data é futura
    public boolean validarDataConsulta(LocalDateTime dataConsulta) {
        return dataConsulta != null && dataConsulta.isAfter(LocalDateTime.now());
    }

    // ✅ Define status automaticamente (0 = Agendada, 1 = Realizada, 2 = Cancelada)
    public int definirStatusConsulta(LocalDateTime dataConsulta) {
        return dataConsulta.isAfter(LocalDateTime.now()) ? 0 : 1;
    }

    // ✅ Regras para reagendar (só se faltar mais de 24h)
    public boolean podeReagendar(Consulta c) {
        return c.getData_hora_consulta().isAfter(LocalDateTime.now().plusHours(24));
    }

    // ✅ Cria lembrete automático 20 min antes da consulta
    public void gerarLembreteAutomatico(Consulta consulta, long idCanal) throws SQLException {
        LocalDateTime dataEnvio = consulta.getData_hora_consulta().minusMinutes(20);
        String mensagem = "Lembrete: sua consulta será às " + consulta.getData_hora_consulta();

        CanalComunicacao canal = new CanalComunicacao(idCanal, "WhatsApp", "Mensagem");
        Lembrete lembrete = new Lembrete(mensagem, dataEnvio, canal);

        lembreteRepository.create(lembrete, consulta.getId_consulta(), idCanal);
        consulta.setLembrete(lembrete);
    }
}
