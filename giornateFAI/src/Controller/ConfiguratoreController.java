package Controller;

import java.sql.SQLIntegrityConstraintViolationException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import DataBaseImplementation.Tupla;
import Presentation.CliInput;
import Presentation.CliNotifiche;
import Presentation.CliVisualizzazione;
import ServicesAPI.Configuratore;
import ServicesAPI.DTObject;
import ServicesAPI.DateRange;
import ServicesAPI.Login;
import ServicesAPI.Registratore;
import ServicesAPI.StatiVisite;
import ServicesAPI.VisualizzatoreConfiguratore;

public class ConfiguratoreController implements UtenteController {

    private Configuratore model;
    
    public ConfiguratoreController (Configuratore model) {
        this.model = model;
    }

    public Configuratore getModel() {
        return this.model;
    }

    public void accediSistema() {
        menuConfiguratore();
    }

    public void registrati(Login login) {
        CliNotifiche.avvisa(CliNotifiche.BENVENUTO_NUOVO_CONFIGURATORE);
        boolean registrato = false;
        try {

            do {
                String nickname = CliInput.chiediConLunghezzaMax(CliVisualizzazione.VARIABILE_NICKNAME, CliInput.MAX_CARATTERI_NICKNAME);
                if (model.getRegistratore().nomeUtenteUnivoco(nickname)) {

                    DTObject dati = new Tupla("Configuratore", Tupla.FORMATO_UTENTE);
                    dati.impostaValore(nickname, "Nickname");
                    dati.impostaValore(CliInput.chiediConLunghezzaMax(
                        CliVisualizzazione.VARIABILE_PASSWORD, CliInput.MAX_CARATTERI_PASSWORD), "Password");
                    registrato = login.registraNuovoConfiguratore(dati);
                    if (registrato) CliNotifiche.avvisa(CliNotifiche.CONFIGURATORE_CORRETTAMENTE_REGISTRATO);
                }
                else CliNotifiche.avvisa(CliNotifiche.NICKNAME_GIA_USATO);

            } while (!registrato);
        } catch (Exception e) {
            //se c'è un errore salata la registraizone e va direttamente all'interno per non bloccare il flusso di esecuzione
            CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
        }   
    }

    /**
    * Questa funzione servirà da menu in cui il configuratore potra decidere a che opzione accedere
    */
    private void menuConfiguratore() {

        CliVisualizzazione.ingressoBackendConfiguratore();
        VisualizzatoreConfiguratore aux = model.getVisualizzatore();

        /*Se è la prima volta che il configuratore accede al db dei luoghi e non ci sono dati
        *nel database, deve iniziare la procedura di popolamento generale del corpo dei dati
        */
        if(aux.nonCisonoLuoghiRegistrati()) {

            CliVisualizzazione.avvisoDBVuoto();
            //prima chiede all'utente di inserire l'area di competenza e il max numero partecipanti
            inserisciAreaCompetenza();
            inserisciMaxPartecipanti();

            //poi gli chiede di popolare il corpo dei dati
            popolaDBLuoghiVisteVolontari();
        }

        //manda alla pagina di configuazione se rileva che è il giorno di configurazione
        if (giornoDiConfigurazione()) {
            aggiungiDatePrecluse();
        }

        //per nuove funzioni agigungere nuove righe
        Map<Integer, Runnable> actions = new HashMap<>();
        actions.put(1, this::inserisciMaxPartecipanti);
        actions.put(2, this::inserisciNuovoTipoDiVisita);
        actions.put(3, this::insersciVolontario);
        actions.put(4, this::visualizzaElencoVolontari);     
        actions.put(5, this::visualizzaElencoLuoghi);
        actions.put(6, this::visualizzaElencoTipiDiVisite);
        actions.put(7, this::chiediStatoDaVisualizzare);

        while (true) {
            int scelta = CliInput.menuAzioni(getModel().getNickname(), opzioniConfiguratore);
            if (scelta > actions.size()) break;
            actions.get(scelta).run();
        }      
    }

    //le opzioni stampate a video, in modo da da sapere in che ordine le vede l'utente
    private static final String[] opzioniConfiguratore = {
        "Modifica max numero partecipanti",
        "Introduzione nuovo tipo di visita",
        "Introduzione nuovo volontario",
        "Visualizza elenco volontari",
        "Visualizza luoghi visitabili",
        "Visualizza tipi di visite",
        "Visualizza visite in archivio a seconda dello stato",
        "Esci" 
    };

    private void visualizzaElencoVolontari() {
        CliVisualizzazione.barraIntestazione(model.getNickname());
        CliVisualizzazione.visualizzaRisultati(model.getVisualizzatore().visualizzaElencoVolontari(), "Volontari");
    }

    private void visualizzaElencoLuoghi() {
        CliVisualizzazione.barraIntestazione(model.getNickname());
        CliVisualizzazione.visualizzaRisultati(model.getVisualizzatore().visualizzaElencoLuoghi(), "Luoghi");
    }

