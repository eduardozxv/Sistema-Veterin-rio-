import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.StringConverter;
import javafx.scene.Node;

import java.util.Arrays;

public class Main extends Application {

    private int proximoCodigoConsulta = 1;
    private int proximoCodigoAnimal = 1;
    private int proximoCodigoVeterinario = 1;

    private boolean temaEscuro = true;
    private Scene cena;

    private final GerenciadorClinica gerenciador =
            new GerenciadorClinica(100);

    private final TableView<Animal> tabela = new TableView<>();
    private final TableView<Veterinario> tabelaVeterinarios =
            new TableView<>();
    private final TableView<Consulta> tabelaConsultas =
            new TableView<>();

    private Animal animalSelecionadoParaEdicao;
    private Veterinario veterinarioSelecionadoParaEdicao;

    private final Label totalAnimais = new Label("0");
    private final Label totalVeterinarios = new Label("0");
    private final Label totalConsultas = new Label("0");

    private BorderPane layout;
    private VBox menu;
    private VBox conteudo;
    private Label titulo;
    private Label subtitulo;
    private VBox cabecalho;
    private VBox cartoes;
    private VBox areaTabelaInicio;
    private Button botaoTema;

    @Override
    public void start(Stage stage) {

        layout = new BorderPane();
        layout.getStyleClass().add("app-root");

        // MENU LATERAL
        menu = new VBox(12);
        menu.setPadding(new Insets(30, 18, 25, 18));
        menu.setPrefWidth(225);
        menu.getStyleClass().add("menu-lateral");

        Label logo = new Label("VETCARE");
        logo.getStyleClass().add("logo");

        Label descricao = new Label("Gestão Veterinária");
        descricao.getStyleClass().add("descricao-menu");

        Button inicio = criarBotaoMenu("⌂   Início");
        Button animais = criarBotaoMenu("♧   Animais");
        Button veterinarios = criarBotaoMenu("✚   Veterinários");
        Button consultas = criarBotaoMenu("▣   Consultas");
        botaoTema = criarBotaoMenu("☼   Mudar tema");
        Button sair = criarBotaoMenu("↩   Sair");

        VBox identificacao = new VBox(5, logo, descricao);
        identificacao.setPadding(new Insets(0, 0, 25, 5));

        VBox espaco = new VBox();
        VBox.setVgrow(espaco, Priority.ALWAYS);

        menu.getChildren().addAll(
                identificacao,
                inicio,
                animais,
                veterinarios,
                consultas,
                botaoTema,
                espaco,
                sair
        );

        // CONTEÚDO PRINCIPAL
        conteudo = new VBox(25);
        conteudo.setPadding(new Insets(30));

        titulo = new Label("Visão geral");
        titulo.getStyleClass().add("titulo-pagina");

        subtitulo = new Label(
                "Bem-vindo ao painel de gerenciamento da clínica."
        );
        subtitulo.getStyleClass().add("subtitulo-pagina");

        cabecalho = new VBox(7, titulo, subtitulo);

        // CARTÕES
        VBox cardAnimais = criarCartao(
                "Animais cadastrados",
                totalAnimais,
                "Pacientes ativos"
        );

        VBox cardVeterinarios = criarCartao(
                "Veterinários",
                totalVeterinarios,
                "Profissionais ativos"
        );

        VBox cardConsultas = criarCartao(
                "Consultas agendadas",
                totalConsultas,
                "Consultas pendentes"
        );

        HBox cartoesLinha = new HBox(
                18, cardAnimais, cardVeterinarios, cardConsultas
        );

        HBox.setHgrow(cardAnimais, Priority.ALWAYS);
        HBox.setHgrow(cardVeterinarios, Priority.ALWAYS);
        HBox.setHgrow(cardConsultas, Priority.ALWAYS);

        cartoes = new VBox(cartoesLinha);

        // TABELA INICIAL
        configurarTabelaAnimais();

        Label tituloTabela = new Label("Animais cadastrados");
        tituloTabela.getStyleClass().add("titulo-secao");

        areaTabelaInicio = new VBox(15, tituloTabela, tabela);
        areaTabelaInicio.setPadding(new Insets(22));
        areaTabelaInicio.getStyleClass().add("painel");

        VBox.setVgrow(tabela, Priority.ALWAYS);
        VBox.setVgrow(areaTabelaInicio, Priority.ALWAYS);

        conteudo.getChildren().addAll(
                cabecalho,
                cartoes,
                areaTabelaInicio
        );

        layout.setLeft(menu);
        layout.setCenter(conteudo);

        // NAVEGAÇÃO
        inicio.setOnAction(event -> abrirInicio());

        animais.setOnAction(event ->
                abrirCadastroAnimais(
                        conteudo, titulo, subtitulo, cabecalho
                )
        );

        veterinarios.setOnAction(event ->
                abrirCadastroVeterinarios(
                        conteudo, titulo, subtitulo, cabecalho
                )
        );

        consultas.setOnAction(event ->
                abrirCadastroConsultas(
                        conteudo, titulo, subtitulo, cabecalho
                )
        );

        botaoTema.setOnAction(event -> alternarTema());

        sair.setOnAction(event -> stage.close());

        cena = new Scene(layout, 1100, 700);
        cena.getStylesheets().add(
                getClass().getResource("/vetcare.css").toExternalForm()
        );

        aplicarTema();

        stage.setTitle("VetCare | Sistema Veterinário");
        stage.setMinWidth(850);
        stage.setMinHeight(600);
        stage.setScene(cena);
        stage.show();

        atualizarTabelaAnimais();
        atualizarTabelaVeterinarios();
        atualizarDashboard();
    }

