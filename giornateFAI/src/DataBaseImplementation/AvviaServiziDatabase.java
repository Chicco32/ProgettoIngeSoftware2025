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

    private AvviaServiziDatabase() {
    }

    public static AvviaServiziDatabase getFactory() {
        if (factory == null) {
            factory = new AvviaServiziDatabase();
        }
        return factory;
    }

    public Registratore creaRegistratore() {
       return  new RegistratoreSQL(PercorsiFiles.pathRegistratore);
    }

    public VisualizzatoreVolontario creaVisualizzatoreVolontario() {
        return new VisualizzatoreSQL();
    }

    public VisualizzatoreConfiguratore creaVisualizzatoreConfiguratore() {
        return new VisualizzatoreSQL();
    }

    public GestoreDateDisponibili inizializzaDateDisponibili() {
        return new XMLDateDisponibili(PercorsiFiles.pathDateDisponibili);
    }

    public GestoreDatePrecluse inizializzaDatePrecluse() {
        return new XMLDatePrecluse(PercorsiFiles.pathDatePrecluse);
    }

}
