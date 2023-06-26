import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class Banco {
    private Connection conn = null;
    private Statement stmt = null;
    private String banco = "escola";
    private String porta = "3306";
    private String IpHost = "//localhost";

    public Connection ConectarBanco() {

        final String DB_URL = "jdbc:mysql:" + this.IpHost + ":" + this.porta + "/" + this.banco;
        final String USERNAME = "root";
        final String PASSWORD = "50ft4t1";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            // this.stmt = conn.createStatement();
            return this.conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void desconectar() {
        try {
            this.conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao fechar");
        }
    }
}
