package br.com.fiap.challenge.model;

import java.time.LocalDateTime;

// atributo de Hospital ou Site.

public class Contato {
    private long id_contato;
    private String nome;
    private String email;
    private String mensagem;
    private LocalDateTime dataenvio;
    private int status_contato;

    public Contato() {
    }

    public Contato(long id_contato, String nome, String email, String mensagem, LocalDateTime dataenvio, int status_contato) {
        this.id_contato = id_contato;
        this.nome = nome;
        this.email = email;
        this.mensagem = mensagem;
        this.dataenvio = dataenvio;
        this.status_contato = status_contato;
    }

    public long getId_contato() {
        return id_contato;
    }

    public void setId_contato(long id_contato) {
        this.id_contato = id_contato;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDataenvio() {
        return dataenvio;
    }

    public void setDataenvio(LocalDateTime dataenvio) {
        this.dataenvio = dataenvio;
    }

    public int getStatus_contato() {
        return status_contato;
    }

    public void setStatus_contato(int status_contato) {
        this.status_contato = status_contato;
    }
}