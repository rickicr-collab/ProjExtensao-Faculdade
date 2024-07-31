package classes;

public class Professor extends Pessoa {
    private String lingua;
    private String turma;

    public Professor(String nome, String lingua, String turma) {
        super(nome);
        this.lingua = lingua;
        this.turma = turma;
    }

    public String getLingua() {
        return lingua;
    }

    public void setLingua(String lingua) {
        this.lingua = lingua;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }
}
