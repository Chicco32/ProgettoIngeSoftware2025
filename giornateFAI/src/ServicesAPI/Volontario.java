package ServicesAPI;

public class Volontario extends Utente{

    private RegistroDateDisponibili registroDateDisponibili;

    public Volontario(boolean PrimoAccesso, String nickname, Visualizzatore visualizzatore, RegistroDateDisponibili dateDisponibili) {
        super(PrimoAccesso, nickname, visualizzatore);
        this.registroDateDisponibili = dateDisponibili;
    }

    public RegistroDateDisponibili getRegistroDateDisponibili() {
        return registroDateDisponibili;
    }

}
