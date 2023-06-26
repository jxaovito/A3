import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class CadastroProfessor extends JFrame {
    // fonte padrão do programa
    final private Font fontePadrao = new Font("Arial", Font.BOLD, 18);
    // declaração das variaveis de texto
    JTextField campoNome;
    JTextField campoCpf;
    JTextField campoEmail;
    JTextField campoEndereco;
    JTextField campoCelular;
    private String banco = "escola";
    private String porta = "3306";
    private String IpHost = "//localhost";

    public CadastroProfessor(){
        this.initialize();
    }

    public void initialize() {
        // inicializando todos os campos e seus nomes
        JLabel labelCadastroProfessor = new JLabel("Cadastrar Professor", SwingConstants.CENTER);
        labelCadastroProfessor.setFont(fontePadrao);

        JLabel labelNome = new JLabel("Nome:");
        labelNome.setFont(fontePadrao);

        campoNome = new JTextField();
        campoNome.setFont(fontePadrao);

        JLabel labelCpf = new JLabel("CPF:");
        labelCpf.setFont(fontePadrao);

        campoCpf = new JTextField();
        campoCpf.setFont(fontePadrao);

        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setFont(fontePadrao);

        campoEmail = new JTextField();
        campoEmail.setFont(fontePadrao);

        JLabel labelEndereco = new JLabel("Endereço:");
        labelEndereco.setFont(fontePadrao);

        campoEndereco = new JTextField();
        campoEndereco.setFont(fontePadrao);

        JLabel labelCelular = new JLabel("Celular:");
        labelCelular.setFont(fontePadrao);

        campoCelular = new JTextField();
        campoCelular.setFont(fontePadrao);

        // container que contem os campos de texto e seus respectivos nomes
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        formPanel.add(labelCadastroProfessor);
        formPanel.add(labelNome);
        formPanel.add(campoNome);
        formPanel.add(labelCpf);
        formPanel.add(campoCpf);
        formPanel.add(labelEmail);
        formPanel.add(campoEmail);
        formPanel.add(labelEndereco);
        formPanel.add(campoEndereco);
        formPanel.add(labelCelular);
        formPanel.add(campoCelular);

        // criação do botão cadastrar
        JButton botaoCadastrar = new JButton("Cadastrar");
        // ações dos botões da tela de cadastro de aluno
        botaoCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarProfessor();
            }

            // funcionamento da função registrarProfessor: aqui os dados inseridos nos campos
            // são associados às variáveis da classe aluno
            private void registrarProfessor() {
                String nome = campoNome.getText();
                String cpf = campoCpf.getText();
                String endereco = campoEndereco.getText();
                String email = campoEmail.getText();
                String celular = campoCelular.getText();

                // verifica se todos os campos estão preenchidos, caso não estejam ocorre um
                // erro
                if (nome.isEmpty() || cpf.isEmpty() || endereco.isEmpty() || email.isEmpty() || celular.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos e tente novamente", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // se estiverem preenchidos, aluno é adicionado ao banco de dados
                    Boolean deuCerto = adicionarProfessorNoBanco(nome, cpf, email, endereco, celular);

                    if (deuCerto) {
                        // se der tudo certo, um aviso é dado e o usuário pode cadastrar outro aluno
                        JOptionPane.showMessageDialog(null, "Deu boa!!", "Boa",
                                JOptionPane.PLAIN_MESSAGE);
                    } else {
                        // se não, ocorre um erro na tela
                        JOptionPane.showMessageDialog(null, "Falha ao cadastrar professor", "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
        // criando o botão cancelar
        JButton botaoCancelar = new JButton("Fechar");
        // funcionamento do botão "Cancelar" na interface
        botaoCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fecharTela();
            }

            private void fecharTela() {
                dispose();
            }

        });

        // container dos botões
        JPanel botoesPanel = new JPanel();
        botoesPanel.setLayout(new GridLayout(1, 2, 10, 0));
        botoesPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        botoesPanel.add(botaoCadastrar);
        botoesPanel.add(botaoCancelar);

        /*************** Inicia o frame ***************/
        add(formPanel, BorderLayout.NORTH);
        add(botoesPanel, BorderLayout.SOUTH);

        // configurações básicas para iniciar a tela no tamanho certo
        setTitle("Cadastro de professor");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setMinimumSize(new Dimension(500, 450));
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
    }


    private boolean adicionarProfessorNoBanco(String nome, String cpf, String email, String endereco, String celular) {
        // Dados para conexão ao banco

        final String DB_URL = "jdbc:mysql:" + this.IpHost + ":" + this.porta + "/" + this.banco;
        final String USERNAME = "root";
        final String PASSWORD = "";

        // Conexão do banco de dados
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO professor (nm_professor, cpf_professor, em_professor, endereco_professor, cel_professor)"
                    + "VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, cpf);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, endereco);
            preparedStatement.setString(5, celular);

            int camposAdicionados = preparedStatement.executeUpdate();
            if (camposAdicionados > 0) {
                // mudar linha: falta matricula
                // aluno = new Aluno(nome, cpf, email, endereco, celular, matricula);
            }
            stmt.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
