import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {


    private static final String defaultNickname = "admin";
    private static final String defaultPassword = "admin";
    private Connection connection;

    public Login() {
        this.connection = ConnesioneSQL.getConnection();
    }

    /*
     * Metodo di login per il configuratore
     */
    public Utente loginConfiguratore(String nickname, String password) {
        
        //se entra con le credenziali di default
        if (nickname.equals(defaultNickname) && password.equals(defaultPassword)) {
            CliUtente.loginRiuscito();

            //Segna che è il primo accesso
            return new Configuratore(true);
        }

        //se entra con le credenziali di un utente già registrato
        else if (connection != null) {
            try {
                String selectProva = "SELECT Nickname FROM `dbingesw`.`configuratore` WHERE Nickname = '" + nickname + "' AND Password = '" + password + "'";
                ResultSet rs = this.connection.createStatement().executeQuery(selectProva);
                if (rs.next()) {
                    CliUtente.loginRiuscito();

                    //immette l'utente nel backEnd
                    Utente utente = new Configuratore(false);
                    utente.setNickname(nickname);
                    return utente;
                }
                //se le credenziali sono sbagliate
                else {
                    CliUtente.credenzialiErrate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
