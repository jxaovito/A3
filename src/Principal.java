
    import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Principal extends JFrame {
    // fonte padrão do programa
    final private Font fontePadrao = new Font("Arial", Font.BOLD, 18);
    // declaração das variaveis de texto

    public Principal(){
        this.initialize();
    }

    public void initialize() {

        // container que contem os campos de texto e seus respectivos nomes
        //JPanel formPanel = new JPanel();
        //formPanel.setLayout(new GridLayout(0, 1, 10, 10));
        //formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        //formPanel.add(labelCadastroAluno);

        // criação do botão cadastrar
        JButton botaoCadastroAluno = new JButton("Cadastrar Aluno");
        // ações dos botões da tela de cadastro de aluno
        botaoCadastroAluno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CadastroAluno();
            }

        });
        // criando o botão cancelar
        JButton botaoCancelar = new JButton("Cadastrar Professor");
        // funcionamento do botão "Cancelar" na interface
        botaoCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CadastroProfessor();
            }

        });

        // container dos botões
        JPanel botoesPanel = new JPanel();
        botoesPanel.setLayout(new GridLayout(0, 1, 10, 10));
        botoesPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        botoesPanel.add(botaoCadastroAluno);
        botoesPanel.add(botaoCancelar);

        /*************** Inicia o frame ***************/
        //add(formPanel, BorderLayout.NORTH);
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

