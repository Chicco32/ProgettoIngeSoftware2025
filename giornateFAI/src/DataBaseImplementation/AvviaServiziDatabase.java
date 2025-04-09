package DataBaseImplementation;

import ServicesAPI.FactoryServizi;
import ServicesAPI.GestoreConfiguratore;
import ServicesAPI.GestoreDateDisponibili;
import ServicesAPI.GestoreDatePrecluse;
import ServicesAPI.GestoreFruitore;
import ServicesAPI.Registratore;
import ServicesAPI.RegistratoreIscrizioni;
import ServicesAPI.VisualizzatoreConfiguratore;
import ServicesAPI.VisualizzatoreFruitore;
import ServicesAPI.VisualizzatoreVolontario;

public class AvviaServiziDatabase implements FactoryServizi {

    //implementazione singleton
    private static AvviaServiziDatabase factory;
    private RegistratoreSQL registratoreSQL;
    private VisualizzatoreSQL visualizzatoreSQL;
    private XMLDateDisponibili xmlDateDisponibili;
    private XMLDatePrecluse xmlDatePrecluse;
    private XMLConfiguratore xmlConfiguratore;
    private XMLFruitore xmlFruitore;

    private AvviaServiziDatabase() {
        // Inizializzazione dei servizi
        this.registratoreSQL = new RegistratoreSQL(PercorsiFiles.pathRegistratore);
        this.visualizzatoreSQL = new VisualizzatoreSQL();
        this.xmlDateDisponibili = new XMLDateDisponibili(PercorsiFiles.pathDateDisponibili);
        this.xmlDatePrecluse = new XMLDatePrecluse(PercorsiFiles.pathDatePrecluse);
        this.xmlConfiguratore = new XMLConfiguratore(PercorsiFiles.pathRegistratore);
        this.xmlFruitore = new XMLFruitore(PercorsiFiles.pathRegistratore);
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

    public RegistratoreIscrizioni creaRegistratoreIscrizioni() {
        return this.registratoreSQL;
    }

    public VisualizzatoreFruitore creaVisualizzatoreFruitore() {
        return this.visualizzatoreSQL;
    }

    public GestoreConfiguratore creaGestoreConfiguratore() {
        return this.xmlConfiguratore;
    }

    public GestoreFruitore creaGestoreFruitore() {
       return this.xmlFruitore;
    }

}
