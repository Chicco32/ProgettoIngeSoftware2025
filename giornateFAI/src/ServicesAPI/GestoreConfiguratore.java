package ServicesAPI;

import java.io.FileNotFoundException;

import ServicesAPI.Eccezioni.ConfigFilesException;

/**
 * Interfaccia Repository per la gestione dei files di configurazione dei servizi di cui ha bisogno il configuratore.
 * Questa interfaccia dovrebbe essere implementata da un gestore di lettura e scrittura di dati per poter permettere
 * l'accesso persistente ai dati di configurazione come la zona di competenza della società al programma. 
 * In particolare questa interfaccia estende le funzionalità di GestoreFilesConfigurazione per la gestione generica dei file di configurazione.
 * 
 * @see GestoreFilesConfigurazione
 */
public interface GestoreConfiguratore extends GestoreFilesConfigurazione {

    /**
     * Scrive il file di default del registratore con i valori inseriti.
     * @param maxPartecipanti il numero massimo di partecipanti che lo stesso Fruitore può inserire
     * @param areaCompetenza l'area geografica di competenza della società
     * @throws FileNotFoundException se il file non esiste o ci sono problemi di accesso
     */
    public void scriviRegistratoreDefault(String areaCompetenza, int maxPartecipanti) throws FileNotFoundException;

    /**
     * Legge il numero massimo di partecipanti. Puo' essere letto solo da un Configuratore.
     * @throws ConfigFilesException In caso di errore di scrittura del file di configurazione.
     * @throws FileNotFoundException se il file non esiste o ci sono problemi di accesso
     */
    public int leggiNumeroPartecipanti() throws FileNotFoundException, Eccezioni.ConfigFilesException;

    /**
     * Legge l'area di competenza della società. Puo' essere letto solo da un Configuratore.
     * @throws ConfigFilesException In caso di errore di scrittura del file di configurazione.
     * @throws FileNotFoundException se il file non esiste o ci sono problemi di accesso
     */
    public String leggiAreaCompetenza() throws FileNotFoundException, Eccezioni.ConfigFilesException;

    /**
     * Modifica l'area di competenza della società, Ogni volta che viene invocata questa funzione viene anche scritta nel file
     * di default. Puo' essere modificata solo da un Configuratore.
     * Può essere invocata la prima volta per settare il primo valore  in caso non fosse ancora inserito.
     * @param areaCompetenza la nuova area di competenza in cui adopera la società che riguarderà i luoghi da inserire.
     * @throws ConfigFilesException In caso di errore di scrittura del file di configurazione.
     */
    public void modificaAreaCompetenza(String areaCompetenza) throws Eccezioni.ConfigFilesException;

    /**
     * Modifica il max numero di partecipanti che possono essere iscritti. Ogni volta che viene invocata questa funzione viene anche scritta nel file
     * di default. Puo' essere modificata solo da un Configuratore.
     * Può essere invocata la prima volta per settare il primo valore in caso non fosse ancora inserito.
     * @param areaCompetenza la nuova area di competenza in cui adopera la società che riguarderà i luoghi da inserire.
     * @throws ConfigFilesException In caso di errore di scrittura del file di configurazione.
     */
    public void modificaMaxPartecipanti(int maxPartecipanti) throws Eccezioni.ConfigFilesException;

}
