public class Animal {
    private int codigo;
    private String nome;
    private String especie;
    private String raca;
    private int idade;
    private String nomeTutor;
    private boolean ativo;

    public Animal(int codigo, String nome, String especie, String nomeTutor){
        this.codigo = codigo;
        this.nome = nome;
        this.especie = especie;
        this.nomeTutor = nomeTutor;
        this.raca = null;
        this.ativo = true;
        this.idade = 0;
    }

    public int getCodigo() {
        if(codigo >= 0) {
            return codigo;
        }
        return 0;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if(nome != null){
            this.nome = nome;
        }
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        if(especie != null) {
            this.especie = especie;
        }
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        if(idade >= 0) {
            this.idade = idade;
        }
    }

    public String getNomeTutor() {
        return nomeTutor;
    }

    public void setNomeTutor(String nomeTutor) {
        if(nomeTutor != null) {
            this.nomeTutor = nomeTutor;
        }
    }

    public boolean isAtivo() {
        return ativo;
    }
    public Animal (int codigo, String nome, String especie, String raca, int idade, String nomeTutor){
        this.codigo = codigo;
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.idade = idade;
        this.nomeTutor = nomeTutor;
        this.ativo = true;
    }
    public void desativar(){
        ativo = false;
    }

    public void exibirDados(){
        System.out.println("Codigo do animal:" + codigo);
        System.out.println("Nome do animal: " + nome);
        System.out.println("Especie: " + especie);
        System.out.println("Raca: " + raca);
        System.out.println("Idade do animal: " + idade);
        System.out.println("Nome do tutor: " + nomeTutor);
        System.out.println("Status: " + ativo);
    }
    public void atualizarDados(
            String nome,
            String especie,
            String raca,
            int idade,
            String nomeTutor) {

        if (nome != null && !nome.isBlank()) {
            this.nome = nome;
        }

        if (especie != null && !especie.isBlank()) {
            this.especie = especie;
        }

        this.raca = raca;

        if (idade >= 0) {
            this.idade = idade;
        }

        if (nomeTutor != null && !nomeTutor.isBlank()) {
            this.nomeTutor = nomeTutor;
        }

    }

}
