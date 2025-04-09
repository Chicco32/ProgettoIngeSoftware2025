package Controller;

import java.util.Map;
import Presentation.CliNotifiche;
import ServicesAPI.Configuratore;
import ServicesAPI.Fruitore;
import ServicesAPI.Utente;
import ServicesAPI.Volontario;

public class FactoryController {

    //Se si aggiunge un nuovo tipo di utente basta inserire una nuova riga della mappa
    private static final Map<Class<? extends Utente>, Class<? extends UtenteController>> associazioni = Map.of(
    Configuratore.class, ConfiguratoreController.class,
    Fruitore.class, FruitoreController.class,
    Volontario.class, VolontarioController.class);

    public static UtenteController associaController(Utente utente) {
        try {
            Class<? extends UtenteController> controllerClass = associazioni.get(utente.getClass());
            if (controllerClass == null) {
                throw new IllegalArgumentException("Nessun controller associato per " + utente.getClass().getSimpleName());
            }
            return controllerClass.getDeclaredConstructor(utente.getClass()).newInstance(utente);
        } catch (Exception e) {
            CliNotifiche.avvisa(CliNotifiche.ERRORE_CONNESSIONE);
            return null;
        }
    }

}
