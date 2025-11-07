package br.com.fiap.challenge.model;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Especialidade {
    private long id_especialidade;
    private String nome;
    private String descricao;

    public Especialidade() {}

    public Especialidade(long id_especialidade, String nome, String descricao) {
        this.id_especialidade = id_especialidade;
        this.nome = nome;
        this.descricao = descricao;
    }

    public long getId_especialidade() {
        return id_especialidade;
    }

    public void setId_especialidade(long id_especialidade) {
        this.id_especialidade = id_especialidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
