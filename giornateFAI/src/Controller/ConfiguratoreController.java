package Controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import DataBaseImplementation.Tupla;

import Presentation.CliInput;
import Presentation.CliNotifiche;
import Presentation.CliVisualizzazione;
import ServicesAPI.Configuratore;
import ServicesAPI.DTObject;
import ServicesAPI.DateRange;
import ServicesAPI.Eccezioni;
import ServicesAPI.Eccezioni.ConfigFilesException;
import ServicesAPI.Eccezioni.DBConnectionException;
import ServicesAPI.Login;
import ServicesAPI.PlannerVisite;
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
        } catch (Eccezioni.DBConnectionException e) {
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
        try {
            if(aux.nonCisonoLuoghiRegistrati()) {

                CliVisualizzazione.avvisoDBVuoto();
                //prima chiede all'utente di inserire l'area di competenza e il max numero partecipanti
                inserisciAreaCompetenza();
                inserisciMaxPartecipanti();

                //poi gli chiede di popolare il corpo dei dati
                popolaDBLuoghiVisteVolontari();
            }
        } catch (Exception e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
        }

        //manda alla pagina di configuazione se rileva che è il giorno di configurazione
        if (giornoDiConfigurazione()) {
            aggiungiDatePrecluse();
            registraNuovoPianoVisite();
        }

        //per nuove funzioni agigungere nuove righe
        Map<String, Runnable> actions = new LinkedHashMap<>();
        actions.put("Modifica max numero partecipanti", this::inserisciMaxPartecipanti);
        actions.put("Introduzione nuovo tipo di visita", this::inserisciNuovoTipoDiVisita);
        actions.put("Introduzione nuovo volontario", this::inserisciNuovoVolontario);
        actions.put("Introduzione nuovo luogo", this::popolaDBLuoghi); //se introducessi inserisci luogo sarrebbe una funzione identica
        actions.put("Visualizza elenco volontari", this::visualizzaElencoVolontari);     
        actions.put("Visualizza luoghi visitabili", this::visualizzaElencoLuoghi);
        actions.put("Visualizza tipi di visite", this::visualizzaElencoTipiDiVisite);
        actions.put("Visualizza visite in archivio a seconda dello stato", this::chiediStatoDaVisualizzare);
        actions.put("Rimuovi volontario dalla lista", this::rimuoviVolontario);
        actions.put("Rimuovi luogo", this::rimuoviLuogo);
        actions.put("Rimuovi tipo di visita", this::rimuoviTipoDiVisita);

        actions.put("Esci",() -> System.exit(0));

        //genero dinamicamente il menu in base alle aizoni disponibili
        String[] opzioniConfiguratore = actions.keySet().toArray(new String[0]);

        while (true) {
            int scelta = CliInput.menuAzioni(getModel().getNickname(), opzioniConfiguratore);
            //scelta va da 1 a n+1, quindi se è uguale a n+1 esce
            actions.get(opzioniConfiguratore[scelta - 1]).run();
        }      
    }

    private void visualizzaElencoVolontari() {
        CliVisualizzazione.barraIntestazione(model.getNickname());
        try {
            CliVisualizzazione.visualizzaRisultati(model.getVisualizzatore().visualizzaElencoVolontari(), "Volontari");
        } catch (DBConnectionException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_CONNESSIONE);
        }
    }

    private void visualizzaElencoLuoghi() {
        CliVisualizzazione.barraIntestazione(model.getNickname());
        try {
            CliVisualizzazione.visualizzaRisultati(model.getVisualizzatore().visualizzaElencoLuoghi(), "Luoghi");
        } catch (DBConnectionException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_CONNESSIONE);
        }
    }

    private void visualizzaElencoTipiDiVisite() {
        CliVisualizzazione.barraIntestazione(model.getNickname());
        try {
            CliVisualizzazione.visualizzaRisultati(model.getVisualizzatore().visualizzaElencoTipiDiVisite(), "Tipi Di Visite");
        } catch (DBConnectionException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_CONNESSIONE);
        }
    }

    private void inserisciAreaCompetenza() {
        String areaCompetenza = CliInput.chiediConConferma(CliVisualizzazione.AREA_COMPETENZA);
        try {
            model.getRegistratore().modificaAreaCompetenza(areaCompetenza);
        } catch (ConfigFilesException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_SCRITTURA_FILE);
        }
    }

    private void inserisciMaxPartecipanti() {
        int maxPartecipanti = Integer.parseInt(CliInput.chiediConConferma(CliVisualizzazione.MAX_PARTECIPANTI));
        try {
            model.getRegistratore().modificaMaxPartecipanti(maxPartecipanti);
        } catch (ConfigFilesException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_SCRITTURA_FILE);
        }
    }

    private void inserisciNuovoVolontario() {
        String nickname = registraNomeVolontario();
        CliNotifiche.avvisa(CliNotifiche.NECESSARIO_ABBINARE_VOLONTARIO);
        CliInput.invioPerContinuare();
        DTObject visitaAbbinata;
        boolean altraVisita = true;
        try {
            while (altraVisita) {
                visitaAbbinata = CliInput.SelezionaTipoVisita(model.getVisualizzatore().visualizzaElencoTipiDiVisite());
                associaVolontarioTipoVisita(nickname, visitaAbbinata);
                altraVisita = CliInput.aggiungiAltroCampo("Tipo di visita", "volontario");
            } 
        }catch (DBConnectionException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_CONNESSIONE);
        }
 
    }

    //ritorna il nickname del volontario se l'inserimento è riuscito, null altrimenti
    private String registraNomeVolontario() {
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
        } catch (Eccezioni.DBConnectionException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
        }
        CliInput.invioPerContinuare();
        return nickname;
    }

    private void inserisciNuovoTipoDiVisita () {
        //Chiede la lista di nomi dei luoghi possibile su cui inserire la visita
        List<String> luoghiDisponibili;
        try {
            luoghiDisponibili = model.getVisualizzatore().listaLuoghiRegistrati();
            String luogoSelezionato = CliInput.selezionaLuogo(luoghiDisponibili);
            //se seleziona un luogo fra quelli disponibili inoltra alla procedura di inserimento delle visite
            if (luogoSelezionato != null) popolaDBTipiVisite(luogoSelezionato);
        }catch (DBConnectionException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_CONNESSIONE);
        }
    }
    
    private void chiediStatoDaVisualizzare() {
        CliVisualizzazione.barraIntestazione(model.getNickname());
        StatiVisite stato = CliInput.chiediStatoVisita();
        try {
            CliVisualizzazione.visualizzaRisultati(
                model.getVisualizzatore().visualizzaVisite(stato), "Archivio di: " + stato.toString());
        } catch (DBConnectionException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_CONNESSIONE);
        }
    }

    private void popolaDBLuoghiVisteVolontari() {
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
        } catch (Eccezioni.CoerenzaException e) {
           CliNotifiche.avvisa(CliNotifiche.NOME_LUOGO_GIA_USATO);
        } catch (Eccezioni.DBConnectionException e) {
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
            String[] giorniSettimana = CliInput.chiediGiorniSettimanaVisita();
            data.impostaValore(giorniSettimana, "Giorni settimana");

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

    private void popolaDBVolontari(DTObject data) {
        boolean altroVolontario = true;        
        while (altroVolontario) {
            //pagina per i volontari, prima mostra quelli gia registrati e chiede di sceglierne uno
            VisualizzatoreConfiguratore aux = model.getVisualizzatore();
            //visualizza la lista di volontari già registrati
            DTObject[] tabellaVolontari;
            try {
                tabellaVolontari = aux.visualizzaElencoVolontari();
                List <String> volontariRegistrati = estraiCampoTabella(tabellaVolontari, "Volontario Nickname");
                String volontarioSelezionato = CliInput.selezionaVolontarioConNull(volontariRegistrati);
                //altrimenti permette di inserire un nuovo volontario se prima ha inserito null
                if (volontarioSelezionato == null) volontarioSelezionato = registraNomeVolontario();
                associaVolontarioTipoVisita(volontarioSelezionato, data);
            } catch (Exception e) {
                CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
            }
            altroVolontario = CliInput.aggiungiAltroCampo("Volontario", "Tipo di visita");
        }        
    }

    /**
     * Associa un volontario a una visita. Se il volontario è già associato alla visita, l'operazione fallisce.
     * @param nomeVolontario il nome del volontario da associare
     * @param visita l'oggetto visita a cui associare il volontario
     */
    private void associaVolontarioTipoVisita (String nomeVolontario, DTObject visita) {
        try {
            //parte di inserimento nel db della nuova coppia visita e volontario associato
            DTObject associazione = new Tupla("Volontari disponibili", new String[]{"CodiceVisita", "Volontario"});
        
            //In questa implementazione le visite sono identificate da un codice ma
            //possono essere sostitutiti da altri tipi di chiave a seconda dell'implmentazione
            //in caso basta cambiare l'identificatore della visita
            int codiceVisita = (int) visita.getValoreCampo("Codice Tipo di Visita");
            associazione.impostaValore(codiceVisita, "CodiceVisita");
            associazione.impostaValore(nomeVolontario, "Volontario");
            model.getRegistratore().associaVolontarioVisita(associazione);
            CliNotifiche.avvisa(CliNotifiche.VOLONTARIO_CORRETTAMENTE_ASSOCIATO);
        } catch (Eccezioni.CoerenzaException e) {
            CliNotifiche.avvisa(CliNotifiche.VOLONTARIO_GIA_ABBINATO_VISITA);
        } catch (Eccezioni.DBConnectionException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
        }
    }

    private void aggiungiDatePrecluse() {
        CliNotifiche.avvisa(CliNotifiche.GIORNO_CONFIGURAZIONE);
        Date[] input = CliInput.chiediDatePrecluse(model.getCalendario());
        try {
            model.getRegistroDatePrecluse().registraDatePrecluse(input);
        } catch (FileNotFoundException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_SCRITTURA_FILE);
        }
    } 

    private boolean giornoDiConfigurazione () {
        try {
            return model.getRegistroDatePrecluse().giornoDiConfigurazione();
        } catch (ConfigFilesException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_LETTURA_FILE);
        }
        return true;
    }

    private boolean registraNuovoPianoVisite() {
		VisualizzatoreConfiguratore aux = model.getVisualizzatore();
        PlannerVisite planner = new PlannerVisite(model.getRegistroDateDisponibili(), aux);
        boolean statoPianoVisite = true;
        try {
            DTObject[] tabella;
            tabella = aux.visualizzaElencoVolontari();
            List<String> lista = estraiCampoTabella(tabella, "Volontario Nickname");
            DTObject[] proposte = planner.creaPianoVisite(lista);
            for (DTObject proposta : proposte) {
                boolean registrato = model.getRegistratore().registraIstanzaDiVisita(proposta);
                if (! registrato) statoPianoVisite = false;
            }
            return statoPianoVisite;
        } catch (DBConnectionException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_CONNESSIONE);
        }
        return false;
	}

    private List<String> estraiCampoTabella(DTObject[] tabella, String nomeCampo) {

        List<String> listaElementi = new ArrayList<>(); 
        for (DTObject tupla : tabella) {
            String elemento = (String) tupla.getValoreCampo(nomeCampo);
            if (listaElementi.isEmpty() || !listaElementi.contains(elemento)){ 
                //aggiunge l'elemento alla lista di quelli già registrati
                listaElementi.add(elemento);
            }   
        }
        return listaElementi;
    }

    private void rimuoviVolontario() {

        VisualizzatoreConfiguratore aux = model.getVisualizzatore();
        try {
            DTObject[] tabella;
            tabella = aux.visualizzaElencoVolontari();
            List<String> lista = estraiCampoTabella(tabella, "Volontario Nickname"); //violazione abbasstanza brutta di inversione della dipendenza
            String volontarioSelezionato = CliInput.selezionaVolontario(lista);
            model.getRegistratore().rimozioneVolontario(volontarioSelezionato);
            model.getRegistratore().verificaCoerenzaPostRimozione();
            model.getRegistroDateDisponibili().eliminaVolontario(volontarioSelezionato);
        } catch (DBConnectionException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_CONNESSIONE);
        }
    }

    private void rimuoviLuogo() {

        VisualizzatoreConfiguratore aux = model.getVisualizzatore();
        try {
            DTObject[] tabella;
            tabella = aux.visualizzaElencoLuoghi();
            List<String> lista = estraiCampoTabella(tabella, "Nome");
            String elemento = CliInput.selezionaLuogo(lista);
            model.getRegistratore().rimozioneLuogo(elemento);
            model.getRegistratore().verificaCoerenzaPostRimozione();
        } catch (DBConnectionException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_CONNESSIONE);
        }
    }

    private void rimuoviTipoDiVisita() {

        VisualizzatoreConfiguratore aux = model.getVisualizzatore();
        try {
            DTObject[] tabella;
            tabella = aux.visualizzaElencoTipiDiVisite();
            String elemento = (String) CliInput.SelezionaTipoVisita(tabella).getValoreCampo("Titolo");
            model.getRegistratore().rimozioneVisita(elemento);
            model.getRegistratore().verificaCoerenzaPostRimozione();
        } catch (DBConnectionException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_CONNESSIONE);
        }
    }

}
