package br.com.fiap.challenge.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class CanalComunicacao {
    private long id_canal_comu;
    private String nome;
    private String tipo;

    public CanalComunicacao(long id_canal_comu, String nome, String tipo) {
        this.id_canal_comu = id_canal_comu;
        this.nome = nome;
        this.tipo = tipo;
    }

    public long getId_canal_comu() {
        return id_canal_comu;
    }

    public void setId_canal_comu(long id_canal_comu) {
        this.id_canal_comu = id_canal_comu;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
