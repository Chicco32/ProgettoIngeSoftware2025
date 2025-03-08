import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import it.unibs.fp.mylib.InputDati;

public class CliUtente {

    //Menu di intestazione con informazioni di base
    public static void barraIntestazione(String utente) {
         LocalDate data = LocalDate.now();
         LocalDateTime ora = LocalDateTime.now();
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
         String oraFormattata = ora.format(formatter);
         System.out.println(data + "\t\t\t" + oraFormattata + "    " + utente);
    }

    private static void pulisciSchermo() {
        //codice demoniaco per invocare la pulizia dello schermo
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void invioPerContinuare () {
        InputDati.leggiStringa("Premi invio per continuare");
    }

    //Interazione per la creazione di un nuovo Configuratore
    public static void creaNuovoConfiguratore() {
        System.out.println("Benvenuto! Per creare un nuovo Configuratore inserisci le seguenti informazioni:");
    }

    //Interazione di Configuratore coorettamente registrato
    public static void configuratoreCorrettamenteRegistrato() {
        System.out.println("Configuratore correttamente reigstrato!");
    }

    //Interazione di errore nella registrazione
    public static void erroreRegistrazione() {
        System.out.println("Errore nella registrazione!");
    }

    //Interazione di Configuratore coorettamente registrato
    public static void volontarioCorrettamenteRegistrato() {
        System.out.println("Volontario correttamente registrato!");
    }

    //Interazione di Configuratore correttamente associato
    public static void volontarioCorrettamenteAssociato() {
        System.out.println("Volontario correttamente associato!");
    }

    //Interazione di Configuratore coorettamente registrato
    public static void LuogoCorrettamenteRegistrato() {
        System.out.println("Luogo correttamente registrato!");
    }

    //Interazione di Configuratore coorettamente registrato
    public static void visitaCorrettamenteRegistrata() {
        System.out.println("Visita correttamente registrata!");
    }
    
    //Interazione di bentoranto al Configuratore
    public static void benvenutoConfiguratore() {
        System.out.println("Benvenuto Configuratore! Stai per accedere al backEnd di sistema");
        CliUtente.invioPerContinuare();
    }

    //Interazione per chiedere il nickname
    public static String chiediNickname() {
        String nome = InputDati.leggiStringa("Nickname: ");
        while (nome.length() > 45) {
            System.out.println("Nickname troppo lungo, inseriscine uno più breve");
            nome = InputDati.leggiStringa("Nickname: ");
        }
        return nome; 
    }

    //Interazione per chiedere una password
    public static String chiediPassword() {
        String nome = InputDati.leggiStringa("Password: ");
        while (nome.length() > 45) {
            System.out.println("Password troppo lunga, inseriscine una più breve");
            nome = InputDati.leggiStringa("Password: ");
        }
        return nome; 
    }

    //avviso di reindirizzamento alla creaizone del profilo
    public static void creaProfilo() {
        System.out.println("Ti rimandiamo alla creazione del tuo profilo!");
    }

    //avviso di login riuscito
    public static void loginRiuscito() {
        System.out.println("Login riuscito!");
    }

    //avviso di credenziali errate
    public static void credenzialiErrate() {
        System.out.println("Credenziali errate!");
    }

    //avviso di nickname già in uso
    public static void nicknameGiaInUso() {
        System.out.println("Nickname già in uso, inseriscine un altro!");
    }

    //avviso di nome del luogo già in uso
    public static void NomeLuogoGiaInUso() {
        System.out.println("Questo luogo risulta gia inserito o ve nè un'altro con lo stesso nome, inseriscine un altro!");
    }

    //chiede all'utente di inserire il campo area di competenza e di confermare la scelta
    public static String chiediAreaCompetenza() {
        boolean conferma = false;
        do{
            String area = InputDati.leggiStringaNonVuota("Inserisci la area di competenza della società: ");
            conferma = InputDati.yesOrNo("Confermi l'area di competenza inserita? ");
            if(conferma){
                return area;
            }
        }while(!conferma);
        return null;
    }

    //chiede all'utente di inserire il campo massimo partecipanti e di confermare la scelta
    public static int chiediMaxPartecipanti() {
        boolean conferma = false;
        do{
            int maxPartecipanti = InputDati.leggiIntero("Inserisci il numero massimo di partecipanti che lo stesso fruitore può avere: ", 1, 1000);
            conferma = InputDati.yesOrNo("Confermi il numero massimo di partecipanti inserito? ");
            if(conferma){
                return maxPartecipanti;
            }
        }while(!conferma);
        return 0;
    }

    // Menu di interazione con il configuratore
    public static int menuConfiguratore(String utente) {
        boolean conferma = false;
        int scelta = 0;
        do {

            CliUtente.pulisciSchermo();
            CliUtente.barraIntestazione(utente);
            System.out.println("Scegli un'opzione dal menu:");
            System.out.println("1. Modifica max numero partecipanti");
            System.out.println("2. Introduzione nuovo tipo di visita");
            System.out.println("3. Introduzione nuovo volontario");
            System.out.println("4. Visualizza elenco volontari");
            System.out.println("5. Visualizza luoghi visitabili");
            System.out.println("6. Visualizza tipi di visite");
            System.out.println("7. Visualizza visite in archivio a seconda dello stato");
            System.out.println("8. Esci");
            
            scelta = InputDati.leggiIntero("Inserisci il numero della tua scelta: ", 1, 8);
            conferma = InputDati.yesOrNo("Confermi la scelta inserita? ");
            CliUtente.pulisciSchermo();
        } while (!conferma);
        
        return scelta;
    }

    public static void visualizzaRisultati(ResultSet results, String tabella) {
        System.out.println("Visualizzazione dei risultati della tabella: " + tabella);
        try {

            //ottengo il numero di colonne
            int numColonne = results.getMetaData().getColumnCount();
            
            //stampo i nomi delle colonne
            for (int i = 1; i <= numColonne; i++) {
                System.out.print(results.getMetaData().getColumnName(i) + "\t");
            }
            System.out.println("\n");

            //stampo i risultati
            while (results.next()) {
                for (int i = 1; i <= numColonne; i++) {
                    System.out.print(results.getString(i) + "\t");
                }
                System.out.println("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Fine tabella");
        CliUtente.invioPerContinuare();
    }

    public static void visualizzaArchivioVisite(StatiVisite stato) {
        System.out.println("Visualizzazione delle visite con stato: " + stato);
    }

    public static StatiVisite chiediStatoVisita(String utente) {
        CliUtente.barraIntestazione(utente);
        System.out.println("Stati possibili su cui filtrare le visite: ");
        System.out.println("1. Proposta");
        System.out.println("2. Completa");
        System.out.println("3. Annullata");
        System.out.println("4. Confermata");
        System.out.println("5. Effettuata");
        int scelta = InputDati.leggiIntero("Inserisci il numero dello stato da visualizzare: ", 1, 5);
        StatiVisite stato = null;
        switch (scelta) {
            case 1:
                stato = StatiVisite.PROPOSTA;
                break;
            case 2:
                stato = StatiVisite.COMPLETA;
                break;
            case 3:
                stato = StatiVisite.ANNULLATA;
                break;
            case 4:
                stato = StatiVisite.CONFERMATA;
                break;
            case 5:
                stato = StatiVisite.EFFETTUATA;
                break;
            default:
                break;
        }
        return stato;
    }

    public static void inserimentoVolontario() {
        CliUtente.pulisciSchermo();
        System.out.println("Configuratore, questa è la pagina per l'inserimento di nuovi volontari nel DB");
        System.out.println("Di seguito puoi riportare i dati dei volontari");
    }

    public static void inserimentoNuovoLuogo() {
        CliUtente.pulisciSchermo();
        System.out.println("Configuratore, questa è la pagina per l'inserimento di nuovi luoghi nel DB");
        System.out.println("Di seguito puoi riportare i dati del luogo");
    }

    public static void inserimentoNuovoTipoVisita() {
        CliUtente.pulisciSchermo();
        System.out.println("Configuratore, questa è la pagina per l'inserimento di nuovi tipi di Visita nel DB");
        System.out.println("Di seguito puoi riportare i dati della visita");
    }

    public static String inserimentoNuovoNome() {
        String nome = InputDati.leggiStringa("Nome: ");
        while (nome.length() > 45) {
            System.out.println("Nome troppo lungo, inseriscine uno più breve");
            nome = InputDati.leggiStringa("Nome: ");
        }
        return nome; 
    }

    public static String inserimentoNuovaDescrizione() {
        String nome = InputDati.leggiStringa("Descrizione: ");
        while (nome.length() > 200) {
            System.out.println("Descrizione troppo lunga, inseriscine una più breve");
            nome = InputDati.leggiStringa("Descrizione: ");
        }
        return nome; 
    }

    public static String inserimentoNuovoIndirizzo() {
        String nome = InputDati.leggiStringa("Indirizzo: ");
        while (nome.length() > 45) {
            System.out.println("Indirizzo troppo lungo, inseriscine uno più breve");
            nome = InputDati.leggiStringa("Indirizzo: ");
        }
        return nome;  
    }

    public static void avvisoDBVuoto() {
        System.out.println("Configuratore il DB deve ancora essere popolato per poter avviare l'applicazione a regime.");
        System.out.println("Verrai Reindirizzato alla pagina per la creazione del corpo dei dati");
        CliUtente.invioPerContinuare();
        CliUtente.pulisciSchermo();        
    }

    public static void avvisoNuovoLuogo() {
        System.out.println("Configuratore, per ogni nuovo luogo bisogna associare almeno un tipo di visita, verrai rindirizzato alla pagina per la registrazione"); 
        CliUtente.invioPerContinuare();
        CliUtente.pulisciSchermo();
    }

    public static void avvisoNuovoTipoVisita() {
        System.out.println("Configuratore, per ogni nuovo tipo di visita bisogna associare almeno un volontario disponibile, verrai rindirizzato alla pagina per la registrazione"); 
        CliUtente.invioPerContinuare();
        CliUtente.pulisciSchermo();
    }


    public static String inserimentoNuovoTitolo() {
        String nome = InputDati.leggiStringa("Titolo: ");
        while (nome.length() > 45) {
            System.out.println("Titolo troppo lungo, inseriscine uno più breve");
            nome = InputDati.leggiStringa("Titolo: ");
        }
        return nome; 
    }

    public static DateRange inserimentoPeriodoAnno() {
        boolean conferma = false;
        Date dataInizio = null;
        Date dataFine = null;
        System.out.println("Inserisci il periodo dell'anno:");
        do {
            try {
                String dataInizioStr = InputDati.leggiStringa("Data di inizio (yyyy-MM-dd): ");
                dataInizio = Date.valueOf(dataInizioStr);
                String dataFineStr = InputDati.leggiStringa("Data di fine (yyyy-MM-dd): ");
                dataFine = Date.valueOf(dataFineStr);
                //non posso invertire le date
                if (!dataFine.after(dataInizio)) {
                    System.out.println("La data di fine non può precedere quella di inizio");
                    throw new IllegalArgumentException();
                }
                conferma = InputDati.yesOrNo("Confermi il periodo inserito? ");
            } catch (IllegalArgumentException e) {
                System.out.println("Formato data non valido. Riprova.");
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

    public static int inserimentoMinPartecipantiVisita() {
        boolean conferma = false;
        int minPartecipanti = 0;
        do {
            minPartecipanti = InputDati.leggiIntero("Inserisci il numero minimo di partecipanti per la visita: ", 1, 1000);
            conferma = InputDati.yesOrNo("Confermi il numero minimo di partecipanti inserito? ");
        } while (!conferma);
        return minPartecipanti;
    }

    public static int inserimentoMaxPartecipantiVisita(int minPartecipanti) {
        boolean conferma = false;
        do {
            minPartecipanti = InputDati.leggiIntero("Inserisci il numero massimo di partecipanti per la visita: ", minPartecipanti, 1000);
            conferma = InputDati.yesOrNo("Confermi il numero massimo di partecipanti inserito? ");
        } while (!conferma);
        return minPartecipanti;
    }    

    /*
     * la funzione mostra all'utente i volontari gia registrait e chiede di scegliere fra uno di essi,
     * se non sceglie nessuno della lista per inserire uno nuovo o la lista è vuota ritorna null 
     */
    public static String selezionaVolontario (ArrayList<String> volontariRegistrati) {
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

    public static String selezionaLuogo (ArrayList<String> luoghiRegistrati) {

        System.out.println("Luoghi registrati:");
        for (int i = 0; i < luoghiRegistrati.size(); i++) {
            System.out.println((i + 1) + ". " + luoghiRegistrati.get(i));
        }

        boolean indietro = false;
        indietro = CliUtente.tornareIndietro();
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

    private static boolean tornareIndietro() {
        return InputDati.yesOrNo("Desideri tornare indietro?");        
    }
        
    public static boolean aggiungiAltroLuogo () {
        boolean input = InputDati.yesOrNo("Vuoi aggiungere un ulteriore luogo?");
        CliUtente.pulisciSchermo();
        return input;
    }

    public static boolean aggiungiAltraVisitaLuogo () {
        boolean input =  InputDati.yesOrNo("Vuoi aggiungere un ulteriore tipo di visita a questo luogo?");
        CliUtente.pulisciSchermo();
        return input;
    }

    public static boolean aggiungiAltroVolontarioVisita () {
        boolean input =  InputDati.yesOrNo("Vuoi aggiungere un ulteriore volontario a questo tipo di visita?");
        CliUtente.pulisciSchermo();
        return input;
    }

    public static void volontarioGiaAbbinatoVisita() {
        System.out.println("Questo volontario è gia stato registrato per questo tipo di visita");
    }




}
