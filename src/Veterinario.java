public class Veterinario {
   private int codigo;
   private String nome;
   private String especialidade;
   private String crmv;
   private boolean ativo;

    public Veterinario(int codigo, String nome, String especialidade, String crmv){
        this.codigo = codigo;
        this.nome = nome;
        this.especialidade = especialidade;
        this.crmv = crmv;
        this.ativo = true;
    }
    public int getCodigo() {
        return codigo;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getCrmv() {
        return crmv;
    }

    public void setCrmv(String crmv) {
        this.crmv = crmv;
    }

    public boolean isAtivo() {
        return ativo;
    }
    public void desativar(){
        this.ativo = false;
    }
    public void exibirDados(){
        System.out.println("Codigo :" + codigo);
        System.out.println("Nome do veterinário: " + nome);
        System.out.println("Especialidade: " + especialidade);
        System.out.println("CRMV: " + crmv);
        System.out.println("Situação: " + ativo);
    }
}