    // ==========================================
    // TEMA CLARO E ESCURO
    // ==========================================

    private void alternarTema() {
        temaEscuro = !temaEscuro;
        aplicarTema();
    }

    private void aplicarTema() {

        layout.getStyleClass().removeAll(
                "tema-escuro",
                "tema-claro"
        );

        if (temaEscuro) {
            layout.getStyleClass().add("tema-escuro");
            botaoTema.setText("☼   Mudar para tema claro");
        } else {
            layout.getStyleClass().add("tema-claro");
            botaoTema.setText("☾   Mudar para tema escuro");
        }
    }

    private void abrirInicio() {

        titulo.setText("Visão geral");
        subtitulo.setText(
                "Bem-vindo ao painel de gerenciamento da clínica."
        );

        conteudo.getChildren().setAll(
                cabecalho,
                cartoes,
                areaTabelaInicio
        );

        atualizarTabelaAnimais();
        atualizarDashboard();
    }

    // ==========================================
    // TABELA DE ANIMAIS
    // ==========================================

    private void configurarTabelaAnimais() {

        tabela.setPlaceholder(
                new Label("Nenhum animal cadastrado.")
        );

        tabela.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        TableColumn<Animal, Integer> codigo =
                new TableColumn<>("Código");
        codigo.setCellValueFactory(
                new PropertyValueFactory<>("codigo")
        );

        TableColumn<Animal, String> nome =
                new TableColumn<>("Nome");
        nome.setCellValueFactory(
                new PropertyValueFactory<>("nome")
        );

        TableColumn<Animal, String> especie =
                new TableColumn<>("Espécie");
        especie.setCellValueFactory(
                new PropertyValueFactory<>("especie")
        );

        TableColumn<Animal, String> raca =
                new TableColumn<>("Raça");
        raca.setCellValueFactory(
                new PropertyValueFactory<>("raca")
        );

        TableColumn<Animal, Integer> idade =
                new TableColumn<>("Idade");
        idade.setCellValueFactory(
                new PropertyValueFactory<>("idade")
        );

        TableColumn<Animal, String> tutor =
                new TableColumn<>("Tutor");
        tutor.setCellValueFactory(
                new PropertyValueFactory<>("nomeTutor")
        );

        tabela.getColumns().setAll(
                codigo, nome, especie, raca, idade, tutor
        );
    }

    // ==========================================
    // CADASTRO DE ANIMAIS
    // ==========================================

