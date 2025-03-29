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

    private void menuVolontario() {
        
        CliVisualizzazione.ingressoBackendVolontario();

        //per nuove funzioni agigungere nuove righe
        Map<String, Runnable> actions = new HashMap<>();
        actions.put("Inserisci le disponibilità", this::inserisciDisponibilita);
        actions.put("Visualizza visite a cui sei associato", this::visualizzaVisiteAssociate);

        //genero dinamicaemnte il menu in vbase alle aizoni disponibili
        String[] opzioniConfiguratore = new String[actions.size() + 1];
        int i = 0;
        for(String azione : actions.keySet()) {
            opzioniConfiguratore[i] = azione;
            i++;
        }
        opzioniConfiguratore[i] = "Esci";

        while (true) {
            int scelta = CliInput.menuAzioni(getModel().getNickname(), opzioniConfiguratore);
            //scelta va da 1 a n+1, quindi se è uguale a n+1 esce
            if (scelta > actions.size()) break;
            actions.get(opzioniConfiguratore[scelta - 1]).run();
        } 
    }

    private void inserisciDisponibilita() {
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
        try {
            CliVisualizzazione.visualizzaRisultati(model.getVisualizzatore().visualizzaElenecoTipiDiVisiteAssociate(aux), "Visite Associate");
        } catch (Exception e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
        }
    }

}
