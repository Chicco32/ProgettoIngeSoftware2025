package ServicesAPI;

/**
 * Interfaccia abstract factory per avviare i servizi alla creazione
 */
public interface FactoryServizi {

    public Registratore creaRegistratore();

    public RegistratoreIscrizioni creaRegistratoreIscrizioni();

    public VisualizzatoreVolontario creaVisualizzatoreVolontario();

    public VisualizzatoreConfiguratore creaVisualizzatoreConfiguratore();

    public VisualizzatoreFruitore creaVisualizzatoreFruitore();

    public GestoreDateDisponibili inizializzaDateDisponibili();

    public GestoreDatePrecluse inizializzaDatePrecluse();

	public GestoreConfiguratore creaGestoreConfiguratore();

	public GestoreFruitore creaGestoreFruitore();


}
