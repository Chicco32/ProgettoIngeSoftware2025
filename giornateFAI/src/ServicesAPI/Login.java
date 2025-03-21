package ServicesAPI;

/**
 * Interfaccia che definisce i metodi per la gestione del login e del cambio password
 * per gli utenti di un sistema.
 */
public interface Login {

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
     * @throws Exception In caso di errore di connessione al database.
     */
    public Utente loginUtente(String nickname, String password) throws Exception;

    /**
     * Metodo per consentire il cambio della password di un utente.
     * 
     * @param nickname Il nickname dell'utente che richiede il cambio password.
     * @param password La nuova password da impostare.
     * @return {@code true} se il cambio password è avvenuto con successo, altrimenti {@code false}.
     * @throws Exception In caso di errore di connessione al database o di altri problemi.
     */
    public static boolean cambioPassword(String nickname, String password) throws Exception {
        throw new IllegalAccessException();
    };

}
