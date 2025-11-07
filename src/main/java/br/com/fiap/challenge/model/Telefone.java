package br.com.fiap.challenge.model;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Telefone {
    private int ddd;
    private String numeroTel;

    public Telefone(int ddd, String numeroTel) {
        this.ddd = ddd;
        this.numeroTel = numeroTel;
    }

    public int getDdd() {
        return ddd;
    }

    public void setDdd(int ddd) {
        this.ddd = ddd;
    }

    public String getNumeroTel() {
        return numeroTel;
    }

    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }
}
