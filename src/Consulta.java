public class Consulta {
    private int codigo;
    private int codigAnimal;
    private int codigoVeteriano;
    private int diaSemana;
    private int horario;
    private String motivo;
    private boolean ativa;
    private boolean realizada;

    public Consulta(int codigo, int codigoAnimal, int codigoVeteriano,
                    int diaSemana, int horario, String motivo){
        this.codigo = codigo;
        this.codigAnimal = codigoAnimal;
        this.codigoVeteriano = codigoVeteriano;
        this.diaSemana = diaSemana;
        this.horario = horario;
        this.motivo = motivo;
        this.ativa = true;
        this.realizada = false;
    }

    public static String nomeDia(int nomeDia) {
        return null;
    }

    public int getCodigo() {
        return codigo;
    }

    public int getCodigAnimal() {
        return codigAnimal;
    }

    public int getCodigoVeteriano() {
        return codigoVeteriano;
    }

    public int getDiaSemana() {
        return diaSemana;
    }

    public int getHorario() {
        return horario;
    }

    public String getMotivo() {
        return motivo;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public boolean isRealizada() {
        return realizada;
    }

    public void setDiaSemana(int diaSemana) {
        if(diaSemana >= 0 && diaSemana <= 4) {
            this.diaSemana = diaSemana;
        }
    }

    public void setHorario(int horario) {
        if(horario >= 0 && horario <= 7) {
            this.horario = horario;
        }
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    public void cancelar(){
        ativa = false;
        realizada = false;
    }
    public void realizar(){
        realizada = true;
        ativa = false;
    }
    public void exibirDados(){
        System.out.println("Codigo da consulta :" + codigo);
        System.out.println("Codigo do animal: " + codigAnimal);
        System.out.println("Codigo do veterinário: " + codigoVeteriano);
        System.out.println("Dia da semana: " + diaSemana);
        System.out.println("Horario: " + horario);
        System.out.println("Motivo da consulta: " + motivo);
        System.out.println("Situação da consulta: " +
                (ativa ? "Agendada" : (realizada ? "Realizada" : "Cancelada")));    }
}
