package br.com.fiap.challenge.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ConsultaDTO {
    private String cpfPaciente;
    private String especialidade;
    private String dataConsulta; // formato esperado: YYYY-MM-DD
    private String horarioConsulta; // formato esperado: HH:mm

    // ✅ Validação simples dos campos obrigatórios
    public boolean isValid() {
        return cpfPaciente != null && !cpfPaciente.isBlank()
                && especialidade != null && !especialidade.isBlank()
                && dataConsulta != null && !dataConsulta.isBlank()
                && horarioConsulta != null && !horarioConsulta.isBlank();
    }

    // ✅ Conversão segura para LocalDateTime (suporta múltiplos formatos)
    public LocalDateTime toLocalDateTime() {
        List<String> formatos = List.of(
                "yyyy-MM-dd",   // padrão ISO
                "dd/MM/yyyy",   // formato brasileiro
                "dd-MMM-yyyy"   // formato com nome do mês (ex: 31-Dec-2026)
        );

        for (String formato : formatos) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato, Locale.ENGLISH);
                LocalDate data = LocalDate.parse(this.dataConsulta, formatter);
                LocalTime hora = LocalTime.parse(this.horarioConsulta, DateTimeFormatter.ofPattern("HH:mm"));
                return LocalDateTime.of(data, hora);
            } catch (DateTimeParseException ignored) {
                // Tenta o próximo formato
            }
        }

        throw new IllegalArgumentException("Formato de data inválido: " + this.dataConsulta);
    }

    // Getters e Setters
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
