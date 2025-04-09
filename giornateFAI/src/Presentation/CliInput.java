package Presentation;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ServicesAPI.Calendario;
import ServicesAPI.DTObject;
import ServicesAPI.DateRange;
import ServicesAPI.StatiVisite;
import it.unibs.fp.mylib.InputDati;

public class CliInput {

    public static final int MAX_CARATTERI_NICKNAME = 45;
    public static final int MAX_CARATTERI_PASSWORD = 45;
    public static final int MAX_CARATTERI_NOME = 45;
    public static final int MAX_CARATTERI_DESCRIZIONE = 200;
    public static final int MAX_CARATTERI_INDIRIZZO = 45;
    public static final int MAX_CARATTERI_TITOLO = 45;

    
    public static void invioPerContinuare() {
        InputDati.leggiStringa("Premi invio per continuare");
    }

    //Interazione per chiedere una variabile di lunghezza massima nota
    public static String chiediConLunghezzaMax(String variabile, int lunghezzaMax) {
        String nome = InputDati.leggiStringaNonVuota(variabile + ": ");
        while (nome.length() > lunghezzaMax) {
            System.out.println("Valore troppo lungo, inseriscine uno più breve");
            nome = InputDati.leggiStringa(variabile + ": ");
        }
        return nome; 
    }

    //chiede all'utente di inserire il campo e di confermare la scelta
    public static String chiediConConferma(String campo) {
        boolean conferma = false;
        do{
            String area = InputDati.leggiStringaNonVuota("Inserisci " + campo + ": ");
            conferma = InputDati.yesOrNo("Confermi " + campo + " inserito/a? ");
            if(conferma){
                return area;
            }
        }while(!conferma);
        return null;
    }

    // Menu di interazione con il configuratore
    public static int menuAzioni(String utente, String[] opzioni) {
        boolean conferma = false;
        int scelta = 0;
        do {
            CliVisualizzazione.pulisciSchermo();
            CliVisualizzazione.barraIntestazione(utente);
            for (int i = 0; i < opzioni.length; i++) {
                System.out.println((i + 1) + ") " + opzioni[i]);
            }
            scelta = InputDati.leggiIntero("Inserisci il numero della tua scelta: ", 1, opzioni.length);
            conferma = InputDati.yesOrNo("Confermi la scelta inserita? ");
            CliVisualizzazione.pulisciSchermo();
        } while (!conferma);
        
        return scelta;
    }

    public static StatiVisite chiediStatoVisita() {
        //cerca gli stati possibili
        StatiVisite[] stati = StatiVisite.values();
        Map<Integer, StatiVisite> statiMappa = new HashMap<>();
        //li carica nella mappa da mostrare
        for (int i = 0; i < stati.length; i++) {
            statiMappa.put(i + 1, stati[i]);
            System.out.println((i + 1) + ") " + stati[i]);  // Stampa le opzioni dinamicamente
        }
        int scelta = InputDati.leggiIntero("Inserisci il numero dello stato da visualizzare: ", 1, stati.length);
        return statiMappa.getOrDefault(scelta, null);
    }

    public static DateRange inserimentoPeriodoAnno() {
        boolean conferma = false;
        Date dataInizio = null;
        Date dataFine = null;
        System.out.println("Inserisci il periodo dell'anno:");
        do {
            try {
                
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String dataInizioStr = InputDati.leggiStringa("Data di inizio (yyyy-MM-dd): ");
                dataInizio = format.parse(dataInizioStr);
                String dataFineStr = InputDati.leggiStringa("Data di fine (yyyy-MM-dd): ");
                dataFine = format.parse(dataFineStr);
                //non posso invertire le date
                if (!dataFine.after(dataInizio)) throw new IllegalArgumentException();
                conferma = InputDati.yesOrNo("Confermi il periodo inserito? ");

            } catch (ParseException e) {
                System.out.println("Formato data non valido. Riprova.");
            } catch (IllegalArgumentException e) {
                System.out.println("La data di fine non può precedere quella di inizio");
            }
        } while (!conferma);
        return new DateRange(dataInizio, dataFine);
    }

