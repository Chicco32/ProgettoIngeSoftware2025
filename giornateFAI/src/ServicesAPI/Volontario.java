package ServicesAPI;

public class Volontario extends Utente{

    private VisualizzatoreVolontario visualizzatore;
    private RegistroDateDisponibili registroDateDisponibili;

    public Volontario(boolean PrimoAccesso, String nickname, FactoryServizi servizi) {
        super(PrimoAccesso, nickname);
        this.registroDateDisponibili = new RegistroDateDisponibili(servizi.inizializzaDateDisponibili(),
            new RegistroDatePrecluse(servizi.inizializzaDatePrecluse()), nickname);
        this.visualizzatore = servizi.creaVisualizzatoreVolontario();
        this.setRuolo("Volontario");
    }

    public RegistroDateDisponibili getRegistroDateDisponibili() {
        return registroDateDisponibili;
    }

    public VisualizzatoreVolontario getVisualizzatore() {
        return this.visualizzatore;
    }

}
