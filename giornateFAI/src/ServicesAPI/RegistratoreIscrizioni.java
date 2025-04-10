package ServicesAPI;

import ServicesAPI.Eccezioni.DBConnectionException;

/**
 * Versione del registratore con i permessi di registrazione e rimozione delle iscrizioni del fruitore in frontEnd.
 * Questa interfaccia permette di iscrivere e rimuovere le iscrizioni del fruitore a una visita.
 */
public interface RegistratoreIscrizioni {

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
	 * Permette di iscrivere un fruitore a una visita, in particolare questa funzione inserisce i dati nel DB riguardo la nuova 
	 * iscrizione del fruitore. Questa funzione fa  anche il controllo che l'inserimento della funzione non superi il numero 
	 * massimo di partecipanti alla visita. Se il numero massimo di partecipanti è già stato raggiunto,
	 * viene lanciata l'eccezione IscrizioneImpossibileException.
	 * Infine in caso di corretta iscrizione ritorna il codice univoco dell'iscrizione.
	 * @param codiceIstanza il codice dell'istanza della visita a cui ci si vuole iscrivere
	 * @param nickname il nickname del fruitore che si vuole iscrivere alla visita
	 * @param numPartecipanti il numero di partecipanti che si vogliono iscrivere alla visita nella stessa iscrizione
	 * @return una stringa che rappresenta il codice univoco dell'iscrizione, null in caso di errore.
	 * @throws DBConnectionException In caso di errore di connessione al database.
	 * @throws Eccezioni.IscrizioneImpossibileException in caso il numero di partecipanti superi il numero massimo di partecipanti alla visita.
	 */
	public String iscrivitiVisita(int codiceIstanza, String nickname, int numPartecipanti) throws Eccezioni.DBConnectionException, Eccezioni.IscrizioneImpossibileException;

	/**
	 * Funzione che aggiorna lo stato dell'istanza di visita a seguito dell'iscrizione. 
	 * In particolare se il numero di iscritti con l'aggiunta dell'iscrizione totale raggiunge il massimo, cambia lo stato in 'Completa'.
	 * La funzione richiede che il numero di iscritti non sia superiore al massimo di persone che possono iscriversi.
	 * @param codiceIstanza il codice dell'istanza a cui l'iscrizione fa riferimento
	 * @throws DBConnectionException In caso di errore di connessione al database.
	 */
	public void aggiornaStatoPostIscrizione(int codiceIstanza) throws Eccezioni.DBConnectionException;

	/**
	 * Permette di rimuovere un'iscrizione a una visita, in particolare questa funzione rimuove i dati dal DB riguardo l'iscrizione del fruitore.
	 * Richiede come metro di controllo il codice di iscrizione del fruitore alla visita.
	 * Questa funzione non controlla se l'iscrizione è già stata effettuata, ma lancia l'eccezione RimozioneIscrizioneImpossibileException in caso di errore.
	 * Prima di rimuovere la visita la funzione controlla se il codice inserito
	 * corrisponde al codice di iscrizione del fruitore, in caso contrario lancia l'eccezione IscrizioneImpossibile.
	 * Infine in caso di corretta rimozione ritorna true.
	 * @param codiceIstanza il codice dell'istanza della visita a cui ci si vuole iscrivere
	 * @param nickname il nickname del fruitore che si vuole disiscrivere alla visita
	 * @param CodiceIscrizione il codice che è stato fornito al fruitore al momento dell'iscrizione
	 * @return true in caso di corretta rimozione dell'iscrizione, false in caso di errore.
	 * @throws DBConnectionException In caso di errore di connessione al database.
	 * @throws Eccezioni.RimozioneIscrizioneImpossibileException in caos di tentativo di rimozione di una visita in stato 'Confermata' o se il codice di iscrizione non è corretto.
	 */
	public boolean rimuoviIscrizioneVisita(int codiceIstanza, String nickname, String CodiceIscrizione) throws Eccezioni.DBConnectionException, Eccezioni.RimozioneIscrizioneImpossibileException;

	/**
	 * Funzione che aggiorna lo stato della visita a seguito della rimozione dell'iscrizione.
	 * In particolare la funzione controlla lo stato dell'iscrizione prima della rimozione e se era in stato 'completa' lo aggiorna in 'proposta',
	 * altrimenti se la funzione era già in stato 'proposta' non fa nulla.
	 * @param codiceIstanza il codice dell'istanza a cui l'iscrizione fa riferimento
	 * @throws DBConnectionException In caso di errore di connessione al database.
	 */
	public void aggiornaStatoPostRimozione(int codiceIstanza) throws Eccezioni.DBConnectionException;


}
