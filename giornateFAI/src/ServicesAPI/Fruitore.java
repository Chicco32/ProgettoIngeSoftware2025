package ServicesAPI;

public class Fruitore extends Utente {

	private VisualizzatoreFruitore visualizzatore;
	private RegistratoreIscrizioni registratoreIscrizioni;
	private GestoreFruitore fileManager;
	private int maxPartecipanti;

	/**
     * Crea un nuovo utente di tipo configuratore. Il fruitore è l'utente finale che accede alle visite e ha dirittot di iscrizone.
	 * Richiede percio l'accesso a una versione speciale di visualizzatore e registratore.
     * @param PrimoAccesso se è la prima volta che accede e che quindi dovrà registrarsi
     * @param nickname il nickname da usare in futuro per riferirsi all'utente
     * @param visualizzatore l'implementazione dell'API {@code VisualizzatoreFruitore}
     * @param registratoreIscrizioni l'implementazione dell'API {@code RegistratoreIscrizioni}
     * 
	 * @see VisualizzatoreFruitore
	 * @see RegistratoreIscrizioni
     */
	public Fruitore(boolean PrimoAccesso, String nickname, FactoryServizi servizi) {
		super(PrimoAccesso, nickname);
		this.visualizzatore = servizi.creaVisualizzatoreFruitore();
		this.registratoreIscrizioni = servizi.creaRegistratoreIscrizioni();
		this.setRuolo("Fruitore");
		this.fileManager = servizi.creaGestoreFruitore();

		try {
            // Carica i dati di default dal file
            if (GestoreFilesConfigurazione.fileExists(fileManager.getPath())) {
                this.maxPartecipanti = fileManager.getMaxNumeroPartecipantiIscrizione();
            }
            //avvia la creazione di un nuovo file default
            else {
                GestoreFilesConfigurazione.creaFile(fileManager.getPath());
                this.maxPartecipanti = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

	}

	public GestoreFruitore getFileManager() {
		return fileManager;
	}

	public int getMaxPartecipanti() {
		return maxPartecipanti;
	}

	public VisualizzatoreFruitore getVisualizzatore() {
		return visualizzatore;
	}

	public RegistratoreIscrizioni getRegistratoreIscrizioni() {
		return registratoreIscrizioni;
	}
	

}
