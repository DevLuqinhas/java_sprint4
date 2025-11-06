package br.com.fiap.challenge.model;

public class FormuContatoDTO {
    private String nome;
    private String email;
    private String mensagem;
    private Character formulario_origem;
    private Integer nota;

    public FormuContatoDTO() {}

    public FormuContatoDTO(String nome, String email, String mensagem, Character formulario_origem, Integer nota) {
        this.nome = nome;
        this.email = email;
        this.mensagem = mensagem;
        this.formulario_origem = formulario_origem;
        this.nota = nota;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public Character getFormulario_origem() { return formulario_origem; }
    public void setFormulario_origem(Character formulario_origem) { this.formulario_origem = formulario_origem; }

    public Integer getNota() { return nota; }
    public void setNota(Integer nota) { this.nota = nota; }

    public boolean isValid() {
        return nome != null && !nome.isBlank()
                && email != null && email.contains("@")
                && mensagem != null && !mensagem.isBlank()
                && (formulario_origem != null && (formulario_origem == 'S' || formulario_origem == 'H'));
    }
}
