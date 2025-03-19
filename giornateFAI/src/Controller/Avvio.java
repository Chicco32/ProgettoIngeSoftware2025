package Controller;

import java.sql.Connection;
import ConfigurationFiles.ConnessioneSQL;
import ConfigurationFiles.LoginSQL;
import Presentation.CliInput;
import Presentation.CliNotifiche;
import Presentation.CliVisualizzazione;
import Services.Utente;
import Services.Login;

public class Avvio {

    public Avvio() {
        //crea la prima e unica istanza di sqlconnection
        ConnessioneSQL.getConnection();
        notificaConnesione(ConnessioneSQL.getConnection());
    }

    public Avvio(String url, String user, String psw) {
        //crea la prima e unica istanza di sqlconnection
        ConnessioneSQL.getConnection(url,user,psw);
        notificaConnesione(ConnessioneSQL.getConnection());
    }

    private void notificaConnesione(Connection connection) {
        if (connection != null) CliNotifiche.avvisa(CliNotifiche.CONNESSIONE_RIUSCITA);
        else CliNotifiche.avvisa(CliNotifiche.ERRORE_CONNESSIONE);
    }

    public void avviaApp() {

        Login login = new LoginSQL();

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
        } while (utente == null);

        UtenteController controller = FactoryController.associaController(utente);

        if (utente.isPrimoAccesso()) {
           controller.registrati();
        }
        
        controller.accediSistema();
    }
}
