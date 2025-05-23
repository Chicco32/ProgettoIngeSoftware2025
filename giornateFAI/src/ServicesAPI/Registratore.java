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
     * @throws DBConnectionException In caso di errore di connessione al database.
     */
    public boolean registraNuovoVolontario (DTObject volontario) throws Eccezioni.DBConnectionException;

    /**
     * Questo metodo verifica se un dato nome utente è univoco, cioè se non è già stato registrato da 
     * nessun altro utente, indipendentemente dal loro ruolo. 
     * Viene invocato ogni volta che un utente tenta di registrare un nuovo nickname.
     * @param nomeUtente Il nome utente che si vuole registrare.
     * @return true se il nome utente non è già registrato, false altrimenti.
     * @throws DBConnectionException In caso di errore di connessione al database.
     */
    public boolean nomeUtenteUnivoco (String nomeUtente) throws Eccezioni.DBConnectionException;

    /**
     * Funzione per la registrazione di un nuovo luogo nel DB.
     * In particolare la funzione richiede al DB l'inserimento dei dati forniti e riporta la risposta del DB in caso di avvenuto inserimento o meno
     * La funzione ritorna true se la registrazione è andata a buon fine, false altrimenti.
     * @param nome il nome del luogo
     * @param descrizione la descrizione di al massimo 100 caratteri del luogo
     * @param indirizzo l'indirizzo di al massimo 45 caratteri
     * @return lo stato della registrazione, true se è andata a buon fine, false altrimenti
     * @throws DBConnectionException In caso di errore di connessione al database.
     * @throws CoerenzaException In caso di errore di coerenza dei dati.
     */
    public boolean registraNuovoLuogo (DTObject luogo) throws Eccezioni.CoerenzaException, Eccezioni.DBConnectionException;

    /**
     * Funzione che registra un nuovo tipo di visita nel DB.
     * La funzione richiede le funzioni aggiuntive:  
     * @param codice codice univoco che identifica il tipo di visita
     * @param luogo il luogo in cui si tiene, deve essere gia registrato nella tabella luoghi
     * @param titolo il titolo che riassume la visita
     * @param descrizione una breve descrizione dell'evento
     * @param dataInizio un oggetto di tipo {@code Date} che serve per rappresentare l'inizio del periodo del'evento
     * @param dataFine  un oggetto di tipo {@code Date} che serve per rappresentare la fine del periodo del'evento
     * @param oraInizio oggetto di tipo {@code Time} l'ora in cui l'evento si svolge
     * @param durata la durata in minuti dell'evento
     * @param necessitaBiglietto un oggetto {@code boolean} che rappresenta se la visita ha bisogno di un biglietto
     * @param minPartecipanti il numero minino di partecipanti affinche l'evento sia effettuato
     * @param maxPartecipanti il numero massimo di partecipanti che l'evento può ospitare
     * @param configuratore il nickname del configuratore che ha inserito l'evento
     * @return true se la registrazione è andata a buon fine, false altrimenti
     * @throws DBConnectionException In caso di errore di connessione al database.
     * 
     * @see java.util.Date
     * @see java.util.Time
     */
    public boolean registraNuovoTipoVisita(DTObject tipoVisita) throws Eccezioni.DBConnectionException;

    /**
     * Funziona per inserire le disponibilità dei volontari a uno specifico tipo di visita
     * @param codiceVisita il codice del tipo di visita da associare
     * @param volontarioSelezionato il nickname del volontario
     * @return true se l'inserimento è andato a buon fine, false altrimenti
     * @throws DBConnectionException In caso di errore di connessione al database.
     * @throws CoerenzaException In caso di errore di coerenza dei dati.
     */
    public boolean associaVolontarioVisita (DTObject associazione) throws Eccezioni.CoerenzaException, Eccezioni.DBConnectionException;

    /**
     * Funzione per la rimozione di un luogo dal DB.
     * In particolare la funzione richiede al DB la rimozione dei dati forniti e riporta la risposta del DB in caso di avvenuta rimozione o meno
     * La funzione deve rimuovere anche tutti gli eventuali tipi di visita istanziabili in quel luogo e le disponibilità associate a quelle visite.
     * @param nomeLuogo il nome del luogo da rimuovere
     * @return lo stato della rimozione, true se è andata a buon fine, false altrimenti
     * @throws Eccezioni.DBConnectionException In caso di errore di connessione al database.
     */
    public boolean rimozioneLuogo (String nomeLuogo) throws Eccezioni.DBConnectionException;

    /**
     * Funzione per la rimozione di un tipo di Visita dal DB.
     * In particolare la funzione richiede al DB la rimozione dei dati forniti e riporta la risposta del DB in caso di avvenuta rimozione o meno
     * La funzione deve anche rimuovere a cascata tutte le disponibilità associate al tipo di visita.
     * @param titoloVisita il nome del tipo di Visita da rimuovere
     * @return lo stato della rimozione, true se è andata a buon fine, false altrimenti
     * @throws Eccezioni.DBConnectionException In caso di errore di connessione al database.
     */
    public boolean rimozioneVisita (String titoloVisita) throws Eccezioni.DBConnectionException;

    /**
     * Funzione per la rimozione di un tipo di volontario dal DB.
     * In particolare la funzione richiede al DB la rimozione dei dati forniti e riporta la risposta del DB in caso di avvenuta rimozione o meno
     * La funzione deve anche rimuovere a cascata tutte le disponibilità associate al volontario.
     * @param nickname il nome del tipo di volontario da rimuovere
     * @return lo stato della rimozione, true se è andata a buon fine, false altrimenti
     * @throws Eccezioni.DBConnectionException In caso di errore di connessione al database.
     */
    public boolean rimozioneVolontario (String nickname) throws Eccezioni.DBConnectionException;

    /**
     * Funzione che va invocata successivamente alla rimozione di un qualsiasi elemento dal DB, sia esso un luogo, un volontario o Un tipo di visita.
     * In particolare il programma richiede che non vi siano:
     * <ul>
     * <li> Volontari salvati a cui non sia associata più nessuna disponibilità </li>
     * <li> Tipi di visite che non hanno più nessun volontario disponibile associato a quella visita  </li>
     * <Li> Luoghi che non hanno più nessun tipo di visita associato </li>
     * </ul>
     * @throws Eccezioni.DBConnectionException In caso di errore di connessione al database.
     */
    public void verificaCoerenzaPostRimozione() throws Eccezioni.DBConnectionException;

    /**
     * Funzione che registra le effettive istanze dei tipi di visita nel DB. 
     * In particolare essa va chiamata dopo la creazione del piano delle visite
     * in quanto semplicemente registra le nuove istanze di visite create senza il controllo sulla loro validità.
     * Cosa debba contenere l'istanza dipende dall'implementazione specifica del database.
     * La funzione non si occupa nè dell'aggiornamento dei dati ne sulla rimozione delle istanze.
     * @param istanza i dati sull'istanza di visita da salvare
     * @return true in caso di registrazione eseguita correttamente, false altrimenti
     * @throws Eccezioni.DBConnectionException  In caso di errore di connessione al database.
     */
    public boolean registraIstanzaDiVisita(DTObject istanza) throws Eccezioni.DBConnectionException;
}
