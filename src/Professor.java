public class Professor extends Pessoa {
    private String codFuncionario;

    public Professor(String nome, String cpf, String endereco, String email, String celular) {
        super(nome, cpf, endereco, email, celular);
    }

    public String getCodFuncionario() {
        return codFuncionario;
    }

}
