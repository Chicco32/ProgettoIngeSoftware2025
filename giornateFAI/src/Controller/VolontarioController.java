package Controller;

import java.util.HashMap;
import java.util.Map;

import DataBaseImplementation.LoginSQL;
import Presentation.CliInput;
import Presentation.CliNotifiche;
import Presentation.CliVisualizzazione;
import ServicesAPI.Utente;
import ServicesAPI.Volontario;

public class VolontarioController implements UtenteController {

    private Volontario model;

    public VolontarioController (Volontario model) {
        this.model = model;
    }

    public Utente getModel() {
        return this.model;
    }

    public void accediSistema() {
        menuVolontario();
    }

    public void registrati() {
        boolean passwordCambiata = false;
        try {
            do {
                passwordCambiata = LoginSQL.cambioPassword(model.getNickname(), 
                    CliInput.chiediConLunghezzaMax(CliVisualizzazione.VARIABILE_PASSWORD, CliInput.MAX_CARATTERI_PASSWORD));
            } while (!passwordCambiata);
        } catch (Exception e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_ACCESSO);
        }
    }

    public static final String[] opzioniVolontario = {
        "Esci" };

    private void menuVolontario() {
        
        CliVisualizzazione.ingressoBackendVolontario();

        //per nuove funzioni agigungere nuove righe
        Map<Integer, Runnable> actions = new HashMap<>();

        while (true) {
            int scelta = CliInput.menuAzioni(getModel().getNickname(), opzioniVolontario);
            if (scelta > actions.size()) break;
            actions.get(scelta).run();
        }
    }

}
