import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class CadastroTurma extends JFrame {

    // fonte padrão do programa
    final private Font fontePadrao = new Font("Arial", Font.BOLD, 18);
    // declaração das variaveis de texto
    JTextField campoNome;
    JTextField campoCargaHoraria;
    JTextField campoDescricao;
    Connection conn = null;
    Statement stmt = null;
    int iCurso = 0;
    int iSala = 0;
    String textoErro = "";

    JComboBox<String> comboBoxCurso;
    int[] codigosCurso;
    int indexCursoSelecionado;

    JComboBox<String> comboBoxSala;
    int[] codigosSala;
    int indexSalaSelecionado;

    JComboBox<String> comboBoxDiasSemana;
    int[] codigosDiasSemana = { 1, 2, 3, 4, 5 };
    int indexDiaSemanaSelecionado;

    public CadastroTurma() {
        this.initialize();
    }

    public void initialize() {
        // inicializando todos os campos e seus nomes
        JLabel labelCadastroTurma = new JLabel("Criar Turma", SwingConstants.CENTER);
        labelCadastroTurma.setFont(fontePadrao);

        JLabel labelCurso = new JLabel("Curso:");
        labelCurso.setFont(fontePadrao);
        this.criarComboBoxCurso();

        JLabel labelSala = new JLabel("Sala:");
        labelSala.setFont(fontePadrao);
        this.criarComboBoxSala();

        JLabel labelDiaSemana = new JLabel("Dia da Semana:");
        labelDiaSemana.setFont(fontePadrao);
        this.criarComboBoxDiasSemana();

        this.comboBoxCurso.addActionListener(e -> {
            this.indexCursoSelecionado = (Integer) comboBoxCurso.getSelectedIndex();
        });

        this.comboBoxSala.addActionListener(e -> {
            this.indexSalaSelecionado = (Integer) comboBoxSala.getSelectedIndex();
        });

        this.comboBoxDiasSemana.addActionListener(e -> {
            this.indexDiaSemanaSelecionado = (Integer) comboBoxDiasSemana.getSelectedIndex();
        });

        // container que contem os campos de texto e seus respectivos nomes
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        formPanel.add(labelCadastroTurma);
        formPanel.add(labelCurso);
        formPanel.add(comboBoxCurso);
        formPanel.add(labelSala);
        formPanel.add(comboBoxSala);
        formPanel.add(labelDiaSemana);
        formPanel.add(comboBoxDiasSemana);

        // criação do botão cadastrar
        JButton botaoCadastrar = new JButton("Cadastrar");
        // ações dos botões da tela de cadastro de Sala
        botaoCadastrar.addActionListener(e -> {
            Integer codigCurso = this.codigosCurso[indexCursoSelecionado];
            Integer codigoSala = this.codigosSala[indexSalaSelecionado];
            Integer codigoDiaSemana = this.codigosDiasSemana[indexDiaSemanaSelecionado];

            if (codigCurso == 0) {
                this.textoErro += "Selecione um curso \n";
            }

            if (codigoSala == 0) {
                this.textoErro += "Selecione um aluno \n";
            }

            if (this.textoErro != "") {
                JOptionPane.showMessageDialog(null, textoErro, "Erro",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                Boolean deuCerto = associarTurma(codigCurso, codigoSala, codigoDiaSemana);

                if (deuCerto) {
                    // se der tudo certo, um aviso é dado e o usuário pode cadastrar outro aluno
                    JOptionPane.showMessageDialog(null, "Operação efetuada com sucesso!!", "Sucesso",
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
        setTitle("Cadastro de turma");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setMinimumSize(new Dimension(500, 450));
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
    }

    private boolean associarTurma(int cdCurso, int cdSala, int cdDiaSemana) {

        try {
            Banco banco = new Banco();

            if (banco.ConectarBanco()) {
                conn = banco.getConn();
                stmt = banco.getStmt();
            }

            // validações
            // Validação para verificar se sala comporta quantidade de alunos
            String sql = "SELECT sala.capacidade_total, COUNT(curso_aluno.matricula) AS quantidade FROM turma LEFT JOIN sala ON sala.cd_sala = turma.cd_sala LEFT JOIN curso_aluno ON curso_aluno.cd_curso = turma.cd_curso WHERE turma.cd_sala = ? AND turma.cd_curso = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, cdSala);
            preparedStatement.setInt(2, cdCurso);
            ResultSet resultSet = preparedStatement.executeQuery();

            Integer capacidadeSala = 0;
            Integer qntdAlunos = 0;
            while (resultSet.next()) {
                capacidadeSala = resultSet.getInt("capacidade_total");
                qntdAlunos = resultSet.getInt("quantidade");
            }

            if (capacidadeSala < qntdAlunos) {
                this.textoErro += "Capacidade da sala não comporta quantidade de alunos \n";
                return false;
            }

            // Validação para verificar se curso já foi atribuido a aquele dia da semana
            sql = "SELECT cd_curso from turma WHERE cd_dia = ? AND cd_curso = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, cdDiaSemana);
            preparedStatement.setInt(2, cdCurso);
            resultSet = preparedStatement.executeQuery();
            resultSet.last();
            if (resultSet.getRow() > 0) {
                this.textoErro += "Esse curso já foi associado a esse dia da semana \n";
                return false;
            }

            // Validação para verificar se sala já foi atribuido a aquele dia da semana
            sql = "SELECT cd_sala from turma WHERE cd_dia = ? AND cd_sala = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, cdDiaSemana);
            preparedStatement.setInt(2, cdSala);
            resultSet = preparedStatement.executeQuery();
            resultSet.last();
            if (resultSet.getRow() > 0) {
                this.textoErro += "Esta sala já foi associada a este dia da semana \n";
                return false;
            }

            // Inserir no banco
            sql = "INSERT INTO turma (cd_curso, cd_sala, cd_dia)"
                    + "VALUES (?,?,?)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, cdCurso);
            preparedStatement.setInt(2, cdSala);
            preparedStatement.setInt(3, cdDiaSemana);

            preparedStatement.executeUpdate();

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

    private boolean criarComboBoxSala() {

        try {
            Banco banco = new Banco();

            if (banco.ConectarBanco()) {
                conn = banco.getConn();
                stmt = banco.getStmt();
            }

            this.comboBoxSala = new JComboBox<>();

            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT count(cd_sala) as quantidade FROM sala");

            while (resultSet.next()) {
                Integer quantidade = resultSet.getInt("quantidade");
                this.codigosSala = new int[quantidade];
            }

            // Consulta ao banco de dados para obter os nomes dos cursos
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT nm_sala, cd_sala FROM sala");

            // Populando o JComboBox com os nomes dos cursos
            DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
            while (resultSet.next()) {
                comboBoxModel.addElement(resultSet.getString("nm_sala"));
                this.codigosSala[iSala] = resultSet.getInt("cd_sala");
                iSala++;
            }

            this.comboBoxSala.setModel(comboBoxModel);

            stmt.close();
            conn.close();
            banco.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean criarComboBoxDiasSemana() {

        try {
            Banco banco = new Banco();

            if (banco.ConectarBanco()) {
                conn = banco.getConn();
                stmt = banco.getStmt();
            }

            this.comboBoxDiasSemana = new JComboBox<>();

            // Consulta ao banco de dados para obter os nomes dos cursos
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT ds_dia FROM dias_semana");

            // Populando o JComboBox com os nomes dos cursos
            DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
            while (resultSet.next()) {
                comboBoxModel.addElement(resultSet.getString("ds_dia"));
            }

            this.comboBoxDiasSemana.setModel(comboBoxModel);

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
