import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class CadastroCurso extends JFrame implements InterfaceCadastros {

    // fonte padrão do programa
    final private Font fontePadrao = new Font("Arial", Font.BOLD, 18);
    // declaração das variaveis de texto
    JTextField campoNome;
    JTextField campoCargaHoraria;
    JTextField campoDescricao;
    Connection conn = null;
    Statement stmt = null;
    JComboBox<String> comboBox;
    int i = 0;
    int[] codigosProfessores;
    int indexProfessorSelecionado;

    public CadastroCurso() {
        this.initialize();
    }

    public void initialize() {
        // inicializando todos os campos e seus nomes
        JLabel labelCadastroCurso = new JLabel("Cadastrar Curso", SwingConstants.CENTER);
        labelCadastroCurso.setFont(fontePadrao);

        JLabel labelNome = new JLabel("Nome:");
        labelNome.setFont(fontePadrao);

        campoNome = new JTextField();
        campoNome.setFont(fontePadrao);

        JLabel labelCargaHoraria = new JLabel("Carga Horária:");
        labelCargaHoraria.setFont(fontePadrao);

        campoCargaHoraria = new JTextField();
        campoCargaHoraria.setFont(fontePadrao);

        JLabel labelDescricao = new JLabel("Descrição:");
        labelDescricao.setFont(fontePadrao);

        campoDescricao = new JTextField();
        campoDescricao.setFont(fontePadrao);

        JLabel labelProfessor = new JLabel("Professor:");
        labelProfessor.setFont(fontePadrao);
        this.criarComboBoxProfessor();

        this.comboBox.addActionListener(e -> {
            this.indexProfessorSelecionado = (Integer) comboBox.getSelectedIndex();
        });

        // container que contem os campos de texto e seus respectivos nomes
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        formPanel.add(labelCadastroCurso);
        formPanel.add(labelNome);
        formPanel.add(campoNome);
        formPanel.add(labelCargaHoraria);
        formPanel.add(campoCargaHoraria);
        formPanel.add(labelDescricao);
        formPanel.add(campoDescricao);
        formPanel.add(labelProfessor);
        formPanel.add(this.comboBox);

        // criação do botão cadastrar
        JButton botaoCadastrar = new JButton("Cadastrar");
        // ações do botão
        botaoCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarCurso();
            }

            // funcionamento da função registrarCurso: aqui os dados inseridos nos campos
            // são associados às variáveis da classe aluno
            private void registrarCurso() {
                String nome = campoNome.getText();
                int cargaHoraria = Integer.parseInt(campoCargaHoraria.getText());
                String descricao = campoDescricao.getText();

                // verifica se todos os campos estão preenchidos, um aviso é mostrado na tela
                String textoErro = "";
                if (nome.isEmpty()) {
                    textoErro += "Preencha o campo nome \n\n";
                }
                if (cargaHoraria == 0) {
                    textoErro += "Preencha o campo carga horária \n";
                }
                if (descricao.isEmpty()) {
                    textoErro += "Preencha o campo descrição \n\n";
                }

                if (textoErro != "") {
                    JOptionPane.showMessageDialog(null, textoErro, "Erro",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // se estiverem preenchidos, aluno é adicionado ao banco de dados
                    Boolean deuCerto = adicionarNoBanco(nome, cargaHoraria, descricao);

                    if (deuCerto) {
                        // se der tudo certo, um aviso é dado e o usuário pode cadastrar outro aluno
                        JOptionPane.showMessageDialog(null, "Operação efetuada com sucesso!!", "Sucesso",
                                JOptionPane.PLAIN_MESSAGE);
                    } else {
                        // se não, ocorre um erro na tela
                        JOptionPane.showMessageDialog(null, "Falha ao cadastrar Curso", "Erro",
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

    private boolean adicionarNoBanco(String nome, int cargaHoraria, String descricao) {

        try {
            Banco banco = new Banco();

            if (banco.ConectarBanco()) {
                conn = banco.getConn();
                stmt = banco.getStmt();
            }
            String sql = "INSERT INTO curso (nm_curso, carga_horaria, ds_curso, cd_funcionario)"
                    + "VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, nome);
            preparedStatement.setInt(2, cargaHoraria);
            preparedStatement.setString(3, descricao);
            preparedStatement.setInt(4, this.codigosProfessores[this.indexProfessorSelecionado]);

            int camposAdicionados = preparedStatement.executeUpdate();
            if (camposAdicionados > 0) {
            }
            stmt.close();
            conn.close();
            banco.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean criarComboBoxProfessor() {

        try {
            Banco banco = new Banco();

            if (banco.ConectarBanco()) {
                conn = banco.getConn();
                stmt = banco.getStmt();
            }

            this.comboBox = new JComboBox<>();

            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT count(cd_funcionario) as quantidade FROM professor");

            while (resultSet.next()) {
                Integer quantidade = resultSet.getInt("quantidade");
                this.codigosProfessores = new int[quantidade];
            }

            // Consulta ao banco de dados para obter os nomes dos cursos
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT nm_professor, cd_funcionario FROM professor");

            // Populando o JComboBox com os nomes dos cursos
            DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
            while (resultSet.next()) {
                String nomeProfessor = resultSet.getString("nm_professor");
                this.codigosProfessores[i] = resultSet.getInt("cd_funcionario");
                comboBoxModel.addElement(nomeProfessor);
                i++;
            }

            this.comboBox.setModel(comboBoxModel);

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
