
public class GerenciadorClinica {

    private Animal[] animais;
    private Veterinario[] veterinarios;
    private Consulta[] consultas;

    private int[][] agendaVeterinarios;

    private int quantidadeAnimais;
    private int quantidadeVeterinarios;
    private int quantidadeConsultas;

    public GerenciadorClinica(int capacidade) {
        animais = new Animal[capacidade];
        veterinarios = new Veterinario[capacidade];
        consultas = new Consulta[capacidade];

        agendaVeterinarios = new int[capacidade][8];

        quantidadeAnimais = 0;
        quantidadeVeterinarios = 0;
        quantidadeConsultas = 0;
    }

    // ==========================================
    // MÉTODOS DE ANIMAIS
    // ==========================================

    public boolean cadastrarAnimal(Animal animal) {
        if (animal == null || quantidadeAnimais >= animais.length) {
            return false;
        }

        animais[quantidadeAnimais] = animal;
        quantidadeAnimais++;

        return true;
    }

    public Animal buscarAnimal(int codigo) {
        for (int i = 0; i < quantidadeAnimais; i++) {
            if (animais[i].getCodigo() == codigo) {
                return animais[i];
            }
        }

        return null;
    }

    public boolean desativarAnimal(int codigo) {
        Animal animal = buscarAnimal(codigo);

        if (animal == null || !animal.isAtivo()) {
            return false;
        }

        animal.desativar();

        for (int i = 0; i < quantidadeConsultas; i++) {
            if (consultas[i].getCodigAnimal() == codigo
                    && consultas[i].isAtiva()) {
                cancelarConsulta(consultas[i].getCodigo());
            }
        }

        return true;
    }

    public void exibirAnimais() {
        for (int i = 0; i < quantidadeAnimais; i++) {
            animais[i].exibirDados();
        }
    }

    public Animal[] listarAnimais() {
        Animal[] resultado = new Animal[quantidadeAnimais];

        for (int i = 0; i < quantidadeAnimais; i++) {
            resultado[i] = animais[i];
        }

        return resultado;
    }

    public Animal[] listarAnimaisAtivos() {
        int total = 0;

        for (int i = 0; i < quantidadeAnimais; i++) {
            if (animais[i].isAtivo()) {
                total++;
            }
        }

        Animal[] resultado = new Animal[total];
        int indice = 0;

        for (int i = 0; i < quantidadeAnimais; i++) {
            if (animais[i].isAtivo()) {
                resultado[indice] = animais[i];
                indice++;
            }
        }

        return resultado;
    }

    // ==========================================
    // MÉTODOS DE VETERINÁRIOS
    // ==========================================

    public boolean cadastrarVeterinario(Veterinario veterinario) {
        if (veterinario == null
                || quantidadeVeterinarios >= veterinarios.length) {
            return false;
        }

        veterinarios[quantidadeVeterinarios] = veterinario;
        quantidadeVeterinarios++;

        return true;
    }

    public Veterinario buscarVeterinario(int codigo) {
        for (int i = 0; i < quantidadeVeterinarios; i++) {
            if (veterinarios[i].getCodigo() == codigo) {
                return veterinarios[i];
            }
        }

        return null;
    }

    public boolean desativarVeterinario(int codigo) {
        Veterinario veterinario = buscarVeterinario(codigo);

        if (veterinario == null || !veterinario.isAtivo()) {
            return false;
        }

        veterinario.desativar();

        for (int i = 0; i < quantidadeConsultas; i++) {
            if (consultas[i].getCodigoVeteriano() == codigo
                    && consultas[i].isAtiva()) {
                cancelarConsulta(consultas[i].getCodigo());
            }
        }

        return true;
    }

    public void exibirVeterinarios() {
        for (int i = 0; i < quantidadeVeterinarios; i++) {
            veterinarios[i].exibirDados();
        }
    }

    public Veterinario[] listarVeterinarios() {
        Veterinario[] resultado = new Veterinario[quantidadeVeterinarios];

        for (int i = 0; i < quantidadeVeterinarios; i++) {
            resultado[i] = veterinarios[i];
        }

        return resultado;
    }

    public Veterinario[] listarVeterinariosAtivos() {
        int total = 0;

        for (int i = 0; i < quantidadeVeterinarios; i++) {
            if (veterinarios[i].isAtivo()) {
                total++;
            }
        }

        Veterinario[] resultado = new Veterinario[total];
        int indice = 0;

        for (int i = 0; i < quantidadeVeterinarios; i++) {
            if (veterinarios[i].isAtivo()) {
                resultado[indice] = veterinarios[i];
                indice++;
            }
        }

        return resultado;
    }

    // ==========================================
    // MÉTODOS DE CONSULTAS
    // ==========================================

    public boolean agendarConsulta(Consulta consulta) {
        if (consulta == null || quantidadeConsultas >= consultas.length) {
            return false;
        }

        Animal animal = buscarAnimal(consulta.getCodigAnimal());
        Veterinario veterinario =
                buscarVeterinario(consulta.getCodigoVeteriano());

        if (animal == null || veterinario == null) {
            return false;
        }

        if (!animal.isAtivo() || !veterinario.isAtivo()) {
            return false;
        }

        int dia = consulta.getDiaSemana();
        int horario = consulta.getHorario();

        if (dia < 0 || dia > 4 || horario < 0 || horario > 7) {
            return false;
        }

        if (veterinarioOcupado(
                consulta.getCodigoVeteriano(), dia, horario)) {
            return false;
        }

        if (animalOcupado(
                consulta.getCodigAnimal(), dia, horario)) {
            return false;
        }

        consultas[quantidadeConsultas] = consulta;
        quantidadeConsultas++;

        int indice = indiceDoVeterinario(
                consulta.getCodigoVeteriano());

        agendaVeterinarios[indice][dia] = 1;

        return true;
    }

