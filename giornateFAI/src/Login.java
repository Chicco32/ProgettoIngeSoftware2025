import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
 */
public class Login {


    private static final String defaultNickname = "admin";
    private static final String defaultPassword = "admin";
    private Connection connection;

    public Login() {
        this.connection = ConnesioneSQL.getConnection();
    }
    public Login(String url, String user, String psw){
	    this.connection=ConnessioneSQL.getConnection(url,user,psw);
    }

    /**
     * Metodo di login per il configuratore
     * @param nickname Il nickname dell'utente inserisce nel form
     * @param password La password dell'utente inserisce nel form
     * @return Un oggetto di tipo Utente, null se le credenziali sono sbagliate
     * 
     * @throws SQLException in caso di errore di connessione al database
     */
    public Utente loginUtente(String nickname, String password) {
        
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