    public static Time inserimentoOraInizio() {
        boolean conferma = false;
        Time oraInizio = null;
        do {

            String oraInizioStr = InputDati.leggiStringa("Ora di inizio (HH:mm): ");
            try {
            //controlla se l'orario è compreso nelle 24 ore altrimenti lancia un eccezione
            LocalTime oraValida = LocalTime.parse(oraInizioStr, DateTimeFormatter.ofPattern("HH:mm"));
            oraInizio = Time.valueOf(oraValida + ":00"); 
            conferma = InputDati.yesOrNo("Confermi l'ora di inizio inserita? ");
            } catch (IllegalArgumentException e) {
            System.out.println("Formato ora non valido. Riprova.");
            } catch (DateTimeParseException e) {
            System.out.println("Formato ora non valido. Riprova.");
            }

        } while (!conferma);
        return oraInizio;
    }

    public static int inserimentoDurataVisita() {
        boolean conferma = false;
        int durata = 0;
        do {
            durata = InputDati.leggiIntero("Inserisci la durata della visita in minuti: ", 1, 1440);
            conferma = InputDati.yesOrNo("Confermi la durata inserita? ");
        } while (!conferma);
        return durata;
    }

    public static boolean chiediNecessitaBiglietto() {
        return InputDati.yesOrNo("La visita necessita di un biglietto? ");
    }

    public static int inserimentoPartecipantiVisita(int minPartecipanti, String msg) {
        boolean conferma = false;
        int partecipanti = 0;
        do {
            partecipanti = InputDati.leggiIntero("Inserisci il numero " + msg + " di partecipanti per la visita: ", minPartecipanti, 1000);
            conferma = InputDati.yesOrNo("Confermi il numero " + msg + " di partecipanti inserito? ");
        } while (!conferma);
        return partecipanti;
    }

    public static String[] chiediGiorniSettimanaVisita () {
        CliVisualizzazione.pulisciSchermo();
        System.out.println("Di seguito inserisci i giorni in cui è possibile attuare la visita:");
        List<String> giorniSettimana = new ArrayList<>();
        String[] settimana = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica"};

        //all'inizio tutti i giorni della settimana sono validi e poi la lista si riduce
        List<String> giorniValidi = new ArrayList<>();
        for (String giorno : settimana) giorniValidi.add(giorno);
        boolean continua = true;

        while (continua && giorniValidi.size() > 0) {
            System.out.println("Giorni disponibili:");
            for (int i = 0; i < giorniValidi.size(); i++) {
            System.out.println((i + 1) + ") " + giorniValidi.get(i));
            }

            int scelta = InputDati.leggiIntero("Seleziona il numero del giorno della settimana: ", 1, giorniValidi.size());
            String giornoSelezionato = giorniValidi.get(scelta - 1);
            
            //se conferma il giorno giusto lo sposta dai giorni validi a quelli selezionati
            if (InputDati.yesOrNo("Confermi " + giornoSelezionato + "? ")) {
                giorniSettimana.add(giornoSelezionato);
                giorniValidi.remove(giornoSelezionato);
            }
            continua = InputDati.yesOrNo("Vuoi aggiungere un altro giorno?");
            //se continua pulisci lo schermo e rinizia
            CliVisualizzazione.pulisciSchermo();
        }

        return giorniSettimana.toArray(new String[0]);
    }
 
    /*
     * la funzione mostra all'utente i volontari gia registrati e chiede di scegliere fra uno di essi,
     * se non sceglie nessuno della lista per inserire uno nuovo o la lista è vuota ritorna null 
     */
    public static String selezionaVolontarioConNull (List<String> volontariRegistrati) {
        if (volontariRegistrati.isEmpty()) {
            System.out.println("Nessun volontario registrato.");
            return null;
        }

        System.out.println("Volontari registrati:");
        for (int i = 0; i < volontariRegistrati.size(); i++) {
            System.out.println((i + 1) + ". " + volontariRegistrati.get(i));
        }

        boolean nuovaSelezione = InputDati.yesOrNo("Vuoi registrare un nuovo volontario che non sia fra quelli nell'elenco qua sopra? ");
        if (nuovaSelezione) {
            return null;
        }

        boolean conferma = false;
        while (!conferma) {
            int scelta = InputDati.leggiIntero("Seleziona il numero del volontario: ", 1, volontariRegistrati.size());
            String volontarioSelezionato = volontariRegistrati.get(scelta - 1);
            conferma = InputDati.yesOrNo("Hai selezionato " + volontarioSelezionato + ". Confermi? ");
            if (conferma){
                return volontarioSelezionato;
            }
        }
        return null;
    }

