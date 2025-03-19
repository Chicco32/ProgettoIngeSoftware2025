package Presentation;

//import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
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

    //Interazione di bentoranto al Configuratore
    public static void benvenutoBackendConfiguratore() {
        System.out.println("Benvenuto Configuratore! Stai per accedere al backEnd di sistema");
        CliInput.invioPerContinuare();
    }

    /*
    public static void visualizzaRisultati(ResultSet results, String tabella) {
        System.out.println("\n<========= " + tabella.toUpperCase() + " =========>");
        try {

            //ottengo il numero di colonne
            int numColonne = results.getMetaData().getColumnCount();
            
            //stampo i nomi delle colonne
            List<String> colonne = new ArrayList<>();
            for (int i = 1; i <= numColonne; i++) {
                colonne.add(results.getMetaData().getColumnName(i));
            }
    
            // Stampare la riga di intestazione
            System.out.println(String.join(" | ", colonne));

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
        CliInput.invioPerContinuare();
    } */

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
                if (results != null) {
                    for (int i = 0; i<results.length; i++) { //cicla per ogni riga della tabella
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

    //Intestazione della pagina di rindirizzamento per l'inserimento di un certo tipo di dato
    public static void intestazionePaginaInserimento(String campo) {
        CliVisualizzazione.pulisciSchermo();
        System.out.println("Configuratore, questa è la pagina per l'inserimento di " + campo + " nel DB");
        System.out.println("Di seguito puoi riportare i dati su " + campo);
    }    

    /**
     * Avviso di rindirizzamento alla pagina di inserimento di un nuovo campo.
     * @param campo il campo su cui si viene reindirizzati per l'inserimento
     * @param paginaCorrente la pagina su cui si trova al momento l'utente
     */
    public static void avvisaReindirizzamentoNuovoCampo(String campo, String paginaCorrente) {
        System.out.println("Configuratore, per ogni nuovo " + paginaCorrente + " bisogna associare almeno un " + campo + ", verrai rindirizzato alla pagina per la registrazione"); 
        CliInput.invioPerContinuare();
        pulisciSchermo(); 
    }

}
