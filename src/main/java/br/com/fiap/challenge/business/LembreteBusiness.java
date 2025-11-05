package br.com.fiap.challenge.business;

import br.com.fiap.challenge.model.CanalComunicacao;
import br.com.fiap.challenge.model.Consulta;
import br.com.fiap.challenge.model.Lembrete;
import br.com.fiap.challenge.repository.LembreteRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class LembreteBusiness {

    private final LembreteRepository lembreteRepository = new LembreteRepository();

    // ✅ Gera lembrete automático ao agendar consulta
    public void gerarLembreteAutomatico(Consulta consulta, long idCanal) throws SQLException {
        LocalDateTime dataEnvio = consulta.getData_hora_consulta().minusMinutes(20);
        String mensagem = "Lembrete: sua consulta será às " + consulta.getData_hora_consulta();

        CanalComunicacao canal = new CanalComunicacao(idCanal, "WhatsApp", "M"); // tipo = 'M' (mensagem)
        Lembrete lembrete = new Lembrete(mensagem, dataEnvio, canal);

        lembreteRepository.create(lembrete, consulta.getId_consulta(), idCanal);
        consulta.setLembrete(lembrete);
    }

    // ✅ Versão defensiva (não quebra se lembrete for nulo)
    public Lembrete gerarLembrete(Consulta consulta) {
        LocalDateTime dataEnvio = consulta.getData_hora_consulta().minusMinutes(20);
        String mensagem = "Lembrete: sua consulta será às " + consulta.getData_hora_consulta();
        CanalComunicacao canal = (consulta.getLembrete() != null)
                ? consulta.getLembrete().getCanal_comunicacao()
                : new CanalComunicacao(1L, "WhatsApp", "M");
        return new Lembrete(mensagem, dataEnvio, canal);
    }

    public boolean prontoParaEnvio(Lembrete l) {
        return LocalDateTime.now().isAfter(l.getDataEnvio());
    }
}
