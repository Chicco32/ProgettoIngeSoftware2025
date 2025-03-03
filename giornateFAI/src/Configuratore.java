public class Configuratore extends Utente {
    
    public Configuratore(boolean PrimoAccesso) {
        super(PrimoAccesso);
        this.setRuolo("Configuratore");
    }

    public void registrati(Registratore registratore) {
        CliUtente.creaNuovoConfiguratore();
        do {
            // Chiede il nickname e la password finche' non vengono inseriti correttamente
            this.setNickname(CliUtente.chiediNickname());
        } while (!registratore.registraNuovoConfiguratore(this.getNickname(), CliUtente.chiediPassword()));
        this.setPrimoAccesso(false);
    }

}
