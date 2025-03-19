package Services;

import ConfigurationFiles.PercorsiFiles;
import ConfigurationFiles.RegistratoreSQL;


public class Configuratore extends Utente {
    
    private Registratore registratore;
    private RegistroDate registroDate;

    public Configuratore(boolean PrimoAccesso, String nickname) {
        super(PrimoAccesso, nickname);
        this.setRuolo("Configuratore");
        this.registratore = new RegistratoreSQL(PercorsiFiles.pathRegistratore);
        this.registroDate = new RegistroDate(PercorsiFiles.pathDatePrecluse, this.calendario);
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
