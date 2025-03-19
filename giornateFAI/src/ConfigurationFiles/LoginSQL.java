package ConfigurationFiles;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import Services.Configuratore;
import Services.Login;
import Services.Utente;

/**
 * Classe per il login, si occupa di verificare le credenziali inserite dall'utente
 * e di accedere al sistema.
 * Una volta verificata la correttezza delle credenziali, restituisce un oggetto di tipo Utente
 * che può essere un Configuratore o un Volontario o Fruitore
 * 
 * I valori per l'accesso con credenziali di default per un nuovo configuratore sono:
 * Nickname = "admin"
 * Password = "admin"
 * 
 * @see Utente
 * @see Configuratore
 * @see Login
 */
public class LoginSQL implements Login {


    private static final String defaultNickname = "admin";
    private static final String defaultPassword = "admin";
    private Connection connection;

    public LoginSQL() {
        this.connection = ConnessioneSQL.getConnection();
    }


    public Utente loginUtente(String nickname, String password) throws SQLException {
        
        //se entra con le credenziali di default
        if (nickname.equals(defaultNickname) && password.equals(defaultPassword)) {

            //Segna che è il primo accesso
            return new Configuratore(true, defaultNickname);
        }

        //se entra con le credenziali di un utente già registrato
        else if (connection != null) {
            
            ResultSet rs;

            //Non funziona, trovare motivo
            /*String loginQuery = "SELECT Nickname FROM `dbingesw`.`configuratore` WHERE Nickname = ? AND Password = ?;";
            try (PreparedStatement stmt = connection.prepareStatement(loginQuery)) {
                stmt.setString(1, nickname);
                stmt.setString(2, password);
                System.out.println();
                rs = stmt.executeQuery();
            } */

            String query = "SELECT Nickname FROM `dbingesw`.`configuratore` WHERE Nickname = '" + nickname + "' AND Password = '" + password + "';";
            rs = connection.createStatement().executeQuery(query); 

            if (rs.next()) {
                //immette l'utente nel backEnd
                Utente utente = new Configuratore(false, nickname);
                rs.close();
                return utente;
            }
            rs.close();
        }
        return null;
    }
}
