package Services;

import java.io.File;
import java.util.Date;

/**
 * Interfaccia per la gestione delle impostazioni di Configurazione.
 * Questa inerfaccia dovrebbe essere implementata da un gestore di lettura e scrittura di dati per poter permettere
 * l'accesso persistente alle impostazioni di configurazione del programma. 
 */
public interface GestoreFilesConfigurazione {

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

    /**
     * Legge una variabile da un file sapendone il tag associato.
     * Questo metodo permette di leggere solo un valore e funziona solo se di quel tag c'è una sola occorrenza
     * altrmenti legge la prima occorrenza disponibile nel file.
     * @param path il percorso del file
     * @param tag il tag associato alla variabile da leggere
     * @return Il valore della variabile letta nel file
     */
    public String leggiVariabile (String tag);

    /**
     * Scrive il file di default del registratore con i valori inseriti.
     * @param maxPartecipanti il numero massimo di partecipanti che lo stesso Fruitore può inserire
     * @param areaCompetenza l'area geografica di competenza della società
     */
    public void scriviRegistratoreDefault(String areaCompetenza, int maxPartecipanti);

    /**
	 * Scrive il file delle date precluse del mese corrente e del successivo.
	 * @param today la data odierna
	 * @param current l'array di date precluse
	 */
    public void scriviDatePrecluse(Date today, Date[] current);

    /**
	 * Funzione che legge le date precluse per il mese corrente
	 * @return l'array di date precluse
	 */
    public Date[] leggiDatePrecluse();

    /**
     * Funzione che resetta le date precluse salvate sul file
     * @param data la data odierna
     */
	public void cleanDates(Date data);

}
