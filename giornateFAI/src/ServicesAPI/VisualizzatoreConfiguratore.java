package ServicesAPI;

import java.util.List;

/**
 * Interfaccia che definisce i metodi per visualizzare e configurare i dati relativi alle visite, 
 * ai volontari e ai luoghi registrati, con i permessi del visualizzatore.
 */
public interface VisualizzatoreConfiguratore {

    /**
     * Richiede di filtrare le istanze di visita in cui lo stato equivale a quello richiesto
     * @param stato lo stato delle visite su cui filtrare
     * @return un oggetto {@code DTObject} con le visite salvate
     * @throws CoerenzaException Se lo stato inserito è uno stato invalido
     */
    public DTObject[] visualizzaVisite(StatiVisite stato);

    /**
     * Riporta la lista con anche i volontari non associati, da eliminare in futuro per mantenere la coerenza
     * @deprecated andrà rimossa una volta che si creano i sistemi di controllo sui volontari per asssicurare che non ci siano
     * volontari non associati
     */
    public List<String> listaCompletaVolontari() throws IllegalArgumentException;

    /**
     * Controlla se ci sono luoghi registrati per avviare la procedura di popolamento del DB
     * @return true se non sono saalvati nessun luogo, false altrimenti
     */
    public boolean nonCisonoLuoghiRegistrati();

    /**
     * Visualizza la lista dei volontari registrati
     * @return un oggetto {@code DTObject} con i volontari registrati
     */
    public DTObject[] visualizzaElencoVolontari();

    /**
     * Visualizza la lista dei luoghi registrati
     * @return un oggetto {@code DTObject} con i luoghi registrati
     */
    public DTObject[] visualizzaElencoLuoghi();

    /**
     * Ritorna i nomi dei luoghi registrati direttamente in forma di lista
     * @return {@code List<String>} con i nomi dei luoghi.
     */
    public List<String> listaLuoghiRegistrati();

    /**
     * Visualizza la lista dei tipi di visita registrati
     * @return un oggetto {@code DTObject} con i tipi di visita registrati
     */
    public DTObject[] visualizzaElencoTipiDiVisite();


}
