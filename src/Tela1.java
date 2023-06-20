import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Tela1 extends JDialog{
    // atributos ligados aos campos e botões da interface
    private JTextField campoNome;
    private JTextField campoCpf;
    private JTextField campoEndereco;
    private JTextField campoEmail;
    private JTextField campoCelular;
    private JButton botaoCadastrar;
    private JButton botaoCancelar;
    private JPanel RegistroAluno;

    public Tela1(JFrame parent){
        // configurações básicas como título, tamanho, alinhamento entre outros elementos da interface
        super(parent);
        setTitle("Cadastre um aluno");
        setContentPane(RegistroAluno);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);

        // ações dos botões da tela de cadastro de aluno
        botaoCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarAluno();
            }
        });
        // funcionamento do botão "Cancelar" na interface
        botaoCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fecharTela();
            }
        });

        setVisible(true);
    }

    // função fechar tela através do botão na interface
    private void fecharTela() {
        dispose();
    }

    private void registrarAluno() {
        String nome = campoNome.getText();
        String cpf = campoCpf.getText();
        String endereco = campoEndereco.getText();
        String email = campoEmail.getText();
        String celular = campoCelular.getText();

        // verifica se todos os campos estão preenchidos, caso não estejam ocorre um erro
        if (nome.isEmpty() || cpf.isEmpty() || endereco.isEmpty() || email.isEmpty() || celular.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos e tente novamente", "Erro", JOptionPane.ERROR_MESSAGE);
        } else {
            // se estiverem preenchidos, aluno é adicionado ao banco de dados
            aluno = adicionarAlunoNoBanco(nome, cpf, email, endereco, celular);

            if (aluno != null) {
                // se der tudo certo, a janela é fechada
                dispose();
            } else {
                // se não, ocorre um erro na tela
                JOptionPane.showMessageDialog(this, "Falha ao cadastrar aluno", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

public Aluno aluno;
private Aluno adicionarAlunoNoBanco(String nome, String cpf, String email, String endereco, String celular){
    Aluno aluno = null;
    // Dados para conexão ao banco
    final String DB_URL = "";
    final String USERNAME = "";
    final String PASSWORD = "";

    // Conexão do banco de dados
    try {
        Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

        Statement stmt = conn.createStatement();
        String sql = "INSERT INTO users (nome, cpf, email, endereco, celular)" + "VALUES (?,?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, nome);
        preparedStatement.setString(2, cpf);
        preparedStatement.setString(3, email);
        preparedStatement.setString(4, endereco);
        preparedStatement.setString(5, celular);

        int camposAdicionados = preparedStatement.executeUpdate();
        if (camposAdicionados > 0){
            //
//            aluno = new Aluno(nome, cpf, email, endereco, celular, matricula);


        }
        stmt.close();
        conn.close();
    } catch(Exception e){
        e.printStackTrace();
    }
    return aluno;
}

private static void main(String[] args){
    Tela1 primeiratela = new Tela1(null);
    Aluno aluno = primeiratela.aluno;

    if(aluno != null) {
        System.out.println("Aluno cadastrado com sucesso");
    } else {
        System.out.println("Cadastro não efetuado");
    }
}
}