    private void abrirCadastroAnimais(
            VBox conteudo,
            Label titulo,
            Label subtitulo,
            VBox cabecalho
    ) {

        animalSelecionadoParaEdicao = null;

        titulo.setText("Cadastro de animais");
        subtitulo.setText(
                "Cadastre, pesquise e edite os animais da clínica."
        );

        TextField campoNome = new TextField();
        campoNome.setPromptText("Nome do animal");

        TextField campoEspecie = new TextField();
        campoEspecie.setPromptText("Ex.: Cachorro, Gato");

        TextField campoRaca = new TextField();
        campoRaca.setPromptText("Raça");

        TextField campoIdade = new TextField();
        campoIdade.setPromptText("Idade em anos");

        TextField campoTutor = new TextField();
        campoTutor.setPromptText("Nome do tutor");

        Label mensagem = criarMensagem();

        Button cadastrar = new Button("Cadastrar animal");
        Button editarAnimal = new Button("Editar animal");
        Button desativarAnimal = new Button("Desativar animal");
        Button limpar = new Button("Limpar campos");

        cadastrar.getStyleClass().add("botao-principal");

        GridPane formulario = criarFormulario();

        formulario.add(new Label("Nome:"), 0, 0);
        formulario.add(campoNome, 1, 0);
        formulario.add(new Label("Espécie:"), 0, 1);
        formulario.add(campoEspecie, 1, 1);
        formulario.add(new Label("Raça:"), 0, 2);
        formulario.add(campoRaca, 1, 2);
        formulario.add(new Label("Idade:"), 0, 3);
        formulario.add(campoIdade, 1, 3);
        formulario.add(new Label("Tutor:"), 0, 4);
        formulario.add(campoTutor, 1, 4);

        configurarColunasFormulario(formulario);

        VBox painelFormulario = criarPainel(
                "Dados do animal",
                formulario,
                new HBox(10, cadastrar, limpar),
                mensagem
        );

        Label tituloTabela = criarTituloSecao("Animais cadastrados");

        TextField campoPesquisa = new TextField();
        campoPesquisa.setPromptText(
                "Pesquisar por nome ou código..."
        );

        Button pesquisar = new Button("Pesquisar");
        Button mostrarTodos = new Button("Mostrar todos");

        HBox barraPesquisa = new HBox(
                10,
                campoPesquisa,
                pesquisar,
                mostrarTodos,
                editarAnimal,
                desativarAnimal
        );

        HBox.setHgrow(campoPesquisa, Priority.ALWAYS);

        VBox areaTabela = new VBox(
                15, tituloTabela, barraPesquisa, tabela
        );
        areaTabela.setPadding(new Insets(22));
        areaTabela.getStyleClass().add("painel");

        VBox.setVgrow(tabela, Priority.ALWAYS);
        VBox.setVgrow(areaTabela, Priority.ALWAYS);

        conteudo.getChildren().setAll(
                cabecalho, painelFormulario, areaTabela
        );

        // EDITAR ANIMAL
        editarAnimal.setOnAction(event -> {

            Animal selecionado =
                    tabela.getSelectionModel().getSelectedItem();

            if (selecionado == null) {
                mostrarMensagem(
                        mensagem,
                        "Selecione um animal na tabela.",
                        true
                );
                return;
            }

            animalSelecionadoParaEdicao = selecionado;

            campoNome.setText(selecionado.getNome());
            campoEspecie.setText(selecionado.getEspecie());
            campoRaca.setText(
                    selecionado.getRaca() == null
                            ? "" : selecionado.getRaca()
            );
            campoIdade.setText(
                    String.valueOf(selecionado.getIdade())
            );
            campoTutor.setText(selecionado.getNomeTutor());

            cadastrar.setText("Salvar alterações");

            mostrarMensagem(
                    mensagem,
                    "Editando: " + selecionado.getNome(),
                    false
            );
        });

        // DESATIVAR ANIMAL
        desativarAnimal.setOnAction(event -> {

            Animal selecionado =
                    tabela.getSelectionModel().getSelectedItem();

            if (selecionado == null) {
                mostrarMensagem(
                        mensagem,
                        "Selecione um animal para desativar.",
                        true
                );
                return;
            }

            if (confirmarDesativacao(selecionado.getNome())) {

                boolean sucesso = gerenciador.desativarAnimal(
                        selecionado.getCodigo()
                );

                if (sucesso) {

                    animalSelecionadoParaEdicao = null;
                    atualizarTabelaAnimais();

                    limparFormulario(
                            campoNome, campoEspecie, campoRaca,
                            campoIdade, campoTutor
                    );

                    cadastrar.setText("Cadastrar animal");

                    mostrarMensagem(
                            mensagem,
                            "Animal desativado com sucesso.",
                            false
                    );

                } else {
                    mostrarMensagem(
                            mensagem,
                            "Não foi possível desativar o animal.",
                            true
                    );
                }
            }
        });

        // CADASTRAR OU EDITAR ANIMAL
        cadastrar.setOnAction(event -> {

            String nome = campoNome.getText().trim();
            String especie = campoEspecie.getText().trim();
            String raca = campoRaca.getText().trim();
            String idadeTexto = campoIdade.getText().trim();
            String tutor = campoTutor.getText().trim();

            if (nome.isEmpty() || especie.isEmpty()
                    || idadeTexto.isEmpty() || tutor.isEmpty()) {

                mostrarMensagem(
                        mensagem,
                        "Preencha nome, espécie, idade e tutor.",
                        true
                );
                return;
            }

            int idade;

            try {
                idade = Integer.parseInt(idadeTexto);

                if (idade < 0) {
                    mostrarMensagem(
                            mensagem,
                            "A idade não pode ser negativa.",
                            true
                    );
                    return;
                }

            } catch (NumberFormatException erro) {
                mostrarMensagem(
                        mensagem,
                        "Digite uma idade usando números inteiros.",
                        true
                );
                return;
            }

            if (animalSelecionadoParaEdicao != null) {

                animalSelecionadoParaEdicao.atualizarDados(
                        nome,
                        especie,
                        raca.isEmpty() ? null : raca,
                        idade,
                        tutor
                );

                mostrarMensagem(
                        mensagem,
                        "Animal atualizado com sucesso!",
                        false
                );

            } else {

                Animal animal = new Animal(
                        proximoCodigoAnimal,
                        nome,
                        especie,
                        raca.isEmpty() ? null : raca,
                        idade,
                        tutor
                );

                if (gerenciador.cadastrarAnimal(animal)) {

                    proximoCodigoAnimal++;

                    mostrarMensagem(
                            mensagem,
                            "Animal cadastrado com sucesso!",
                            false
                    );

                } else {
                    mostrarMensagem(
                            mensagem,
                            "Não foi possível cadastrar. Verifique os dados.",
                            true
                    );
                    return;
                }
            }

            atualizarTabelaAnimais();

            limparFormulario(
                    campoNome, campoEspecie, campoRaca,
                    campoIdade, campoTutor
            );

            animalSelecionadoParaEdicao = null;
            cadastrar.setText("Cadastrar animal");
        });

        // LIMPAR CAMPOS
        limpar.setOnAction(event -> {

            limparFormulario(
                    campoNome, campoEspecie, campoRaca,
                    campoIdade, campoTutor
            );

            animalSelecionadoParaEdicao = null;
            cadastrar.setText("Cadastrar animal");
            mensagem.setText("");
        });

        // PESQUISAR ANIMAL
        pesquisar.setOnAction(event -> {

            String busca = campoPesquisa.getText()
                    .trim().toLowerCase();

            Animal[] animais = gerenciador.listarAnimaisAtivos();

            tabela.setItems(
                    FXCollections.observableArrayList(
                            Arrays.stream(animais)
                                    .filter(animal ->
                                            animal.getNome()
                                                    .toLowerCase()
                                                    .contains(busca)
                                                    || String.valueOf(
                                                    animal.getCodigo()
                                            ).contains(busca)
                                    )
                                    .toList()
                    )
            );
        });

        mostrarTodos.setOnAction(event -> {
            campoPesquisa.clear();
            atualizarTabelaAnimais();
        });
    }