    public static String selezionaVolontario(List<String> volontariRegistrati) {
        if (volontariRegistrati.isEmpty()) {
            System.out.println("Nessun volontario registrato.");
            return null;
        }

        System.out.println("Volontari registrati:");
        for (int i = 0; i < volontariRegistrati.size(); i++) {
            System.out.println((i + 1) + ". " + volontariRegistrati.get(i));
        }

        boolean conferma = false;
        while (!conferma) {
            int scelta = InputDati.leggiIntero("Seleziona il numero del volontario: ", 1, volontariRegistrati.size());
            String volontarioSelezionato = volontariRegistrati.get(scelta - 1);
            conferma = InputDati.yesOrNo("Hai selezionato " + volontarioSelezionato + ". Confermi? ");
            if (conferma){
                return volontarioSelezionato;
            }
        }
        return null;
    }

    public static String selezionaLuogo (List<String> luoghiRegistrati) {

        System.out.println("Luoghi registrati:");
        for (int i = 0; i < luoghiRegistrati.size(); i++) {
            System.out.println((i + 1) + ". " + luoghiRegistrati.get(i));
        }

        boolean indietro = false;
        indietro = tornareIndietro();
        if (indietro) return null;
                
        boolean conferma = false;
        while (!conferma) {
            int scelta = InputDati.leggiIntero("Seleziona il numero del luogo: ", 1, luoghiRegistrati.size());
            String luogoSelezionato = luoghiRegistrati.get(scelta - 1);
            conferma = InputDati.yesOrNo("Hai selezionato " + luogoSelezionato + ". Confermi? ");
            if (conferma){
                return luogoSelezionato;
            }
        }
        return null;
    }

    public static DTObject SelezionaTipoVisita(DTObject[] elencoTipiDiVisite) {
        CliVisualizzazione.pulisciSchermo();
        System.out.println("Tipi di visita registrati:");
        for (int i = 0; i < elencoTipiDiVisite.length; i++) {
            System.out.println((i + 1) + ") " + elencoTipiDiVisite[i].getValoreCampo("Titolo"));
        }

        boolean conferma = false;
        while (!conferma) {
            int scelta = InputDati.leggiIntero("Seleziona il numero del tipo di visita: ", 1, elencoTipiDiVisite.length);
            DTObject visitaSelezionata = elencoTipiDiVisite[scelta - 1];
            conferma = InputDati.yesOrNo("Hai selezionato " + visitaSelezionata.getValoreCampo("Titolo") + ". Confermi? ");
            if (conferma){
                return visitaSelezionata;
            }
        }
        return null;
	}

    public static boolean tornareIndietro() {
        return InputDati.yesOrNo("Desideri tornare indietro?");        
    }

    /**
     * Funzione che chiede se riavviare la pagina di inserimento di un tipo di dato dipendente da un altro. 
     * In particolare il dato da reinserire è campo da aggiungere, mentre la pagina corrente è il valore da cui dipende.
     * Se il campo da aggiungere non dipende da nessun altro campo, inserire null a paginaCorrente
     * @param campoDaAggiungere il valore su cui si interroga se ricominciare
     * @param paginaCorrente la pagina su ci si trova ora l'utente
     * @return true se l'utente vuole inserire un altra istanza di capo da aggiungere, false altrimenti
     */
    public static boolean aggiungiAltroCampo (String campoDaAggiungere, String paginaCorrente) {
        String messaggio = "Vuoi inserire un altro " + campoDaAggiungere + (paginaCorrente != null ? " a questo " + paginaCorrente : "") + "?";
        boolean input = InputDati.yesOrNo(messaggio);
        CliVisualizzazione.pulisciSchermo();
        return input;
    }
        
