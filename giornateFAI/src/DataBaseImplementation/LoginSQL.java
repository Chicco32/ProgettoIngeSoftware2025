package DataBaseImplementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import ServicesAPI.Configuratore;
import ServicesAPI.Login;
import ServicesAPI.RegistroDateDisponibili;
import ServicesAPI.RegistroDatePrecluse;
import ServicesAPI.Utente;
import ServicesAPI.Volontario;

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
 * @see Volontario
 * @see Login
 */
public class LoginSQL implements Login {


    private static final String defaultNicknameAdmin = "admin";
    private static final String defaultPasswordAdmin = "admin";
    private static final String defaultPasswordVolontario = "volontario";
    private Connection connection;

    public LoginSQL() {
        this.connection = ConnessioneSQL.getConnection();
    }

    private static final Map<Class<? extends Utente>, Queries> accessi = Map.of(
        Configuratore.class, Queries.PASSWORD_CONFIGURATORE,
        Volontario.class, Queries.PASSWORD_VOLONTARIO
    );

    public Utente loginUtente(String nickname, String passwordInserita) throws Exception {
    
        if (connection == null) throw new SQLException();

        //preparo le istanze concrete degli strumenti che l'API offre all'applicazione 
        VisualizzatoreSQL visualizzatore = new VisualizzatoreSQL();
        RegistratoreSQL registratore = new RegistratoreSQL(PercorsiFiles.pathRegistratore);
        RegistroDatePrecluse registroDatePrecluse = new RegistroDatePrecluse(new XMLDatePrecluse(PercorsiFiles.pathDatePrecluse));
        RegistroDateDisponibili registroDateDisponibili = new RegistroDateDisponibili(new XMLDateDisponibili(PercorsiFiles.pathDateDisponibili));
        
        //prima una catena di controllo sulle credenziali di default
        if (nickname.equals(defaultNicknameAdmin) && passwordInserita.equals(defaultPasswordAdmin)) {
            return new Configuratore(true, defaultNicknameAdmin, visualizzatore, registratore, registroDatePrecluse);
        }
        else if(presenteNelDB(nickname, defaultPasswordVolontario, accessi.get(Volontario.class))) {
            return new Volontario(true, nickname, visualizzatore, registroDateDisponibili);
        }

        //se entra con le credenziali di un utente già registrato si crea una catena di richieste per capire il tipo di utente
        if (presenteNelDB(nickname, passwordInserita, accessi.get(Configuratore.class))) {
            return new Configuratore(false, nickname, visualizzatore, registratore, registroDatePrecluse);
        }
        else if (presenteNelDB(nickname, passwordInserita, accessi.get(Volontario.class))) {
            return new Volontario(false, nickname, visualizzatore, registroDateDisponibili);
        }

        return null;
    }

    /**
     * Funzione che chiede all'utente se il nickname inserito è effettivamente presente e se la password coincide
     * @param nickname il nickname che si tenta di cercare
     * @param passwordInserita la password che l'utente ha inserito
     * @param interrogazione l'effettiva richiesta al DB di controllare, questo permette di essere riusata in caso di salvataggi su db diversi o su schemi diversi
     * @return true se le credenziali sono valide, false altrimenti
     */
    private boolean presenteNelDB(String nickname, String passwordInserita, Queries interrogazione) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(interrogazione.getQuery())) {
            stmt.setString(1, nickname);
            try (ResultSet rs = stmt.executeQuery()) {
                //Se il nickname risulta effettivamente presente nel DB recupera la password
                if (rs.next()) {
                    //TODO: mettere l'hash delle password
                    String passwordSalvata = rs.getString("Password");
                    if (passwordSalvata.equals(passwordInserita))
                        return true;
                }
            }
        }
        return false;
    }

    public static String getDefaultPasswordVolontario() {
        return defaultPasswordVolontario;
    }

    public static boolean cambioPassword(String nickname, String password) throws Exception {
        // TODO Auto-generated method stub per gestire un futuro hashing delle password
        throw new UnsupportedOperationException("Unimplemented method 'cambioPassword'");
    }

}
