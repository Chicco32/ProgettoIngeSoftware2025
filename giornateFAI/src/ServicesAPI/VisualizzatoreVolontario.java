package ServicesAPI;

public interface VisualizzatoreVolontario {

	/**
	 * Visualizzza le visite a cui questo volontario è associato ed è disponibile a fare da guida
	 * @param volontarioAssociato il nickname del volontario associato
	 * @return
	 */
	public DTObject[] visualizzaElenecoTipiDiVisiteAssociate(String volontarioAssociato);
}
