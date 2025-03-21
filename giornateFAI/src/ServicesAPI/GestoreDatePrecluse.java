package ServicesAPI;

import java.util.Date;

public interface GestoreDatePrecluse extends GestoreFilesConfigurazione{

    /**
	 * Scrive il file delle date precluse del mese corrente e del successivo.
	 * @param today la data odierna
	 * @param current l'array di date precluse
	 */
    public void scriviDatePrecluse(Date today, Date[] current);

    /**
	 * Funzione che legge le date precluse per il mese corrente
	 * @return l'array di date precluse
	 */
    public Date[] leggiDatePrecluse();

    /**
     * Funzione che resetta le date precluse salvate sul file
     * @param data la data odierna
     */
	public void cleanDates(Date data);

}
