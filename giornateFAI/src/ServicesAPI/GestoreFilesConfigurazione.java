package ServicesAPI;

import java.io.File;

/**
 * Interfaccia per la gestione delle impostazioni di Configurazione.
 * Questa inerfaccia dovrebbe essere implementata da un gestore di lettura e scrittura di dati per poter permettere
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
     * Crea un file
     * @param path il percorso del file
     */
    public static void creaFile(String path) {
        try {
            File file = new File(path);
            file.createNewFile();
        } catch (Exception e) {
            System.out.println("Errore nella creazione del file:");
            System.out.println(e.getMessage());
        }
    }

    public String getPath();

    /**
     * Legge una variabile da un file sapendone il tag associato.
     * Questo metodo permette di leggere solo un valore e funziona solo se di quel tag c'è una sola occorrenza
     * altrmenti legge la prima occorrenza disponibile nel file.
     * @param path il percorso del file
     * @param tag il tag associato alla variabile da leggere
     * @return Il valore della variabile letta nel file
     */
    public String leggiVariabile (String tag);

}
