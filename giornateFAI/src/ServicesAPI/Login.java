package ServicesAPI;

public interface Login {

    /**
     * Metodo di login per il configuratore
     * @param nickname Il nickname dell'utente inserisce nel form
     * @param password La password dell'utente inserisce nel form
     * @return Un oggetto di tipo Utente, null se le credenziali sono sbagliate
     * 
     * @throws Exception in caso di errore di connessione al database
     */
    public Utente loginUtente(String nickname, String password) throws Exception;

}
