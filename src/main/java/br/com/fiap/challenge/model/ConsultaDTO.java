package br.com.fiap.challenge.model;

import java.time.LocalDateTime;

public class ConsultaDTO {
    private String cpfPaciente;
    private String especialidade;
    private String dataConsulta; // formato YYYY-MM-DD
    private String horarioConsulta; // formato HH:mm

    // ✅ Adicione esses métodos:
    public boolean isValid() {
        return cpfPaciente != null && !cpfPaciente.isBlank()
                && especialidade != null && !especialidade.isBlank()
                && dataConsulta != null && horarioConsulta != null;
    }

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.parse(dataConsulta + "T" + horarioConsulta);
    }

    public String getCpfPaciente() {
        return cpfPaciente;
    }

    public void setCpfPaciente(String cpfPaciente) {
        this.cpfPaciente = cpfPaciente;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(String dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public String getHorarioConsulta() {
        return horarioConsulta;
    }

    public void setHorarioConsulta(String horarioConsulta) {
        this.horarioConsulta = horarioConsulta;
    }
}
