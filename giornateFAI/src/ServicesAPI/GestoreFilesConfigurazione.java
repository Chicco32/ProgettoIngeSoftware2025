package ServicesAPI;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Interfaccia Repository per la gestione delle impostazioni di Configurazione.
 * Questa interfaccia dovrebbe essere implementata da un gestore di lettura e scrittura di dati per poter permettere
 * l'accesso persistente alle impostazioni di configurazione del programma. 
 */
public abstract interface GestoreFilesConfigurazione {

    /**
     * Controlla se un file esiste
     * @param path il percorso del file
     * @return true se il file esiste, false altrimenti
     */
    public static boolean fileExists(String path) {
        return new File(path).exists();
    }

    /**
     * Crea un file nel percorso specificato se il file non esiste gia, altrimenti non fa nulla.
     * @param path il percorso del file
     */
    public static void creaFile(String path) {
        try {
            File file = new File(path);
            file.createNewFile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Ritorna il percorso del file di configurazione associato al gestore.
     * @return Il percorso del file
     */
    public String getPath();

    /**
     * Legge una variabile da un file sapendone il tag associato.
     * Questo metodo permette di leggere solo un valore e funziona solo se di quel tag c'è una sola occorrenza
     * altrimenti legge la prima occorrenza disponibile nel file.
     * @param path il percorso del file
     * @param tag il tag associato alla variabile da leggere
     * @return Il valore della variabile letta nel file
     * @throws FileNotFoundException se il file non esiste o ci sono problemi di accesso
     */
    public String leggiVariabile (String tag) throws FileNotFoundException;

}
