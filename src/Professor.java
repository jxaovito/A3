public class Professor extends Pessoa {
    private String codFuncionario;

    // Construtor da classe
    public Professor(String nome, String cpf, String endereco, String email, String celular) {
        super(nome, cpf, endereco, email, celular);
    }

    // métodos
    public String getCodFuncionario() {
        return codFuncionario;
    }

     public void setMatriculaAluno(String matricula_aluno) {
        this.codFuncionario = matricula_aluno;
    }
}
