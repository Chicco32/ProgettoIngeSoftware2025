package Services;

public interface Registratore {

    /**
     * Funzione per la registrazione di un nuovo configuratore nel DB.
     * In particolare la funzione richiede al DB l'inserimento dei dati forniti e riporta la risposta del DB in caso di avvenuto inserimento o meno
     * La funzione ritorna true se la registrazione è andata a buon fine, false altrimenti.
     * 
     * @param nickname il possibile nickname da registrare
     * @param password la password inserita dall'utente
     * @return lo stato della registrazione, true se è andata a buon fine, false altrimenti
     */
    public boolean registraNuovoConfiguratore (DTObject utente);

    /**
     * Funzione per la registrazione di un nuovo volontario nel DB.
     * In particolare la funzione richiede al DB l'inserimento dei dati forniti e riporta la risposta del DB in caso di avvenuto inserimento o meno
     * La funzione ritorna true se la registrazione è andata a buon fine, false altrimenti.
     * 
     * @param nickname il possibile nickname da registrare
     * @param password la password inserita dall'utente
     * @return lo stato della registrazione, true se è andata a buon fine, false altrimenti
     */
    public boolean registraNuovoVolontario (DTObject utente);

    /**
     * Funzione per la registrazione di un nuovo luogo nel DB.
     * In particolare la funzione richiede al DB l'inserimento dei dati forniti e riporta la risposta del DB in caso di avvenuto inserimento o meno
     * La funzione ritorna true se la registrazione è andata a buon fine, false altrimenti.
     * @param nome il nome del luogo
     * @param descrizione la descrizione di al massimo 100 caratteri del luogo
     * @param indirizzo l'inidirizzo di al massimo 45 caratteri
     * @return lo stato della registrazione, true se è andata a buon fine, false altrimenti
     */
    public boolean registraNuovoLuogo (DTObject luogo);

    /**
     * Il metodo genera una nuova chiave univoca valida per la tabella selezionata. In particolare la funzione conta il numero più alto fra le chiavi e ritorna il
     * numero progressivo successivo come numero da usare come chiave, in questa maniera permette la generazione di chiavi anche in caso di eliminazioni di righe dalla tabella. 
     * 
     * @param tabella la tabella da selezionare in cui generare la chiave
     * @return un {@code int} che rappresenta il valore della chiave da inserire. In caso di tabella di tabella vuota restitutisce valore {@code 1} e in caso di errori nella generezione restituisce {@code -1}
     */
    public int generaNuovaChiave(String tabella);

    /**
     * Funzione per la registrazione di un nuovo tipo di visita nel DB.
     * @param tipoDiVisita la tupla da inserie nel DB
     * @return
     */
    public boolean registraNuovoTipoVisita(DTObject tipoDiVisita);

    /**
     * Funziona per inserire le disponibilità dei volontari a uno specifico tipo di visita
     * @param codiceVisita il codice del tipo di visita da associare
     * @param volontarioSelezionato il nickname del volontario
     * @return true se l'inserimento è andato a buon fine, false altrimenti
     */
    public boolean associaVolontarioVisita (int codiceVisita, String volontarioSelezionato);

    /**
     * Modifica l'area di competenza della società, Ogni volta che viene invocata questa funzione viene anche scritta nel file
     * di default. Puo' essere modificata solo da un Configuratore.
     * Può essere invocata la prima volta per settare il primo valore  in caso non fosse ancora inserito.
     * @param areaCompetenza la nuova area di competenza in cui adopera la società che riguardeà i luoghi da inserire.
     */
    public void modificaAreaCompetenza(String areaCompetenza);

    /**
     * Modifica il max numero di partecipanti che possono essere iscritti. Ogni volta che viene invocata questa funzione viene anche scritta nel file
     * di default. Puo' essere modificata solo da un Configuratore.
     * Può essere invocata la prima volta per settare il primo valore in caso non fosse ancora inserito.
     * @param areaCompetenza la nuova area di competenza in cui adopera la società che riguardeà i luoghi da inserire.
     */
    public void modificaMaxPartecipanti(int maxPartecipanti);
}
