
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Principal extends JFrame {
    // fonte padrão do programa
    // declaração das variaveis de texto

    public Principal() {
        this.initialize();
    }

    public void initialize() {

        // Criação e aplicação dos botões de entrada em cada tela de cadastro
        JButton botaoCadastroAluno = new JButton("Cadastrar Aluno");
        botaoCadastroAluno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CadastroAluno();
            }

        });
        JButton botaoCadastroProfessor = new JButton("Cadastrar Professor");
        botaoCadastroProfessor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CadastroProfessor();
            }

        });
        JButton botaoCadastroCurso = new JButton("Cadastrar Curso");
        botaoCadastroCurso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CadastroCurso();
            }

        });
        JButton botaoCadastroTurma = new JButton("Cadastrar Turma");
        botaoCadastroTurma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CadastroTurma();
            }

        });
        JButton botaoCadastroSala = new JButton("Cadastrar Sala");
        botaoCadastroSala.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CadastroSala();
            }

        });

        JButton botaoLista = new JButton("Abrir lista de cursos");
        botaoLista.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Lista();

            }

        });

        JButton botaoAssociarAlunoCurso = new JButton("Associar aluno ao curso");
        botaoAssociarAlunoCurso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CadastroAlunoCurso();

            }

        });

        // criação e funcionamento do botão de fechar
        JButton botaoCancelar = new JButton("Fechar");
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
        botoesPanel.setLayout(new GridLayout(0, 1, 10, 10));
        botoesPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        botoesPanel.add(botaoCadastroAluno);
        botoesPanel.add(botaoCadastroProfessor);
        botoesPanel.add(botaoCadastroCurso);
        botoesPanel.add(botaoCadastroTurma);
        botoesPanel.add(botaoCadastroSala);
        botoesPanel.add(botaoLista);
        botoesPanel.add(botaoAssociarAlunoCurso);
        botoesPanel.add(botaoCancelar);

        /*************** Inicia o frame ***************/
        // add(formPanel, BorderLayout.NORTH);
        add(botoesPanel, BorderLayout.NORTH);

        // configurações básicas para iniciar a tela no tamanho certo
        setTitle("Página principal");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setMinimumSize(new Dimension(500, 450));
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
    }

}
