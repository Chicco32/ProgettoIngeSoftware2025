package ServicesAPI;

import java.io.FileNotFoundException;

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
     * Crea un nuovo utente di tipo configuratore. Il configuratore ha piu permessi rispoetto all'utente normale in quanto
     * possiede il dirtitto  di registrare le informazioni. Richiede percio l'accesso sia al visulaizzatore che al registratore
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
            if (GestoreFilesConfigurazione.fileExists(fileManager.getPath())) {
                this.maxPartecipanti = Integer.parseInt(fileManager.leggiVariabile(GestoreConfiguratore.MAX_PARTECIPANTI));
                this.areaCompetenza = fileManager.leggiVariabile( GestoreConfiguratore.AREA_COMPETENZA);
            }
            //avvia la creazione di un nuovo file default
            else {
                GestoreFilesConfigurazione.creaFile(fileManager.getPath());
                this.areaCompetenza = null;
                this.maxPartecipanti = 0;
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

    /**
     * Modifica l'area di competenza della società, Ogni volta che viene invocata questa funzione viene anche scritta nel file
     * di default. Puo' essere modificata solo da un Configuratore.
     * Può essere invocata la prima volta per settare il primo valore  in caso non fosse ancora inserito.
     * @param areaCompetenza la nuova area di competenza in cui adopera la società che riguardeà i luoghi da inserire.
     * @throws ConfigFilesException In caso di errore di scrittura del file di configurazione.
     */
    public void modificaAreaCompetenza(String areaCompetenza) throws Eccezioni.ConfigFilesException {
        try {
            this.areaCompetenza = areaCompetenza;
            int maxPartecipanti = Integer.parseInt(fileManager.leggiVariabile(GestoreConfiguratore.MAX_PARTECIPANTI));
            fileManager.scriviRegistratoreDefault(areaCompetenza, maxPartecipanti);
        } catch (FileNotFoundException e) {
            throw new Eccezioni.ConfigFilesException("File non trovato", e);
        }
    }

    /**
     * Modifica il max numero di partecipanti che possono essere iscritti. Ogni volta che viene invocata questa funzione viene anche scritta nel file
     * di default. Puo' essere modificata solo da un Configuratore.
     * Può essere invocata la prima volta per settare il primo valore in caso non fosse ancora inserito.
     * @param areaCompetenza la nuova area di competenza in cui adopera la società che riguardeà i luoghi da inserire.
     * @throws ConfigFilesException In caso di errore di scrittura del file di configurazione.
     */
    public void modificaMaxPartecipanti(int maxPartecipanti) throws Eccezioni.ConfigFilesException {
        try {
            this.maxPartecipanti = maxPartecipanti;
            String areaCompetenza = fileManager.leggiVariabile(GestoreConfiguratore.AREA_COMPETENZA);
            fileManager.scriviRegistratoreDefault(areaCompetenza, maxPartecipanti);
        } catch (FileNotFoundException e) {
            throw new Eccezioni.ConfigFilesException("File non trovato", e);
        }
    }

}