    // ==========================================
    // TABELA DE VETERINÁRIOS
    // ==========================================

    private void configurarTabelaVeterinarios() {

        tabelaVeterinarios.setPlaceholder(
                new Label("Nenhum veterinário cadastrado.")
        );

        tabelaVeterinarios.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        TableColumn<Veterinario, Integer> codigo =
                new TableColumn<>("Código");
        codigo.setCellValueFactory(
                new PropertyValueFactory<>("codigo")
        );

        TableColumn<Veterinario, String> nome =
                new TableColumn<>("Nome");
        nome.setCellValueFactory(
                new PropertyValueFactory<>("nome")
        );

        TableColumn<Veterinario, String> especialidade =
                new TableColumn<>("Especialidade");
        especialidade.setCellValueFactory(
                new PropertyValueFactory<>("especialidade")
        );

        TableColumn<Veterinario, String> crmv =
                new TableColumn<>("CRMV");
        crmv.setCellValueFactory(
                new PropertyValueFactory<>("crmv")
        );

        tabelaVeterinarios.getColumns().setAll(
                codigo, nome, especialidade, crmv
        );
    }

    // ==========================================
    // CADASTRO DE VETERINÁRIOS
    // ==========================================

    private void abrirCadastroVeterinarios(
            VBox conteudo,
            Label titulo,
            Label subtitulo,
            VBox cabecalho
    ) {

        veterinarioSelecionadoParaEdicao = null;

        titulo.setText("Cadastro de veterinários");
        subtitulo.setText(
                "Cadastre, pesquise e gerencie os profissionais da clínica."
        );

        TextField campoNome = new TextField();
        campoNome.setPromptText("Nome do veterinário");

        TextField campoEspecialidade = new TextField();
        campoEspecialidade.setPromptText("Ex.: Clínica geral");

        TextField campoCrmv = new TextField();
        campoCrmv.setPromptText("Número do CRMV");

        Label mensagem = criarMensagem();

        Button cadastrar = new Button("Cadastrar veterinário");
        Button editar = new Button("Editar veterinário");
        Button desativar = new Button("Desativar veterinário");
        Button limpar = new Button("Limpar campos");

        cadastrar.getStyleClass().add("botao-principal");

        GridPane formulario = criarFormulario();

        formulario.add(new Label("Nome:"), 0, 0);
        formulario.add(campoNome, 1, 0);
        formulario.add(new Label("Especialidade:"), 0, 1);
        formulario.add(campoEspecialidade, 1, 1);
        formulario.add(new Label("CRMV:"), 0, 2);
        formulario.add(campoCrmv, 1, 2);

        configurarColunasFormulario(formulario);

        VBox painelFormulario = criarPainel(
                "Dados do veterinário",
                formulario,
                new HBox(10, cadastrar, limpar),
                mensagem
        );

        Label tituloTabela =
                criarTituloSecao("Veterinários cadastrados");

        TextField campoPesquisa = new TextField();
        campoPesquisa.setPromptText(
                "Pesquisar por nome ou CRMV..."
        );

        Button pesquisar = new Button("Pesquisar");
        Button mostrarTodos = new Button("Mostrar todos");

        HBox barraPesquisa = new HBox(
                10, campoPesquisa, pesquisar,
                mostrarTodos, editar, desativar
        );

        HBox.setHgrow(campoPesquisa, Priority.ALWAYS);

        VBox areaTabela = new VBox(
                15, tituloTabela, barraPesquisa, tabelaVeterinarios
        );
        areaTabela.setPadding(new Insets(22));
        areaTabela.getStyleClass().add("painel");

        VBox.setVgrow(tabelaVeterinarios, Priority.ALWAYS);
        VBox.setVgrow(areaTabela, Priority.ALWAYS);

        conteudo.getChildren().setAll(
                cabecalho, painelFormulario, areaTabela
        );

        // EDITAR VETERINÁRIO
        editar.setOnAction(event -> {

            Veterinario selecionado =
                    tabelaVeterinarios.getSelectionModel()
                            .getSelectedItem();

            if (selecionado == null) {
                mostrarMensagem(
                        mensagem,
                        "Selecione um veterinário na tabela.",
                        true
                );
                return;
            }

            veterinarioSelecionadoParaEdicao = selecionado;

            campoNome.setText(selecionado.getNome());
            campoEspecialidade.setText(
                    selecionado.getEspecialidade()
            );
            campoCrmv.setText(selecionado.getCrmv());

            cadastrar.setText("Salvar alterações");

            mostrarMensagem(
                    mensagem,
                    "Editando: " + selecionado.getNome(),
                    false
            );
        });

        // DESATIVAR VETERINÁRIO
        desativar.setOnAction(event -> {

            Veterinario selecionado =
                    tabelaVeterinarios.getSelectionModel()
                            .getSelectedItem();

            if (selecionado == null) {
                mostrarMensagem(
                        mensagem,
                        "Selecione um veterinário para desativar.",
                        true
                );
                return;
            }

            if (confirmarDesativacao(selecionado.getNome())) {

                boolean sucesso =
                        gerenciador.desativarVeterinario(
                                selecionado.getCodigo()
                        );

                if (sucesso) {

                    veterinarioSelecionadoParaEdicao = null;
                    atualizarTabelaVeterinarios();

                    limparFormularioVeterinario(
                            campoNome, campoEspecialidade, campoCrmv
                    );

                    cadastrar.setText("Cadastrar veterinário");

                    mostrarMensagem(
                            mensagem,
                            "Veterinário desativado com sucesso.",
                            false
                    );

                } else {
                    mostrarMensagem(
                            mensagem,
                            "Não foi possível desativar o veterinário.",
                            true
                    );
                }
            }
        });

        // CADASTRAR OU EDITAR VETERINÁRIO
        cadastrar.setOnAction(event -> {

            String nome = campoNome.getText().trim();
            String especialidade =
                    campoEspecialidade.getText().trim();
            String crmv = campoCrmv.getText().trim();

            if (nome.isEmpty() || especialidade.isEmpty()
                    || crmv.isEmpty()) {

                mostrarMensagem(
                        mensagem,
                        "Preencha nome, especialidade e CRMV.",
                        true
                );
                return;
            }

            if (veterinarioSelecionadoParaEdicao != null) {

                veterinarioSelecionadoParaEdicao.atualizarDados(
                        nome, especialidade, crmv
                );

                mostrarMensagem(
                        mensagem,
                        "Veterinário atualizado com sucesso!",
                        false
                );

            } else {

                Veterinario veterinario = new Veterinario(
                        proximoCodigoVeterinario,
                        nome,
                        especialidade,
                        crmv
                );

                if (gerenciador.cadastrarVeterinario(veterinario)) {

                    proximoCodigoVeterinario++;

                    mostrarMensagem(
                            mensagem,
                            "Veterinário cadastrado com sucesso!",
                            false
                    );

                } else {
                    mostrarMensagem(
                            mensagem,
                            "Não foi possível cadastrar. Verifique os dados.",
                            true
                    );
                    return;
                }
            }

            atualizarTabelaVeterinarios();

            limparFormularioVeterinario(
                    campoNome, campoEspecialidade, campoCrmv
            );

            veterinarioSelecionadoParaEdicao = null;
            cadastrar.setText("Cadastrar veterinário");
        });

        // LIMPAR CAMPOS
        limpar.setOnAction(event -> {

            limparFormularioVeterinario(
                    campoNome, campoEspecialidade, campoCrmv
            );

            veterinarioSelecionadoParaEdicao = null;
            cadastrar.setText("Cadastrar veterinário");
            mensagem.setText("");
        });

        // PESQUISAR VETERINÁRIO
        pesquisar.setOnAction(event -> {

            String busca = campoPesquisa.getText()
                    .trim().toLowerCase();

            Veterinario[] veterinarios =
                    gerenciador.listarVeterinariosAtivos();

            tabelaVeterinarios.setItems(
                    FXCollections.observableArrayList(
                            Arrays.stream(veterinarios)
                                    .filter(veterinario ->
                                            veterinario.getNome()
                                                    .toLowerCase()
                                                    .contains(busca)
                                                    || veterinario.getCrmv()
                                                    .toLowerCase()
                                                    .contains(busca)
                                    )
                                    .toList()
                    )
            );
        });

        // MOSTRAR TODOS
        mostrarTodos.setOnAction(event -> {
            campoPesquisa.clear();
            atualizarTabelaVeterinarios();
        });
    }

