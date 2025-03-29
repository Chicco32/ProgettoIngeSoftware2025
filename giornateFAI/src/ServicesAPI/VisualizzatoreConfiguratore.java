package ServicesAPI;

import java.util.List;

/**
 * Interfaccia che definisce i metodi per ricevere e visualizzare i dati relativi alle visite salvate, 
 * ai volontari e ai luoghi registrati, con i permessi del visualizzatore.
 */
public interface VisualizzatoreConfiguratore {

    /**
     * Richiede di filtrare le istanze di visita in cui lo stato equivale a quello richiesto
     * @param stato lo stato delle visite su cui filtrare
     * @return un oggetto {@code DTObject} con le visite salvate
     * @throws Eccezioni.DBConnectionException Se si verifica un errore di connessione al database
     */
    public DTObject[] visualizzaVisite(StatiVisite stato) throws Eccezioni.DBConnectionException;

    /**
     * Controlla se ci sono luoghi registrati per avviare la procedura di popolamento del DB
     * @return true se non sono saalvati nessun luogo, false altrimenti
     * @throws Eccezioni.DBConnectionException Se si verifica un errore di connessione al database
     */
    public boolean nonCisonoLuoghiRegistrati() throws Eccezioni.DBConnectionException;

    /**
     * Visualizza la lista dei volontari registrati
     * @return un oggetto {@code DTObject} con i volontari registrati
     * @throws Eccezioni.DBConnectionException Se si verifica un errore di connessione al database
     */
    public DTObject[] visualizzaElencoVolontari() throws Eccezioni.DBConnectionException;

    /**
     * Visualizza la lista dei luoghi registrati
     * @return un oggetto {@code DTObject} con i luoghi registrati
     * @throws Eccezioni.DBConnectionException Se si verifica un errore di connessione al database
     */
    public DTObject[] visualizzaElencoLuoghi() throws Eccezioni.DBConnectionException;

    /**
     * Ritorna i nomi dei luoghi registrati direttamente in forma di lista
     * @return {@code List<String>} con i nomi dei luoghi.
     * @throws Eccezioni.DBConnectionException Se si verifica un errore di connessione al database
     */
    public List<String> listaLuoghiRegistrati() throws Eccezioni.DBConnectionException;

    /**
     * Visualizza la lista dei tipi di visita registrati
     * @return un oggetto {@code DTObject} con i tipi di visita registrati
     * @throws Eccezioni.DBConnectionException Se si verifica un errore di connessione al database
     */
    public DTObject[] visualizzaElencoTipiDiVisite() throws Eccezioni.DBConnectionException;

}
