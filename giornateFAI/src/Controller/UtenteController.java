package Controller;

import ServicesAPI.Login;
import ServicesAPI.Utente;

public interface UtenteController {

    /**
     * Ritorna l'oggetto {@code Utente} riferito a questo controller specifico
     * @return un istanza di {@code Utente}
     */
    public Utente getModel();

    /**
     * Istanza specifica per l'utente che avvia la procedura di registraizone dell'utente nel database
     */
    public void registrati(Login login);

    /**
     * Metodo che permette l'accesso al controller di sistema e alle funzionalità specifiche per l'utente a cui fa riferimento.
     * Idealmente questo metodo va chiamato solo una volt aall'inzio per reindirizzare l'utente verso l'area del sistema di sua competenza
     * o su cui ha i permessi di accesso
     */
    public void accediSistema();

}