    // ==========================================
    // CADASTRO DE CONSULTAS
    // ==========================================

    private void abrirCadastroConsultas(
            VBox conteudo,
            Label titulo,
            Label subtitulo,
            VBox cabecalho
    ) {

        titulo.setText("Gerenciamento de consultas");
        subtitulo.setText(
                "Agende, consulte, cancele e finalize atendimentos."
        );

        ComboBox<Animal> comboAnimal = new ComboBox<>();
        comboAnimal.getItems().setAll(
                gerenciador.listarAnimaisAtivos()
        );
        comboAnimal.setPromptText("Selecione o animal");
        comboAnimal.setMaxWidth(Double.MAX_VALUE);

        comboAnimal.setConverter(new StringConverter<>() {
            @Override
            public String toString(Animal animal) {
                return animal == null ? "" :
                        animal.getCodigo() + " - " + animal.getNome();
            }

            @Override
            public Animal fromString(String texto) {
                return null;
            }
        });

        ComboBox<Veterinario> comboVeterinario =
                new ComboBox<>();

        comboVeterinario.getItems().setAll(
                gerenciador.listarVeterinariosAtivos()
        );

        comboVeterinario.setPromptText("Selecione o veterinário");
        comboVeterinario.setMaxWidth(Double.MAX_VALUE);

        comboVeterinario.setConverter(new StringConverter<>() {
            @Override
            public String toString(Veterinario veterinario) {
                return veterinario == null ? "" :
                        veterinario.getCodigo() + " - "
                                + veterinario.getNome();
            }

            @Override
            public Veterinario fromString(String texto) {
                return null;
            }
        });

        ComboBox<String> comboDia = new ComboBox<>();
        comboDia.getItems().addAll(
                "Segunda-feira",
                "Terça-feira",
                "Quarta-feira",
                "Quinta-feira",
                "Sexta-feira"
        );
        comboDia.setPromptText("Selecione o dia");
        comboDia.setMaxWidth(Double.MAX_VALUE);

        ComboBox<String> comboHorario = new ComboBox<>();
        comboHorario.getItems().addAll(
                "08:00", "09:00", "10:00", "11:00",
                "14:00", "15:00", "16:00", "17:00"
        );
        comboHorario.setPromptText("Selecione o horário");
        comboHorario.setMaxWidth(Double.MAX_VALUE);

        TextField campoMotivo = new TextField();
        campoMotivo.setPromptText("Motivo da consulta");

        Label mensagem = criarMensagem();

        Button cadastrar = new Button("Agendar consulta");
        Button cancelar = new Button("Cancelar consulta");
        Button realizar = new Button("Finalizar consulta");
        Button limpar = new Button("Limpar campos");

        cadastrar.getStyleClass().add("botao-principal");

        GridPane formulario = criarFormulario();

        formulario.add(new Label("Animal:"), 0, 0);
        formulario.add(comboAnimal, 1, 0);
        formulario.add(new Label("Veterinário:"), 0, 1);
        formulario.add(comboVeterinario, 1, 1);
        formulario.add(new Label("Dia:"), 0, 2);
        formulario.add(comboDia, 1, 2);
        formulario.add(new Label("Horário:"), 0, 3);
        formulario.add(comboHorario, 1, 3);
        formulario.add(new Label("Motivo:"), 0, 4);
        formulario.add(campoMotivo, 1, 4);

        configurarColunasFormulario(formulario);

        VBox painelFormulario = criarPainel(
                "Dados do agendamento",
                formulario,
                new HBox(10, cadastrar, limpar),
                mensagem
        );

        configurarTabelaConsultas();

        Label tituloTabela =
                criarTituloSecao("Consultas registradas");

        HBox botoesTabela = new HBox(
                10, cancelar, realizar
        );

        VBox areaTabela = new VBox(
                15,
                tituloTabela,
                tabelaConsultas,
                botoesTabela
        );

        areaTabela.setPadding(new Insets(22));
        areaTabela.getStyleClass().add("painel");

        VBox.setVgrow(tabelaConsultas, Priority.ALWAYS);
        VBox.setVgrow(areaTabela, Priority.ALWAYS);

        conteudo.getChildren().setAll(
                cabecalho,
                painelFormulario,
                areaTabela
        );

        atualizarTabelaConsultas();
        atualizarDashboard();

        // AGENDAR CONSULTA
        cadastrar.setOnAction(event -> {

            Animal animal = comboAnimal.getValue();
            Veterinario veterinario = comboVeterinario.getValue();

            int dia = comboDia.getSelectionModel().getSelectedIndex();
            int horario = comboHorario.getSelectionModel()
                    .getSelectedIndex();

            String motivo = campoMotivo.getText().trim();

            if (animal == null || veterinario == null
                    || dia < 0 || horario < 0 || motivo.isEmpty()) {

                mostrarMensagem(
                        mensagem,
                        "Preencha todos os campos do agendamento.",
                        true
                );
                return;
            }

            Consulta consulta = new Consulta(
                    proximoCodigoConsulta,
                    animal.getCodigo(),
                    veterinario.getCodigo(),
                    dia,
                    horario,
                    motivo
            );

            if (gerenciador.agendarConsulta(consulta)) {

                proximoCodigoConsulta++;

                atualizarTabelaConsultas();
                atualizarDashboard();

                mostrarMensagem(
                        mensagem,
                        "Consulta agendada com sucesso!",
                        false
                );

                comboAnimal.setValue(null);
                comboVeterinario.setValue(null);
                comboDia.setValue(null);
                comboHorario.setValue(null);
                campoMotivo.clear();

            } else {

                mostrarMensagem(
                        mensagem,
                        "Não foi possível agendar. Verifique se o animal " +
                                "ou veterinário já possui consulta nesse horário.",
                        true
                );
            }
        });

        // CANCELAR CONSULTA
        cancelar.setOnAction(event -> {

            Consulta selecionada =
                    tabelaConsultas.getSelectionModel()
                            .getSelectedItem();

            if (selecionada == null) {
                mostrarMensagem(
                        mensagem,
                        "Selecione uma consulta na tabela.",
                        true
                );
                return;
            }

            if (gerenciador.cancelarConsulta(
                    selecionada.getCodigo()
            )) {

                atualizarTabelaConsultas();
                atualizarDashboard();

                mostrarMensagem(
                        mensagem,
                        "Consulta cancelada com sucesso.",
                        false
                );

            } else {
                mostrarMensagem(
                        mensagem,
                        "Não foi possível cancelar essa consulta.",
                        true
                );
            }
        });

        // FINALIZAR CONSULTA
        realizar.setOnAction(event -> {

            Consulta selecionada =
                    tabelaConsultas.getSelectionModel()
                            .getSelectedItem();

            if (selecionada == null) {
                mostrarMensagem(
                        mensagem,
                        "Selecione uma consulta na tabela.",
                        true
                );
                return;
            }

            if (gerenciador.realizarConsulta(
                    selecionada.getCodigo()
            )) {

                atualizarTabelaConsultas();
                atualizarDashboard();

                mostrarMensagem(
                        mensagem,
                        "Consulta finalizada com sucesso.",
                        false
                );

            } else {
                mostrarMensagem(
                        mensagem,
                        "Não foi possível finalizar essa consulta.",
                        true
                );
            }
        });

        // LIMPAR CAMPOS
        limpar.setOnAction(event -> {

            comboAnimal.setValue(null);
            comboVeterinario.setValue(null);
            comboDia.setValue(null);
            comboHorario.setValue(null);
            campoMotivo.clear();

            mensagem.setText("");
        });
    }

