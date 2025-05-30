package ServicesAPI;

public interface VisualizzatoreFruitore {

	/**
	 * Visualizza le istanze di visita disponibili per il fruitore a cui può iscriversi.
	 * In particolare ritorna tutte le istanze di visite attive al momento che siano nello stato
	 * <ul>
	 * <li> 'Proposta' </li>
	 * <li> 'Confermata' </li>
	 * </ul>
	 * la funzione non filtra sulle visite su cui il fruitore è già iscritto.
	 * @param nickname il nickname del fruitore su cui filtrare la ricerca
	 * @return  Un oggetto di tipo {@code DTObject} che contiene le visite disponibili
	 * @throws Eccezioni.DBConnectionException Se si verifica un errore di connessione al database
	 */
	public DTObject[] VisualizzaIstanzeVisiteDisponibili(String nickname) throws Eccezioni.DBConnectionException;

	/**
	 * Visualizza le istanze di visita a cui il fruitore è iscritto.
	 * In particolare ritorna tutte le istanze di visite a cui il fruitore è iscritto e che siano nello stato.
	 * In particolare per ogni iscrizione mostra: titolo, descrizione, numero partecipanti iscritti, data di svolgimento, 
	 * ora di inizio, necessita biglietto. Attenzione che NON mostra i codici di prenotazione, il fruitore dovrà salvarli
	 * in un altro modo.
	 * <ul>
	 * <li> 'Proposta' </li>
	 * <li> 'Confermata' </li>
	 * </Ul>
	 * @param nickname il nickname del fruitore su cui filtrare la ricerca
	 * @return Un oggetto di tipo {@code DTObject} che contiene le visite a cui il fruitore è iscritto
	 * @throws Eccezioni.DBConnectionException Se si verifica un errore di connessione al database
	 */
	public DTObject[] VisualizzaIstanzeIscritte(String nickname) throws Eccezioni.DBConnectionException;

	/**
	 * Visualizza le istanze di visita a cui il fruitore è iscritto e che sono state cancellate.
	 * Delle visite cancellate il fruitore visualizza solo titolo e data di mancato svolgimento.
	 * Da notare come le visite cancellate siano eliminate il giorno successivo alla data di mancato svolgimento.
	 * @param nickname il nickname del fruitore su cui filtrare la ricerca
	 * @return Un oggetto di tipo {@code DTObject} che contiene le visite cancellate
	 * @throws Eccezioni.DBConnectionException Se si verifica un errore di connessione al database
	 */
	public DTObject[] VisualizzaIstanzeCancellate(String nickname) throws Eccezioni.DBConnectionException;

	/**
	 * Funzione che ritorna il numero di posti ancora disponili in una visita in stato 'proposta'.
	 * La funzione non controlla lo stato della visita inserita.
	 * @param codiceIscrizione il codice su cui fare il calcolo
	 * @return un int con il numero di posti ancora disponibili
	 * @throws Eccezioni.DBConnectionException Se si verifica un errore di connessione al database o se si filtra su una visita non proposta
	 */
	public int ottieniPostiDisponibili(int codiceIscrizione) throws Eccezioni.DBConnectionException;

}
