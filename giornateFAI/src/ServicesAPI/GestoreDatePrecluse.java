package ServicesAPI;

import java.io.FileNotFoundException;
import java.util.Date;

public interface GestoreDatePrecluse extends GestoreFilesConfigurazione{

    /**
	 * Scrive il file delle date precluse del mese corrente e del successivo.
	 * @param today la data odierna
	 * @param current l'array di date precluse
	 * @throws FileNotFoundException se il file non esiste o ci sono problemi di accesso
	 */
    public void scriviDatePrecluse(Date today, Date[] current) throws FileNotFoundException;

    /**
	 * Funzione che legge le date precluse per il mese corrente
	 * @return l'array di date precluse
	 * @throws FileNotFoundException se il file non esiste o ci sono problemi di accesso
	 */
    public Date[] leggiDatePrecluse() throws FileNotFoundException;

    /**
     * Funzione che resetta le date precluse salvate sul file
     * @param data la data odierna
     */
	public void cleanDates(Date data);

}
