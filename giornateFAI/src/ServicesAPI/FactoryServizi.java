package ServicesAPI;

/**
 * Interfaccia abstract factory per avviare i servizi alla creazione
 */
public interface FactoryServizi {

    public Registratore creaRegistratore();

    public VisualizzatoreVolontario creaVisualizzatoreVolontario();

    public VisualizzatoreConfiguratore creaVisualizzatoreConfiguratore();

    public GestoreDateDisponibili inizializzaDateDisponibili();

    public GestoreDatePrecluse inizializzaDatePrecluse();

}
