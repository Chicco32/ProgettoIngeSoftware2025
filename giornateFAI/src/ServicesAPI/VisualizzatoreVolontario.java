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
}
