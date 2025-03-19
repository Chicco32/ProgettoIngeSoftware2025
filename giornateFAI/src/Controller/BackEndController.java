package Controller;

import java.util.HashMap;
import java.util.Map;

import DataBaseImplementation.Queries;
import Presentation.CliInput;
import Presentation.CliVisualizzazione;
import ServicesAPI.Configuratore;
import ServicesAPI.Utente;

/**
 * Questa classe rappresenta il back-end del sistema, ovvero la parte che si occupa di gestire i dati e le operazioni
 * che il configuratore può fare sul sistema. Questa classe si occupa dell'interazione con l'utente e delega le operazioni
 * all'utente stesso in base alle scelte effettuate e alle azioni che l'utente ha i diritti di fare.
 * 
 * @see Utente
 * @see Configuratore
 */
public class BackEndController {

    public BackEndController() {
    }

    /**
     * Questa funzione servirà da menu in cui il configuratore potra decidere a che opzione accedere
     */
    public void menuConfiguratore(ConfiguratoreController configuratore) {

        CliVisualizzazione.benvenutoBackendConfiguratore();

        /*Se è la prima volta che il configuratore accede al db dei luoghi e non ci sono dati
        *nel database, deve iniziare la procedura di popolamento generale del corpo dei dati
        */
        if(configuratore.controllaDBVuoti("luogo")) {

            CliVisualizzazione.avvisoDBVuoto();
            //prima chiede all'utente di inserire l'area di competenza e il max numero partecipanti
            configuratore.inserisciAreaCompetenza();
            configuratore.inserisciMaxPartecipanti();

            //poi gli chiede di popolare il corpo dei dati
            configuratore.popolaDBLuoghiVisteVolontari();
        }

        //manda alla pagina di configuazione se rileva che è il giorno di configurazione
        if (configuratore.giornoDiConfigurazione()) {
            configuratore.aggiungiDatePrecluse();
        }

        //per nuove funzioni agigungere nuove righe
        Map<Integer, Runnable> actions = new HashMap<>();
        actions.put(1, configuratore::inserisciMaxPartecipanti);
        actions.put(2, configuratore::inserisciNuovoTipoDiVisita);
        actions.put(3, configuratore::insersciVolontario);
        actions.put(4, () -> configuratore.visualizzaTabellaDatabase(Queries.SELEZIONA_VOLONTARI, "Volontari"));
        actions.put(5, () -> configuratore.visualizzaTabellaDatabase(Queries.SELEZIONA_LUOGHI, "Luoghi"));
        actions.put(6, () -> configuratore.visualizzaTabellaDatabase(Queries.SELEZIONA_TIPI_VISITE, "Tipi di visite"));
        actions.put(7, configuratore::chiediStatoDaVisualizzare);

        while (true) {
            int scelta = CliInput.menuConfiguratore(configuratore.getModel().getNickname());
            if (scelta > actions.size()) break;
            actions.get(scelta).run();
        }

        /* Di seguito la lista nel cli
        1. Modifica max numero partecipanti
        2. Introduzione nuovo tipo di visita
        3. Introduzione nuovo volontario
        4. Visualizza elenco volontari
        5. Visualizza luoghi visitabili
        6. Visualizza tipi di visite
        7. Visualizza visite in archivio a seconda dello stato
        8. Esci" */
        
    }
    
}
