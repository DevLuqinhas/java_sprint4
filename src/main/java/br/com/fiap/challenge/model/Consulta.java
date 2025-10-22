package br.com.fiap.challenge.model;

import java.time.LocalDateTime;

public class Consulta {
    private long id_consulta;
    private LocalDateTime data_hora_consulta;
    private MedicoResp medico_resp;
    private int status_consulta;
    private Lembrete lembrete;
    private String link;
    private Paciente paciente;
    private Hospital hospital;

    public Consulta(long id_consulta, LocalDateTime data_hora_consulta, MedicoResp medico_resp, int status_consulta, Lembrete lembrete, String link, Paciente paciente, Hospital hospital) {
        this.id_consulta = id_consulta;
        this.data_hora_consulta = data_hora_consulta;
        this.medico_resp = medico_resp;
        this.status_consulta = status_consulta;
        this.lembrete = lembrete;
        this.link = link;
        this.paciente = paciente;
        this.hospital = hospital;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getId_consulta() {
        return id_consulta;
    }

    public void setId_consulta(long id_consulta) {
        this.id_consulta = id_consulta;
    }

    public LocalDateTime getData_hora_consulta() {
        return data_hora_consulta;
    }

    public void setData_hora_consulta(LocalDateTime data_hora_consulta) {
        this.data_hora_consulta = data_hora_consulta;
    }

    public MedicoResp getMedico_resp() {
        return medico_resp;
    }

    public void setMedico_resp(MedicoResp medico_resp) {
        this.medico_resp = medico_resp;
    }

    public int getStatus_consulta() {
        return status_consulta;
    }

    public void setStatus_consulta(int status_consulta) {
        this.status_consulta = status_consulta;
    }

    public Lembrete getLembrete() {
        return lembrete;
    }

    public void setLembrete(Lembrete lembrete) {
        this.lembrete = lembrete;
    }
}