    public Consulta[] listarConsultas() {
        Consulta[] resultado = new Consulta[quantidadeConsultas];

        for (int i = 0; i < quantidadeConsultas; i++) {
            resultado[i] = consultas[i];
        }

        return resultado;
    }

    public Consulta buscarConsulta(int codigo) {
        for (int i = 0; i < quantidadeConsultas; i++) {
            if (consultas[i].getCodigo() == codigo) {
                return consultas[i];
            }
        }

        return null;
    }

    public boolean cancelarConsulta(int codigo) {
        Consulta consulta = buscarConsulta(codigo);

        if (consulta == null || !consulta.isAtiva()
                || consulta.isRealizada()) {
            return false;
        }

        consulta.cancelar();

        int indice = indiceDoVeterinario(
                consulta.getCodigoVeteriano());

        if (indice >= 0) {
            int dia = consulta.getDiaSemana();
            int horario = consulta.getHorario();

            boolean existeOutraConsulta = false;

            for (int i = 0; i < quantidadeConsultas; i++) {
                Consulta outra = consultas[i];

                if (outra.getCodigo() != codigo
                        && outra.isAtiva()
                        && !outra.isRealizada()
                        && outra.getCodigoVeteriano()
                        == consulta.getCodigoVeteriano()
                        && outra.getDiaSemana() == dia
                        && outra.getHorario() == horario) {
                    existeOutraConsulta = true;
                    break;
                }
            }

            if (!existeOutraConsulta) {
                agendaVeterinarios[indice][dia] = 0;
            }
        }

        return true;
    }

    public boolean realizarConsulta(int codigo) {
        Consulta consulta = buscarConsulta(codigo);

        if (consulta == null || !consulta.isAtiva()
                || consulta.isRealizada()) {
            return false;
        }

        consulta.realizar();

        int indice = indiceDoVeterinario(
                consulta.getCodigoVeteriano());

        if (indice >= 0) {
            int dia = consulta.getDiaSemana();
            int horario = consulta.getHorario();

            boolean existeOutraConsulta = false;

            for (int i = 0; i < quantidadeConsultas; i++) {
                Consulta outra = consultas[i];

                if (outra.getCodigo() != codigo
                        && outra.isAtiva()
                        && !outra.isRealizada()
                        && outra.getCodigoVeteriano()
                        == consulta.getCodigoVeteriano()
                        && outra.getDiaSemana() == dia
                        && outra.getHorario() == horario) {
                    existeOutraConsulta = true;
                    break;
                }
            }

            if (!existeOutraConsulta) {
                agendaVeterinarios[indice][dia] = 0;
            }
        }

        return true;
    }

    public void exibirConsultas() {
        for (int i = 0; i < quantidadeConsultas; i++) {
            consultas[i].exibirDados();
        }
    }

    public void exibirConsultas(int codigoAnimal) {
        for (int i = 0; i < quantidadeConsultas; i++) {
            if (consultas[i].getCodigAnimal() == codigoAnimal) {
                consultas[i].exibirDados();
            }
        }
    }

    public void exibirAgenda() {
        for (int i = 0; i < quantidadeVeterinarios; i++) {
            System.out.println(
                    "Agenda do veterinário: "
                            + veterinarios[i].getNome());

            for (int dia = 0; dia < 5; dia++) {
                System.out.println(
                        "Dia " + dia + ": "
                                + (agendaVeterinarios[i][dia] == 1
                                ? "Ocupado" : "Livre"));
            }
        }
    }

    // ==========================================
    // MÉTODOS AUXILIARES
    // ==========================================

    private int indiceDoVeterinario(int codigo) {
        for (int i = 0; i < quantidadeVeterinarios; i++) {
            if (veterinarios[i].getCodigo() == codigo) {
                return i;
            }
        }

        return -1;
    }

    private boolean veterinarioOcupado(
            int codigoVeterinario, int dia, int horario) {

        for (int i = 0; i < quantidadeConsultas; i++) {
            Consulta consulta = consultas[i];

            if (consulta.isAtiva()
                    && !consulta.isRealizada()
                    && consulta.getCodigoVeteriano()
                    == codigoVeterinario
                    && consulta.getDiaSemana() == dia
                    && consulta.getHorario() == horario) {
                return true;
            }
        }

        return false;
    }

    private boolean animalOcupado(
            int codigoAnimal, int dia, int horario) {

        for (int i = 0; i < quantidadeConsultas; i++) {
            Consulta consulta = consultas[i];

            if (consulta.isAtiva()
                    && !consulta.isRealizada()
                    && consulta.getCodigAnimal() == codigoAnimal
                    && consulta.getDiaSemana() == dia
                    && consulta.getHorario() == horario) {
                return true;
            }
        }

        return false;
    }

    private boolean textoInvalido(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    // ==========================================
    // MÉTODOS DO DASHBOARD
    // ==========================================

    public int getQuantidadeAnimais() {
        return quantidadeAnimais;
    }

    public int getQuantidadeVeterinarios() {
        return quantidadeVeterinarios;
    }

    public int getQuantidadeConsultasAgendadas() {
        int total = 0;

        for (int i = 0; i < quantidadeConsultas; i++) {
            if (consultas[i].isAtiva()
                    && !consultas[i].isRealizada()) {
                total++;
            }
        }

        return total;
    }

    public int getQuantidadeConsultasRealizadas() {
        int total = 0;

        for (int i = 0; i < quantidadeConsultas; i++) {
            if (consultas[i].isRealizada()) {
                total++;
            }
        }

        return total;
    }
}
