package ServicesAPI;

import ServicesAPI.Eccezioni.ConfigFilesException;

public class Configuratore extends Utente {
    
    private Registratore registratore;
    private VisualizzatoreConfiguratore visualizzatore;
    private RegistroDatePrecluse datePrecluse;
    private RegistroDateDisponibili dateDisponibili;
    private GestoreConfiguratore fileManager;
    private String areaCompetenza;
    private int maxPartecipanti;

    /**
     * Crea un nuovo utente di tipo configuratore. Il configuratore ha piu permessi rispetto all'utente normale in quanto
     * possiede il diritto  di registrare le informazioni. Richiede percio l'accesso sia al visualizzatore che al registratore
     * @param PrimoAccesso se è la prima volta che accede e che quindi dovrà registrarsi
     * @param nickname il nickname da usare in futuro per riferirsi all'utente
     * @param visualizzatore l'implementazione dell'API {@code VisualizzatoreConfiguratore}
     * @param registratore l'implementazione dell'API {@code registratore}
     * @param datePrecluse l'implementazione dell'API {@code RegistroDatePrecluse}
     * @param dateDisponibili l'implementazione dell'API {@code RegistroDateDisponibili}
     * 
     * @see VisualizzatoreConfiguratore
     * @see Registratore
     * @see RegistroDatePrecluse
     * @see RegistroDateDisponibili
     */
    public Configuratore(boolean PrimoAccesso, String nickname, FactoryServizi servizi) {
        super(PrimoAccesso, nickname);
        this.visualizzatore = servizi.creaVisualizzatoreConfiguratore();
        this.setRuolo("Configuratore");
        this.registratore = servizi.creaRegistratore();
        this.datePrecluse = new RegistroDatePrecluse(servizi.inizializzaDatePrecluse());
        this.dateDisponibili = new RegistroDateDisponibili(servizi.inizializzaDateDisponibili(), datePrecluse);
        this.fileManager = servizi.creaGestoreConfiguratore();

        try {
            // Carica i dati di default dal file
            if (fileManager.fileExists(fileManager.getPath())) {
                this.maxPartecipanti = fileManager.leggiNumeroPartecipanti();
                this.areaCompetenza = fileManager.leggiAreaCompetenza();
            }
            //avvia la creazione di un nuovo file default
            else {
                fileManager.creaFile(fileManager.getPath());
                this.areaCompetenza = null;
                this.maxPartecipanti = 0;
                fileManager.scriviRegistratoreDefault("", maxPartecipanti);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public RegistroDateDisponibili getRegistroDateDisponibili() {
        return this.dateDisponibili;
    }

    public int getMaxPartecipanti() {
        return maxPartecipanti;
    }

    public String getAreaCompetenza() {
        return areaCompetenza;
    }

    public void setMaxPartecipanti(int maxPartecipanti) {
        this.maxPartecipanti = maxPartecipanti;
    }

    public void modificaAreaCompetenza(String areaCompetenza) throws ConfigFilesException {
        try {
            fileManager.modificaAreaCompetenza(areaCompetenza);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modificaMaxPartecipanti(int maxPartecipanti) throws ConfigFilesException {
        try {
            fileManager.modificaMaxPartecipanti(maxPartecipanti);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
