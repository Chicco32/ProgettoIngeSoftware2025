package Controller;

import ServicesAPI.GestoreDateDisponibili;
import ServicesAPI.GestoreDatePrecluse;
import ServicesAPI.Registratore;
import ServicesAPI.VisualizzatoreConfiguratore;
import ServicesAPI.VisualizzatoreVolontario;

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
