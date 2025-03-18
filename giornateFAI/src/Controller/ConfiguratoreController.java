package Controller;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import ConfigurationFiles.CostantiDB;
import ConfigurationFiles.PercorsiFiles;
import ConfigurationFiles.Queries;
import ConfigurationFiles.VisualizzatoreSQL;
import Presentation.CliInput;
import Presentation.CliNotifiche;
import Presentation.CliVisualizzazione;
import Services.Calendario;
import Services.Configuratore;
import Services.DateRange;
import Services.RegistroDate;
import Services.StatiVisite;

public class ConfiguratoreController implements UtenteController {

    private Configuratore model;

    public ConfiguratoreController (Configuratore model) {
        this.model = model;
    }

    public Configuratore getModel() {
        return this.model;
    }

    public String getRuolo() {
        return model.getRuolo();
    }

    public void accediSistema() {
        new BackEndController().menuConfiguratore(this); 
    }

    public void registrati() {
        CliNotifiche.avvisa(CliNotifiche.BENVENUTO_NUOVO_CONFIGURATORE);
        boolean registrato = false;
        while (!registrato) {
            try {
                registrato = model.registrati(
                    CliInput.chiediConLunghezzaMax(
                    CliVisualizzazione.VARIABILE_NICKNAME, CliInput.MAX_CARATTERI_NICKNAME),
                    CliInput.chiediConLunghezzaMax(
                    CliVisualizzazione.VARIABILE_PASSWORD, CliInput.MAX_CARATTERI_PASSWORD));
            } catch (SQLIntegrityConstraintViolationException e) {
                CliNotifiche.avvisa(CliNotifiche.NICKNAME_GIA_USATO);
            } catch (Exception e) {
                CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
            }   
        }
        CliNotifiche.avvisa(CliNotifiche.CONFIGURATORE_CORRETTAMENTE_REGISTRATO);
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
        String nickname;
        CliVisualizzazione.intestazionePaginaInserimento(CliVisualizzazione.VARIABILE_VOLONTARI);
        try {
            nickname = CliInput.chiediConLunghezzaMax(CliVisualizzazione.VARIABILE_NICKNAME, CliInput.MAX_CARATTERI_NICKNAME);
            model.getRegistratore().registraNuovoVolontario(nickname,
                CliInput.chiediConLunghezzaMax(
                CliVisualizzazione.VARIABILE_PASSWORD, CliInput.MAX_CARATTERI_PASSWORD)
            );
            CliNotifiche.avvisa(CliNotifiche.VOLONTARIO_CORRETTAMENTE_REGISTRATO);
            return nickname;
        } catch (SQLIntegrityConstraintViolationException e) {
            CliNotifiche.avvisa(CliNotifiche.NICKNAME_GIA_USATO);;
        } catch (Exception e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
        }
        return null;
    }

