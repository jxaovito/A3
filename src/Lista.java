import javax.print.DocFlavor.STRING;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Lista extends JFrame {
    Connection conn = null;
    Statement stmt = null;
    JLabel labelNome = new JLabel();
    JLabel labelCargaHoraria = new JLabel();
    JLabel labelDescricao = new JLabel();
    JLabel labelNomeAluno = new JLabel();
    int quantidade = 0;
    int i = 0;
    int[] codigoCurso;

    public Lista() {
        this.initialize();
    }

    public void initialize() {
        JComboBox<String> comboBox;
        comboBox = new JComboBox<>();

        try {
            // Conexão com o banco de dados
            Banco banco = new Banco();

            if (banco.ConectarBanco()) {
                conn = banco.getConn();
                stmt = banco.getStmt();
            }
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT count(cd_curso) as quantidade FROM curso");

            while (resultSet.next()) {
                this.quantidade = resultSet.getInt("quantidade");
                this.codigoCurso = new int[quantidade];
            }

            // Consulta ao banco de dados para obter os nomes dos cursos
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT nm_curso, cd_curso FROM curso");

            // Populando o JComboBox com os nomes dos cursos
            DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
            while (resultSet.next()) {
                comboBoxModel.addElement(resultSet.getString("nm_curso"));
                this.codigoCurso[i] = resultSet.getInt("cd_curso");
                i++;
            }

            comboBox.setModel(comboBoxModel);

            // Fechando a conexão
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();

        }

        comboBox.addActionListener(e -> {
            // Ação ao selecionar um curso no JComboBox
            String cursoSelecionado = (String) comboBox.getSelectedItem();
            Integer indexCursoSelecionado = (Integer) comboBox.getSelectedIndex();
            if (cursoSelecionado != null) {
                try {
                    Banco banco = new Banco();

                    if (banco.ConectarBanco()) {
                        conn = banco.getConn();
                        stmt = banco.getStmt();
                    }

                    String query = "SELECT curso.nm_curso, professor.nm_professor, sala.nm_sala FROM curso LEFT JOIN professor ON professor.cd_funcionario = curso.cd_funcionario LEFT JOIN turma AS t ON t.cd_curso = curso.cd_curso LEFT JOIN sala ON t.cd_sala = sala.cd_sala WHERE curso.cd_curso = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1, this.codigoCurso[indexCursoSelecionado]);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    // Exibindo os dados do curso
                    if (resultSet.next()) {
                        String nomeCurso = resultSet.getString("nm_curso");
                        String professor = resultSet.getString("nm_professor");
                        String sala = resultSet.getString("nm_sala");

                        labelNome.setText("Nome do curso: " + nomeCurso);
                        labelCargaHoraria.setText("Professor: " + professor);
                        labelDescricao.setText("Sala: " + sala);

                    }

                    query = "SELECT count(aluno.nm_aluno) as quantidade from curso_aluno left join aluno on aluno.matricula = curso_aluno.matricula WHERE curso_aluno.cd_curso = ?";
                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1, this.codigoCurso[indexCursoSelecionado]);
                    resultSet = preparedStatement.executeQuery();

                    String[] nomes;
                    Integer quantidade = 0;
                    Integer i = 0;

                    if (resultSet.next()) {
                        quantidade = resultSet.getInt("quantidade");
                    }

                    nomes = new String[quantidade];

                    query = "SELECT aluno.nm_aluno from curso_aluno left join aluno on aluno.matricula = curso_aluno.matricula WHERE curso_aluno.cd_curso = ?";
                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1, this.codigoCurso[indexCursoSelecionado]);
                    resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        nomes[i] = resultSet.getString("nm_aluno");
                        i++;
                    }

                    String nomeAluno = "";
                    for (String nome : nomes) {
                        nomeAluno += ", " + nome;
                    }
                    labelNomeAluno.setText("Alunos: " + nomeAluno);

                } catch (SQLException ex) {
                    ex.printStackTrace();

                }
            }

        });
        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new GridLayout(5, 1, 10, 0));
        listaPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        listaPanel.add(comboBox);
        listaPanel.add(labelNome);
        listaPanel.add(labelCargaHoraria);
        listaPanel.add(labelDescricao);
        listaPanel.add(labelNomeAluno);

        /** Inicia o frame **/
        add(listaPanel, BorderLayout.NORTH);

        // Configuração do JFrame
        setTitle("Lista de cursos");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setMinimumSize(new Dimension(500, 450));
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
    }

    // public JComboBox combobxi() {

    // try {
    // // Conexão com o banco de dados
    // Banco banco = new Banco();

    // if (banco.ConectarBanco()) {
    // conn = banco.getConn();
    // stmt = banco.getStmt();
    // }

    // // Consulta ao banco de dados para obter os nomes dos cursos
    // Statement statement = conn.createStatement();
    // ResultSet resultSet = statement.executeQuery("SELECT nm_curso FROM curso");

    // // Populando o JComboBox com os nomes dos cursos
    // DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
    // while (resultSet.next()) {
    // String nomeCurso = resultSet.getString("nm_curso");
    // comboBoxModel.addElement(nomeCurso);
    // }
    // JComboBox<String> comboBoxUm;
    // comboBoxUm = new JComboBox<>();
    // comboBoxUm.setModel(comboBoxModel);

    // // Fechando a conexão
    // resultSet.close();
    // statement.close();
    // conn.close();
    // return comboBoxUm;
    // } catch (SQLException ex) {
    // ex.printStackTrace();
    // return null;

    // }
    // }
}