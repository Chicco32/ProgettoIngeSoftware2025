package Presentation;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import ServicesAPI.DTObject;

public class CliVisualizzazione {

    public static final String VARIABILE_NICKNAME = "Nickname";
    public static final String VARIABILE_PASSWORD = "Password";
    public static final String VARIABILE_NOME = "Nome";
    public static final String VARIABILE_DESCRIZIONE = "Descrizione";
    public static final String VARIABILE_INDIRIZZO = "Indirizzo";
    public static final String VARIABILE_TITOLO = "Titolo";
    public static final String VARIABILE_VOLONTARI = "volontari";
    public static final String VARIABILE_LUOGHI = "luoghi";
    public static final String VARIABILE_TIPI_VISITE = "tipi di visita";

    public static final String AREA_COMPETENZA = "l'area di competenza della società";
    public static final String MAX_PARTECIPANTI = "il numero massimo di partecipanti che lo stesso fruitore può avere";


    //Menu di intestazione con informazioni di base
    public static void barraIntestazione(String utente) {
         LocalDate data = LocalDate.now();
         LocalDateTime ora = LocalDateTime.now();
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
         String oraFormattata = ora.format(formatter);
         System.out.println(data + "\t\t\t" + oraFormattata + "    " + utente);
    }

    public static void pulisciSchermo() {
        //codice demoniaco per invocare la pulizia dello schermo
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void ingressoBackendConfiguratore() {
        System.out.println("Benvenuto Configuratore! Stai per accedere al Backend di sistema");
        CliInput.invioPerContinuare();
    }

    public static void ingressoBackendVolontario() {
        System.out.println("Benvenuto Volontario! Stai per accedere al Backend di sistema");
        CliInput.invioPerContinuare();
    }

    public static void ingressoFrontendFruitore() {
        System.out.println("Benvenuto Fruitore! Stai per accedere al Frontend di sistema");
        CliInput.invioPerContinuare();
    }

    /**
     * Funzione per stampare a video i dati di una qualche relazione in formato tabellare.
     * In particolare stampa ogni elemento {@code DtObject} della relazione in una riga della tabella.
     * Prima di stamparne il contenuto stampa anche i nomi delle colonne.
     * @param results L'insieme di dati da stampare, in formato {@code DTObject[]}
     * @param tabella Il nome della tabella da stampare, che verrà visualizzato come titolo informativo.
     */
    public static void visualizzaRisultati(DTObject[] results, String tabella) {
        System.out.println("\n<========= " + tabella.toUpperCase() + " =========>");
        try {
            //se non ha oggetti da stampare salta direttamente
            if(results.length > 0) {
                //ottengo il numero di colonne
                int numColonne = results[0].getNumeroCampi();
                
                //stampo i nomi delle colonne
                List<String> colonne = results[0].getCampi();

                // Stampare la riga di intestazione
                System.out.println(String.join(" | ", colonne));

                //stampo i risultati
                System.out.println(""); //per distanziare i dati dalla tabella
                if (results != null) {
                    for (int i = 0; i<results.length; i++) { //ciclo per ogni riga della tabella
                        for (int j = 0; j < numColonne; j++) { //stampa ogni elemento
                            System.out.print(results[i].getValoreCampo(colonne.get(j)) + "\t");
                        }
                        System.out.println("\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Fine tabella");
        CliInput.invioPerContinuare();
    }

    public static void avvisoDBVuoto() {
        System.out.println("Configuratore il DB deve ancora essere popolato per poter avviare l'applicazione a regime.");
        System.out.println("Verrai Reindirizzato alla pagina per la creazione del corpo dei dati");
        CliInput.invioPerContinuare();
        pulisciSchermo();        
    }

    //Intestazione della pagina di reindirizzamento per l'inserimento di un certo tipo di dato
    public static void intestazionePaginaInserimento(String campo) {
        CliVisualizzazione.pulisciSchermo();
        System.out.println("Configuratore, questa è la pagina per l'inserimento di " + campo + " nel DB");
        System.out.println("Di seguito puoi riportare i dati su " + campo);
    }
    
    //Intestazione della pagina di reindirizzamento per la rimozione di un certo tipo di dato
    public static void intestazionePaginaRimozione(String campo) {
        CliVisualizzazione.pulisciSchermo();
        System.out.println("Configuratore, questa è la pagina per la rimozione di " + campo + " nel DB");
        System.out.println("Di seguito puoi riportare i dati su " + campo);
    } 

    /**
     * Avviso di reindirizzamento alla pagina di inserimento di un nuovo campo.
     * @param campo il campo su cui si viene reindirizzati per l'inserimento
     * @param paginaCorrente la pagina su cui si trova al momento l'utente
     */
    public static void avvisaReindirizzamentoNuovoCampo(String campo, String paginaCorrente) {
        System.out.println("Configuratore, per ogni nuovo " + paginaCorrente + " bisogna associare almeno un " + campo + ", verrai reindirizzato alla pagina per la registrazione"); 
        CliInput.invioPerContinuare();
        pulisciSchermo(); 
    }

    public static void inserimentoVolontarioBloccato () {
        CliNotifiche.avvisa(CliNotifiche.GIORNO_CONFIGURAZIONE);
        System.out.println("Perciò questa azione è bloccata fino a che la configurazione non sarà completata");
    }

	public static void VisualizzaCodiceIscrizione(String codice) {
		System.out.println("Il codice di iscrizione è: " + codice);
        System.out.println("Puoi usarlo per disiscriverti in un secondo momento");
        System.out.println("Ti verrà chiesto di inserirlo al momento di inizio della visita e non potrai recuperarlo dalla piattaforma, quindi annotatelo bene!");
        CliInput.invioPerContinuare();
        pulisciSchermo();
	}

}
