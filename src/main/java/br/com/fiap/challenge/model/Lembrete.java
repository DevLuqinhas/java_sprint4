package br.com.fiap.challenge.model;

import java.time.LocalDateTime;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Lembrete {
    private String mensagem;
    private LocalDateTime dataEnvio;
    private CanalComunicacao canal_comunicacao;

    public Lembrete(String mensagem, LocalDateTime dataEnvio, CanalComunicacao canal_comunicacao) {
        this.mensagem = mensagem;
        this.dataEnvio = dataEnvio;
        this.canal_comunicacao = canal_comunicacao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public CanalComunicacao getCanal_comunicacao() {
        return canal_comunicacao;
    }

    public void setCanal_comunicacao(CanalComunicacao canal_comunicacao) {
        this.canal_comunicacao = canal_comunicacao;
    }

}
