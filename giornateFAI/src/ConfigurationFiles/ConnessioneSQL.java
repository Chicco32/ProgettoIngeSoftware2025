package ConfigurationFiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe Singleton per la connessione al DB
 */
public class ConnessioneSQL {
    // Dati per la connessione
    private static final String URL = "localhost:3306/dbingesw";
    private static final String USER = "root"; 
    private static final String PASSWORD = "root";
    private static Connection connessione;

    public static Connection getConnection() {
	return getConnection(URL,USER,PASSWORD);
    }

    public static Connection getConnection(String url, String user, String psw){
        //se è la prima volta crea la connessione con i dati in input, altrimenti li ignora e ritorna la connessione gia stabilita
        if (ConnessioneSQL.connessione == null) {
            try {
                // Carica il driver MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Crea la connessione
                Connection conn = DriverManager.getConnection("jdbc:mysql://"+url, user, psw);
                ConnessioneSQL.connessione = conn;

            } catch (ClassNotFoundException e) {
                System.out.println("Driver non trovato!");
            } catch (SQLException e) {
                System.out.println("Errore nella connessione!");
            }
        }
        return ConnessioneSQL.connessione;
    }

}
