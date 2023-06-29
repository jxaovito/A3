import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.*;

public class Lista extends JFrame {
    Connection conn = null;
    Statement stmt = null;
    JLabel labelNome = new JLabel();
    JLabel labelCargaHoraria = new JLabel();
    JLabel labelDescricao = new JLabel();
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
                String nomeCurso = resultSet.getString("nm_curso");
                this.codigoCurso[i] = resultSet.getInt("cd_curso");
                comboBoxModel.addElement(nomeCurso);
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

                    String query = "SELECT nm_curso, carga_horaria, ds_curso FROM curso WHERE cd_curso = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1, this.codigoCurso[indexCursoSelecionado]);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    // Exibindo os dados do curso
                    if (resultSet.next()) {
                        String nomeCurso = resultSet.getString("nm_curso");
                        int cargaHoraria = resultSet.getInt("carga_horaria");
                        String descricaoCurso = resultSet.getString("ds_curso");

                        labelNome.setText("Nome do curso: " + nomeCurso);
                        labelCargaHoraria.setText("Carga horária: " + cargaHoraria);
                        labelDescricao.setText("Descrição: " + descricaoCurso);

                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();

                }
            }

        });
        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new GridLayout(4, 1, 10, 0));
        listaPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        listaPanel.add(comboBox);
        listaPanel.add(labelNome);
        listaPanel.add(labelCargaHoraria);
        listaPanel.add(labelDescricao);

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