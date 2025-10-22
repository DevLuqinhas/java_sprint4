package br.com.fiap.challenge.app;


import br.com.fiap.challenge.model.*;
import br.com.fiap.challenge.repository.*;
import br.com.fiap.challenge.repository.PacienteRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static PacienteRepository pacienteRepo = new PacienteRepository();
    private static MedicoRepository medicoRepo = new MedicoRepository();
    private static HospitalRepository hospitalRepo = new HospitalRepository();
    private static ConsultaRepository consultaRepo = new ConsultaRepository();
    private static LembreteRepository lembreteRepo = new LembreteRepository();
    private static EspecialidadeRepository espRepo = new EspecialidadeRepository();
    private static CanalComunicacaoRepository canalRepo = new CanalComunicacaoRepository();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1 - Cadastrar Paciente");
            System.out.println("2 - Listar Pacientes");
            System.out.println("3 - Agendar Consulta");
            System.out.println("4 - Listar Consultas");
            System.out.println("5 - Criar Lembrete");
            System.out.println("6 - Listar Lembretes de uma Consulta");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            opcao = sc.nextInt();
            sc.nextLine(); // limpa buffer

            try {
                switch (opcao) {
                    case 1 -> cadastrarPaciente(sc);
                    case 2 -> listarPacientes();
                    case 3 -> agendarConsulta(sc);
                    case 4 -> listarConsultas();
                    case 5 -> criarLembrete(sc);
                    case 6 -> listarLembretes(sc);
                    case 0 -> System.out.println("Saindo do sistema...");
                    default -> System.out.println("Opção inválida!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } while (opcao != 0);

        sc.close();
    }

    private static void cadastrarPaciente(Scanner sc) throws SQLException {
        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("CPF: ");
        String cpf = sc.nextLine();

        System.out.print("Data de nascimento (AAAA-MM-DD): ");
        LocalDate dataNasc = LocalDate.parse(sc.nextLine());

        System.out.print("Telefone (apenas números): ");
        String tel = sc.nextLine();

        Paciente p = new Paciente(0, nome, cpf, dataNasc, new Telefone(11, tel));
        pacienteRepo.create(p);
        System.out.println("Paciente cadastrado com sucesso!");
    }

    private static void listarPacientes() throws SQLException {
        List<Paciente> pacientes = pacienteRepo.findAll();
        System.out.println("\n--- Pacientes ---");
        for (Paciente p : pacientes) {
            System.out.println("ID: " + p.getId_paciente() + " | Nome: " + p.getNome() + " | CPF: " + p.getCpf());
        }
    }

    private static void agendarConsulta(Scanner sc) throws SQLException {
        System.out.print("ID do Paciente: ");
        long idPaciente = sc.nextLong();

        System.out.print("ID do Médico: ");
        long idMedico = sc.nextLong();

        System.out.print("ID do Hospital: ");
        long idHospital = sc.nextLong();

        sc.nextLine(); // limpar buffer

        LocalDateTime data = LocalDateTime.now().plusDays(1); // exemplo: agendada para amanhã
        Consulta c = new Consulta(0, data, null, 0, null, null, null, null);

        consultaRepo.create(c, idPaciente, idMedico, idHospital);
        System.out.println("Consulta agendada!");
    }

    private static void listarConsultas() throws SQLException {
        List<Consulta> consultas = consultaRepo.findAll();
        System.out.println("\n--- Consultas ---");
        for (Consulta c : consultas) {
            System.out.println("ID: " + c.getId_consulta() + " | Status: " + c.getStatus_consulta() +
                    " | Data: " + c.getData_hora_consulta());
        }
    }

    private static void criarLembrete(Scanner sc) throws SQLException {
        System.out.print("ID da Consulta: ");
        long idConsulta = sc.nextLong();

        System.out.print("ID do Canal de Comunicação: ");
        long idCanal = sc.nextLong();

        sc.nextLine(); // limpar buffer

        System.out.print("Mensagem: ");
        String mensagem = sc.nextLine();

        LocalDateTime dataLembrete = LocalDateTime.now().plusMinutes(5);
        Lembrete l = new Lembrete(mensagem, dataLembrete, null);

        lembreteRepo.create(l, idConsulta, idCanal);
        System.out.println("Lembrete criado com sucesso!");
    }

    private static void listarLembretes(Scanner sc) throws SQLException {
        System.out.print("ID da Consulta: ");
        long idConsulta = sc.nextLong();

        List<Lembrete> lembretes = lembreteRepo.findByConsulta(idConsulta);
        System.out.println("\n--- Lembretes ---");
        for (Lembrete l : lembretes) {
            System.out.println("Mensagem: " + l.getMensagem() +
                    " | Data envio: " + l.getDataEnvio());
        }
    }
}
