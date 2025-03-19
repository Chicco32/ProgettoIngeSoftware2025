package Services;


import java.util.List;

public interface Visualizzatore {

    /**
     * Ritorna i risultati dell'esecuzone di una certa query sul database
     * @param query la query da far eseguire, il metodo è specifico per le query solo di visualizzazione non di modifica, per quelle usare il registratore
     * @return Un oggetto {@code ResultSet} con le tuple risultate dalla interrogazione
     */
    public DTObject[] visualizzaTabella(String query) throws CoerenzaException;

    /**
     * Richiede al DB di filtrare le istanze di visita in cui lo stato equivale a quello richiesto
     * @param stato lo stato delle visite su cui filtrare
     * @return un oggetto {@code ResulSet} con i risultati della query
     * @throws CoerenzaException Se lo stato inserito è uno stato invalido
     */
    public DTObject[] visualizzaVisite(StatiVisite stato) throws CoerenzaException;

    /**
     * Riporta la lista con anche i volontari non associati, da eliminare in futuro per mantenere la coerenza
     * @deprecated
     */
    public List<String> listaCompletaVolontari() throws IllegalArgumentException;

    /**
     * Estrae solo tutti i valori assunti da uno specifico atributo. Siccome esso sposta avanti il puntatore va evocato su un oggetto non ancora letto altrimenti causerà eccezioni
     * @param resul un oggetto che rappresenta tutti i risultati della query su cui filtrare
     * @param campo la colonna selezionata su cui filtrare
     * @return un arraylist contenente tuttti i valori assunti dalla colonna selezionata
     * @throws IllegalArgumentException se l'oggetto resultset è null o se il campo è null
     */
    public List<String> estraiColonna(DTObject[] results, String campo) throws IllegalArgumentException, CoerenzaException;

    /**
     * Funzione che avvisa se una tabella del DB è vuota, in particolare controlla se non vi e nemmeno una tupla in esso
     * @param tabella il nome della tabella da ispezionare (per ora supporta solo "luogo" e "tipo di visita")
     * @return true se la tabella è vuota, false altrimenti
     * @throws CoerenzaException se si cerca una tabella non esistente
     */
    public boolean tabellaDBVuota(String tabella) throws CoerenzaException;
}
