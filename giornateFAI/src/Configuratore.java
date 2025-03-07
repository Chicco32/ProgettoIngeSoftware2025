import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class Configuratore extends Utente {
    
    private Registratore registratore;

    public Configuratore(boolean PrimoAccesso) {
        super(PrimoAccesso);
        this.setRuolo("Configuratore");
        this.registratore = new Registratore();
    }

    public void registrati() {
        CliUtente.creaNuovoConfiguratore();
        do {
            // Chiede il nickname e la password finche' non vengono inseriti correttamente
            this.setNickname(CliUtente.chiediNickname());
        } while (!registratore.registraNuovoConfiguratore(this.getNickname(), CliUtente.chiediPassword()));
        this.setPrimoAccesso(false);
    }

    public void inserisciAreaCompetenza() {
        String areaCompetenza = CliUtente.chiediAreaCompetenza();
        registratore.modificaAreaCompetenza(areaCompetenza);
    }

    public void inserisciMaxPartecipanti() {
        int maxPartecipanti = CliUtente.chiediMaxPartecipanti();
        registratore.modificaMaxPartecipanti(maxPartecipanti);
    }

    public void insersciVolontario() {
        CliUtente.menuInserimentoVolontario();
        this.registratore.registraNuovoVolontario(CliUtente.chiediNickname(), CliUtente.chiediPassword());
    }

    /**
     * Questo metodo permette di controllare se una tabella del database è vuota. In particolare questa funzione permette l'interazione
     * fra il Configuratore e il database per controllare se una tabella è vuota.
     * @param tabella la tabella da controllare accetta come valore "Luogo" o "Tipo di Visita"
     * @return true se la tabella è vuota, false altrimenti
     */
    public boolean controllaDBVuoti(String tabella) {
        try {
            switch (tabella) {
                case "Luogo":
                    return this.visualizzatore.DBLuoghiIsEmpty();
                case "Tipo di Visita":
                    return this.visualizzatore.DBTipiVisiteIsEmpty();
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void visualizzaVolontari() {
        this.visualizzatore.visualizzaVolontari();
    }

    public void visualizzaLuoghiDaVisitare() {
        this.visualizzatore.visualizzaLuoghiDaVisitare();
    }

    public void visualizzaTipiDiVisite() {
        this.visualizzatore.visualizzaTipiDiVisite();
    }
    
    public void chiediStatoDaVisualizzare() {
        this.visualizzatore.visualizzaVisite(CliUtente.chiediStatoVisita(this.getNickname()));
    }

    public void popolaDBLuoghiVisteVolontari() {
        //tutto il corpo da inserie vedi casi d'uso e consegna
        //struttra ricorsiva scomposta nelle 3 funzioni private di popolamento
        boolean altroLuogo = true;
        while (altroLuogo) {
            this.popolaDBLuoghi();
            altroLuogo = CliUtente.aggiungiAltroLuogo();
        }
        
    }

    private void popolaDBLuoghi () {
        //pagina per i luoghi
        CliUtente.inserimentoNuovoLuogo();
        String nomeLuogo = CliUtente.inserimentoNuovoNome();
        this.registratore.registraNuovoLuogo(nomeLuogo, CliUtente.inserimentoNuovaDescrizione(), CliUtente.inserimentoNuovoIndirizzo());
        CliUtente.avvisoNuovoLuogo();
        popolaDBTipiVisite(nomeLuogo);
    }

    private void popolaDBTipiVisite(String nomeLuogo) {
        //pagina per i tipi di visite
        CliUtente.inserimentoNuovoTipoVisita();
        boolean altraVisita = true;
        while (altraVisita) {
            int nuovoCodice = this.registratore.generaNuovaChiave(Registratore.TABELLATIPOVISITE);
            String titolo = CliUtente.inserimentoNuovoTitolo();
            String descrizione = CliUtente.inserimentoNuovaDescrizione();
            DateRange perido = CliUtente.inserimentoPeriodoAnno();
            Time ora = CliUtente.inserimentoOraInizio();
            int durata = CliUtente.inserimentoDurataVisita();
            boolean biglietto = CliUtente.chiediNecessitaBiglietto();
            int minPartecipanti =  CliUtente.inserimentoMinPartecipantiVisita();
            int maxPartecipanti =  CliUtente.inserimentoMaxPartecipantiVisita(minPartecipanti);
            this.registratore.registraNuovoTipoVisita(nuovoCodice, nomeLuogo, titolo, descrizione, perido.getStartDate(), perido.getEndDate(),
             ora, durata, biglietto, minPartecipanti, maxPartecipanti, this.getNickname());
            CliUtente.avvisoNuovoTipoVisita();
            popolaDBVolontari(nuovoCodice);
            altraVisita = CliUtente.aggiungiAltraVisitaLuogo();
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
            ArrayList <String> volontariRegistrati = this.visualizzatore.visualizzaVolontari();
            volontariRegistrati = sanificaLista(volontariRegistrati);
            String volontarioSelezionato = CliUtente.selezionaVolontario(volontariRegistrati);
            //altrimenti permette di inserire un nuovo volontario
            if (volontarioSelezionato == null) {
                volontarioSelezionato = CliUtente.chiediNickname();
                this.registratore.registraNuovoVolontario(volontarioSelezionato, CliUtente.chiediPassword());
            }
            //parte di inserimento nel db della nuova coppia visita e volontario associato
            this.registratore.associaVolontarioVisita(CodiceVisita, volontarioSelezionato);
            altroVolontario = CliUtente.aggiungiAltroVolontarioVisita();
        }

    }
}
