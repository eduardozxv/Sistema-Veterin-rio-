
public class GerenciadorClinica {
    private Animal[] animais;
    private Veterinario[] veterinarios;
    private Consulta[] consultas;
    private int[][] agendaVeterinarios;
    private int quantidadeAnimais;
    private int quantidadeVeterinarios;
    private int quantidadeConsultas;

    public GerenciadorClinica(int capacidadeMaxima) {
        if (capacidadeMaxima <= 0) {
            capacidadeMaxima = 1;
        }
        animais = new Animal[capacidadeMaxima];
        veterinarios = new Veterinario[capacidadeMaxima];
        consultas = new Consulta[capacidadeMaxima];
        agendaVeterinarios = new int[capacidadeMaxima][8];
        quantidadeAnimais = 0;
        quantidadeVeterinarios = 0;
        quantidadeConsultas = 0;
    }

    public boolean cadastrarAnimal(Animal animal) {
        if (animal == null || quantidadeAnimais >= animais.length) {
            return false;
        }
        if (animal.getCodigo() <= 0 || textoInvalido(animal.getNome())
                || textoInvalido(animal.getEspecie()) || textoInvalido(animal.getNomeTutor())
                || animal.getIdade() < 0) {
            return false;
        }
        if (buscarAnimal(animal.getCodigo()) != null) {
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

    public boolean desativarAnimal(int codigoAnimal) {
        Animal animal = buscarAnimal(codigoAnimal);
        if (animal == null || !animal.isAtivo()) {
            return false;
        }
        animal.desativar();
        for (int i = 0; i < quantidadeConsultas; i++) {
            if (consultas[i].getCodigAnimal() == codigoAnimal && consultas[i].isAtiva()) {
                consultas[i].cancelar();
            }
        }
        return true;
    }

    public void exibirAnimais() {
        if (quantidadeAnimais == 0) {
            System.out.println("Nenhum animal cadastrado.");
            return;
        }
        for (int i = 0; i < quantidadeAnimais; i++) {
            animais[i].exibirDados();
            System.out.println("--------------------");
        }
    }

    public boolean cadastrarVeterinario(Veterinario veterinario) {
        if (veterinario == null || quantidadeVeterinarios >= veterinarios.length) {
            return false;
        }
        if (veterinario.getCodigo() <= 0 || textoInvalido(veterinario.getNome())
                || textoInvalido(veterinario.getEspecialidade()) || textoInvalido(veterinario.getCrmv())) {
            return false;
        }
        if (buscarVeterinario(veterinario.getCodigo()) != null) {
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

    public boolean desativarVeterinario(int codigoVeterinario) {
        Veterinario veterinario = buscarVeterinario(codigoVeterinario);
        if (veterinario == null || !veterinario.isAtivo()) {
            return false;
        }
        veterinario.desativar();
        for (int i = 0; i < quantidadeConsultas; i++) {
            if (consultas[i].getCodigoVeteriano() == codigoVeterinario && consultas[i].isAtiva()) {
                consultas[i].cancelar();
            }
        }
        return true;
    }

    public void exibirVeterinarios() {
        if (quantidadeVeterinarios == 0) {
            System.out.println("Nenhum veterinário cadastrado.");
            return;
        }
        for (int i = 0; i < quantidadeVeterinarios; i++) {
            veterinarios[i].exibirDados();
            System.out.println("--------------------");
        }
    }

    public boolean agendarConsulta(Consulta consulta) {
        if (consulta == null || quantidadeConsultas >= consultas.length) {
            return false;
        }
        if (consulta.getCodigo() <= 0 || buscarConsulta(consulta.getCodigo()) != null) {
            return false;
        }
        Animal animal = buscarAnimal(consulta.getCodigAnimal());
        Veterinario veterinario = buscarVeterinario(consulta.getCodigoVeteriano());
        if (animal == null || !animal.isAtivo() || veterinario == null || !veterinario.isAtivo()) {
            return false;
        }
        if (consulta.getDiaSemana() < 0 || consulta.getDiaSemana() > 4
                || consulta.getHorario() < 0 || consulta.getHorario() > 7
                || textoInvalido(consulta.getMotivo())) {
            return false;
        }
        if (veterinarioOcupado(consulta.getCodigoVeteriano(), consulta.getDiaSemana(), consulta.getHorario())) {
            return false;
        }
        if (animalOcupado(consulta.getCodigAnimal(), consulta.getDiaSemana(), consulta.getHorario())) {
            return false;
        }
        consultas[quantidadeConsultas] = consulta;
        quantidadeConsultas++;
        return true;
    }

    public Consulta buscarConsulta(int codigo) {
        for (int i = 0; i < quantidadeConsultas; i++) {
            if (consultas[i].getCodigo() == codigo) {
                return consultas[i];
            }
        }
        return null;
    }

    public boolean cancelarConsulta(int codigoConsulta) {
        Consulta consulta = buscarConsulta(codigoConsulta);
        if (consulta == null || !consulta.isAtiva() || consulta.isRealizada()) {
            return false;
        }
        consulta.cancelar();
        return true;
    }

    public boolean realizarConsulta(int codigoConsulta) {
        Consulta consulta = buscarConsulta(codigoConsulta);
        if (consulta == null || !consulta.isAtiva() || consulta.isRealizada()) {
            return false;
        }
        consulta.realizar();
        return true;
    }

    public void exibirConsultas() {
        if (quantidadeConsultas == 0) {
            System.out.println("Nenhuma consulta cadastrada.");
            return;
        }
        for (int i = 0; i < quantidadeConsultas; i++) {
            consultas[i].exibirDados();
            System.out.println("--------------------");
        }
    }

    public void exibirConsultas(int codigoAnimal) {
        boolean encontrou = false;
        for (int i = 0; i < quantidadeConsultas; i++) {
            if (consultas[i].getCodigAnimal() == codigoAnimal) {
                consultas[i].exibirDados();
                System.out.println("--------------------");
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Nenhuma consulta encontrada para esse animal.");
        }
    }

    public void exibirAgenda(int diaSemana) {
        if (diaSemana < 0 || diaSemana > 4) {
            System.out.println("Dia inválido.");
            return;
        }
        for (int i = 0; i < quantidadeVeterinarios; i++) {
            for (int j = 0; j < 8; j++) {
                agendaVeterinarios[i][j] = 0;
            }
        }
        for (int i = 0; i < quantidadeConsultas; i++) {
            Consulta consulta = consultas[i];
            if (consulta.isAtiva() && consulta.getDiaSemana() == diaSemana) {
                int indice = indiceDoVeterinario(consulta.getCodigoVeteriano());
                if (indice >= 0) {
                    agendaVeterinarios[indice][consulta.getHorario()] = 1;
                }
            }
        }
        System.out.println("Agenda de " + Consulta.nomeDia(diaSemana));
        System.out.println("VET.  08h 09h 10h 11h 14h 15h 16h 17h");
        for (int i = 0; i < quantidadeVeterinarios; i++) {
            System.out.print(veterinarios[i].getCodigo() + "    ");
            for (int j = 0; j < 8; j++) {
                System.out.print(agendaVeterinarios[i][j] + "   ");
            }
            System.out.println();
        }
    }

    private int indiceDoVeterinario(int codigoVeterinario) {
        for (int i = 0; i < quantidadeVeterinarios; i++) {
            if (veterinarios[i].getCodigo() == codigoVeterinario) {
                return i;
            }
        }
        return -1;
    }

    private boolean veterinarioOcupado(int codigoVeterinario, int diaSemana, int horario) {
        for (int i = 0; i < quantidadeConsultas; i++) {
            Consulta consulta = consultas[i];
            if (consulta.isAtiva() && consulta.getCodigoVeteriano() == codigoVeterinario
                    && consulta.getDiaSemana() == diaSemana && consulta.getHorario() == horario) {
                return true;
            }
        }
        return false;
    }

    private boolean animalOcupado(int codigoAnimal, int diaSemana, int horario) {
        for (int i = 0; i < quantidadeConsultas; i++) {
            Consulta consulta = consultas[i];
            if (consulta.isAtiva() && consulta.getCodigAnimal() == codigoAnimal
                    && consulta.getDiaSemana() == diaSemana && consulta.getHorario() == horario) {
                return true;
            }
        }
        return false;
    }

    private boolean textoInvalido(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}