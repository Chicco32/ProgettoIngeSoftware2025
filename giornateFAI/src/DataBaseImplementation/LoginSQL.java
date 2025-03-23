package DataBaseImplementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import Controller.FactoryServizi;
import ServicesAPI.Configuratore;
import ServicesAPI.DTObject;
import ServicesAPI.Login;
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
    private Connection connection;
    private FactoryServizi servizi;

    public LoginSQL() {
        this.connection = ConnessioneSQL.getConnection();
        this.servizi = AvviaServiziDatabase.getFactory();
    }

    private static final Map<Class<? extends Utente>, Queries> accessi = Map.of(
        Configuratore.class, Queries.PASSWORD_ACCESSO_CONFIGURATORE,
        Volontario.class, Queries.PASSWORD_ACCESSO_VOLONTARIO
    );

    public Utente loginUtente(String nickname, String passwordInserita) throws Exception {
    
        if (connection == null) throw new SQLException();

        
        //prima un controllo sulle credenziali di default
        if (nickname.equals(defaultNicknameAdmin) && passwordInserita.equals(defaultPasswordAdmin)) {
            return new Configuratore(true, defaultNicknameAdmin, servizi);
        }

        //se entra con le credenziali di un utente già registrato si crea una catena di richieste per capire il tipo di utente
        //Configuratore:
        if (presenteNelDB(nickname, passwordInserita, accessi.get(Configuratore.class))) {
            return new Configuratore(false, nickname, servizi);
        }
        //Volontario:
        else if (presenteNelDB(nickname, passwordInserita, accessi.get(Volontario.class))) {
            Boolean primoAccesso = false;
            if (passwordInserita.equals(defaultPasswordVolontario)) primoAccesso = true; 
            return new Volontario(primoAccesso, nickname, servizi);
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
                    String passwordSalvata = rs.getString("Password");
                    if (ServizioHash.passwordValida(passwordInserita, passwordSalvata)  )
                        return true;
                }
            }
        }
        return false;
    }

    public boolean nomeUtenteUnivoco(String nomeUtente) throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement(Queries.NICKNAME_UNIVOCO.getQuery())) {
            stmt.setString(1, nomeUtente);
            //prova a cercare il nome nel DB
            ResultSet rs = stmt.executeQuery();
            //se non è presente nel DB è un nome valido
            if (!rs.next()) return true;
        }
        return false;
    }

    private static final Map<String, Queries> cambioPassword = Map.of(
        "Configuratore", Queries.CAMBIO_PASSWORD_CONFIGURATORE,
        "Volontario", Queries.CAMBIO_PASSWORD_VOLONTARIO
    );

    public boolean cambioPassword(DTObject dati, String ruolo) throws Exception {
        //in nmaniera trasparente all'utente aggiunge i layer di sicurezza
        ServizioHash.cifraPassword(dati);
        String nickname = (String)dati.getValoreCampo("Nickname");
        String password = (String)dati.getValoreCampo("Password");
        String sale = (String)dati.getValoreCampo("Salt");

        Queries query = cambioPassword.get(ruolo);
        try (PreparedStatement stmt = connection.prepareStatement(query.getQuery())) {
            stmt.setString(1, password);
            stmt.setString(2, sale);
            stmt.setString(3,nickname);
            stmt.executeUpdate();
            return true;
        }
    }

    public boolean registraNuovoConfiguratore(DTObject configuratore) throws Exception {
        //in nmaniera trasparente all'utente aggiunge i layer di sicurezza
        ServizioHash.cifraPassword(configuratore);
        String nickname = (String)configuratore.getValoreCampo("Nickname");
        String password = (String)configuratore.getValoreCampo("Password");
        String sale = (String)configuratore.getValoreCampo("Salt");

        try (PreparedStatement stmt = connection.prepareStatement(Queries.REGISTRA_CONFIGURATORE.getQuery())) {
            stmt.setString(1,nickname);
            stmt.setString(2, password);
            stmt.setString(3, sale);
            stmt.executeUpdate();
            return true;
        }
    }

}
