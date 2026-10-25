🐾 Sistema de Gerenciamento de Clínica Veterinária

Sistema desenvolvido em Java como projeto acadêmico do curso de Engenharia de Software, com o objetivo de aplicar conceitos de Programação Orientada a Objetos (POO) no gerenciamento de uma clínica veterinária.

A aplicação permite cadastrar animais e veterinários, gerenciar consultas e controlar a agenda de atendimentos, utilizando validações para garantir a consistência das operações.

🚀 Funcionalidades

Cadastro de animais: registro de informações como nome, espécie, raça, idade e tutor.

Cadastro de veterinários: gerenciamento de dados, especialidades e CRMV dos profissionais.

Agendamento de consultas: associação entre animais e veterinários, com definição de dia, horário e motivo.

Controle de consultas: possibilidade de agendar, cancelar e registrar consultas como realizadas.

Validação de horários: identificação de conflitos na agenda de veterinários e animais.

Gerenciamento de cadastros: busca por código e desativação de animais e veterinários.

Consulta de atendimentos: visualização de todas as consultas ou filtragem por animal.

Visualização da agenda: exibição dos horários ocupados dos veterinários por dia da semana.

🛠️ Tecnologias e conceitos

Linguagem: Java

Paradigma: Programação Orientada a Objetos (POO)

Conceitos aplicados: classes, objetos, encapsulamento, construtores, métodos e sobrecarga de construtores.

Estruturas de dados: arrays e matrizes.

Lógica de programação: estruturas condicionais, laços de repetição, validação de dados e implementação de regras de negócio.

Entrada de dados: classe Scanner.

📂 Organização do projeto

O sistema está organizado nas seguintes classes:

Classe

Responsabilidade

Animal.java

Representa os animais cadastrados na clínica.

Veterinario.java

Representa os veterinários e seus dados profissionais.

Consulta.java

Gerencia os dados e a situação das consultas.

GerenciadorClinica.java

Centraliza os cadastros, agendamentos, buscas e regras de negócio.

Main.java

Implementa o menu interativo e a interação com o usuário.
