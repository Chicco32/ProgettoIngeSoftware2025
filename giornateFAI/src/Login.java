import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import it.unibs.fp.mylib.InputDati;

public class Login {


    private Registratore registratore;
    private static final String defaultNickname = "admin";
    private static final String defaultPassword = "admin";

    public Login() {
        this.registratore = new Registratore();
    }

    /*
     * Metodo di login per le credenziali di default
     */
    public boolean loginCredenzialiDefault(String nickname, String password) {
        if (nickname.equals(defaultNickname) && password.equals(defaultPassword)) {
            System.err.println("Login riuscito!");

            //creazione del profilo
            System.err.println("Ti rimandiamo alla creazione del tuo profilo!");
            String nuovoNickname = InputDati.leggiStringa("Inserisci il tuo nuovo Nickname");
            String nuovaPassword = InputDati.leggiStringa("Inserisci la tua nuova Password");
            this.registratore.registraNuovoConfiguratore(nuovoNickname, nuovaPassword);
            return true;

        } else {
            System.err.println("Credenziali errate!");
            return false;
        }
    }

    /*
     * Metodo di login per il configuratore già registrato
     */
    public boolean loginConfiguratore(String nickname, String password) {
        Connection conn = ConnesioneSQL.getConnection();
        if (conn != null) {
            try {
                String selectProva = "SELECT Nickname FROM `dbingesw`.`configuratore` WHERE Nickname = '" + nickname + "' AND Password = '" + password + "'";
                ResultSet rs = conn.createStatement().executeQuery(selectProva);
                if (rs.next()) {
                    System.err.println("Login riuscito!");
                    return true;
                } else {
                    System.err.println("Credenziali errate!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
