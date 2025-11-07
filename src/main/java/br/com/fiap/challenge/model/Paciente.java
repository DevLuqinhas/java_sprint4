package br.com.fiap.challenge.model;

import java.time.LocalDate;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Paciente {
    private long id_paciente;
    private String nome;
    private String cpf; // novo atributo
    private Telefone telefone;
    private LocalDate dataNascimento;

    public Paciente() {}

    public Paciente(long id_paciente, String nome, String cpf, LocalDate dataNascimento, Telefone telefone) {
        this.id_paciente = id_paciente;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public long getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(long id_paciente) {
        this.id_paciente = id_paciente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }
}
