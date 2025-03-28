package ServicesAPI;

/**
 * Interfaccia che definisce i metodi per la gestione del login e del cambio password
 * per gli utenti di un sistema.
 */
public interface Login {

    public static final String defaultPasswordVolontario = "volontario";

    /**
     * Metodo di login per l'utente generico. Il metodo deve occuparsi sia di riconoscere
     * che tipo di utente sta tentando di accedere sia di verificarne l'autenticità.
     * Il metodo, per assicurare l'autenticazione, ritorna un oggetto di tipo
     * {@code Utente} che varia a seconda del ruolo di chi accede oppure {@code null}.
     * 
     * @param nickname Il nickname inserito dall'utente nel form di login.
     * @param password La password inserita dall'utente nel form di login.
     * @return Un oggetto {@code Utente} se le credenziali sono corrette e l'utente è verificato,
     *         altrimenti {@code null} se le credenziali sono errate o non registrate.
     * @throws DBConnectionException In caso di errore di connessione al database.
     */
    public Utente loginUtente(String nickname, String password) throws Eccezioni.DBConnectionException;

    /**
     * Metodo per consentire il cambio della password di un utente.
     * @param datiUtente un oggetto {@DTObject} con i nuovi dati dell'utente
     * @param ruolo il ruolo che ricopre l'utente
     * @return {@code true} se il cambio password è avvenuto con successo, altrimenti {@code false}.
     * @throws DBConnectionException In caso di errore di connessione al database.
     */
    public boolean cambioPassword(DTObject datiUtente, String ruolo) throws Eccezioni.DBConnectionException;

    /**
     * Questo metodo verifica se un dato nome utente è univoco, cioè se non è già stato registrato da 
     * nessun altro utente, indipendentemente dal loro ruolo. 
     * Viene invocato ogni volta che un utente tenta di registrare un nuovo nickname.
     * @param nomeUtente Il nome utente che si vuole registrare.
     * @return true se il nome utente non è già registrato, false altrimenti.
     * @throws DBConnectionException In caso di errore di connessione al database.
     */
    public boolean nomeUtenteUnivoco(String nomeUtente) throws Eccezioni.DBConnectionException;

    /**
     * Funzione per la registrazione di un nuovo configuratore nel DB.
     * In particolare la funzione richiede al DB l'inserimento dei dati forniti e riporta la risposta del DB in caso di avvenuto inserimento o meno
     * La funzione ritorna true se la registrazione è andata a buon fine, false altrimenti.
     * 
     * @param nickname il possibile nickname da registrare
     * @param password la password inserita dall'utente
     * @return lo stato della registrazione, true se è andata a buon fine, false altrimenti
     * @throws DBConnectionException In caso di errore di connessione al database.
     */
    public boolean registraNuovoConfiguratore(DTObject configuratore) throws Eccezioni.DBConnectionException;

}