    public static Date[] chiediDatePrecluse(Calendario calendario) {
        List<Date> datePrecluse = new ArrayList<>();

        int meseCorrente = calendario.getMonth();
        int annoCorrente = calendario.getYear();

        //controlla se si trova nei primi 15 o meno 
        if (calendario.getDay() <= 15) {
            meseCorrente --;
            if (meseCorrente == -1) {
                meseCorrente = 11;
                annoCorrente --;
            }
        }   

        // Calcola il mese di interesse
        int meseRichiesto = (meseCorrente + 3) % 12;
        int annoRichiesto = annoCorrente;
        if (meseRichiesto < meseCorrente) {
            annoRichiesto++; // Se il mese richiesto è gennaio dopo ottobre, incrementiamo l'anno
        }

        // Otteniamo il primo e l'ultimo giorno del mese richiesto
        Calendario calMeseRichiesto = new Calendario(annoRichiesto, meseRichiesto, 1); 
        Date primoGiornoMese = calMeseRichiesto.getTime();
        //spostalo a fine del mese richiesto
        calMeseRichiesto.add(GregorianCalendar.DATE, calMeseRichiesto.getActualMaximum(GregorianCalendar.DATE) -1 );
        Date ultimoGiornoMese = calMeseRichiesto.getTime();
        DateRange range = new DateRange(primoGiornoMese, ultimoGiornoMese);

        System.out.println("Inserisci le date precluse per "+ (meseRichiesto + 1)  + "/" + annoRichiesto);
        boolean continua = true;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        do {
            try {
                String dataStr = InputDati.leggiStringa("Inserisci una data preclusa (yyyy-MM-dd) o 'fine' per terminare: ");
                if (dataStr.equalsIgnoreCase("fine")) {
                    continua = false;
                    break;
                }
                Date dataInserita = format.parse(dataStr);
                if (!range.insideRange(dataInserita)) {
                    System.out.println("La data non rientra nel mese richiesto. Riprova.");
                } else {
                    datePrecluse.add(dataInserita);
                }
            } catch (Exception e) {
                System.out.println("Formato data non valido. Riprova.");
            }
        } while (continua);

        Date[] arrayDatePrecluse = datePrecluse.toArray(new Date[0]);
        return arrayDatePrecluse;
    }

	public static Date[] chiediDateDisponibilà(Date[] datePossibili) {
        List<Date> dateDisponibili = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dateString = new ArrayList<>();

        //stampa le date possibili in righe da 5 date
        System.out.println("Date possibili:");
        for (int i = 0; i < datePossibili.length; i++) {
            String stringa = format.format(datePossibili[i]);
            dateString.add(stringa);
            System.out.print(stringa);
            System.out.print("\t");
            if (i % 5 == 4) System.out.println(""); //ritorna a capo ogni 5 elementi
        }
        System.out.println("");

        boolean continua = true;
        do {
            try {
                String inserimento = InputDati.leggiStringa("Inserisci una data disponibile (yyyy-MM-dd) o 'fine' per terminare: ");
                if (inserimento.equalsIgnoreCase("fine")) {
                    continua = false;
                    break;
                }

                //cerca fra le date salvate se corrisponde a una di essi
                Date dataInserita = new Date();
                boolean valida = false;
                for (String data : dateString) {
                    if (data.equalsIgnoreCase(inserimento)) {
                        valida = true;
                        dataInserita = format.parse(inserimento);
                        break;
                    }
                }
                
                //comportamento dopo l'inserimento
                if (!valida) {
                    System.out.println("La data inserita non è tra quelle possibili. Riprova.");
                } else if (dateDisponibili.contains(dataInserita)) {
                    System.out.println("Hai già selezionato questa data. Riprova.");
                } else {
                    dateDisponibili.add(dataInserita);
                }
            } catch (ParseException e) {
                System.out.println("Formato data non valido. Riprova.");
            }
        } while (continua);

        return dateDisponibili.toArray(new Date[0]);
	}

	public static int selezionaIstanza(DTObject[] VisiteDisponibili) {

        while (true) {
            int codiceArchivio = InputDati.leggiIntero("Inserisci il codice archivio della visita desiderata: ");

            for (int i = 0; i < VisiteDisponibili.length; i++) {
                if (VisiteDisponibili[i].getValoreCampo("Codice Archivio").equals(codiceArchivio)) {
                    CliVisualizzazione.pulisciSchermo();
                    return codiceArchivio; // Ritorna l'indice della visita selezionata
                }
            }

            System.out.println("Codice archivio non valido. Riprova.");
        }
	}

}
