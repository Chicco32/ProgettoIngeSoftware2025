package ServicesAPI;

public interface VisualizzatoreVolontario {

	/**
	 * Visualizzza le visite a cui questo volontario è associato ed è disponibile a fare da guida
	 * @param volontarioAssociato il nickname del volontario associato
	 * @return Un oggetto di tipo {@code DTObject} che contiene le visite associate al volontario
	 * @throws Eccezioni.DBConnectionException Se si verifica un errore di connessione al database
	 */
	public DTObject[] visualizzaElenecoTipiDiVisiteAssociate(String volontarioAssociato) throws Eccezioni.DBConnectionException;
}
