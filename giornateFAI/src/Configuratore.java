package giornateFAI;

import giornateFAI.*;
import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class Configuratore extends Utente {
    
    private Registratore registratore;

    public Configuratore(boolean PrimoAccesso, Connection conn) {
        super(PrimoAccesso,conn);
        this.setRuolo("Configuratore");
        this.registratore = new Registratore(conn);
    }

    public void registrati() {
        CliUtente.creaNuovoConfiguratore();
        boolean registrato = false;
        while (!registrato) {
            try {
                // Chiede il nickname e la password finche' non vengono inseriti correttamente
                this.setNickname(CliUtente.chiediNickname());
                registrato = registratore.registraNuovoConfiguratore(this.getNickname(), CliUtente.chiediPassword());
            } catch (SQLIntegrityConstraintViolationException e) {
                CliUtente.nicknameGiaInUso();
                e.printStackTrace();
            } catch (Exception e) {
                CliUtente.erroreRegistrazione();
                e.printStackTrace();
            }   
        }
        CliUtente.configuratoreCorrettamenteRegistrato();
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
        CliUtente.inserimentoVolontario();
        try {
            this.registratore.registraNuovoVolontario(CliUtente.chiediNickname(), CliUtente.chiediPassword());
            CliUtente.volontarioCorrettamenteRegistrato();
        } catch (SQLIntegrityConstraintViolationException e) {
            CliUtente.nicknameGiaInUso();
            e.printStackTrace();
        } catch (Exception e) {
            CliUtente.erroreRegistrazione();
            e.printStackTrace();
        }
    }

    public void inserisciNuovoTipoDiVisita () {
        ArrayList<String> luoghiDisponibili = this.visualizzatore.visualizzaLuoghiDaVisitare();
        String luogoSelezionato = CliUtente.selezionaLuogo(luoghiDisponibili);
        if (luogoSelezionato != null) popolaDBTipiVisite(luogoSelezionato);
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
        try {
            this.registratore.registraNuovoLuogo(nomeLuogo, CliUtente.inserimentoNuovaDescrizione(), CliUtente.inserimentoNuovoIndirizzo());
            CliUtente.LuogoCorrettamenteRegistrato();
        } catch (SQLIntegrityConstraintViolationException e) {
            CliUtente.NomeLuogoGiaInUso();
        } catch (Exception e) {
            CliUtente.erroreRegistrazione();
            e.printStackTrace();
        }
        CliUtente.avvisoNuovoLuogo();
        popolaDBTipiVisite(nomeLuogo);
    }

    private void popolaDBTipiVisite(String nomeLuogo) {
        //pagina per i tipi di visite
        CliUtente.inserimentoNuovoTipoVisita();
        boolean altraVisita = true;
        while (altraVisita) {
            int nuovoCodice = this.registratore.generaNuovaChiave(CostantiDB.TIPO_VISITA);
            String titolo = CliUtente.inserimentoNuovoTitolo();
            String descrizione = CliUtente.inserimentoNuovaDescrizione();
            DateRange perido = CliUtente.inserimentoPeriodoAnno();
            Time ora = CliUtente.inserimentoOraInizio();
            int durata = CliUtente.inserimentoDurataVisita();
            boolean biglietto = CliUtente.chiediNecessitaBiglietto();
            int minPartecipanti =  CliUtente.inserimentoMinPartecipantiVisita();
            int maxPartecipanti =  CliUtente.inserimentoMaxPartecipantiVisita(minPartecipanti);
            try {
                this.registratore.registraNuovoTipoVisita(nuovoCodice, nomeLuogo, titolo, descrizione, perido.getStartDate(), perido.getEndDate(),
                 ora, durata, biglietto, minPartecipanti, maxPartecipanti, this.getNickname());
                CliUtente.visitaCorrettamenteRegistrata();
                } catch (Exception e) {
                CliUtente.erroreRegistrazione();
                e.printStackTrace();
            }
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
                try {
                    this.registratore.registraNuovoVolontario(volontarioSelezionato, CliUtente.chiediPassword());
                    CliUtente.volontarioCorrettamenteRegistrato();
                } catch (SQLIntegrityConstraintViolationException e) {
                    CliUtente.nicknameGiaInUso();
                } catch (Exception e) {
                    CliUtente.erroreRegistrazione();
                    e.printStackTrace();
                }
            }
            //parte di inserimento nel db della nuova coppia visita e volontario associato
            try {
                this.registratore.associaVolontarioVisita(CodiceVisita, volontarioSelezionato);
                CliUtente.volontarioCorrettamenteAssociato();
            } catch (SQLIntegrityConstraintViolationException e) {
                CliUtente.volontarioGiaAbbinatoVisita();
            } catch (Exception e) {
                CliUtente.erroreRegistrazione();
                e.printStackTrace();
            }
            altroVolontario = CliUtente.aggiungiAltroVolontarioVisita();
        }

    }
}