    public void inserisciNuovoTipoDiVisita () {
        //Chiede la lista di nomi dei luoghi possibile su cui inserire la visita
        ArrayList<String> luoghiDisponibili = new ArrayList<>();
        String luogoSelezionato = null;
        VisualizzatoreSQL aux = model.getVisualizzatore();
        try {
            luoghiDisponibili = aux.estraiColonna(
                aux.visualizzaTabella(Queries.SELEZIONA_LUOGHI.getQuery()), "Nome");
            luogoSelezionato = CliInput.selezionaLuogo(luoghiDisponibili);
        } catch (IllegalArgumentException | SQLException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_QUERY);
        }
        //se seleziona un luogo fra quelli disponibili inoltra alla procedura di inserimento delle visite
        if (luogoSelezionato != null) popolaDBTipiVisite(luogoSelezionato);
    }

    /**
     * Questo metodo permette di controllare se una tabella del database è vuota. In particolare questa funzione permette l'interazione
     * fra il Configuratore e il database per controllare se una tabella è vuota.
     * @param tabella la tabella da controllare accetta come valore "luogo" o "tipo di Visita"
     * @return true se la tabella è vuota, false altrimenti
     */
    public boolean controllaDBVuoti(String tabella) {
        try {
            model.getVisualizzatore().tabellaDBVuota(tabella);
        } catch (SQLException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_QUERY);
        }
        return false;
    }

    public void visualizzaTabellaDatabase(Queries query, String nomeDaMostrare) {
        try {
            CliVisualizzazione.visualizzaRisultati(
                model.getVisualizzatore().visualizzaTabella(query.getQuery()), nomeDaMostrare);
        } catch (SQLException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_QUERY);
        }
    }
    
    public void chiediStatoDaVisualizzare() {
        CliVisualizzazione.barraIntestazione(model.getNickname());
        StatiVisite stato = CliInput.chiediStatoVisita();
        try {
            CliVisualizzazione.visualizzaRisultati(
                model.getVisualizzatore().visualizzaVisite(stato), "Archivio di: " + stato.toString());
        } catch (SQLException e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_QUERY);
        }
    }

    public void popolaDBLuoghiVisteVolontari() {
        //tutto il corpo da inserie vedi casi d'uso e consegna
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
            model.getRegistratore().registraNuovoLuogo(nomeLuogo,
                CliInput.chiediConLunghezzaMax(CliVisualizzazione.VARIABILE_DESCRIZIONE, CliInput.MAX_CARATTERI_DESCRIZIONE),
                CliInput.chiediConLunghezzaMax(CliVisualizzazione.VARIABILE_INDIRIZZO, CliInput.MAX_CARATTERI_INDIRIZZO)
            );
            CliNotifiche.avvisa(CliNotifiche.LUOGO_CORRETTAMENTE_REGISTRATO);
        } catch (SQLIntegrityConstraintViolationException e) {
           CliNotifiche.avvisa(CliNotifiche.NOME_LUOGO_GIA_USATO);
        } catch (Exception e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
        }
        CliVisualizzazione.avvisaReindirizzamentoNuovoCampo("Tipi di visita", "Luogo");
        popolaDBTipiVisite(nomeLuogo);
    }

    //USARE I DTO PER MIGLIOR COMPRENSIBILITà
    private void popolaDBTipiVisite(String nomeLuogo) {
        //pagina per i tipi di visite
        CliVisualizzazione.intestazionePaginaInserimento("Tipo di visita");
        boolean altraVisita = true;
        while (altraVisita) {
            int nuovoCodice = model.getRegistratore().generaNuovaChiave(CostantiDB.TIPO_VISITA);
            String titolo = CliInput.chiediConConferma(CliVisualizzazione.VARIABILE_TITOLO);
            String descrizione = CliInput.chiediConConferma(CliVisualizzazione.VARIABILE_DESCRIZIONE);
            DateRange perido = CliInput.inserimentoPeriodoAnno();
            Time ora = CliInput.inserimentoOraInizio();
            int durata = CliInput.inserimentoDurataVisita();
            boolean biglietto = CliInput.chiediNecessitaBiglietto();
            int minPartecipanti =  CliInput.inserimentoPartecipantiVisita(1, "minimo");
            int maxPartecipanti =  CliInput.inserimentoPartecipantiVisita(minPartecipanti, "massimo");
            try {
                model.getRegistratore().registraNuovoTipoVisita(nuovoCodice, nomeLuogo, titolo, descrizione, perido.getStartDate(), perido.getEndDate(),
                  ora, durata, biglietto, minPartecipanti, maxPartecipanti, model.getNickname());
                CliNotifiche.avvisa(CliNotifiche.VISITA_CORRETTAMENTE_REGISTRATA);
            } catch (Exception e) {
                CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
            }
            CliVisualizzazione.avvisaReindirizzamentoNuovoCampo("Volontario", "Tipo di visita");
            popolaDBVolontari(nuovoCodice);
            altraVisita = CliInput.aggiungiAltroCampo("Tipo di visita", "Luogo");
        }
    }

    private static ArrayList<String> sanificaLista(ArrayList<String> listaVolontari) {
        // Usare un HashSet per rimuovere i duplicati
        Set<String> setVolontari = new LinkedHashSet<>(listaVolontari);
        
        // Restituire una nuova ArrayList senza duplicati
        return new ArrayList<>(setVolontari);
    }

    private void popolaDBVolontari(int CodiceVisita) {
        boolean altroVolontario = true;
        while (altroVolontario) {
            //pagina per i volontari, prima mostra quelli gia registrati e chiede di sceglierne uno
            ArrayList <String> volontariRegistrati = new ArrayList<>();
            VisualizzatoreSQL aux = model.getVisualizzatore();
            
            try {
                volontariRegistrati = aux.estraiColonna(
                aux.visualizzaTabella(Queries.SELEZIONA_VOLONTARI.getQuery()), "Volontario Nickname");
                volontariRegistrati = sanificaLista(volontariRegistrati);
                String volontarioSelezionato = CliInput.selezionaVolontario(volontariRegistrati);
                //altrimenti permette di inserire un nuovo volontario se prima ha inserito null
                while (volontarioSelezionato == null) volontarioSelezionato = insersciVolontario();
            
                //parte di inserimento nel db della nuova coppia visita e volontario associato
                model.getRegistratore().associaVolontarioVisita(CodiceVisita, volontarioSelezionato);
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
        model.getRegistroDate().registraDatePrecluse(input);
    } 

    public boolean giornoDiConfigurazione () {
        Calendario aux = model.getCalendario();
        RegistroDate auy = model.getRegistroDate();
        try {
            return aux.giornoDiConfigurazione() && !auy.meseGiaConfigurato(PercorsiFiles.pathDatePrecluse);
        } catch (ParseException e) {
            CliNotifiche.avvisa(CliNotifiche.ERROE_CREAZIONE_FILE);
        }
        return true;
    }

}
