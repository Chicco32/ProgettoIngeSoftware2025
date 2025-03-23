package ServicesAPI;

public class Volontario extends Utente{

    private VisualizzatoreVolontario visualizzatore;
    private RegistroDateDisponibili registroDateDisponibili;

    public Volontario(boolean PrimoAccesso, String nickname, VisualizzatoreVolontario visualizzatore, RegistroDateDisponibili dateDisponibili) {
        super(PrimoAccesso, nickname);
        this.registroDateDisponibili = dateDisponibili;
        this.visualizzatore = visualizzatore;
        this.setRuolo("Volontario");
    }

    public RegistroDateDisponibili getRegistroDateDisponibili() {
        return registroDateDisponibili;
    }

    public VisualizzatoreVolontario getVisualizzatore() {
        return this.visualizzatore;
    }

}
