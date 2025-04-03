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

    /**
     * Metodo per ottenere le date possibili in termini di giorni della settimana
     * su cui un volontario può dare disponibilità, associate anche al tipo di visita in cui la visita si può
     * tenere in quel suddetto giorno.
     * @param volontarioAssociato il nome del volontario su cui filtrare la ricerca
     * @return un array di {@code DTObject} contenente i giorni della settimana e i tipi di visita associati
     * @throws Eccezioni.DBConnectionException Se si verifica un errore di connessione al database
     */
    public DTObject[] estraiDOWPossibiliVolontario(String volontarioAssociato) throws Eccezioni.DBConnectionException;

    /**
     * Funzione che estrae le coppie di volontari e i tipi di visite asscoiati ad ognuno di essi per poter ricostruire 
     * le associazioni durante la creaiozne del piano delle visite. Idealmente questo metodo dovrbbe essere
     * utilizzato solo dal Planner delle visite
     * @return un {@code DTObject} contente le coppie
     * @throws Eccezioni.DBConnectionException Se si verifica un errore di connessione al database
     * @see PlannerVisite
     */
    public DTObject[] estraiTipiDiVisiteVolontario() throws Eccezioni.DBConnectionException;

    /**
     * Funzione che estrae le coppie di giorni della setitmana e i tipi di visite asscoiati ad ognuno di essi per poter ricostruire 
     * le associazioni durante la creazione del piano delle visite. Idealmente questo metodo dovrbbe essere
     * utilizzato solo dal Planner delle visite
     * @return un {@code DTObject} contente le coppie
     * @throws Eccezioni.DBConnectionException Se si verifica un errore di connessione al database
     * @see PlannerVisite
     */
    public DTObject[] estraiGiorniTipoDiVisita() throws Eccezioni.DBConnectionException;
}
