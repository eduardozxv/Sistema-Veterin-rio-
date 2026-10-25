import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GerenciadorClinica gerenciador = new GerenciadorClinica(100);
        int opcao;

        do {
            System.out.println("\n==================== MENU ====================");
            System.out.println("1  - Cadastrar animal");
            System.out.println("2  - Cadastrar veterinário");
            System.out.println("3  - Agendar consulta");
            System.out.println("4  - Exibir todos os animais");
            System.out.println("5  - Exibir todos os veterinários");
            System.out.println("6  - Exibir todas as consultas");
            System.out.println("7  - Exibir consultas de um animal");
            System.out.println("8  - Buscar animal por código");
            System.out.println("9  - Buscar veterinário por código");
            System.out.println("10 - Buscar consulta por código");
            System.out.println("11 - Cancelar consulta");
            System.out.println("12 - Realizar consulta");
            System.out.println("13 - Desativar animal");
            System.out.println("14 - Desativar veterinário");
            System.out.println("15 - Exibir agenda de um dia");
            System.out.println("0  - Sair");
            System.out.print("Opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1: {
                    System.out.print("Código do animal: ");
                    int codigo = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Espécie: ");
                    String especie = scanner.nextLine();
                    System.out.print("Nome do tutor: ");
                    String nomeTutor = scanner.nextLine();
                    System.out.print("Deseja informar raça e idade? (1-Sim / 0-Não): ");
                    int detalhado = scanner.nextInt();
                    scanner.nextLine();
                    Animal animal;
                    if (detalhado == 1) {
                        System.out.print("Raça: ");
                        String raca = scanner.nextLine();
                        System.out.print("Idade: ");
                        int idade = scanner.nextInt();
                        scanner.nextLine();
                        animal = new Animal(codigo, nome, especie, raca, idade, nomeTutor);
                    } else {
                        animal = new Animal(codigo, nome, especie, nomeTutor);
                    }
                    if (gerenciador.cadastrarAnimal(animal)) {
                        System.out.println("Animal cadastrado com sucesso.");
                    } else {
                        System.out.println("Cadastro recusado. Verifique os dados, o código e a capacidade.");
                    }
                    break;
                }
                case 2: {
                    System.out.print("Código do veterinário: ");
                    int codigo = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Especialidade: ");
                    String especialidade = scanner.nextLine();
                    System.out.print("CRMV: ");
                    String crmv = scanner.nextLine();
                    Veterinario veterinario = new Veterinario(codigo, nome, especialidade, crmv);
                    if (gerenciador.cadastrarVeterinario(veterinario)) {
                        System.out.println("Veterinário cadastrado com sucesso.");
                    } else {
                        System.out.println("Cadastro recusado. Verifique os dados, o código e a capacidade.");
                    }
                    break;
                }
                case 3: {
                    System.out.print("Código da consulta: ");
                    int codigo = scanner.nextInt();
                    System.out.print("Código do animal: ");
                    int codigoAnimal = scanner.nextInt();
                    System.out.print("Código do veterinário: ");
                    int codigoVeterinario = scanner.nextInt();
                    System.out.print("Dia (0-Segunda a 4-Sexta): ");
                    int dia = scanner.nextInt();
                    System.out.print("Horário (0-08h, 1-09h, 2-10h, 3-11h, 4-14h, 5-15h, 6-16h, 7-17h): ");
                    int horario = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Motivo: ");
                    String motivo = scanner.nextLine();
                    Consulta consulta = new Consulta(codigo, codigoAnimal, codigoVeterinario, dia, horario, motivo);
                    if (gerenciador.agendarConsulta(consulta)) {
                        System.out.println("Consulta agendada com sucesso.");
                    } else {
                        System.out.println("Agendamento recusado. Verifique os dados, os cadastros e os conflitos de horário.");
                    }
                    break;
                }
                case 4:
                    gerenciador.exibirAnimais();
                    break;
                case 5:
                    gerenciador.exibirVeterinarios();
                    break;
                case 6:
                    gerenciador.exibirConsultas();
                    break;
                case 7:
                    System.out.print("Código do animal: ");
                    int codigoAnimal = scanner.nextInt();
                    scanner.nextLine();
                    gerenciador.exibirConsultas(codigoAnimal);
                    break;
                case 8: {
                    System.out.print("Código do animal: ");
                    int codigo = scanner.nextInt();
                    scanner.nextLine();
                    Animal animal = gerenciador.buscarAnimal(codigo);
                    if (animal != null) {
                        animal.exibirDados();
                    } else {
                        System.out.println("Animal não encontrado.");
                    }
                    break;
                }
                case 9: {
                    System.out.print("Código do veterinário: ");
                    int codigo = scanner.nextInt();
                    scanner.nextLine();
                    Veterinario veterinario = gerenciador.buscarVeterinario(codigo);
                    if (veterinario != null) {
                        veterinario.exibirDados();
                    } else {
                        System.out.println("Veterinário não encontrado.");
                    }
                    break;
                }
                case 10: {
                    System.out.print("Código da consulta: ");
                    int codigo = scanner.nextInt();
                    scanner.nextLine();
                    Consulta consulta = gerenciador.buscarConsulta(codigo);
                    if (consulta != null) {
                        consulta.exibirDados();
                    } else {
                        System.out.println("Consulta não encontrada.");
                    }
                    break;
                }
                case 11: {
                    System.out.print("Código da consulta: ");
                    int codigo = scanner.nextInt();
                    scanner.nextLine();
                    if (gerenciador.cancelarConsulta(codigo)) {
                        System.out.println("Consulta cancelada com sucesso.");
                    } else {
                        System.out.println("Cancelamento recusado. Verifique se a consulta existe e está agendada.");
                    }
                    break;
                }
                case 12: {
                    System.out.print("Código da consulta: ");
                    int codigo = scanner.nextInt();
                    scanner.nextLine();
                    if (gerenciador.realizarConsulta(codigo)) {
                        System.out.println("Consulta registrada como realizada.");
                    } else {
                        System.out.println("Operação recusada. Verifique se a consulta existe e está agendada.");
                    }
                    break;
                }
                case 13: {
                    System.out.print("Código do animal: ");
                    int codigo = scanner.nextInt();
                    scanner.nextLine();
                    if (gerenciador.desativarAnimal(codigo)) {
                        System.out.println("Animal desativado e consultas ativas canceladas.");
                    } else {
                        System.out.println("Operação recusada. Verifique se o animal existe e está ativo.");
                    }
                    break;
                }
                case 14: {
                    System.out.print("Código do veterinário: ");
                    int codigo = scanner.nextInt();
                    scanner.nextLine();
                    if (gerenciador.desativarVeterinario(codigo)) {
                        System.out.println("Veterinário desativado e consultas ativas canceladas.");
                    } else {
                        System.out.println("Operação recusada. Verifique se o veterinário existe e está ativo.");
                    }
                    break;
                }
                case 15: {
                    System.out.print("Dia (0-Segunda a 4-Sexta): ");
                    int dia = scanner.nextInt();
                    scanner.nextLine();
                    gerenciador.exibirAgenda(dia);
                    break;
                }
                case 0:
                    System.out.println("Sistema encerrado.");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);

        scanner.close();
    }
}