    private void visualizzaElencoTipiDiVisite() {
        CliVisualizzazione.barraIntestazione(model.getNickname());
        CliVisualizzazione.visualizzaRisultati(model.getVisualizzatore().visualizzaElencoTipiDiVisite(), "Tipi Di Visite");
    }

    public void inserisciAreaCompetenza() {
        String areaCompetenza = CliInput.chiediConConferma(CliVisualizzazione.AREA_COMPETENZA);
        model.getRegistratore().modificaAreaCompetenza(areaCompetenza);
    }

    public void inserisciMaxPartecipanti() {
        int maxPartecipanti = Integer.parseInt(CliInput.chiediConConferma(CliVisualizzazione.MAX_PARTECIPANTI));
        model.getRegistratore().modificaMaxPartecipanti(maxPartecipanti);
    }

    //ritorna il nickname del volontario se l'inserimento è riuscito, null altrimenti
    public String insersciVolontario() {
        String nickname = null;
        Registratore aux = model.getRegistratore();
        CliVisualizzazione.intestazionePaginaInserimento(CliVisualizzazione.VARIABILE_VOLONTARI);
        
        try {
            Boolean registrato = false;
            do {

                nickname = CliInput.chiediConLunghezzaMax(CliVisualizzazione.VARIABILE_NICKNAME, CliInput.MAX_CARATTERI_NICKNAME);
                //controlla se non è gia stato inserito
                if (aux.nomeUtenteUnivoco(nickname)) {
                    DTObject dati = new Tupla("Volontario", Tupla.FORMATO_UTENTE);
                    dati.impostaValore(nickname, "Nickname");
                    dati.impostaValore(Login.defaultPasswordVolontario, "Password");
                    registrato = aux.registraNuovoVolontario(dati);
                    if (registrato) CliNotifiche.avvisa(CliNotifiche.VOLONTARIO_CORRETTAMENTE_REGISTRATO);
                }
                else CliNotifiche.avvisa(CliNotifiche.NICKNAME_GIA_USATO);
            } while (!registrato);
        } catch (Exception e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
        }
        CliInput.invioPerContinuare();
        return nickname;
    }

    public void inserisciNuovoTipoDiVisita () {
        //Chiede la lista di nomi dei luoghi possibile su cui inserire la visita
        List<String> luoghiDisponibili = model.getVisualizzatore().listaLuoghiRegistrati();
        String luogoSelezionato = CliInput.selezionaLuogo(luoghiDisponibili);
        //se seleziona un luogo fra quelli disponibili inoltra alla procedura di inserimento delle visite
        if (luogoSelezionato != null) popolaDBTipiVisite(luogoSelezionato);
    }
    
    public void chiediStatoDaVisualizzare() {
        CliVisualizzazione.barraIntestazione(model.getNickname());
        StatiVisite stato = CliInput.chiediStatoVisita();
        CliVisualizzazione.visualizzaRisultati(
            model.getVisualizzatore().visualizzaVisite(stato), "Archivio di: " + stato.toString());
    }

    public void popolaDBLuoghiVisteVolontari() {
        //struttra ricorsiva scomposta nelle 3 funzioni private di popolamento
        boolean altroLuogo = true;
        while (altroLuogo) {
            this.popolaDBLuoghi();
            altroLuogo = CliInput.aggiungiAltroCampo("Luogo", null);
        }
    }

