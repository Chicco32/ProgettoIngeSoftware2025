package DataBaseImplementation;

import ServicesAPI.FactoryServizi;
import ServicesAPI.GestoreDateDisponibili;
import ServicesAPI.GestoreDatePrecluse;
import ServicesAPI.Registratore;
import ServicesAPI.VisualizzatoreConfiguratore;
import ServicesAPI.VisualizzatoreVolontario;

public class AvviaServiziDatabase implements FactoryServizi {

    //implementazione singleton
    private static AvviaServiziDatabase factory;
    private RegistratoreSQL registratoreSQL;
    private VisualizzatoreSQL visualizzatoreSQL;
    private XMLDateDisponibili xmlDateDisponibili;
    private XMLDatePrecluse xmlDatePrecluse;

    private AvviaServiziDatabase() {
        // Inizializzazione dei servizi
        this.registratoreSQL = new RegistratoreSQL(PercorsiFiles.pathRegistratore);
        this.visualizzatoreSQL = new VisualizzatoreSQL();
        this.xmlDateDisponibili = new XMLDateDisponibili(PercorsiFiles.pathDateDisponibili);
        this.xmlDatePrecluse = new XMLDatePrecluse(PercorsiFiles.pathDatePrecluse);
    }

    public static AvviaServiziDatabase getFactory() {
        if (factory == null) {
            factory = new AvviaServiziDatabase();
        }
        return factory;
    }

    public Registratore creaRegistratore() {
       return this.registratoreSQL;
    }

    public VisualizzatoreVolontario creaVisualizzatoreVolontario() {
        return this.visualizzatoreSQL;
    }

    public VisualizzatoreConfiguratore creaVisualizzatoreConfiguratore() {
        return this.visualizzatoreSQL;
    }

    public GestoreDateDisponibili inizializzaDateDisponibili() {
        return this.xmlDateDisponibili;
    }

    public GestoreDatePrecluse inizializzaDatePrecluse() {
        return this.xmlDatePrecluse;
    }

}
