package ServicesAPI;

public interface Registratore {

    /**
     * Funzione per la registrazione di un nuovo volontario nel DB.
     * In particolare la funzione richiede al DB l'inserimento dei dati forniti e riporta la risposta del DB in caso di avvenuto inserimento o meno
     * La funzione ritorna true se la registrazione è andata a buon fine, false altrimenti.
     * 
     * @param nickname il possibile nickname da registrare
     * @param password la password inserita dall'utente
     * @return lo stato della registrazione, true se è andata a buon fine, false altrimenti
     */
    public boolean registraNuovoVolontario (DTObject volontario) throws Exception;

    /**
     * Questo metodo verifica se un dato nome utente è univoco, cioè se non è già stato registrato da 
     * nessun altro utente, indipendentemente dal loro ruolo. 
     * Viene invocato ogni volta che un utente tenta di registrare un nuovo nickname.
     * @param nomeUtente Il nome utente che si vuole registrare.
     * @return true se il nome utente non è già registrato, false altrimenti.
     */
    public boolean nomeUtenteUnivoco (String nomeUtente) throws Exception;

    /**
     * Funzione per la registrazione di un nuovo luogo nel DB.
     * In particolare la funzione richiede al DB l'inserimento dei dati forniti e riporta la risposta del DB in caso di avvenuto inserimento o meno
     * La funzione ritorna true se la registrazione è andata a buon fine, false altrimenti.
     * @param nome il nome del luogo
     * @param descrizione la descrizione di al massimo 100 caratteri del luogo
     * @param indirizzo l'inidirizzo di al massimo 45 caratteri
     * @return lo stato della registrazione, true se è andata a buon fine, false altrimenti
     */
    public boolean registraNuovoLuogo (DTObject luogo) throws Exception;

    /**
     * Funzione che registra un nuovo tipo di visita nel DB.
     * La funzione richiede le funzioni aggiuntive:  
     * <PRE>
     *  this.formatoDataPerSQL(Date date); formatoOrarioPerSQL(Time time);
     * </PRE>
     * @param codice codice univoco che identifica il tipo di visita
     * @param luogo il luogo in cui si tiene, deve essere gia registrato nella tabella luoghi
     * @param titolo il titolo che riassume la visita
     * @param descrizione una breve descriizione dell'evento
     * @param dataInizio un oggetto di tipo {@code Date} che serve per rappresentare l'inzio del periodo del'evento
     * @param dataFine  un oggetto di tipo {@code Date} che serve per rappresentare la fine del periodo del'evento
     * @param oraInizio l'ora in cui l'evento si svolge
     * @param durata la durata in minuti dell'evento
     * @param necessitaBiglietto un oggetto {@code boolean} che rappreseta se la visita ha bisongo di un biglietto
     * @param minPartecipanti il numero minino di partecipanti affinche l'evento sia effettuato
     * @param maxPartecipanti il numero massimo di partecipanti che l'evento può ospitare
     * @param configuratore il nickname del configuratore che ha inserito l'evento
     * @return true se la registrazione è andata a buon fine, false altrimenti
     * 
     * @see java.sql.Date
     * @see java.sql.Time
     */
    public boolean registraNuovoTipoVisita(DTObject tipoVisita) throws Exception;

    /**
     * Funziona per inserire le disponibilità dei volontari a uno specifico tipo di visita
     * @param codiceVisita il codice del tipo di visita da associare
     * @param volontarioSelezionato il nickname del volontario
     * @return true se l'inserimento è andato a buon fine, false altrimenti
     * @throws Exception
     */
    public boolean associaVolontarioVisita (DTObject associazione) throws Exception;

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