    private void popolaDBLuoghi () {
        //pagina per i luoghi
        CliVisualizzazione.intestazionePaginaInserimento("Luogo");
        String nomeLuogo = CliInput.chiediConLunghezzaMax(CliVisualizzazione.VARIABILE_NICKNAME, CliInput.MAX_CARATTERI_NICKNAME);
        try {
            DTObject dati = new Tupla("Luogo", Tupla.FORMATO_LUOGO);
            dati.impostaValore(nomeLuogo, "Nome");
            dati.impostaValore(CliInput.chiediConLunghezzaMax(CliVisualizzazione.VARIABILE_DESCRIZIONE, CliInput.MAX_CARATTERI_DESCRIZIONE), "Descrizione");
            dati.impostaValore(CliInput.chiediConLunghezzaMax(CliVisualizzazione.VARIABILE_INDIRIZZO, CliInput.MAX_CARATTERI_INDIRIZZO), "Indirizzo");
            model.getRegistratore().registraNuovoLuogo(dati);
            CliNotifiche.avvisa(CliNotifiche.LUOGO_CORRETTAMENTE_REGISTRATO);
            CliVisualizzazione.avvisaReindirizzamentoNuovoCampo("Tipi di visita", "Luogo");
            popolaDBTipiVisite(nomeLuogo);
        } catch (SQLIntegrityConstraintViolationException e) {
           CliNotifiche.avvisa(CliNotifiche.NOME_LUOGO_GIA_USATO);
        } catch (Exception e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
        }
    }

    
    private void popolaDBTipiVisite(String nomeLuogo) {
        //pagina per i tipi di visite
        CliVisualizzazione.intestazionePaginaInserimento("Tipo di visita");
        boolean altraVisita = true;
        while (altraVisita) {
            
            DTObject data = new Tupla("Tipo visita", Tupla.FORMATO_TIPO_VISITA);
            data.impostaValore(nomeLuogo, "Punto di Incontro");
            data.impostaValore(CliInput.chiediConLunghezzaMax(CliVisualizzazione.VARIABILE_TITOLO, CliInput.MAX_CARATTERI_TITOLO), "Titolo");
            data.impostaValore(CliInput.chiediConLunghezzaMax(CliVisualizzazione.VARIABILE_DESCRIZIONE, CliInput.MAX_CARATTERI_DESCRIZIONE), "Descrizione");
            DateRange perido = CliInput.inserimentoPeriodoAnno();
            data.impostaValore(perido.getStartDate(), "Giorno inzio");
            data.impostaValore(perido.getEndDate(), "Giorno fine");
            data.impostaValore(CliInput.inserimentoOraInizio(), "Ora di inizio");
            data.impostaValore(CliInput.inserimentoDurataVisita(), "Durata");
            data.impostaValore(CliInput.chiediNecessitaBiglietto(), "Necessita Biglietto");
            int minPartecipanti = CliInput.inserimentoPartecipantiVisita(1, "minimo");
            data.impostaValore(minPartecipanti, "Min Partecipanti");
            data.impostaValore(CliInput.inserimentoPartecipantiVisita(minPartecipanti, "massimo"), "Max Partecipanti");
            data.impostaValore(model.getNickname(), "Configuratore referente");

            try {
                model.getRegistratore().registraNuovoTipoVisita(data);
                CliNotifiche.avvisa(CliNotifiche.VISITA_CORRETTAMENTE_REGISTRATA);
                CliVisualizzazione.avvisaReindirizzamentoNuovoCampo("Volontario", "Tipo di visita");
                popolaDBVolontari(data);
            } catch (Exception e) {
                CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
            }
            altraVisita = CliInput.aggiungiAltroCampo("Tipo di visita", "Luogo");
        }
    }

    private static List<String> sanificaLista(List<String> listaVolontari) {
        // Usare un HashSet per rimuovere i duplicati
        Set<String> setVolontari = new LinkedHashSet<>(listaVolontari);
        
        // Restituire una nuova ArrayList senza duplicati
        return new ArrayList<>(setVolontari);
    }

    private void popolaDBVolontari(DTObject data) {
        boolean altroVolontario = true;
        
        //In questa implementazione le visite sono identificate da un codice ma
        //possono essere sostitutiti da atltri tipi di chiave a seconda dell'implmentazione
        //in caso basta cambiare l'identificatore 
        int codiceVisita = (int) data.getValoreCampo("Codice Tipo di Visita"); 
        
        while (altroVolontario) {
            //pagina per i volontari, prima mostra quelli gia registrati e chiede di sceglierne uno
            List <String> volontariRegistrati = new ArrayList<>();
            VisualizzatoreConfiguratore aux = model.getVisualizzatore();
            
            try {
                volontariRegistrati = aux.listaCompletaVolontari();
                //filtra eventuali ripetizioni
                volontariRegistrati = sanificaLista(volontariRegistrati);
                String volontarioSelezionato = CliInput.selezionaVolontario(volontariRegistrati);
                //altrimenti permette di inserire un nuovo volontario se prima ha inserito null
                while (volontarioSelezionato == null) volontarioSelezionato = insersciVolontario();
            
                //parte di inserimento nel db della nuova coppia visita e volontario associato
                DTObject associazione = new Tupla("Volontari disponibili", new String[]{"CodiceVisita", "Volontario"});
                associazione.impostaValore(codiceVisita, "CodiceVisita");
                associazione.impostaValore(volontarioSelezionato, "Volontario");
                model.getRegistratore().associaVolontarioVisita(associazione);
                CliNotifiche.avvisa(CliNotifiche.VOLONTARIO_CORRETTAMENTE_ASSOCIATO);
            } catch (SQLIntegrityConstraintViolationException e) {
                CliNotifiche.avvisa(CliNotifiche.VOLONTARIO_GIA_ABBINATO_VISITA);
            } catch (Exception e) {
                CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
            }
            altroVolontario = CliInput.aggiungiAltroCampo("Volontario", "Tipo di visita");
        }        
    }

    public void aggiungiDatePrecluse() {
        CliNotifiche.avvisa(CliNotifiche.GIORNO_CONFIGURAZIONE);
        Date[] input = CliInput.chiediDatePrecluse(model.getCalendario());
        model.getRegistroDatePrecluse().registraDatePrecluse(input);
    } 

    public boolean giornoDiConfigurazione () {
        try {
            return model.getRegistroDatePrecluse().giornoDiConfigurazione();
        } catch (Exception e) {
            CliNotifiche.avvisa(CliNotifiche.ERROE_CREAZIONE_FILE);
        }
        return true;
    }

}
