import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class CadastroAlunoCurso extends JFrame {

    // fonte padrão do programa
    final private Font fontePadrao = new Font("Arial", Font.BOLD, 18);
    // declaração das variaveis de texto
    JTextField campoNome;
    JTextField campoCargaHoraria;
    JTextField campoDescricao;
    Connection conn = null;
    Statement stmt = null;
    int iCurso = 0;
    int iAluno = 0;
    String textoErro = "";

    JComboBox<String> comboBoxCurso;
    int[] codigosCurso;
    int indexCursoSelecionado;

    JComboBox<String> comboBoxAluno;
    int[] codigosAluno;
    int indexAlunoSelecionado;

    public CadastroAlunoCurso() {
        this.initialize();
    }

    public void initialize() {
        // inicializando todos os campos e seus nomes
        JLabel labelCadastroAlunoCurso = new JLabel("Associar aluno ao curso", SwingConstants.CENTER);
        labelCadastroAlunoCurso.setFont(fontePadrao);

        JLabel labelCurso = new JLabel("Curso");
        labelCurso.setFont(fontePadrao);
        this.criarComboBoxCurso();

        JLabel labelAluno = new JLabel("Aluno:");
        labelAluno.setFont(fontePadrao);
        this.criarComboBoxAluno();

        this.comboBoxCurso.addActionListener(e -> {
            this.indexCursoSelecionado = (Integer) comboBoxCurso.getSelectedIndex();
        });

        this.comboBoxAluno.addActionListener(e -> {
            System.out.println(comboBoxAluno.getSelectedItem());
            this.indexAlunoSelecionado = (Integer) comboBoxAluno.getSelectedIndex();
        });

        // container que contem os campos de texto e seus respectivos nomes
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        formPanel.add(labelCadastroAlunoCurso);
        formPanel.add(labelCurso);
        formPanel.add(comboBoxCurso);
        formPanel.add(labelAluno);
        formPanel.add(comboBoxAluno);

        // criação do botão cadastrar
        JButton botaoCadastrar = new JButton("Cadastrar");
        // ações dos botões da tela de cadastro de aluno
        botaoCadastrar.addActionListener(e -> {
            Integer codigCurso = this.codigosCurso[indexCursoSelecionado];
            Integer codigoAluno = this.codigosAluno[indexAlunoSelecionado];

            if (codigCurso == 0) {
                this.textoErro += "Selecione um curso \n";
            }

            if (codigoAluno == 0) {
                this.textoErro += "Selecione um aluno \n";
            }

            if (this.textoErro != "") {
                JOptionPane.showMessageDialog(null, textoErro, "Erro",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                Boolean deuCerto = associarAlunoCurso(codigCurso, codigoAluno);

                if (deuCerto) {
                    // se der tudo certo, um aviso é dado e o usuário pode cadastrar outro aluno
                    JOptionPane.showMessageDialog(null, "Deu boa!!", "Boa",
                            JOptionPane.PLAIN_MESSAGE);
                } else {
                    // se não, ocorre um erro na tela
                    JOptionPane.showMessageDialog(null, this.textoErro, "Erro",
                            JOptionPane.ERROR_MESSAGE);

                    this.textoErro = "";
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

        JButton botaoVoltar = new JButton("Voltar");
        botaoVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Principal();
            }

        });

        // container dos botões
        JPanel botoesPanel = new JPanel();
        botoesPanel.setLayout(new GridLayout(1, 2, 10, 0));
        botoesPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        botoesPanel.add(botaoCadastrar);
        botoesPanel.add(botaoCancelar);
        botoesPanel.add(botaoVoltar);

        /*************** Inicia o frame ***************/
        add(formPanel, BorderLayout.NORTH);
        add(botoesPanel, BorderLayout.SOUTH);

        // configurações básicas para iniciar a tela no tamanho certo
        setTitle("Cadastro de Curso");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setMinimumSize(new Dimension(500, 450));
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
    }

    private boolean associarAlunoCurso(int cdCurso, int cdAluno) {

        try {
            Banco banco = new Banco();

            if (banco.ConectarBanco()) {
                conn = banco.getConn();
                stmt = banco.getStmt();
            }
            String sql = "INSERT INTO curso_aluno (cd_curso, matricula)"
                    + "VALUES (?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, cdCurso);
            preparedStatement.setInt(2, cdAluno);

            int camposAdicionados = preparedStatement.executeUpdate();
            if (camposAdicionados > 0) {
                // mudar linha: falta matricula
                // aluno = new Aluno(nome, cpf, email, endereco, celular, matricula);
            }
            stmt.close();
            conn.close();
            banco.desconectar();
            return true;
        } catch (Exception e) {
            this.textoErro = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    private boolean criarComboBoxCurso() {

        try {
            Banco banco = new Banco();

            if (banco.ConectarBanco()) {
                conn = banco.getConn();
                stmt = banco.getStmt();
            }

            this.comboBoxCurso = new JComboBox<>();

            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT count(cd_curso) as quantidade FROM curso");

            while (resultSet.next()) {
                Integer quantidade = resultSet.getInt("quantidade");
                this.codigosCurso = new int[quantidade];
            }

            // Consulta ao banco de dados para obter os nomes dos cursos
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT nm_curso, cd_curso FROM curso");

            // Populando o JComboBox com os nomes dos cursos
            DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
            while (resultSet.next()) {
                comboBoxModel.addElement(resultSet.getString("nm_curso"));
                this.codigosCurso[iCurso] = resultSet.getInt("cd_curso");
                iCurso++;
            }

            this.comboBoxCurso.setModel(comboBoxModel);

            stmt.close();
            conn.close();
            banco.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean criarComboBoxAluno() {

        try {
            Banco banco = new Banco();

            if (banco.ConectarBanco()) {
                conn = banco.getConn();
                stmt = banco.getStmt();
            }

            this.comboBoxAluno = new JComboBox<>();

            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT count(matricula) as quantidade FROM aluno");

            while (resultSet.next()) {
                Integer quantidade = resultSet.getInt("quantidade");
                this.codigosAluno = new int[quantidade];
            }

            // Consulta ao banco de dados para obter os nomes dos cursos
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT nm_aluno, matricula FROM aluno");

            // Populando o JComboBox com os nomes dos cursos
            DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
            while (resultSet.next()) {
                comboBoxModel.addElement(resultSet.getString("nm_aluno"));
                this.codigosAluno[iAluno] = resultSet.getInt("matricula");
                iAluno++;
            }

            this.comboBoxAluno.setModel(comboBoxModel);

            stmt.close();
            conn.close();
            banco.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
