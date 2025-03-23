package ServicesAPI;

import Controller.FactoryServizi;

public class Configuratore extends Utente {
    
    private Registratore registratore;
    private VisualizzatoreConfiguratore visualizzatore;
    private RegistroDatePrecluse datePrecluse;

    /**
     * Crea un nuovo utente di tipo configuratore. Il configuratore ha piu permessi rispoetto all'utente normale in quanto
     * possiede il dirtitto  di registrare le informazioni. Richiede percio l'accesso sia al visulaizzatore che al registratore
     * @param PrimoAccesso se è la prima volta che accede e che quindi dovrà registrarsi
     * @param nickname il nickname da usare in futuro per riferirsi all'utente
     * @param visualizzatore l'implementazione dell'API {@code visualizzatore}
     * @param registratore l'implementazione dell'API {@code registratore}
     * @param datePrecluse l'implementazione dell'API {@code RegistroDatePrecluse}
     */
    public Configuratore(boolean PrimoAccesso, String nickname, FactoryServizi servizi) {
        super(PrimoAccesso, nickname);
        this.visualizzatore = servizi.creaVisualizzatoreConfiguratore();
        this.setRuolo("Configuratore");
        this.registratore = servizi.creaRegistratore();
        this.datePrecluse = new RegistroDatePrecluse(servizi.inizializzaDatePrecluse());
    }

    public Registratore getRegistratore() {
        return this.registratore;
    }

    public RegistroDatePrecluse getRegistroDatePrecluse() {
        return this.datePrecluse;
    }

    public VisualizzatoreConfiguratore getVisualizzatore() {
        return this.visualizzatore;
    }

}
