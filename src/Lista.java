import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

                    query = "SELECT aluno.nm_aluno FROM curso_aluno LEFT JOIN aluno ON aluno.matricula = curso_aluno.matricula WHERE curso_aluno.cd_curso = ?";
                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1, this.codigoCurso[indexCursoSelecionado]);
                    resultSet = preparedStatement.executeQuery();

                    List<String> alunos = new ArrayList<>();
                    while (resultSet.next()) {
                        String nomeAluno = resultSet.getString("nm_aluno");
                        alunos.add(nomeAluno);
                    }

                    StringBuilder nomesAlunos = new StringBuilder();
                    for (String nome : alunos) {
                        nomesAlunos.append(nome).append(", ");
                    }

                    if (nomesAlunos.length() > 0) {
                        nomesAlunos.delete(nomesAlunos.length() - 2, nomesAlunos.length());
                    }

                    labelNomeAluno.setText("Alunos: " + nomesAlunos.toString());

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
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

        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new GridLayout(5, 1, 10, 0));
        listaPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        listaPanel.add(comboBox);
        listaPanel.add(labelNome);
        listaPanel.add(labelCargaHoraria);
        listaPanel.add(labelDescricao);
        listaPanel.add(labelNomeAluno);

        JPanel botoesPanel = new JPanel();
        botoesPanel.setLayout(new GridLayout(1, 2, 10, 0));
        botoesPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        botoesPanel.add(botaoVoltar);
        
        /** Inicia o frame **/
        add(listaPanel, BorderLayout.NORTH);
        add(botoesPanel, BorderLayout.SOUTH);


        // Configuração do JFrame
        setTitle("Lista de cursos");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setMinimumSize(new Dimension(500, 450));
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
    }
}