package ServicesAPI;

import java.io.FileNotFoundException;
import java.util.Date;

/**
 * Interfaccia Repository per la gestione delle date precluse.
 * Questa interfaccia dovrebbe essere implementata da un gestore di lettura e scrittura di dati per poter permettere
 * l'accesso persistente alle date precluse del programma. 
 * In particolare questa inerfaccia estende le funzionalità di GestoreFilesConfigurazione per la gestione generica dei file di configurazione..
 * 
 * @see GestoreFilesConfigurazione
 */
public interface GestoreDatePrecluse extends GestoreFilesConfigurazione{

    /**
	 * Scrive il file delle date precluse del mese corrente e del successivo.
	 * @param today la data odierna
	 * @param current l'array di date precluse
	 * @throws FileNotFoundException se il file non esiste o ci sono problemi di accesso
	 */
    public void scriviDatePrecluse(Date today, Date[] current) throws FileNotFoundException;

    /**
	 * Funzione che legge le date precluse per il mese corrente
	 * @return l'array di date precluse
	 * @throws FileNotFoundException se il file non esiste o ci sono problemi di accesso
	 */
    public Date[] leggiDatePrecluse() throws FileNotFoundException;

    /**
     * Funzione che resetta le date precluse salvate sul file
     * @param data la data odierna
     */
	public void cleanDates(Date data);

}
