package br.com.fiap.challenge.app;

import br.com.fiap.challenge.business.*;
import br.com.fiap.challenge.model.*;
import br.com.fiap.challenge.repository.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MainTest {

    public static void main(String[] args) {
        try {
            PacienteRepository pacienteRepo = new PacienteRepository();
            MedicoRepository medicoRepo = new MedicoRepository();
            HospitalRepository hospitalRepo = new HospitalRepository();
            ConsultaRepository consultaRepo = new ConsultaRepository();
            LembreteRepository lembreteRepo = new LembreteRepository();
            EspecialidadeRepository espRepo = new EspecialidadeRepository();
            CanalComunicacaoRepository canalRepo = new CanalComunicacaoRepository();

            PacienteBusiness pacienteBiz = new PacienteBusiness();
            ConsultaBusiness consultaBiz = new ConsultaBusiness();
            LembreteBusiness lembreteBiz = new LembreteBusiness();
            MedicoBusiness medicoBiz = new MedicoBusiness();

            // 1. PACIENTE
            Telefone telPac = new Telefone(11, "9998887771");
            Paciente p = new Paciente(0, "Lucas Vieira", "12345678901",
                    LocalDate.of(2000, 5, 15), telPac);

            if (pacienteBiz.pacienteValido(p)) {
                pacienteRepo.create(p);
                System.out.println("Paciente cadastrado!");
            }

            Paciente pacienteDb = pacienteRepo.findById(1L);
            if (pacienteDb != null) {
                System.out.println("Paciente encontrado: " + pacienteDb.getNome());
            }

            // 2. ESPECIALIDADE e MÉDICO
            Especialidade esp = new Especialidade(1, "Cardiologia", "Consultas cardíacas");
            espRepo.create(esp);

            Telefone telMed = new Telefone(11, "9876543211");
            MedicoResp med = new MedicoResp("Dr. João", "CRM1234", telMed, esp);

            if (medicoBiz.validarCrm(med.getCrm())) {
                medicoRepo.create(med);
                System.out.println("Médico cadastrado!");
            }

            // 3. HOSPITAL
            Endereco end = new Endereco("Rua A", 123, "01234567");
            Telefone telHosp = new Telefone(11, "4002892212");
            Hospital h = new Hospital("Hospital das Clínicas", end, telHosp);
            hospitalRepo.create(h);
            System.out.println("Hospital cadastrado!");

            // 4. CONSULTA
            LocalDateTime dataConsulta = LocalDateTime.now().plusDays(2);
            if (consultaBiz.validarDataConsulta(dataConsulta)) {
                Consulta c = new Consulta(
                        0,
                        dataConsulta,
                        med,
                        0,           // status = 0 (agendada)
                        null,
                        null,
                        p,
                        h
                );
                consultaRepo.create(c, 1L, 1L, 1L);
                System.out.println("Consulta agendada!");
            }

            // 5. CANAL e LEMBRETE
            CanalComunicacao canal = new CanalComunicacao(0, "WhatsApp", "W");
            canalRepo.create(canal);
            System.out.println("Canal de comunicação criado!");

            Consulta consultaDb = consultaRepo.findById(1L);
            if (consultaDb != null) {
                Lembrete lembrete = new Lembrete(
                        "Lembrete da consulta",
                        consultaDb.getData_hora_consulta().minusMinutes(20),
                        canal
                );
                lembreteRepo.create(lembrete, 1L, 1L);
                System.out.println("Lembrete criado!");
            }

            // 6. LISTAGEM
            List<Consulta> consultas = consultaRepo.findAll();
            for (Consulta c : consultas) {
                System.out.println("Consulta: " + c.getId_consulta() + " - Status: " + c.getStatus_consulta());
            }

            List<Lembrete> lembretes = lembreteRepo.findByConsulta(1L);
            for (Lembrete l : lembretes) {
                System.out.println("Lembrete: " + l.getMensagem());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
