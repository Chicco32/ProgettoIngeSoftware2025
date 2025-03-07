import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneSQL {
    // Dati per la connessione
    private static final String URL = "jdbc:mysql://localhost:3306/dbingesw";
    private static final String USER = "root"; 
    private static final String PASSWORD = "root"; 

    public static Connection getConnection() {
	return getConnection(URL,USER,PASSWORD);
    }
    public static Connection getConnection(String url, String user, String psw){
        try {
            // Carica il driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Crea la connessione
            Connection conn = DriverManager.getConnection(url, user, psw);
            System.out.println("Connessione riuscita!");
            return conn;

        } catch (ClassNotFoundException e) {
            System.out.println("Driver non trovato!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Errore nella connessione!");
            e.printStackTrace();
        }
        return null;
    }

}
