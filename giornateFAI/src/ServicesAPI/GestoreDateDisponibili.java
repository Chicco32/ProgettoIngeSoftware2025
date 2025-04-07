package ServicesAPI;
import java.io.FileNotFoundException;
import java.util.Date;

/**
 * Interfaccia Repository per la gestione delle date disponibili.
 * Questa interfaccia dovrebbe essere implementata da un gestore di lettura e scrittura di dati per poter permettere
 * l'accesso persistente alle disponibilità di un volontario al programma. 
 * In particolare questa interfaccia estende le funzionalità di GestoreFilesConfigurazione per la gestione generica dei file di configurazione..
 * 
 * @see GestoreFilesConfigurazione
 */
public interface GestoreDateDisponibili extends GestoreFilesConfigurazione {

    /**
     * Scrive il file con le disponibilità del volontario per il mese
     * successivo. In particolare scrive la data corrente e le date disponibili
     * @param today la data odierna
     * @param dateDisponili l'array di date in cui il volontario ha dato la disponibilità
     * @param nomeVolontario il nome del volontario a cui associare le date
     * @throws FileNotFoundException se il file non esiste o ci sono problemi di accesso
     */
    public void registraDateDisponibili(Date today, Date[] dateDisponili, String nomeVolontario) throws FileNotFoundException;

    /**
	 * Funzione che legge le date disponibili per il mese corrente
	 * @return l'array di date disponibili
     * @throws FileNotFoundException se il file non esiste o ci sono problemi di accesso
	 */
    public Date[] leggiDateDisponibili(String nomeVolontario) throws FileNotFoundException;

    /**
     * Funzione che resetta le date disponibili salvate sul file
     * @param data la data odierna
     */
	public void cleanDates(Date data, String nomeVolontario);

    /**
     * Rimuove fra i files di configurazione tutti i files e sezioni che fanno riferimento al volontario selezionato.
     * Questo metodo va invocato dopo la rimozione di un volontario per non tenere dei files o part di files orfani
     * che potrebbero causare conflitti o rallentare il sistema.
     * @param nickname il nickname del volontario eliminato da cancellare
     */
	public void rimuoviDatiVolontario(String nickname);

}
