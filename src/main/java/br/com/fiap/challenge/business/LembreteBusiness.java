package br.com.fiap.challenge.business;

import br.com.fiap.challenge.model.Consulta;
import br.com.fiap.challenge.model.Lembrete;

import java.time.LocalDateTime;

public class LembreteBusiness {

    // Gera um lembrete 20 minutos antes da consulta
    public Lembrete gerarLembrete(Consulta consulta) {
        LocalDateTime dataEnvio = consulta.getData_hora_consulta().minusMinutes(20);
        String mensagem = "Lembrete: sua consulta será às " + consulta.getData_hora_consulta();
        return new Lembrete(mensagem, dataEnvio, consulta.getLembrete().getCanal_comunicacao());
    }

    // Verifica se já é hora de enviar
    public boolean prontoParaEnvio(Lembrete l) {
        return LocalDateTime.now().isAfter(l.getDataEnvio());
    }
}
