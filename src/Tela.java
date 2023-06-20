import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tela extends JFrame implements ActionListener {
    JTextField texto;
    JTextField texto2;
    JLabel titulo;
    JLabel tituloCaixa;
    public Tela() {
        setTitle("Gerenciador de serviços");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setLocationRelativeTo(null);

        setLayout(null);

        JButton botao = new JButton();
        botao.setText("clique aqui");
        botao.setBounds(200,300,100,50);
        botao.setFont(new Font("Arial", Font.PLAIN, 20));
        botao.setForeground(new Color(200, 200, 200));
        botao.setBackground(new Color(56, 56, 15));


        add(botao);
        botao.addActionListener(this::outroteste);

        texto = new JTextField();
        texto.setBounds(200, 150, 150, 30);
        texto.setFont(new Font("Arial", Font.ITALIC, 40));

        add(texto);

        texto2 = new JTextField();
        texto2.setBounds(200, 250, 150, 30);
        texto2.setFont(new Font("Arial", Font.ITALIC, 40));

        add(texto2);

        tituloCaixa = new JLabel("Nome do aluno:");
        tituloCaixa.setBounds(200, 100, 150, 30);
        tituloCaixa.setFont(new Font("Arial", Font.PLAIN, 15));

        add(tituloCaixa);

        titulo = new JLabel("Gerenciador de alunos");
        titulo.setBounds(this.getX() / 2, 50, 400, 100);
        titulo.setFont(new Font("Arial", Font.PLAIN, 35));

        add(titulo);


        setVisible(true);
    }

    public void outroteste(ActionEvent e) {
        JOptionPane.showMessageDialog(null, texto.getText(), "titulo do joptionpane", JOptionPane.WARNING_MESSAGE);
        titulo.setText("oi");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
