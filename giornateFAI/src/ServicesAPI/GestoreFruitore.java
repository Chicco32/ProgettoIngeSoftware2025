package ServicesAPI;

import java.io.FileNotFoundException;

/**
 * Interfaccia Repository per la gestione dei files di configurazione dei servizi di cui ha bisogno il fruitore.
 * Questa interfaccia dovrebbe essere implementata da un gestore di lettura e scrittura di dati per poter permettere
 * l'accesso persistente ai dati di configuraizone come il numero massimo di partecipanti. 
 * In particolare questa interfaccia estende le funzionalità di GestoreFilesConfigurazione per la gestione generica dei file di configurazione.
 * 
 * @see GestoreFilesConfigurazione
 */
public interface GestoreFruitore  extends GestoreFilesConfigurazione {

	String MAX_PARTECIPANTI = "maxPartecipanti";
	
	/**
	 * Funzione per leggere il dato di configurazione sul numero massimo di 
	 * partecipanti che lo stesso fruitore può iscrivere alla stessa visita
	 * @return un intero che rappresenta il numero massimo di partecipanti
	 * @throws FileNotFoundException in caso il file di configurazione non sia leggibile o corrotto
	 */
	public int getMaxNumeroPartecipantiIscrizione() throws FileNotFoundException;
}
