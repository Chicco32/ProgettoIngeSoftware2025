package Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import DataBaseImplementation.Tupla;
import Presentation.CliInput;
import Presentation.CliNotifiche;
import Presentation.CliVisualizzazione;
import ServicesAPI.DTObject;
import ServicesAPI.Login;
import ServicesAPI.RegistroDateDisponibili;
import ServicesAPI.Utente;
import ServicesAPI.Volontario;

public class VolontarioController implements UtenteController {

    private Volontario model;

    public VolontarioController (Volontario model) {
        this.model = model;
    }

    public Utente getModel() {
        return this.model;
    }

    public void accediSistema() {
        menuVolontario();
    }

    public void registrati(Login login) {
        CliNotifiche.avvisa(CliNotifiche.BENVENUTO_NUOVO_VOLONTARIO);
        boolean passwordCambiata = false;
        try {
            do {
                String nickname = model.getNickname();
                DTObject dati = new Tupla("Configuratore", Tupla.FORMATO_UTENTE);
                dati.impostaValore(nickname, "Nickname");
                dati.impostaValore(CliInput.chiediConLunghezzaMax(
                    CliVisualizzazione.VARIABILE_PASSWORD, CliInput.MAX_CARATTERI_PASSWORD), "Password");
                passwordCambiata = login.cambioPassword(dati, model.getRuolo());
                if (passwordCambiata) {
                    CliNotifiche.avvisa(CliNotifiche.CONFIGURATORE_CORRETTAMENTE_REGISTRATO);
                    model.registrati(dati);
                }        
                
            } while (!passwordCambiata);
        } catch (Exception e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_ACCESSO);
        }
    }

    public static final String[] opzioniVolontario = {
        "Inserisci le disponibilità",
        "Visualizza visite a cui sei associato",
        "Esci" };

    private void menuVolontario() {
        
        CliVisualizzazione.ingressoBackendVolontario();

        //per nuove funzioni agigungere nuove righe
        Map<Integer, Runnable> actions = new HashMap<>();
        actions.put(1, this::inserisciDisponibilia);
        actions.put(2, this::visualizzaVisiteAssociate);

        while (true) {
            int scelta = CliInput.menuAzioni(getModel().getNickname(), opzioniVolontario);
            if (scelta > actions.size()) break;
            actions.get(scelta).run();
        }
    }

    private void inserisciDisponibilia() {
        CliVisualizzazione.barraIntestazione(model.getNickname());
        RegistroDateDisponibili aux = model.getRegistroDateDisponibili();
        try {
            if (!aux.giornoDiConfigurazione()) {
                Date[] datePossibili = aux.calcolaPossibiliDate(model.getNickname());
                aux.registraDateDisponibili(CliInput.chiediDateDisponibilà(datePossibili), model.getNickname());
            }
            else CliVisualizzazione.inserimentoVolontarioBloccato();
        } catch (Exception e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_CONNESSIONE); //trovare l'eccezione corretta
        }
        CliInput.invioPerContinuare();
    }

    private void visualizzaVisiteAssociate() {
        String aux = model.getNickname();
        CliVisualizzazione.barraIntestazione(aux);
        CliVisualizzazione.visualizzaRisultati(model.getVisualizzatore().visualizzaElenecoTipiDiVisiteAssociate(aux), "Visite Associate");
    }

}
