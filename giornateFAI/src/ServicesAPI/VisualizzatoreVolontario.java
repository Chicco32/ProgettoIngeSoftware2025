package ServicesAPI;

/**
 * Interfaccia che definisce i metodi per ricevere e visualizzare i dati relativi alle visite salvate, 
 * ai volontari e ai luoghi registrati, con i permessi del visualizzatore.
 */
public interface VisualizzatoreVolontario {

	/**
	 * Visualizzza le visite a cui questo volontario è associato ed è disponibile a fare da guida
	 * @param volontarioAssociato il nickname del volontario associato
	 * @return Un oggetto di tipo {@code DTObject} che contiene le visite associate al volontario
	 * @throws Eccezioni.DBConnectionException Se si verifica un errore di connessione al database
	 */
	public DTObject[] visualizzaElenecoTipiDiVisiteAssociate(String volontarioAssociato) throws Eccezioni.DBConnectionException;

	/**
     * Metodo per ottenere le date possibili in termini di giorni della settimana
     * su cui un volontario può dare disponibilità, associate anche al tipo di visita in cui la visita si può
     * tenere in quel suddetto giorno.
     * @param volontarioAssociato il nome del volontario su cui filtrare la ricerca
     * @return un array di {@code DTObject} contenente i giorni della settimana e i tipi di visita associati
     * @throws Eccezioni.DBConnectionException
     */
    public DTObject[] estraiDOWPossibiliVolontario(String volontarioAssociato) throws Eccezioni.DBConnectionException;
	
	/**
	 * Funzione che mostra l'elenco Di istanze a cui un volontario è stato associato nella produzione del piano delle visite.
	 * In partiolare a differenza di {@link #visualizzaElenecoTipiDiVisiteAssociate() visualizzaElenecoTipiDiVisiteAssociate} questa funzione
	 * resituisce non i tipi di visita ma tutte le istanze in stato 'Proposta', 'Completa', 'Confermata' a cui il volontario afferisce.
	 * per ogni istanza restituisce il codice archivio, il titolo, il numero di iscrizioni e il numero di parteciapnti attuali. 
	 * @param volontarioAssociato il volontario su cui filtrare la visita
	 * @return un array di {@code DTObject} contente i risultati della ricerca
	 * @throws Eccezioni.DBConnectionException Se si verifica un errore di connessione al database
	 */
	public DTObject[] visualizzaElencoIstanzeVolontario (String volontarioAssociato) throws Eccezioni.DBConnectionException;

	/**
	 * Funzio0ne che ritorna l'elenco di iscrizioni per una specifica istanza di visita.
	 * In particolare ritorna il nome del fruitore che ha eseguito l'iscrizione, il numero di iscritti per quella iscrizione e il codice univoco associato.
	 * Questa funzione permette al volontairo di controllare tutte le iscrizioni a una spefica iscrizione effettuata 
	 * @param codiceIstanza il codice archivio che rappresenta l'istanza di quella visita
	 * @return un array di {@code DTObject} contente i risultati della ricerca
	 * @throws Eccezioni.DBConnectionException Se si verifica un errore di connessione al database
	 */
	public DTObject[] visualizzaElencoIscrittiIstanza (int codiceIstanza) throws Eccezioni.DBConnectionException;
}
