package classes;

public class Aluno extends Pessoa {
    private int idade;
    private String horario;
    private String lingua;
    private String diasDisponiveis;
    private boolean ativo;
    private String motivoDesistencia;
    private String turma; // Novo campo para a turma

    public Aluno(String nome, int idade, String horario, String lingua, String diasDisponiveis, String turma) {
        super(nome);
        this.idade = idade;
        this.horario = horario;
        this.lingua = lingua;
        this.diasDisponiveis = diasDisponiveis;
        this.ativo = true; // Por padrão, o aluno está ativo
        this.turma = turma;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getLingua() {
        return lingua;
    }

    public void setLingua(String lingua) {
        this.lingua = lingua;
    }

    public String getDiasDisponiveis() {
        return diasDisponiveis;
    }

    public void setDiasDisponiveis(String diasDisponiveis) {
        this.diasDisponiveis = diasDisponiveis;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getMotivoDesistencia() {
        return motivoDesistencia;
    }

    public void setMotivoDesistencia(String motivoDesistencia) {
        this.motivoDesistencia = motivoDesistencia;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    // Método para converter o aluno para formato CSV
    public String toCSV() {
        return String.join(",", getNome(), String.valueOf(getIdade()), getHorario(), getLingua(), getDiasDisponiveis(), isAtivo() ? "Ativo" : "Inativo", getTurma());
    }
}
