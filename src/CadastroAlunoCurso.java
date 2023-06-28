import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import java.util.Arrays;

public class CadastroAlunoCurso extends JFrame {

    // fonte padrão do programa
    final private Font fontePadrao = new Font("Arial", Font.BOLD, 18);
    // declaração das variaveis de texto
    JTextField campoNome;
    JTextField campoCargaHoraria;
    JTextField campoDescricao;
    Connection conn = null;
    Statement stmt = null;

    public CadastroAlunoCurso() {
        this.initialize();
    }

    public void initialize() {
        // inicializando todos os campos e seus nomes
        JLabel labelCadastroAlunoCurso = new JLabel("Associar Aluno ao curso", SwingConstants.CENTER);
        labelCadastroAlunoCurso.setFont(fontePadrao);

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

        // container que contem os campos de texto e seus respectivos nomes
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        formPanel.add(labelCadastroAlunoCurso);
        formPanel.add(labelNome);
        formPanel.add(campoNome);
        formPanel.add(labelCargaHoraria);
        formPanel.add(campoCargaHoraria);
        formPanel.add(labelDescricao);
        formPanel.add(campoDescricao);

        // criação do botão cadastrar
        JButton botaoCadastrar = new JButton("Cadastrar");
        // ações dos botões da tela de cadastro de aluno
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
                    Boolean deuCerto = adicionarCursoNoBanco(nome, cargaHoraria, descricao);

                    if (deuCerto) {
                        // se der tudo certo, um aviso é dado e o usuário pode cadastrar outro aluno
                        JOptionPane.showMessageDialog(null, "Deu boa!!", "Boa",
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

    private boolean adicionarCursoNoBanco(String nome, int cargaHoraria, String descricao) {

        try {
            Banco banco = new Banco();
            int qntd = 0;

            if (banco.ConectarBanco()) {
                conn = banco.getConn();
                stmt = banco.getStmt();
            }
            String sql = "SELECT count(cd_curso) as quantidade from curso";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                qntd = resultSet.getInt("quantidade");
            }
            System.out.println(qntd);

            Object[][] a = new Object[qntd][qntd];

            sql = "SELECT cd_curso, nm_curso from curso";
            preparedStatement = conn.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                for (int i = 0; i < qntd; i++) {
                    a[i] = new Object[] { resultSet.getInt("cd_curso"),
                            resultSet.getString("nm_curso") };
                }
            }

            System.out.println(Arrays.deepToString(a));
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