    // ==========================================
    // TABELA DE CONSULTAS
    // ==========================================

    private void configurarTabelaConsultas() {

        tabelaConsultas.setPlaceholder(
                new Label("Nenhuma consulta cadastrada.")
        );

        tabelaConsultas.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        TableColumn<Consulta, String> codigo =
                new TableColumn<>("Código");

        codigo.setCellValueFactory(dado ->
                new SimpleStringProperty(
                        String.valueOf(dado.getValue().getCodigo())
                )
        );

        TableColumn<Consulta, String> animal =
                new TableColumn<>("Animal");

        animal.setCellValueFactory(dado -> {

            Animal encontrado = gerenciador.buscarAnimal(
                    dado.getValue().getCodigAnimal()
            );

            return new SimpleStringProperty(
                    encontrado == null ? "Não encontrado"
                            : encontrado.getNome()
            );
        });

        TableColumn<Consulta, String> veterinario =
                new TableColumn<>("Veterinário");

        veterinario.setCellValueFactory(dado -> {

            Veterinario encontrado = gerenciador.buscarVeterinario(
                    dado.getValue().getCodigoVeteriano()
            );

            return new SimpleStringProperty(
                    encontrado == null ? "Não encontrado"
                            : encontrado.getNome()
            );
        });

        TableColumn<Consulta, String> dia =
                new TableColumn<>("Dia");

        dia.setCellValueFactory(dado ->
                new SimpleStringProperty(
                        Consulta.nomeDia(
                                dado.getValue().getDiaSemana()
                        )
                )
        );

        TableColumn<Consulta, String> horario =
                new TableColumn<>("Horário");

        horario.setCellValueFactory(dado -> {

            int indice = dado.getValue().getHorario();

            String[] horarios = {
                    "08:00", "09:00", "10:00", "11:00",
                    "14:00", "15:00", "16:00", "17:00"
            };

            return new SimpleStringProperty(horarios[indice]);
        });

        TableColumn<Consulta, String> motivo =
                new TableColumn<>("Motivo");

        motivo.setCellValueFactory(dado ->
                new SimpleStringProperty(
                        dado.getValue().getMotivo()
                )
        );

        TableColumn<Consulta, String> situacao =
                new TableColumn<>("Situação");

        situacao.setCellValueFactory(dado -> {

            Consulta consulta = dado.getValue();
            String status;

            if (consulta.isAtiva()) {
                status = "Agendada";
            } else if (consulta.isRealizada()) {
                status = "Realizada";
            } else {
                status = "Cancelada";
            }

            return new SimpleStringProperty(status);
        });

        tabelaConsultas.getColumns().setAll(
                codigo,
                animal,
                veterinario,
                dia,
                horario,
                motivo,
                situacao
        );
    }

