package ServicesAPI;


import java.util.List;

public interface VisualizzatoreConfiguratore {

    /**
     * Richiede al DB di filtrare le istanze di visita in cui lo stato equivale a quello richiesto
     * @param stato lo stato delle visite su cui filtrare
     * @return un oggetto {@code ResulSet} con i risultati della query
     * @throws CoerenzaException Se lo stato inserito è uno stato invalido
     */
    public DTObject[] visualizzaVisite(StatiVisite stato);

    /**
     * Riporta la lista con anche i volontari non associati, da eliminare in futuro per mantenere la coerenza
     * @deprecated andrà rimossa una volta che si creano i sistemi di controllo sui volontari per asssicurare che non ci siano
     * volontari non associati
     */
    public List<String> listaCompletaVolontari() throws IllegalArgumentException;

    public boolean nonCisonoLuoghiRegistrati();

    public DTObject[] visualizzaElencoVolontari();

    public DTObject[] visualizzaElencoLuoghi();

    public DTObject[] visualizzaElencoTipiDiVisite();

    public List<String> listaLuoghiRegistrati();


}
