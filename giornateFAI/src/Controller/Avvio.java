package Controller;

import Presentation.CliInput;
import Presentation.CliNotifiche;
import Presentation.CliVisualizzazione;
import ServicesAPI.Login;
import ServicesAPI.Utente;

public class Avvio {

    private Login login;

    public Avvio(Login login) {
        this.login = login;
    }

    public void avviaApp() {

        //chiede il login finche non è valido
        CliNotifiche.avvisa(CliNotifiche.BENVENUTO);
        Utente utente = null;
        do {
            try {
                utente = login.loginUtente(CliInput.chiediConLunghezzaMax(CliVisualizzazione.VARIABILE_NICKNAME, CliInput.MAX_CARATTERI_NICKNAME),
                CliInput.chiediConLunghezzaMax(CliVisualizzazione.VARIABILE_PASSWORD, CliInput.MAX_CARATTERI_PASSWORD));
            } catch (Exception e) {
                CliNotifiche.avvisa(CliNotifiche.ERRORE_CONNESSIONE);
            }
            if (utente == null) CliNotifiche.avvisa(CliNotifiche.CREDENZIALI_ERRATE);
        } while (utente == null);

        UtenteController controller = FactoryController.associaController(utente);

        if (utente.isPrimoAccesso()) {
           controller.registrati();
        }
        
        controller.accediSistema();
    }
}