    // ==========================================
    // MÉTODOS AUXILIARES
    // ==========================================

    private GridPane criarFormulario() {

        GridPane formulario = new GridPane();
        formulario.setHgap(12);
        formulario.setVgap(12);
        return formulario;
    }

    private VBox criarPainel(String tituloPainel, Node... elementos) {

        Label titulo = criarTituloSecao(tituloPainel);

        VBox painel = new VBox();
        painel.setSpacing(18);
        painel.setPadding(new Insets(22));
        painel.getStyleClass().add("painel");

        painel.getChildren().add(titulo);
        painel.getChildren().addAll(elementos);

        return painel;
    }

    private Label criarTituloSecao(String texto) {

        Label label = new Label(texto);
        label.getStyleClass().add("titulo-secao");
        return label;
    }

    private Label criarMensagem() {

        Label mensagem = new Label();
        mensagem.setWrapText(true);
        mensagem.getStyleClass().add("mensagem");
        return mensagem;
    }

    private void atualizarTabelaAnimais() {

        Animal[] animais = gerenciador.listarAnimaisAtivos();

        tabela.getItems().clear();
        tabela.getItems().addAll(animais);
        tabela.refresh();

        totalAnimais.setText(String.valueOf(animais.length));

        atualizarDashboard();
    }

