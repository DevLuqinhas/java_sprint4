package br.com.fiap.challenge.business;

import br.com.fiap.challenge.model.Consulta;
import br.com.fiap.challenge.model.Lembrete;
import br.com.fiap.challenge.model.CanalComunicacao;
import br.com.fiap.challenge.repository.LembreteRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ConsultaBusiness {

    private final LembreteRepository lembreteRepository = new LembreteRepository();

    /**
     * ✅ Valida se a nova data da consulta é pelo menos 1 mês após a data atual.
     * Permite reagendamentos para antes da data original, desde que não seja antes de hoje + 1 mês.
     */
    public boolean validarDataConsulta(LocalDateTime novaData) {
        if (novaData == null) {
            System.out.println("⚠️ Data recebida é nula!");
            return false;
        }

        // Normaliza fuso horário e compara apenas a data
        LocalDate hoje = LocalDate.now();
        LocalDate limiteMinimo = hoje.plusMonths(1);
        LocalDate dataConsulta = novaData.toLocalDate();

        boolean valido = !dataConsulta.isBefore(limiteMinimo);

        // Log detalhado para depuração
        System.out.println("🕓 Validação reagendamento:");
        System.out.println("   Data atual: " + hoje);
        System.out.println("   Limite mínimo (+1 mês): " + limiteMinimo);
        System.out.println("   Data informada: " + dataConsulta);
        System.out.println("   Resultado: " + (valido ? "✅ Válido" : "❌ Inválido"));

        return valido;
    }

    /**
     * ✅ Define status automaticamente (0 = Agendada, 1 = Realizada, 2 = Cancelada)
     */
    public int definirStatusConsulta(LocalDateTime dataConsulta) {
        return dataConsulta.isAfter(LocalDateTime.now()) ? 0 : 1;
    }

    /**
     * ✅ Permite reagendar apenas se a data da nova consulta for posterior a +24h
     */
    public boolean podeReagendar(Consulta c) {
        return c.getData_hora_consulta().isAfter(LocalDateTime.now().plusHours(24));
    }

    /**
     * ✅ Cria lembrete automático 20 min antes da consulta
     */
    public void gerarLembreteAutomatico(Consulta consulta, long idCanal) throws SQLException {
        LocalDateTime dataEnvio = consulta.getData_hora_consulta().minusMinutes(20);
        String mensagem = "Lembrete: sua consulta será às " + consulta.getData_hora_consulta();

        CanalComunicacao canal = new CanalComunicacao(idCanal, "WhatsApp", "Mensagem");
        Lembrete lembrete = new Lembrete(mensagem, dataEnvio, canal);

        lembreteRepository.create(lembrete, consulta.getId_consulta(), idCanal);
        consulta.setLembrete(lembrete);
    }
}
