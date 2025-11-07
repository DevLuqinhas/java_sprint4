package br.com.fiap.challenge.model;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Hospital {
    private String nome;
    private Endereco endereco;
    private Telefone telefone;

    public Hospital() {}

    public Hospital(String nome, Endereco endereco, Telefone telefone) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }
}
