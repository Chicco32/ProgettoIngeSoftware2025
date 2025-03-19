package ServicesAPI;

public class Configuratore extends Utente {
    
    private Registratore registratore;
    private RegistroDate registroDate;

    /**
     * Crea un nuovo utente di tipo configuratore. Il configuratore ha piu permessi rispoetto all'utente normale in quanto
     * possiede il dirtitto  di registrare le informazioni. Richiede percio l'accesso sia al visulaizzatore che al registratore
     * @param PrimoAccesso
     * @param nickname
     * @param visualizzatore l'implementazione dell'API visualizzatore
     * @param registratore l'implementazione dell'API registratore
     * @param pathRegistratore il percorso in cui trovare i file di configurazione 
     * @param pathDatePrecluse il percorso in cui trovare il file con le date precluse
     */
    public Configuratore(boolean PrimoAccesso, String nickname, Visualizzatore visualizzatore, Registratore registratore,RegistroDate registroDate) {
        super(PrimoAccesso, nickname, visualizzatore);
        this.setRuolo("Configuratore");
        this.registratore = registratore;
        this.registroDate = registroDate;
    }

    public boolean registrati(DTObject dati) throws Exception {
        boolean registrato = false;
        // Chiede il nickname e la password finche' non vengono inseriti correttamente
        this.setNickname((String)dati.getValoreCampo("Nickname"));

        registrato = registratore.registraNuovoConfiguratore(dati); 
        if (registrato) this.setPrimoAccesso(false);
        return registrato; 
    }

    public Registratore getRegistratore() {
        return this.registratore;
    }

    public RegistroDate getRegistroDate() {
        return this.registroDate;
    }

}
