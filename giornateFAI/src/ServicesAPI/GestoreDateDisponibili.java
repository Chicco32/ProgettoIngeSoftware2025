package ServicesAPI;
import java.io.FileNotFoundException;
import java.util.Date;

public interface GestoreDateDisponibili extends GestoreFilesConfigurazione {

    /**
     * Scrive il file con le disponibilità del volontario per il mese
     * successivo. In particolare scrive la data corrente e le date disponibili
     * @param today la data odierna
     * @param dateDisponibli l'array di date in cui il volontario ha dato la disponibilità
     * @param nomeVolontario il nome del volontario a cui associare le date
     * @throws FileNotFoundException se il file non esiste o ci sono problemi di accesso
     */
    public void registraDateDisponibili(Date today, Date[] dateDisponibli, String nomeVolontario) throws FileNotFoundException;

    /**
	 * Funzione che legge le date disponibili per il mese corrente
	 * @return l'array di date disponibili
     * @throws FileNotFoundException se il file non esiste o ci sono problemi di accesso
	 */
    public Date[] leggiDateDisponibili(String nomeVolontario) throws FileNotFoundException;

    /**
     * Funzione che resetta le date disponibili salvate sul file
     * @param data la data odierna
     */
	public void cleanDates(Date data, String nomeVolontario);

}
