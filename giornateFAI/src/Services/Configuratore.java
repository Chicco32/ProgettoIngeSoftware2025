package Services;

import java.sql.SQLIntegrityConstraintViolationException;
import ConfigurationFiles.PercorsiFiles;


public class Configuratore extends Utente {
    
    private Registratore registratore;
    private RegistroDate registroDate;

    public Configuratore(boolean PrimoAccesso, String nickname) {
        super(PrimoAccesso, nickname);
        this.setRuolo("Configuratore");
        this.registratore = new Registratore(PercorsiFiles.pathRegistratore);
        this.registroDate = new RegistroDate(PercorsiFiles.pathDatePrecluse, new Calendario());
    }

    public boolean registrati(String nickname, String password) throws SQLIntegrityConstraintViolationException, Exception {
        boolean registrato = false;
        // Chiede il nickname e la password finche' non vengono inseriti correttamente
        this.setNickname(nickname);
        registrato = registratore.registraNuovoConfiguratore(nickname, password); 
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
