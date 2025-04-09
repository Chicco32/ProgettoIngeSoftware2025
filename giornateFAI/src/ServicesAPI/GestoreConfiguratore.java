package ServicesAPI;

import java.io.FileNotFoundException;

/**
 * Interfaccia Repository per la gestione dei files di configurazione dei servizi di cui ha bisogno il configuratore.
 * Questa interfaccia dovrebbe essere implementata da un gestore di lettura e scrittura di dati per poter permettere
 * l'accesso persistente ai dati di configurazione come la zona di competenza della società al programma. 
 * In particolare questa interfaccia estende le funzionalità di GestoreFilesConfigurazione per la gestione generica dei file di configurazione.
 * 
 * @see GestoreFilesConfigurazione
 */
public interface GestoreConfiguratore extends GestoreFilesConfigurazione {

    String AREA_COMPETENZA = "areaCompetenza";
	String MAX_PARTECIPANTI = "maxPartecipanti";

    /**
     * Scrive il file di default del registratore con i valori inseriti.
     * @param maxPartecipanti il numero massimo di partecipanti che lo stesso Fruitore può inserire
     * @param areaCompetenza l'area geografica di competenza della società
     * @throws FileNotFoundException se il file non esiste o ci sono problemi di accesso
     */
    public void scriviRegistratoreDefault(String areaCompetenza, int maxPartecipanti) throws FileNotFoundException;

}