    private void atualizarTabelaVeterinarios() {

        configurarTabelaVeterinarios();

        Veterinario[] veterinarios =
                gerenciador.listarVeterinariosAtivos();

        tabelaVeterinarios.setItems(
                FXCollections.observableArrayList(veterinarios)
        );

        totalVeterinarios.setText(
                String.valueOf(veterinarios.length)
        );

        atualizarDashboard();
    }

    private void atualizarTabelaConsultas() {

        Consulta[] consultas = gerenciador.listarConsultas();

        tabelaConsultas.setItems(
                FXCollections.observableArrayList(consultas)
        );
    }

    private void atualizarDashboard() {

        totalAnimais.setText(
                String.valueOf(gerenciador.getQuantidadeAnimais())
        );

        totalVeterinarios.setText(
                String.valueOf(gerenciador.getQuantidadeVeterinarios())
        );

        totalConsultas.setText(
                String.valueOf(
                        gerenciador.getQuantidadeConsultasAgendadas()
                )
        );
    }

    private void limparFormulario(
            TextField nome,
            TextField especie,
            TextField raca,
            TextField idade,
            TextField tutor
    ) {
        nome.clear();
        especie.clear();
        raca.clear();
        idade.clear();
        tutor.clear();
    }

    private void limparFormularioVeterinario(
            TextField nome,
            TextField especialidade,
            TextField crmv
    ) {
        nome.clear();
        especialidade.clear();
        crmv.clear();
    }

    private void configurarColunasFormulario(GridPane formulario) {

        ColumnConstraints colunaRotulo = new ColumnConstraints();
        colunaRotulo.setMinWidth(90);

        ColumnConstraints colunaCampo = new ColumnConstraints();
        colunaCampo.setHgrow(Priority.ALWAYS);
        colunaCampo.setFillWidth(true);

        formulario.getColumnConstraints().setAll(
                colunaRotulo, colunaCampo
        );
    }

    private boolean confirmarDesativacao(String nome) {

        Alert confirmacao = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        confirmacao.setTitle("Confirmar desativação");
        confirmacao.setHeaderText("Desativar " + nome + "?");
        confirmacao.setContentText(
                "O registro deixará de aparecer na lista de ativos."
        );

        return confirmacao.showAndWait()
                .orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void mostrarMensagem(
            Label mensagem,
            String textoMensagem,
            boolean erro
    ) {

        mensagem.getStyleClass().removeAll(
                "mensagem-erro",
                "mensagem-sucesso"
        );

        mensagem.getStyleClass().add(
                erro ? "mensagem-erro" : "mensagem-sucesso"
        );

        mensagem.setText(textoMensagem);
    }

    private Button criarBotaoMenu(String nome) {

        Button botao = new Button(nome);
        botao.setMaxWidth(Double.MAX_VALUE);
        botao.setAlignment(Pos.CENTER_LEFT);
        botao.setPadding(new Insets(13, 12, 13, 12));
        botao.getStyleClass().add("botao-menu");

        return botao;
    }

    private VBox criarCartao(
            String titulo,
            Label valor,
            String descricao
    ) {

        Label labelTitulo = new Label(titulo);
        labelTitulo.getStyleClass().add("cartao-titulo");

        valor.getStyleClass().add("cartao-valor");

        Label labelDescricao = new Label(descricao);
        labelDescricao.getStyleClass().add("cartao-descricao");

        VBox cartao = new VBox(
                12, labelTitulo, valor, labelDescricao
        );

        cartao.setPadding(new Insets(22));
        cartao.setMinWidth(0);
        cartao.getStyleClass().add("cartao");

        return cartao;
    }

    private void estilizarBotaoPrincipal(Button botao) {
        botao.getStyleClass().add("botao-principal");
    }

    public static void main(String[] args) {
        launch(args);
    }
}